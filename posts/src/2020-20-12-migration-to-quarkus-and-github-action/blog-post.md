
# Migrating my backend to quarkus / github, my journey

## Summary <a name="summary"></a>

This blog was started with a different tech stack. It was previously based on GCP firebase and storage. A bit overwhelming for a simple blog, isn't it ?
Once again, it was not chosen to fit any needs, but to fit my curiosity.
I made a <a href="https://www.grzi.dev/blog/wootlab-io-architecture-c4-model" target="blank">blog post</a> about the old architecture behind my blog.

But, the old domain name (wootlab.io) is getting to an end, and, as I wanted to go for a new one, I though it was time to test new things ! This time I'll try to make it fit my needs:

- **Get ride of most of my GCP coupling :**
    - Google storage => github static files
    - Google functions => github actions
    - Blog posts storage => github static files && local cache from the back
- **Migrate from Micronaut to Quarkus**
    - Make the new Api / check OpenApi coupling
    - Rest client
    - Simple security
    - Local caching

So, the steps to follow for my migration were the following :

1. Migrate the blog post data pipeline to github action
2. Migrate the static storage content (mostly images) to github
3. Migrate my backend to Quarkus


## Migrate the blog post data pipeline <a href="data-pipeline"></a>

**So, what's the deal ?**

I still want to use markdown as my main language to edit posts. It's easy, and I'm used to it because I also use this to take notes every day.
If I receive an update on any {blog_post_name}[blog_post.md | meta.json] the workflow begin for {blog post_name}.

First I need to have a simple script that print a converted markdown to html and print it to the standard output.

**Python is installed on the ubuntu environment on Github Actions, so:**

```python
def main():
    with open(sys.argv[1], 'r') as file:
        data = file.read()
        print(markdown2.markdown(data, extras=["fenced-code-blocks", "full_yaml_metadata"]))

main()
```

Then, I need to handle my global meta with the following rules :
- Read the ```global_meta.json``` file
- If it doesn't exist, push the current meta.json in an array into ```global_meta.json```
- Else, search in the ```global_meta.json``` if the current blog post is present (search done by uri key)
    - If found then replace it
    - Else just push it at the end of the array

**To do so, I'll use another python3 script:**

```python
def main():
    global_meta_json = json.loads('[]')
    exists = os.path.exists(sys.argv[1])
    if exists == True:
        with open(sys.argv[1], 'r', encoding='utf8') as global_meta:
            global_meta_json = json.loads(global_meta.read())


    with open(sys.argv[2], 'r', encoding='utf8') as modified_meta:
        modified_meta_json = json.loads(modified_meta.read())
        modified_meta_json['path'] = sys.argv[3]
        found = False
        for i in global_meta_json:
            if i['uri'] == modified_meta_json['uri']:
                i = modified_meta_json
                found = True
                break

        if found == False:
            global_meta_json.append(modified_meta_json)

        print(json.dumps(global_meta_json, sort_keys=True, indent=4, ensure_ascii=False))

main()
```

Finally, I just need to call these scripts for each modified blog post, write the result into the appropriate files and push the modifications to the repository.

**I'll just do it in a sh run :**

```sh
  pip install markdown2
  pip install pygments

  git config user.email ${{secrets.MAIL}}
  git config user.name grzi
  git remote add upstream_secured https://grzi:${{secrets.GITHUB_TOKEN}}@github.com/grzi/grzi.dev > /dev/null 2>&1

  commit_id=`git log --format="%H" -n 1`
  modified=`git diff-tree -no-commit-id --name-only -r $commit_id | awk -F "/*[^/]*/*$" '{ print ($1 == "" ? "." : $1); }' | sort --unique | grep 'posts/src/'`

  for i in $modified; do
    python3 scripts/markdown.py $i/blog-post.md > $i/blog-post.html
    global_meta_updated=`python3 scripts/global_meta.py posts/global_meta.json $i/meta.json $i`
    echo $global_meta_updated>posts/global_meta.json
    git add $i/blog-post.html
  done

  git add posts/global_meta.json

  git commit -m "chore: Convert modified blog-post(s) to html and update global_meta.json"
  git push -u upstream_secured HEAD:main
```

