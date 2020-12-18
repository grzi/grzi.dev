package dev.grzi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.grzi.representations.Article;
import dev.grzi.representations.ArticleSummary;
import dev.grzi.representations.Tag;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Collections;
import java.util.HashSet;
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
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Collections.emptySet();
        }

    }

    public Article findByUri(String uri) throws JsonProcessingException {
        try {
            Article article = mapper.readValue(githubClient.findMetaByUri(uri), Article.class);
            article.setContent(githubClient.findContentByUri(uri));
            return article;
        } catch (JsonProcessingException e) {
            throw e;
        }
    }

    public String findTitleByUri(String uri) throws JsonProcessingException {
        return mapper.readValue(githubClient.findMetaByUri(uri), ArticleSummary.class).title();
    }

    public Set<Tag> findTags() {
        return Collections.emptySet();
    }
}
