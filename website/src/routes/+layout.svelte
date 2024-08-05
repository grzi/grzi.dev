<script>
    import './style.css';
</script>

<div class="app">
    <main>
        <slot/>
    </main>
</div>

<style lang="scss">
  .app {
    height: 100%;
  }

  main {
    height: 100%;
    width: 1024px;
    margin: auto;
  }

  .page-container {
    position: relative;
    display: flex;
    flex: 1 1 auto;
    width: 100%;
    height: 100%;
    overflow: hidden;

    &--main {
      width: 100%;
      height: 100%;
      display: flex;
      flex-direction: column;
      background: #edeff2;
    }
  }
</style>