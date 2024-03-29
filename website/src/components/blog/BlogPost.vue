<template>
  <div>
    <Loader v-if="loading || !mainImageLoaded" :text="''" :fullPage="true"/>
    <div class="blog-container" :class="{'displayNone': loading || !mainImageLoaded }">
      <div v-images-loaded:on.progress="loaded" class="blog-image">
        <img :src="article.images.highResolutionUrl"/>
        <div class="post-title-desktop">
          {{article.title}}
        </div>
      </div>
      <div class="blog-container-content">
        <div class="post-title-mobile">
          {{article.title}}
        </div>
        <div class="post-informations">
          <div class="infosArticles">
            <div class="minititle"> <font-awesome-icon icon="clock"/><span class="val">{{article.date}}</span></div>
            <div class="minititle"> <font-awesome-icon icon="user"/><span class="val">{{article.author}}</span></div>
            <div class="minititle"> <font-awesome-icon icon="tag"/><span class="val">{{article.tag}}</span></div>
            <div class="minititle"> <font-awesome-icon icon="image"/><span class="val">{{article.images.credits}}</span></div>
            <div class="clear"> </div>
          </div>
        </div>
        <div class="post-content" v-html="article.content">
        </div>
        <div class="post-content">
          <router-link to="/" tabindex="0" class="pagination-previous">
            Back to blog posts
          </router-link>
        </div>
        <AboutTheAuthor/>
      </div>
      <div class="footer-container">
        <div class="footer-content">
          <SocialNetworkLink :is-floating="false"/>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import SocialNetworkLink from '@/components/common/SocialNetworkLink'
import AboutTheAuthor from '@/components/common/AboutTheAuthor'
import Loader from '@/components/common/Loader'
import imagesLoaded from 'vue-images-loaded'

export default {
  name: 'BlogPost',
  components: { Loader, SocialNetworkLink, AboutTheAuthor },
  data () {
    return {
      'article': {
        'images': {}
      },
      'loading': true,
      'mainImageLoaded': false,
      'retry': 0
    }
  },
  directives: {
    imagesLoaded
  },
  methods: {
    loaded (instance, image) {
      if (image.img.src !== '') {
        setTimeout(() => {
          this.mainImageLoaded = true
          this.$store.state.toggleOffNavbar = false
          const blogImage = this.$el.querySelector('.blog-image')
          if (blogImage) {
            window.addEventListener('scroll', () => {
              if (window.scrollY >= (blogImage.offsetHeight - 75)) {
                blogImage.classList.add('blog-image--fixed--mobile')
              } else {
                blogImage.classList.remove('blog-image--fixed--mobile')
              }
              if (window.scrollY >= (blogImage.offsetHeight - 79)) {
                blogImage.classList.add('blog-image--fixed')
              } else {
                blogImage.classList.remove('blog-image--fixed')
              }
            })
          }
        }, 50)
      }
    },
    loadArticle () {
      axios
        .get('https://raw.githubusercontent.com/grzi/grzi.dev/main/posts/global_meta.json')
        .then(response => {
          var uri = response.data.filter(e => {
            return e.uri === this.$route.params.path
          })
          axios
            .get('https://raw.githubusercontent.com/grzi/grzi.dev/main/' + uri[0].path + '/blog-post.html')
            .then(response => {
              if (response.status === 200 && response.data !== '') {
                this.$set(this.article, '', response.data)
                this.article = uri[0]
                this.article.content = response.data
                this.loading = false
              }
            }
            ).catch(e => {
              this.retry++
              if (this.retry < 5) {
                setTimeout(() => { this.loadArticle() }, 500)
              }
            })
        }).catch(e => {
          this.retry++
          if (this.retry < 5) {
            setTimeout(() => { this.loadArticle() }, 500)
          }
        })
    }
  },
  beforeMount () {
    this.$store.state.toggleOffNavbar = true
    this.loadArticle()
  },
  watch: {
    authenticated () { },
    '$route.params.path' (val) {
      this.loadArticle()
    }
  },
  metaInfo () {
    return {
      title: this.article.title,
      meta: [
        { vmid: 'og:title', name: 'og:title', content: this.article.title },
        { vmid: 'twitter:title', name: 'twitter:title', content: this.article.title },
        { vmid: 'description', name: 'description', content: this.article.description },
        { vmid: 'og:description', name: 'og:description', content: this.article.description },
        { vmid: 'twitter:description', name: 'twitter:description', content: this.article.description },
        { vmid: 'type', name: 'type', content: 'article' },
        { vmid: 'og:type', name: 'og:type', content: 'article' },
        { vmid: 'twitter:card', name: 'twitter:card', content: 'summary' },
        { vmid: 'twitter:site', name: 'twitter:site', content: '@grzi.dev' },
        { vmid: 'twitter:creator', name: 'twitter:creator', content: '@JeremyThulliez' },
        { vmid: 'twitter:image', name: 'twitter:image', content: this.article.images.miniatureUrl },
        { vmid: 'og:image', name: 'og:image', content: this.article.images.miniatureUrl },
        { vmid: 'article:published_time', name: 'article:published_time', content: this.article.date }
      ]
    }
  }
}
</script>

