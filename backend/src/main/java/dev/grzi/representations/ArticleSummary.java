package dev.grzi.representations;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ArticleSummary {
    private String title;
    private String description;
    private String url;
    private String tag;
    private String date;
    private String author;
    private boolean published;
    private Images images;
    private String path;
    

    public String title() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ArticleSummary) obj;
        return Objects.equals(this.title, that.title) &&
                Objects.equals(this.description, that.description) &&
                Objects.equals(this.url, that.url) &&
                Objects.equals(this.tag, that.tag) &&
                Objects.equals(this.date, that.date) &&
                Objects.equals(this.author, that.author) &&
                this.published == that.published &&
                Objects.equals(this.images, that.images) &&
                Objects.equals(this.path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, url, tag, date, author, published, images, path);
    }

    @Override
    public String toString() {
        return "ArticleSummary[" +
                "title=" + title + ", " +
                "description=" + description + ", " +
                "url=" + url + ", " +
                "tag=" + tag + ", " +
                "date=" + date + ", " +
                "author=" + author + ", " +
                "published=" + published + ", " +
                "images=" + images + ", " +
                "path=" + path + ']';
    }

}
