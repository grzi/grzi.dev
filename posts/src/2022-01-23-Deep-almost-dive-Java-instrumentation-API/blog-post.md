
# Introduction

J'ai eu un peu de temps libre ce week end et j'avais noté dans un coin de ma TO-LEARN liste, 
de regarder comment fonctionnaient les agents et par extensions l'<a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.instrument/java/lang/instrument/package-summary.html" target="blank">Instrumentation API</a>.

--- 
Basiquement, **l'instrumentation API** de **JAVA** permet d'intéragir avec le byte code de class java déjà compilées (donc des .class)

Afin d'utiliser cette API prometteuse, on doit créer des **agents**, qui sont donc un point d'entré vers l'API.

Je commence par créer un main très simple : 

```java
public static void main(String[] args) {
    System.out.println("[MAIN] Bonjour depuis l'application");
}
```

# Les agents

Un agent est un JAR, qui va donc utiliser l'API (dans le plupart des cas, mais on verra plus tard qu'il peut être utilisé pour d'autres choses).
Il peut être chargé de deux manière possibles : 

- **Chargement statique :** Va être appelé et permettre de modifier le byte code avant le démarrage de notre application principale. L'agent sera donc éxécuté dans la même JVM que l'application
<br/>Pour permettre le chargement statique, l'agent doit avoir une méthode `premain` : 

```java
public static void premain(String args, Instrumentation instrumentation) {
    System.out.println("[AGENT] Ceci est appelé avant le main de notre application");
}
```

- **Chargement dynamique :** Permet de charger l'agent, dans so propre JVM, mais en se connectant à une JVM existante, en utilisant l'**<a href="https://docs.oracle.com/en/java/javase/11/docs/api/jdk.attach/module-summary.html" target="blank">Attach API</a>**.
<br/>Pour permettre le chargement dynamique, l'agent doit avoir une méthode `agentmain` :

```java
public static void agentmain(String args, Instrumentation instrumentation) {
    System.out.println("[AGENT] Ceci est appelé au chargement de notre agent via l'Attach API");
}
```

Ce qui permet de voir que le cycle de vie d'une application avec agent est bien sûr différent d'un cycle de vie d'application classique. Cf le schéma *ci-après*.

<img src="https://raw.githubusercontent.com/grzi/grzi.dev.statics/main/cycle.png" style="border:1px solid gray; width: 100%;" alt="cycle-application-java-avec-agent"/>

### Test de lancement statique

On compile le tout en créant un manifest qui permet d'indiquer les informations de l'agent : 

```manifest
Manifest-Version: 1.0
Premain-Class: dev.grzi.AgentSimple
Agent-Class: dev.grzi.AgentSimple
Can-Redefine-Classes: true
```

Et j'essaye de lancer mon agent en utilisant la commande `java -javaagent:agent-simple.jar -jar main-app.jar`

Résultat : 

```
[AGENT] Ceci est appelé avant le main de notre application
[MAIN] Bonjour depuis l'application
```

Okay, jusque là rien de bien fou, mais on a quand même confirmer que le premain se lance avant le main.

> **Dans le cadre d'un agent statique, est-ce que le throw d'une exception empêche le démarrage de mon application ?**
> 
> Oui, si j'ajoute `throw new RuntimeException();` à la fin de mon `premain`, cela fait planter mon application au démarrage.

### Test de lancement dynamique

Afin de pouvoir me connecter à mon PID principal, je modifie un peu mon main pour en faire une boucle infini :

```java
public static void main(String[] args) throws InterruptedException {
    System.out.println("[MAIN] Bonjour depuis l'application");
    while(true) {
        Thread.sleep(5000);
        System.out.println("[MAIN] J'ai bien dormi !");
    }
}
```

Je créé une autre application, qui va se charger de faire le lien entre mon application principale et mon agent.

```java
 public static void main(String[] args) throws Exception {
    var vm = VirtualMachine.list().stream()
            .filter(d -> d.displayName().equals("dev.grzi.app.MainApp"))
            .findFirst().orElseThrow();
    VirtualMachine.attach(vm.id())
            .loadAgent("path/to/the/agent/agent-simple.jar");
}
```

