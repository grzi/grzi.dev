<script>
    // 'https://raw.githubusercontent.com/grzi/grzi.dev/main/' + uri[0].path + '/blog-post.html'
    //
    import {onMount} from "svelte";
    import axios from "axios";

    let mock = [{
        date: "2024-05-11",
        title: "From design, to application",
        description: "Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet ",
        uri: "Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet ",
        tag: "Video Game"
    }];

    async function searchLogs() {
            return await axios
                .get(`https://raw.githubusercontent.com/grzi/grzi.dev/main/posts/global_meta.json`)
                .then(function ({data}) {
                    mock = data;
                });
    }

    onMount(async () => {
        await searchLogs();
    });
</script>
<div class="home">
    <div>
        <h3 class="title">
            Grzi.dev
        </h3>
        <div>
            <a href="/">About</a>
        </div>
        <div class="articles">
            {#each mock as m}
                <div class="article">
                    <div class="article-flex">
                        <div class="article-date">
                            <div>{m.date}</div>
                        </div>
                        <div style="width: 100%;">
                            <div class="article-title">
                                <a href={"/logs/" + m.uri}>{m.title}</a>
                            </div>
                            <div class="article-description">{m.description}</div>
                            <div class="article-tags">
                                {m.tag}
                            </div>
                        </div>
                    </div>
                </div>
            {/each}
        </div>
    </div>
</div>

<style>
    .home {
        height: 100%;
        width: 768px;
        margin: auto;
        font-weight: 200;
        font-size: 14px;
        display: flex;
        flex-direction: row;
    }

    .title {
        font-weight: 500;
    }

    .articles {
        display: flex;
        flex-direction: column;
        gap: 15px;
        margin-top: 42px;
        width: 100%;
    }

    .article-flex {
        display: flex;
        gap: 10px;
        width: 100%;
    }

    .article:not(:last-child)::after {
        content: '• • •';
        display: block;
        text-align: center;
        margin: 20px 0 0 0;
        color: #333;
    }

    .article-title {
        font-weight: 400;
    }

    .article-title a {
        color: black;
    }

    .article-tags {
        width: 100%;
        margin-top: 10px;
        font-size: 12px;
        font-weight: 200;
    }

    .article-description {
        font-weight: 100;
    }

    .article-date {
        display: flex;
        flex-direction: row;
        vertical-align: center;
        align-items: center;
        justify-content: center;
        text-align: left;
        font-size: 16px;
        min-width: 110px;
    }
</style>