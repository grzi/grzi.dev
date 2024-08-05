<script>
    let mock = {
        date: "2024-05-11",
        title: "From design, to application",
        description: "Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet ",
        url: "Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet ",
    };
</script>
<div class="home">
    <div>
        <h3 class="title">Grzi.dev</h3>
        <div>
            <a href="/">About</a> - <a href="/logs">Logs</a>
        </div>
        <div class="log">
            <div class="log-title">
                {mock.title}
            </div>
            <div class="log-date">
                {mock.date}
            </div>
            <div class="log-content">
                <p>Recently, due to COVID-19, I returned to this awesome game called "Skyrim". At one point, I had to find an item that I accidentally lost. It was the golden claw that I stole from Riverhood after the quest.</p>

                <p>I collect all the claws, so I needed to find it. I just didn't want to give myself a new one, so I decided to find it. No one has time to look all over Skyrim. So, to find a lost object, do the following:</p>

                <ul>
                    <li>Go to the console and tip <code>save test 1</code> then enter. (This will take some seconds, and will open a notepad with your game save)</li>
                    <li>Look in the opened save for the refID of your item</li>
                    <li>Go to the console and tip <code>player.moveto &lt;refID&gt;</code> (this will move you to the place where you lost your item)</li>
                </ul>

                <p>As far as I'm concerned, the golden claw was in the evidences chest in Whiterun, I don't remember why, but at least I've found where it was.</p>
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
    .log-date{
        text-align: center;
        font-weight: 100;
        font-size: 12px;
    }

    .log-content{
        text-align: justify;
    }

    .log-content h5{
        font-weight: 500;
        font-size: 14px;
    }
    .log-content h4{
        font-weight: 400;
        font-size: 16px;
    }
    .log-content h3{
        font-weight: 400;
    }
    .log-content h2{
        font-weight: 400;
    }
    .log-content h1{
        font-weight: 400;
    }

    .log-content p{
        margin-top: 20px;
        margin-bottom: 20px;
    }

    .log-content .legend{
        font-weight: 100;
        font-size: 10px;
        text-align: center;
        font-style: italic;
    }

    .log-content img{
        width:100%;
        border: 2px solid #A2A2A2;
    }

    .log-content iframe{
        width: 100%;
        height: 500px;
    }

    b{
        font-weight: 500;
    }
</style>