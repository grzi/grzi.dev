package dev.grzi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.grzi.representations.Article;
import dev.grzi.representations.ArticleSummary;
import dev.grzi.representations.Tag;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@ApplicationScoped
public class BlogService {

    @Inject
    @RestClient
    private GithubClient githubClient;

    private final ObjectMapper mapper = new ObjectMapper();

    public Set<ArticleSummary> findAll(String tag, Integer page){
        try {
            return Set.of(mapper.readValue(githubClient.findAll(), ArticleSummary[].class));
        } catch (JsonProcessingException | WebApplicationException e) {
            return Collections.emptySet();
        }
    }

    public Optional<Article> findByUri(String uri) {
        try {
            return findArticleSummaryFromUri(uri)
                    .map(articleSummary ->  {
                        Article article = new Article(articleSummary);
                        article.setContent(githubClient.findContentByPath(article.getPath()));
                        return Optional.of(article);
                    }).orElse(Optional.empty());
        } catch (WebApplicationException e) {
            return Optional.empty();
        }
    }

    public Optional<String> findTitleByUri(String uri) {
        try {
            return findArticleSummaryFromUri(uri)
                .map(articleSummary ->  Optional.of(articleSummary.getTitle()))
                        .orElse(Optional.empty());
        } catch (WebApplicationException e) {
            return Optional.empty();
        }
    }

    private Optional<ArticleSummary> findArticleSummaryFromUri(String uri){
        return findAll(null, null).stream()
                .filter(article -> uri.equals(article.getUri()))
                .findFirst();
    }

    public Set<Tag> findTags() {
        return Collections.emptySet();
    }
}
