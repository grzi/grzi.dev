# How to add a clippy check action to an Amethyst.rs project

In a recent post ( <a href="https://www.wootlab.io/blog/clippy-check-action-on-amethyst-rs" target="_blank">here</a> ), I've talked about a workflow to have automatic comments on any changed file in pull requests.

To go further, I wanted to have a way to automatically apply these changes if I forgot to apply them myself.

The result workflow uses <a href="https://www.github.com/peter-evans/create-pull-request" target="_blank">peter-evans/create-pull-request</a> action and clippy component : 

```yaml
on:
  push:
    branch: main
name: Apply clippy and PR changes
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
      - run: rustup component add clippy
      - run: cargo clippy --fix -Z unstable-options --features vulkan
      - name: Create Pull Request
        uses: peter-evans/create-pull-request@v3
        with:
          token: ${{ secrets.GITHUB_TOKEN }}
          branch-suffix: timestamp
          commit-message: "style(lint): automatically applied clippy lint"
          title: "Automatic lint from clippy"
```

Please find an example of pull request <a href="https://github.com/grzi/rrss2imap/pull/7" target="_blank">here</a>.

Bye bye !