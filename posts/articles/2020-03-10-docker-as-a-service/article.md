# How to use _docker as a service_ on a remote machine ?

Yeah, I know what you're thinking... But I can explain it all, I promise !

## Why ?

I'm currently working on a macbook pro 2017 (no touch bar) that have only 8Gb of RAM.

My basic opened work tools are the following :

- IntelliJ / Visual studio code
- Postman
- Firefox
- Spotify / Slack
- Docker
- Terminal

The problem is that my current project is a big one, with like 50 maven modules and vue js projects. My intelliJ is taking a lot of RAM, and a lot of time to reindex files.

I can't change my machine right now, so I need to optimize a bit my usage to save some RAM. I saw that my docker on mac is taking no less than 1.5GB (Even if I limit it to 1GB) ! That's overkill, because I only need docker for local postgres, local mongo, local elastic search ... and it's taking this even when no container is running...
So, I thought I could use a remote docker instance instead of using my local one. For my simple needs, it's okay for me.

## How to install

So, this is what I need to create a simple **docker as a service** :

- A remote machine to install docker
- Configure a DNS on the development computer to simplify the usage
- Configure the ssh key to be able to run ssh command from the machine
- Configure an alias command to
  - ssh to the remote machine
  - launch the docker commands
  - get the output from the command

I'm currently using a simple VPS that I pay 3,59€ per month. ([Kimsufi](https://www.kimsufi.com/fr/)). This VPS is not dedicated to my docker as a service, I have other things on it.

I set up a simple dns entry in my `etc/hosts` file

```bash
XX.XX.XX.XX     dockercloud
```

Then, I need to generate a ssh key :

```bash
ssh-keygen -t rsa -b 4096 -C "your_email@example.com"
```

I simply add this generated key to the vps :

```bash
ssh-copy-id root@XX.XX.XX.XX
```

I create an alias in my .bashrc (actually in my .zProfile) :

```bash
dockeraas() {
    echo 'I am running on the cloud ¯\_(ツ)_/¯'
    ssh root@dockercloud "docker $@"
}
```

After I have set up all this things, I can just run my docker command as usual :

```bash
$ : dockeraas ps
I am running on the cloud ¯\_(ツ)_/¯
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS                      NAMES
35dc9622cd36        postgres:10.5       "docker-entrypoint.s…"   About an hour ago   Up About an hour    0.0.0.0:5432->5432/tcp     order_db
444476070ef8        mongo               "docker-entrypoint.s…"   9 months ago        Up 9 months         0.0.0.0:27017->27017/tcp   brave_morse
```

And now I'm good to go. 1.5GB available on my local machine to give to google chrome :P !

## Conclusion

### Pros

- I have now more than 1.5 GB free to use on my machine
- It's simple to set up for basic needs like docker run
- I can still launch my docker in local if I need it (example for maven compilation based on docker)

### Cons

- You need a more complicated script and alias thing to handle docker-compose and volumes BUT it is possible
- You need to have an Internet connection, so no dockeraas in a plane

As far as I'm concerned, I plan on working a bit more on this. I want to be able to use more complicated command from my dockeraas command. For example, I usually need to use cross compilation from rust. It's based on docker, so I guess I could compile it directly on my remote machine instead of using my local one.
