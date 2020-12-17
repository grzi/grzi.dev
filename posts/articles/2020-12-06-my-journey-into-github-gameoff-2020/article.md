# My journey into Github GameOff 2020


## What is GithubGameOff ?

> Github Game Off is a game jam, where every participant spend the month of November creating games based on a secret theme.

This year, the theme was "MOONSHOT". eg: 
- An extremely ambitious and innovative project
- The act of launching a spacecraft to the moon
- A hit or thrown ball that travels a great distance with a high trajectory

The them was revealed November 1st.

## The steps

It was my first real game jam, by that I mean the first one I really wanted to do from start to finish. A month is, IMHO, a good time to do a mini-game. I tried jams of 3 days, but I'm clearly not ready to do quality in such a short time.

So what were my milestones? Well ... the best way to describe them is surely to list:

1. Brainstorming & Changing mind
2. Sketches
3. Key features
4. Secondary features
5. Final Debugging and tuning
6. Level design
7. Oh, no ! Nothing works on Linux ! 

I will detail each step a bit.

### Brainstorming & Changing mind

Initially, I had 3 ideas when the theme was released. And the funny thing about it all is that none of them was actually the final match.

Of these 3 ideas, the one that really emerged was a point-and-click story, where Noom, the main heroes, would try to understand why he had been left alone in a mansion since he was a child. The difficulty with this idea was art. I needed a lot of drawing and less code. So, I immediately tried to draw a sketch to see if I would do it on time. Aaaaaand no, that was ugly, and I really need to relearn how to draw basic things.

Then I remembered the game I played when I was a kid. You controlled a ship on a map and had to land on a platform to refuel. Actually that's all I remember about this game. No name, nothing else, I'm still looking for this game.

I also looked for a way to fully fit into the moonshot theme, and not just with the landing stuff, but also with a short "story".

I decided to list all the items I wanted in the game so I would know what to do next.
<img style="margin:auto; border=1px solid gray;" src='https://storage.googleapis.com/wootlab-io-production.appspot.com/starlight.png' alt='list'>


### Sketches

To start it all off, I created a sample amethyst.rs (a rust game engine framework) project from scratch.

I then started working on artistic sketches for the game, to have a first sprite to use to work on the basic features.

This is how I built the first scene in the game (the colors were really different ... ^^):

<img style="margin:auto; border=1px solid gray;" src='https://storage.googleapis.com/wootlab-io-production.appspot.com/starlight2.jpeg'>

### Key features

After the sketches, I needed to start the main features work. I have divided all my ideas into two lists. First, key features, then secondary features. Basically, key features are the heart of the game. Secondary features are details and things or behaviors that are based on key features.

To be more specific, the main features included: 

- **Controls:** Simple control of the ship with arrows. We just need the power and the spin. The whole game is based on these 2 controls.

- **Gravity:** The gist of the game is that the spaceship experiences gravity. I first wanted to mix zero gravity and this, but first I focused on the gravity part.

- **Project CI/CD:** In order to build the game for each platform, I had to quickly build the release pipeline. That way, I could give each alpha version of the game to the testers, to validate that everything works on their operating system.

- **Collision:** Implementing collision is one of the key feature of the game because it is used by all the following behaviour : 'walls', 'bullets', 'landing', 'doors', 'bonus' 

- **Landing:** The main goal of the game is to land at the arrival. 

- **Tiling system:** After the other features, I encountered a big problem where everything was blinking. I had to find a solution, and using the Tiling system from the game engine didn't work because it is in a KO state in the amethyst version I am using. It also doesn't handle Z layers. 

Once all these key features were done, I had a first version of my project that was 'good' enough for testing and to be considered as a **game**: 

<blockquote class="twitter-tweet" style ="margin:auto;"><p lang="en" dir="ltr">I&#39;m happy with my progress :D<br>Okay, now let&#39;s focus a bit on the UI informations needed to know that you can land or that you will crash 🤯 <a href="https://t.co/QGZkiPwGuH">pic.twitter.com/QGZkiPwGuH</a></p>&mdash; Jérémy Thulliez (grzi) (@JeremyThulliez) <a href="https://twitter.com/JeremyThulliez/status/1327901460076634112?ref_src=twsrc%5Etfw">November 15, 2020</a></blockquote> <script async src="https://platform.twitter.com/widgets.js" charset="utf-8"></script>

### Secondary features

Once I had all the key features I had to list all the things I wanted to add to the game to make it more "fun". I made this list:

- **Make real art**
- **Create plasma door that swith on/off following a timer**
- **Create door that will be triggered by a button platform**
- **Add different kind of canons (bullets, SMG, air, plasma..)**
- **Add a simple UI**
- **Create a simple fuel consumption. When fuel is empty, the ship only fall**
- **Blade saw, an enemmy that will follow rails. If the ship touch it, the ship explodes**
- **Victory condition (land on the final platform)**
- **Fuel and health bonuses**
- **Level switching**
- **Reset on levels after death**
- **Add sounds to the game (musics and effects)**
- **Add coins to collect as main objective of each map**

I managed to put everything into the game, as almost all of them used key features that I had already created (physics, collision, gravity, tiling system ...)

### Level design

Finally, when I felt that my game has enough features and was stable enough, I started to make real levels. 

**To make each level :**

First, I drew a path on paper. Then, I made it a bit complex by adding enemies. After that I tried to simplify a bit by adding bonuses and refuel platforms.

### Oh, no ! Nothing works on Linux ! 

Yes :-(

After submitting to Github GameOff, due to lack of Linux testing by me, I found out that in fact my submitted version only worked on Ubuntu 18.04.

I am currently creating many test environments so that I can avoid this in the future.

## Conclusion

If you want to take a look at the history of the project, check the commits or the code, or even try the last release, go on the repository: 

[https://github.com/grzi/starlight-1961](https://github.com/grzi/starlight-1961)


Moreover, I've discovered that rust community is really cool and helpfull !

I also understood that for this kind of hobby, I have to rely on myself first. Then, if someone helps me, it's really cool, but I can't really wait after this to advance. 
'Others' don't really care about me in general, and even in a contest, I've been transparent for almost everyone around me. But it's okay, I do it for myself first !

About creating levels, maybe I did it wrong. Even though I'm sure I followed the right path for each level, I'm pretty sure I made the game too difficult.

I might not win the Github Game Off, but I prove to myself that I can do cool things from start to finish. For me, it's already a victory.