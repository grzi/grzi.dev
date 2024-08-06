<script>
    import axios from "axios";
    import {onMount} from "svelte";
    import {page} from '$app/stores';

    let log = {
        date: "Loading",
        title: "Loading",
        description: "Loading",
        url: "Loading",
    };

    let logContent = "";

    async function getLog() {
        const json = await axios.get("https://raw.githubusercontent.com/grzi/grzi.dev/main/posts/src/" + $page.params.slug + "/meta.json");
        log = json.data;
        const html = await axios.get("https://raw.githubusercontent.com/grzi/grzi.dev/main/posts/src/" + $page.params.slug + "/blog-post.html");
        logContent = html.data;
    }

    onMount(async () => {
        await getLog();
    });
</script>
<div class="home">
    <div>
        <h3 class="title">Grzi.dev</h3>
        <div>
            <a href="/">About</a> - <a href="/logs">Logs</a>
        </div>
        <div class="log">
            <div class="log-title">
                {log.title}
            </div>
            <div class="log-date">
                {log.date}
            </div>
            <div class="log-content">
                {@html logContent}
            </div>
        </div>
    </div>
</div>

<style>
    .title {
        font-weight: 500;
    }

    .home {
        height: 100%;
        width: 768px;
        margin: auto;
        font-weight: 200;
        font-size: 14px;
        display: flex;
        flex-direction: row;
    }

    @media (max-width: 1024px) {
        .home {
            width: 90%;
            margin:auto;
        }
    }

    .log {
        display: flex;
        flex-direction: column;
        gap: 15px;
        margin-top: 42px;
    }

    .log-title {
        font-weight: 600;
        font-size: 24px;
        text-align: center;
    }

    .log-date {
        text-align: center;
        font-weight: 100;
        font-size: 12px;
    }
</style>