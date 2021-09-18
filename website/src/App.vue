<template>
  <div id="app" >
    <Navbar/>
    <router-view/>
  </div>
</template>

<style lang="scss">
#app {
  font-family: 'Open Sans', Arial;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  hanging-punctuation: allow-end;
}
</style>
<script>
import Navbar from '@/components/common/Navbar'
export default {
  components: { Navbar }
}
</script>