You can find <a href="https://github.com/grzi/grzi.dev/blob/main/.github/workflows/meta-or-blog-post-updated.yml" target="blank">here</a> the full Github action workflow file.

And that's it. Launching the workflow with this gave me <a href="https://github.com/grzi/grzi.dev/commit/5b5c428d974405366398f1cb55f675a11d99b918"  target="blank"/>this kind of commit</a>.

## Migrate the static storage content (mostly images) to github  <a href="#storage"></a>

Mostly consist of copy pasting from google storage to github repository and updating src tags from my blog posts. Nothing more interesting to share.
I created a <a href="https://github.com/grzi/grzi.dev.statics"  target="blank"/>repository</a> to do this.

## Migrate from Micronaut to Quarkus <a href="quarkus"></a>

### Init the project

Let's go ! Just simply following this <a href="https://quarkus.io/guides/getting-started#bootstrapping-the-project"  target="blank"/>guide</a>

I use the maven plugin to create the project.

```sh
mvn io.quarkus:quarkus-maven-plugin:1.10.5.Final:create \
    -DprojectGroupId=dev.grzi \
    -DprojectArtifactId=backend \
    -DclassName="dev.grzi.App" \
    -Dpath="/hello"
```

### Create the api

So, looking at my old api, I need the following paths :

- GET /blog/posts
- GET /blog/posts/{uri}
- GET /blog/posts/{uri}/title
- GET /blog/tags

How simple is that ...

Let's do the api, calling an empty blog service for now :

```java
@Path("/blog/")
public class BlogResource {
    @Inject
    private BlogService blogService;
  
    @GET
    @Path("/posts/")
    @PermitAll
    public BlogPostSummaryAndPage findAll(@QueryParam("tag") String tag, @QueryParam("page") Integer page) {
      return new BlogPostSummaryAndPage(blogService.findAll(tag, page, true), blogService.findPageNb(tag) );
    }
  
    @GET
    @Path("posts/{uri}")
    @PermitAll
    public Response findByUri(@NotNull @PathParam("uri") String uri) {
      return blogService.findByUri(uri)
              .map(article -> Response.ok(article))
              .orElse(Response.status(Response.Status.NOT_FOUND)).build();
    }
  
    @GET
    @Path("posts/{uri}/title")
    @PermitAll
    @Produces(MediaType.TEXT_PLAIN)
    public Response findTitleByUri(@PathParam("uri") String uri) {
      return blogService.findTitleByUri(uri)
              .map(title -> Response.ok(title))
              .orElse(Response.status(Response.Status.NOT_FOUND)).build();
    }
  
    @GET
    @PermitAll
    @Path("/tags")
    public Set<Tag> findTags(@PathParam String uri) {
      return blogService.findTags();
    }
}
```

Now I just quickly try to run this with ```./mvnw compile quarkus:dev``` and then test the GET blog/posts/ endpoints to see if I get that empty set.

### Using Open Api && swagger UI

I don't really need to expose an open API contract for my backend. But I want to test a bit some features to see how it's handled by Quarkus.

To play with open Api, I go to this <a href="https://quarkus.io/guides/openapi-swaggerui"  target="blank"/>guide</a>.

I just run this command to add the dependency provided by Quarkus ```./mvnw quarkus:add-extension -Dextensions="quarkus-smallrye-openapi"```

And I restart my App. I then go to ```http://localhost:8080/openapi``` and I have the exposed contract of which here is an extract :

```yaml
---
openapi: 3.0.3
info:
  title: Generated API
  version: "1.0"
paths:
  /blog:
    get:
      parameters:
      - name: page
        in: query
        schema:
          format: int32
          type: integer
      - name: tag
        in: query
        schema:
          type: string
      responses:
        "200":
          description: OK
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/SetBlogPostSummary'
  /blog/posts/{uri}:
    get:
      parameters:
      - name: uri
        in: path
        required: true
        schema:
          type: string
      responses:
        "200":
          description: OK
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/BlogPost'
    parameters:
    - name: uri
      in: path
      required: true
      schema:
        type: string
components:
  schemas:
    BlogPostSummary:
      type: object
      properties:
        author:
          type: string
        date:
          type: string
        description:
          type: string
        images:
          $ref: '#/components/schemas/Images'
        path:
          type: string
        published:
          type: boolean
        tag:
          type: string
        title:
          type: string
        url:
          type: string
```

