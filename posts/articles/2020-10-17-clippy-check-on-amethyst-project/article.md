# How to add a clippy check github action to an amethyst.rs project ?

You may be familiar with clippy, a lint tool tohelp have a cleaner code, in Rustlang. I recently wanted to add a clippy check github action to an amythyst project.

I had to modify a bit the <a href="https://github.com/marketplace/actions/rust-clippy-check" target="_blank">default action</a> to make it work on my amethyst projects.

So, to be quick, two steps : 

- Be carefull to put the right features on your clippy run args, depending on which instance of server you are running on. (I run on ubuntu so I've put Vulkan)
- Install libasound2-dev before running clippy.

The result action : 

```yaml
on: pull_request
name: Apply clippy comments to pull request
jobs:
  clippy_check:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - uses: actions-rs/toolchain@v1
        with:
          toolchain: nightly
          components: clippy
          override: true
      - name: install lib
        run: sudo apt install libasound2-dev
      - run: rustup component add clippy
      - uses: actions-rs/clippy-check@v1
        with:
          token: ${{ secrets.GITHUB_TOKEN }}
          args: --features vulkan
```

Please note that this action doesn't work for PR that comes from forks, due to token limitations.

Bye bye !