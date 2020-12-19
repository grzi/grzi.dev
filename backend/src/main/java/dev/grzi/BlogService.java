package dev.grzi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.grzi.representations.BlogPost;
import dev.grzi.representations.BlogPostSummary;
import dev.grzi.representations.Tag;
import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class BlogService {

    @Inject
    @RestClient
    private GithubClient githubClient;

    private static final long PAGE_SIZE = 6;


    private final ObjectMapper mapper = new ObjectMapper();

    @CacheResult(cacheName = "find-all-cache")
    public Set<BlogPostSummary> findAll(String tag, Integer page, boolean limit){
        try {
            return Set.of(mapper.readValue(githubClient.findAll(), BlogPostSummary[].class))
                    .stream()
                    .filter(summary -> summary.isPublished())
                    .filter(summary -> tag == null || tag.equals(summary.getTag()))
                    .sorted(Comparator.comparing(BlogPostSummary::getDate).reversed())
                    .skip(page != null ? PAGE_SIZE * page : 0)
                    .limit(limit ? PAGE_SIZE: Integer.MAX_VALUE)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        } catch (JsonProcessingException | WebApplicationException e) {
            return Collections.emptySet();
        }
    }

    @CacheInvalidateAll(cacheName = "find-all-cache")
    public void flushCache(){}

    public Optional<BlogPost> findByUri(String uri) {
        try {
            return findArticleSummaryFromUri(uri)
                    .map(blogPostSummary ->  {
                        BlogPost article = new BlogPost(blogPostSummary);
                        var content = githubClient.findContentByPath(article.getPath());
                        article.setContent(content);
                        return Optional.of(article);
                    }).orElse(Optional.empty());
        } catch (WebApplicationException e) {
            return Optional.empty();
        }
    }

    public Optional<String> findTitleByUri(String uri) {
        try {
            return findArticleSummaryFromUri(uri)
                .map(blogPostSummary ->  Optional.of(blogPostSummary.getTitle()))
                        .orElse(Optional.empty());
        } catch (WebApplicationException e) {
            return Optional.empty();
        }
    }

    private Optional<BlogPostSummary> findArticleSummaryFromUri(String uri){
        return findAll(null, null, false).stream()
                .filter(article -> uri.equals(article.getUri()))
                .findFirst();
    }

    public Set<Tag> findTags() {
        return findAll(null, null, false).stream().collect(Collectors.groupingBy(BlogPostSummary::getTag))
                .entrySet().stream().map(entry -> new Tag(entry.getKey(), entry.getValue().size()))
                .collect(Collectors.toSet());
    }

    public long findPageNb(String tag) {
        return findAll(tag, null, false).stream().count() / PAGE_SIZE + 1;
    }
}