It's really easy !
By adding the ```quarkus.swagger-ui.always-include=true``` configuration, I can see the following swagger UI interface :


<img src="https://raw.githubusercontent.com/grzi/grzi.dev.statics/main/swagger_ui.png" alt="swagger_ui_picture"/>


### Creating a REST client for Github API

I need a REST client to call the Github Api. To be precise, I only need to call these endpoints :

- GET https://{url}/global_meta.json
- GET https://{url}/src/{uri}/blog-post.html

Each of these returns a raw content (String).

Once again, I search, find and follow an awesome <a href="https://quarkus.io/guides/rest-client"  target="blank"/>guide</a>.

I start by adding the needed dependencies with ```./mvnw quarkus:add-extension -Dextensions="rest-client,resteasy-jackson"```

I then create the Interface as said in the tutorial :

```java
@Path("/grzi/grzi.dev/main")
@RegisterRestClient(configKey="github-api")
public interface GithubClient{

    @GET
    @Path("posts/global_meta.json")
    @Produces("text/plain")
    String findAll();

    @GET
    @Path("/{path}/blog-post.html")
    @Produces("text/plain")
    String findContentByPath(@PathParam("path") String path);
}
```

I simply link the previous empty service to call this client, and I'm ready to go.

```java
@ApplicationScoped
public class BlogService {

    @Inject
    @RestClient
    private GithubClient githubClient;

    private final ObjectMapper mapper = new ObjectMapper();

    public Set<BlogPostSummary> findAll(String tag, Integer page){
        try {
            return Set.of(mapper.readValue(githubClient.findAll(), BlogPostSummary[].class));
        } catch (JsonProcessingException | WebApplicationException e) {
            return Collections.emptySet();
        }
    }
    ...
}
```

### Adding a cache

As I'm getting the metas informations from only one endpoint and it is for ALL the blog posts, I want to have a flushable local cache on my 'findAll' method in my BlogService. Flushable because I want to be able to manually flush it when I update any blog post.

For this, I'll follow this <a href="https://quarkus.io/guides/cache"  target="blank"/>guide</a>.

I start by adding the dependency with ```./mvnw quarkus:add-extension -Dextensions="cache"```.

Then I add the Cache annotation to the desired method :

```java
    @CacheResult(cacheName = "find-all-cache")
    public Set<BlogPostSummary> findAll(String tag, Integer page){
```

And finally, I create a method with the InvalidateAll, that will later be called by a secured endpoint :

```java
    @CacheInvalidateAll(cacheName = "find-all-cache")
    public void flushCache(){}
```

To test this, I simply add a debug breakpoint inside the findAll. When starting the app and making a call, I should get it only one time (And that's the case).

### Secured endpoint

To be able to trigger the cache flushing, I need to create a simple secured endpoint.

I follow this guide for this.

First, I add the following dependency ``` quarkus-elytron-security-properties-file```

I then enable security and configure my user in the application.properties :

```properties
quarkus.http.auth.basic=true
quarkus.security.users.embedded.plain-text=true
quarkus.security.users.embedded.enabled=true
quarkus.security.users.embedded.users.github=password
quarkus.security.users.embedded.roles.github=FLUSHER
```

I configure the previous endpoints to permitAll, and add a secured endpoint :

```java
    @GET
    @Path("/posts/")
    @PermitAll
    public Set<BlogPostSummary> findAll(@QueryParam("tag") String tag, @QueryParam("page") Integer page) {
        return blogService.findAll(tag, page);
    }

    @PATCH
    @RolesAllowed("FLUSHER")
    public void flushCache(){
        blogService.flushCache();
    }
```

Right now, calling without any auth informations :
- ```GET http://localhost:8080/blog/posts``` returns 200
- ```PATCH http://localhost:8080/blog/flush-cache``` returns 401.

But if I add the auth informations : ```curl -i -X PATCH -u github:password http://localhost:8080/blog/flush-cache``` then it's flushing the cache


> And that's it ! I've got a new backend to use, in quarkus !! I certainly have a colleague that will be proud :P