Et j'observe le résultat : 

```
[MAIN] Bonjour depuis l'application au pid 365960
[MAIN] J'ai bien dormi !
[AGENT] Ceci est appelé au chargement de notre agent via l'Attach API
[MAIN] J'ai bien dormi !
```

On a donc réussi à charger un agent, dans une application, en utilisant l'Attach API. L'agent s'est bien chargé et a appelé la méthode 
agentmain.

> **Dans le cadre d'un agent dynamique, est-ce que le throw d'une exception impacte mon application ?**
>
> Non, si j'ajoute `throw new RuntimeException();` à la fin de mon `agentmain`, cela fait planter mon application qui utilise l'attach API, mais 
> mon application principale affiche une mini trace et continue de fonctionner normalement : 
>
> ```java
> Exception in thread "Attach Listener" java.lang.reflect.InvocationTargetException
> at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
> at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
> at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
> at java.base/java.lang.reflect.Method.invoke(Method.java:566)
> at java.instrument/sun.instrument.InstrumentationImpl.loadClassAndStartAgent(InstrumentationImpl.java:513)
> at java.instrument/sun.instrument.InstrumentationImpl.loadClassAndCallAgentmain(InstrumentationImpl.java:535)
> Caused by: java.lang.RuntimeException
> at dev.grzi.AgentSimple.agentmain(AgentSimple.java:13)
> ... 6 more
> Agent failed to start!
> ```

### Etude de cas simple, l'agent `jmx exporter prometheus`

Ayant déjà utilisé prometheus et plus particulièrement ici, l'exporter JMX prometheus, je me suis penché sur le fonctionnement de ce dernier afin de
comprendre ce qu'il y avait sous le capot. Ca tombe bien, cet agent est en fait très 'simple'. Il démontre qu'on peut, même sans utiliser l'instrumentation API (qu'on va aborder plus tard) faire usage des agents.

Le fonctionnement de base de l'exporter est en fait super simple, il va :
- Lire une config fournie en paramètre
- Créer un exporter qui va être ajouté à un registre d'exporteurs statiques 
- Créer un serveur HTTP qui va exposer les datas du registry et qui se lancera en background

Le code en question : 

```java
public static void premain(String agentArgument, Instrumentation instrumentation) throws Exception {
    ...
    try {
        Config config = parseConfig(agentArgument, host);

        new BuildInfoCollector().register();
        new JmxCollector(new File(config.file), JmxCollector.Mode.AGENT).register();
        DefaultExports.initialize();
        server = new HTTPServer(config.socket, CollectorRegistry.defaultRegistry, true);
    }
    catch (IllegalArgumentException e) {
        ...
    }
}
```

Et c'est tout. En regardant un peu je remarque que la méthode agentmain qui permet un chargement dynamique appelle le premain.
Cela voudrait-il dire qu'on peut charger l'exporter jmx prometheus depuis l'attach API ? Et bien oui, une découverte pour moi :) 

Cela dit, même si on voit qu'on peut faire des choses sans même utiliser l'instrumentation API, j'ai quand même envie d'aller voir ce qu'on peut en faire.

## L'instrumentation API

Maintenant qu'on sait charger un agent dans une jvm, on va regarder en détail en quoi consiste cette fameuse API.



### Comment modifier le byte code ?

Il existe un certains nombres de libs qui permettent de manipuler le byte code. Je pense que les deux plus populaires sont `bytebuddy` et `javassist`.


# Ressources utilisées pour cet article
Comme je n'ai rien inventé, voici une liste de sources qui m'ont permis de grandir sur ce sujet : 

- <a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.instrument/java/lang/instrument/package-summary.html" target="blank">Documentation Oracle sur l'instrumentation API</a>
- <a href="https://www.baeldung.com/java-instrumentation" target="blank">Article Baeldung sur l'instrumentation API</a>
- <a href="https://github.com/prometheus/jmx_exporter/tree/master/jmx_prometheus_javaagent_java6" target="blank">Code source de JMX exporter</a>
- <a href="https://www.youtube.com/watch?v=oflzFGONG08" target="blank">The definitive guide to Java agents by Rafael Winterhalter</a>

