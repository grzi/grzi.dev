package dev.grzi.representations;

import java.util.Set;

public class BlogPostSummaryAndPage {
    private Set<BlogPostSummary> posts;
    private long totalPages;

    public BlogPostSummaryAndPage() {
    }

    public BlogPostSummaryAndPage(Set<BlogPostSummary> posts, long totalPages) {
        this.posts = posts;
        this.totalPages = totalPages;
    }

    public Set<BlogPostSummary> getPosts() {
        return posts;
    }

    public long getTotalPages() {
        return totalPages;
    }
}

