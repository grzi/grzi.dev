package dev.grzi;

import dev.grzi.representations.ArticleSummary;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Set;

@Path("/grzi/grzi.dev/main/posts/")
@RegisterRestClient(configKey="github-api")
public interface GithubClient{

    @GET
    @Path("/global_meta.json")
    @Produces("text/plain")
    String findAll();

    @GET
    @Path("/{uri}/meta.json")
    @Produces("text/plain")
    String findMetaByUri(@PathParam("uri") String uri);

    @GET
    @Path("/{uri}/blog-post.html")
    @Produces("text/plain")
    String findContentByUri(@PathParam("uri") String uri);
}
