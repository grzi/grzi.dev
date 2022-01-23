<template>
  <div class="about-author-container">
    <div class="about-author">
      <div class="about-author-flexbox">
        <div class="title-container">
          <div class="title-principal">
          </div>
          <div class="author-name">Jérémy THULLIEZ</div>
          <div class="author-description">
            Tech explorer, Dad, I love everything that is relative to JAVA, Rust, JS, Cloud, Big Data... <br/><br/>
            <i>Ex</i> Zenika, currently working as Tech Lead for <b>Benefiz</b>, a French Startup company that helps SMEs to bring benefits to their
            employees like big companies.
          </div>
          <div class="btns-list-container">
            <SocialNetworkLink hide-title="true"/>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import SocialNetworkLink from '@/components/common/SocialNetworkLink'
export default {
  name: 'AboutTheAuthor',
  components: { SocialNetworkLink }
}
</script>

<style scoped>
  .about-author-container {
    padding-left: 40px;
    padding-right: 40px;
    margin-top:30px;
  }

  .about-author {
    padding-top: 20px;
    padding-bottom: 20px;
    border-top: 1px solid rgba(0, 0, 0, 0.1);
    border-bottom: 1px solid rgba(0, 0, 0, 0.1);
  }
  .photo-author{
    min-width: 120px;
  }

  .img-author{
    border-radius: 50%;
    height: 90px;
    border:3px solid white;
    margin-right:50px;
  }

  .about-author-flexbox {
    font-size: 1rem;
    font-weight: 400;
    line-height: 1.3;
    display: flex;
  }

  .writtenby{
    color: rgba(0, 0, 0, 0.54);
    text-transform: uppercase;
  }

  .author-name {
    color:black;
    font-weight: bold;
    font-size: 1.5rem;
  }

  .author-description{
    margin-top: 15px;
  }

  .btns-list-container{
    margin-top: 15px;
  }
</style>
