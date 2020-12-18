package dev.grzi.representations;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class Article extends ArticleSummary {
    private String next;
    private String previous;
    private String content;

    public Article() {
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Article) obj;
        return Objects.equals(this.next, that.next) &&
                Objects.equals(this.previous, that.previous) &&
                Objects.equals(this.content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(next, previous, content);
    }

    @Override
    public String toString() {
        return "Article[" +
                "next=" + next + ", " +
                "previous=" + previous + ", " +
                "content=" + content + ']';
    }

};

;


