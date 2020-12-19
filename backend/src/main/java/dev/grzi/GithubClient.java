package dev.grzi;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/grzi/grzi.dev/main")
@RegisterRestClient(configKey="github-api")
public interface GithubClient{

    @GET
    @Path("posts/global_meta.json")
    @Produces("text/plain")
    String findAll();

    @GET
    @Path("/{uri}/blog-post.html")
    @Produces("text/plain")
    String findContentByUri(@PathParam("uri") String uri);
}