<style lang="scss">

  .post-title-mobile{
    display:none;
  }

  .reduced-margin-top{
    margin-top: 20px !important;
  }

  .leftauto{
    margin-left: auto;
  }

  .rightauto{
    margin-right: auto;
  }

  .commentDraft-content{
    padding:40px;
  }

  .blog-image {
    img{
      width:100%;
    }
    line-height:0px;
    z-index:10;
  }

  .footer-content{
    width:1024px;
    padding-left: 40px;
    color:white;
    margin:auto;
  }

  .blog-container-content{
    width:1024px;
    margin: -10px auto auto;
    background:white;
    padding-bottom:40px;
    padding-top:15px;
  }

  .post-video{
    margin-top:20px;
    margin-bottom:20px;
    width:100%;
    height:500px;
  }

  .post-content p{
    text-align: justify;
  }

  @media (min-width: 1024px) {
    .minititle{
      float: left;
    }

    .blog-image{

       img {
         transition: filter ease-in-out .3s;
       }

       &--fixed {
         position: sticky;
         transform: translateY(calc(-100% + 80px));
         top: 0;
         left: 0;
         box-shadow: 0px 2px 6px 0px rgba(0, 0, 0, 0.52);

         img {
           filter: brightness(50%);
         }

         .post-title-desktop {
           position: absolute;
           font-size: 2.2rem;
           padding: 0;
           bottom: 22px;
         }
       }
     }
  }

  .with-separator {
    padding-top: 10px;
    border-top: 1px solid rgba(0, 0, 0, 0.1);
    width:100%;
  }

  @media (max-width: 1024px) {

    .blog-image{
      img {
        transition: filter ease-in-out .3s;
      }

      &--fixed--mobile {
        position: sticky;
        transform: translateY(calc(-100% + 70px));
        top: 0;
        left: 0;
        line-height:0px;
        box-shadow: 0px 2px 6px 0px rgba(0, 0, 0, 0.52);

        img {
          filter: brightness(50%);
        }
      }
    }

    .blog-container-content{
      width:100%;
    }

    .footer-content{
      width:100%;
      padding-left: 20px;
      text-align:center;
    }

    .post-video{
      margin-top:20px;
      margin-bottom:20px;
      width:100%;
      max-height:500px;
      min-height:300px;
      height:auto;
    }

    .post-title-desktop{
      display:none;
    }

    .post-title-mobile{
      display:block;
      width:100%;
      padding:20px 40px 20px 40px;
      text-align: center;
      font-family: 'Arvo', sans-serif;
      font-size:3em;
      font-weight:bold;
    }

    .displayNone {
      height:0px;
      overflow: hidden;
    }
  }

  .blog-container{
    text-align:left;
  }

  .post-title-desktop{
    line-height:1.1;
    position:absolute;
    margin-top:-350px;
    width:100%;
    padding:40px;
    text-align: center;
    color:white;
    font-family: 'Arvo', sans-serif;
    font-size:5em;
    font-weight:bold;
    -webkit-text-stroke: 1px black;
    -webkit-text-fill-color: white;
  }

  .minititle{
    margin-right:0.8em; color:#666666;
    white-space: nowrap;
  }

  .minititle .val{
    margin-left:5px;
  }

  .post-image-credits{
    font-style: italic;
    text-align: right;
    padding-right:10px;
    padding-top:5px;
    font-size: 0.8em;
  }

  .blog-container img {
    box-shadow: 0px 2px 6px 0px rgba(0, 0, 0, 0.12);
  }

  .post-title{
    padding-left: 20px;
    padding-right: 20px;
    font-size:2.8em;
    text-align: center;
    font-weight: bold;
  }

  .post-informations{
    font-size:14px;
    color:lightgray;
    padding-left:40px;
  }

  .post-content {
    margin-top: 40px;
    padding-left: 40px;
    padding-right: 40px;
    font-size: 1rem;

    h1 {
      font-size: 2.5rem;
      line-height: 3.75rem;
      font-weight: 700;
      border-bottom: 1px solid gray;
      margin-bottom:20px;
    }

    h2{
      font-size: 1.8rem;
      line-height: 3.75rem;
      font-weight: 600;
    }

    h3{
      font-size: 1.8rem;
      line-height: 3.55rem;
      font-weight: 400;
      border-bottom: 1px solid lightgray;
      margin-bottom:20px;
    }

    blockquote {
      background-color: #f5f5f5;
      border-left: 5px solid #79e0e6;
      padding: 1rem 1rem;
      margin-bottom: 20px;
    }

    blockquote p {
      margin: 0;
    }

    p {
      font-size: 1rem;
      line-height: 1.5rem;
      font-weight: 400;
      margin: 0 0 1.5rem;
      text-align-last: left;
    }

    ol, ul {
      margin-left: 50px;
      margin-bottom: 30px;
      counter-reset: item;
    }

    ul {
      list-style-type: square;
    }

    ul li::marker {
      font-size: 1.0em;
    }

    ol li {
      display: block;
      line-height: 1.9rem;
    }

    ol li:before {
      content: counter(item) ". ";
      counter-increment: item;
      font-weight: bold;
    }

    img{
      width: 70%;
      margin-left:15% !important;
      margin-top:30px !important;
      margin-bottom:30px !important;
      border: 1px solid gray !important;
    }
  }

  @media (max-width: 1024px) {
    .post-content {
      h1 {
        font-size: 1.4rem;
        line-height: 2rem;
        font-weight: 700;
        border-bottom: 1px solid gray;
        margin-bottom: 20px;
      }

      h2 {
        font-size: 1.2rem;
        line-height: 2rem;
        font-weight: 600;
        margin-bottom: 20px;
      }

      h3 {
        font-size: 1.1rem;
        line-height: 2rem;
        font-weight: 400;
        border-bottom: 1px solid lightgray;
        margin-bottom: 20px;
      }
    }
  }

  @media (max-width: 535px) {
      .post-title-mobile{
        font-size:2em;
      }
    }

    .share-content{
      padding-left:40px;
      padding-right:40px;
    }

    .footer-container{
      background-color:#444;
      padding-top:20px;
      padding-bottom:20px;
    }

    .is-Twitter{
      background:#08a0e9 !important;
    }

    .is-Twitter:hover{
      background:#0790d9 !important;
    }
</style>
