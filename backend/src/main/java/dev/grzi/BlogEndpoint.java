package dev.grzi;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.grzi.representations.Article;
import dev.grzi.representations.ArticleSummary;
import dev.grzi.representations.Tag;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Set;

@Path("/blog/")
public class BlogEndpoint {

    @Inject
    private BlogService blogService;

    @GET
    public Set<ArticleSummary> findAll(@QueryParam("tag") String tag, @QueryParam("page") Integer page) {
        return blogService.findAll(tag, page);
    }

    @GET
    @Path("posts/{uri}")
    @Produces(MediaType.APPLICATION_JSON)
    public Article findByUri(@PathParam("uri") String uri) throws JsonProcessingException {
        return blogService.findByUri(uri);
    }

    @GET
    @Path("posts/{uri}/title")
    @Produces(MediaType.TEXT_PLAIN)
    public String findTitleByUri(@PathParam("uri") String uri) throws JsonProcessingException {
        return blogService.findTitleByUri(uri);
    }

    @GET
    @Path("/tags")
    public Set<Tag> findTags(@PathParam String uri) {
        return blogService.findTags();
    }

}