# Wootlab.io architecture - C4 Model

Wootlab.io is a recent project that I did because I wanted a new website to post things and stuff. I also wanted to play around with things like GCP, Micronaut and Github Actions. I thought it might be fun and interesting to describe how I managed to create this website.

## The needs

This is a concrete list of what I wanted for this website : 

- A single page application
- A simple and inexpensive administration system to publish my articles
- Play with Micronaut
- Play with GCP stuffs
- Integrate with github Actions
- **And more importantly, don't pay anything !**

## C4 model representation

My collegue <a href="https://twitter.com/riduidel" target="_blank">@Riduidel</a> recently published a <a href="https://riduidel.wordpress.com/architecturer-agilement-avec-c4-structurizr/" target="_blank">thread</a> about architecture (in French). In this thread, he describes how he usually documents his architectures. This is how I discovered the C4 model.

After seeing an <a href="https://www.youtube.com/watch?v=x2-rSnhpw0g" target="_blank">introduction</a> to the model by Simon Brown, I decided to go for it, and **try** to represent my website with this system. It will be for me an initiation to the documentation of architecture, I hope that I will not do it badly.

### General System context diagram

The goal here is therefore to have a truly abstract view of everything. This is called a context or level 1 diagram.

<img src="https://storage.googleapis.com/wootlab-io-production.appspot.com/C4-model-wootlab/wootlabC4-context.png" alt="diagram-context" class="simple-centered-image"/> 

Here we can see that we have two types of users and what they "want" to be able to do in the Wootlab.io ecosystem.

### Wootlab.io Container diagram

Let's focus now on the Wootlab.io system. This is actually the website himself without the administration part.
This is called a container, or level 2 diagram

<img src="https://storage.googleapis.com/wootlab-io-production.appspot.com/C4-model-wootlab/C4-model-Container%20diagram%20-%20Wootlab%20io.png" alt="wootlab-container-diagram" class="simple-centered-image"/>

### Administration System diagram

Now, we focus on tha administration system, dedicated to me.

<img src="https://storage.googleapis.com/wootlab-io-production.appspot.com/C4-model-wootlab/C4-model-Container%20diagram%20-%20Wootlab%20io%20Administration.png" alt="wootlab-admin-container-diagram" class="simple-centered-image"/>

To go a little bit further, a blog post is divided into two files :

- **meta.json**, which contains the informations of the post (like its title, author, tag, publish toggle, credits, images url..)
```json
{
  "title": "Wootlab.io architecture - C4 Model",
  "description": "This article document the architecture of wootlab.io and how it is hosted for free and is a hands on C4 model to describe it.",
  "url": "wootlab-io-architecture-c4-model",
  "images": {
    "highResolutionUrl": "imgUrl",
    "miniatureUrl": "MiniatureUrl",
    "credits": "Photo by Robert Thiemann on Unsplash"
  },
  "tag": "Architecture",
  "date": "2020-04-30",
  "author": "Jérémy THULLIEZ",
  "published": true
}
```

- **article.md**, which contains the content of the post, in markdown, that will be converted to html.

So to write a new blog post, I simply create a new folder with these two files, I write their contents (of course) and push to my github repository.

## Continuous delivery on environments

To deliver all of this, here is the list of CD I created on Github Actions : 

- A contextualized vue JS build to netlify
- A contextualized Java/Micronaut application to Google appEngine
- Two python functions to Google Cloud Functions 

Each of this CD are triggered by a push on the master branch or the dev branch. A push on master branch will deliver to the production environment while a push to the dev branch will push to my development environment.

In the same way, in my administration repository, a push to the master branch will trigger the production cloud functions while a push on the dev environment will trigger the developmment cloud functions.

## Conclusion

If you want to have a look on all this work and how my current website is working, you can check <a href="https://github.com/wootlab/wootlab-io" target="_blank">the source repository</a> or the <a href="https://github.com/wootlab/wootlab-io-posts" target="_blank">blog posts repository</a> 

This architecture is using only free tier of GCP, free Netlify hosting service and free Github Actions. So It's basically a **full stack** (Front, back, storing, CI/CD..) for free. 
The only problem is that my appEngine usually stops when no activity is recorded. I had to use a cron to loop through one of my endpoints to keep my application alive.

**Take care !**