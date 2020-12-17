
# A pong in rustlang with amethyst.rs

I often work on my own little games, in my spare time, to learn, make the good tech choice and be ready to start the real big game project that I always wanted to do. Doing this, I've been using a lot of game engine in the last years :

- Unity 3D
- Unreal engine
- Godot engine
- RPG maker
- Lumberyard
- ... (even LWJGL 2)

Most of these game engines / lib gave me the feeling of over-powering my needs (except LWJGL 2, but I don't want a java game). They all are really nice engines and softwares. I've made multiple games with Unity3D, and I will certainly make more, because it's accessible and quick to get started with a new game. But with all these engines, I really don't have the feeling to understand technically speaking all that I'm doing. Indeed, when you create a shader in unity, like a glowing effect. You'll search for "glowing effect shader" on your search engine, and surely find a shader that fill your needs. And that's awesome. But I want to understand these things, not just use them.  I could just learn how it works on Unity, but I want to deep dive into it and know what's behind the scene.

So, for now, I'm looking for a minimalist game engine. The one that will gave me the sensation of "doing" it and that will make me think, maybe during a lot of days, to understand the purpose of concepts like shaders, 3D movements, velocity, gravity...

## What is Amethyst ?

Amethyst is a Data-driven game engine written in Rust. It uses the ECS concepts to build all the game logic. To quote the amethyst.rs website :

> The ECS is rich in features and very efficient, as it never does any memory locking while remaining entirely thread-safe.
>
> -- <cite>amethyst.rs</cite>

It's beginner friendly, but you need to know how to code in rustlang before. It can be used to make 2D, or 3D games and has advanced features, and more coming.

It is sponsored by some companies like : Netlify, JFrog, Digital Ocean.

It comes as a crate that you import in your rust project. It hasn't any software with ui. Just libs.

And, it is OPEN SOURCE. (yeah !!!)

## A simple pong 

I followed the tutorial that is in the amethyst book to make a pong. And what a pleasure !

### The ron format
Amethyst supports by default the usage of ron configs. Ron stands for Rusty object notation and is an alternative to json format. Take a look at [https://github.com/ron-rs/ron](https://github.com/ron-rs/ron)

### Entity-Component-System

As I said before, Amethyst is based on the ECS concepts. So to build the game, you must follow them. 
What does it brings ? Good practices, I guess. And moreover, a structured game. You're forced to 
think your game logic to make it fit ECS.   

In the pong, entities are paddles, balls. Components are the properties associated (like the color, the velocity..). 

```rust
pub struct Ball {
	pub velocity: (f32, f32),
	pub radius: f32
}

impl Ball {
    pub fn new() -> Ball {
        Ball {
			velocity: (BALL_VELOCITY_X, BALL_VELOCITY_Y),
			radius: BALL_RADIUS
        }
    }
}

impl Component for Ball {
    type Storage = DenseVecStorage<Self>;
}
```

Systems are the logic behind the paddle movements, behind the ball bounce or behind the scoring.
Systems usually controls the components and entities.  

```rust
#[derive(SystemDesc)]
pub struct BallSystem;

impl<'s> System<'s> for BallSystem {
    type SystemData = (WriteStorage<'s, Transform>, ReadStorage<'s, Ball>,  Read<'s, Time>);

    fn run(&mut self, (mut transforms, balls, time): Self::SystemData) {
       for (ball, transform) in (&balls, &mut transforms).join() {
		   	transform.prepend_translation_x(ball.velocity.0 * time.delta_seconds());
            transform.prepend_translation_y(ball.velocity.1 * time.delta_seconds());
	   }

    }
}
```

Moreover, you have to handle the camera, textures, sprites... 

The non exhaustive implementation : [https://github.com/grzi/rust-pong](https://github.com/grzi/rust-pong)

## Conclusion

Doing this tutorial, I've been greatly surprised on how simple is this game engine to learn. I also took some time to read a bit the code behind some of the features I've used. And that is really cool, because it's helping me to understand how simple things like how camera works. 

I'm currently making my own little tetris like to go further in the learning on amethyst. I don't know if my good feelings will continue, but for now, I feel like this game engine is going to learn me a lot of things, and just for this, I recommend every one who has a bit of free time to test it. 