package dev.grzi;

import dev.grzi.representations.BlogPostSummaryAndPage;
import dev.grzi.representations.Tag;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

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
    @Produces(MediaType.APPLICATION_JSON)
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

    @PATCH
    @Path("/flush-cache")
    @RolesAllowed("FLUSHER")
    public void flushCache(){
        blogService.flushCache();
    }
}