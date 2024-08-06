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
  @media (max-width: 1024px) {
    .app {
      width: 100%;
      margin:auto;
    }
  }

  main {
    height: 100%;
    width: 1024px;
    margin: auto;
  }
  @media (max-width: 1024px) {
    main {
      width: 100%;
      margin:auto;
    }
  }
</style>