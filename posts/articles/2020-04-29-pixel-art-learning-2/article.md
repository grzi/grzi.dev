# Pixel art learning (part 2: Making progress)

In the [last article](https://www.wootlab.io/blog/pixel-art-learning-1), I started to learn pixel art. My goal after this first article was to be able to make a mini game sprite and make a game with it

**So, where am I now ?**

Well, I focused on the forms as I said. I made a lot of objects, without any color, just to learn more about the angle and the different rules of pixel art that I found in the tutorials.

## The importance of color theory

To understand a little how to choose my colors, I needed to understand a little bit about color theory. The advantage of this is that, it is not only a question of pixel art, but more generally important in all things that require a choice between colors.

I still have a lot to learn, but a big step is taken when you accept that it is not just a detail but something important to manage before doing things.

## The project

For my mini-game sprite, I chose to opt for a palette of 4 colors. I wanted to make a gameboy style game, so I opted for this palette:

<img src="https://storage.googleapis.com/wootlab-io-production.appspot.com/palette.png" class="pixel-art-img" alt="palet"/>

On gameboy, they only had 4 palette colors, one for transparency and 3 for pixel art. They could also use the color of transparency inside to make light. Using this, I made a cool little sprite and a moving character:

<img src="https://storage.googleapis.com/wootlab-io-production.appspot.com/character-gameboy.gif" class="pixel-art-img" alt="character"/>
<img src="https://storage.googleapis.com/wootlab-io-production.appspot.com/sprite_gameboy.png" class="pixel-art-img" alt="sprite"/>

And I managed to use the great amethyst.rs engine to create a little POC game with these:
<div class="post-video">
    <iframe src="https://www.youtube.com/embed/flCVrOKkf88" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen="" style="
    margin-top: 20px;
    margin-bottom:20px;
    width:100%;
    height:500px;
"></iframe>
</div>

Please find the source code here : [https://github.com/grzi/rust-gameboy-game-poc](https://github.com/grzi/rust-gameboy-game-poc)

## What's next ?

Now that I have successfully created a first mini-game with my own sprite, I will try to use a more colorful palette and create a Plateformer sprite this time. I will also go further into the amethyst engine to implement the game.

**Take care !**