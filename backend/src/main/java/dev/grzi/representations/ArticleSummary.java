package dev.grzi.representations;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ArticleSummary {
    private String title;
    private String description;
    private String uri;
    private String tag;
    private String date;
    private String author;
    private boolean published;
    private Images images;
    private String path;
    private String next;
    private String previous;

    public ArticleSummary(String title, String description, String uri, String tag, String date, String author, boolean published, Images images, String path, String next, String previous) {
        this.title = title;
        this.description = description;
        this.uri = uri;
        this.tag = tag;
        this.date = date;
        this.author = author;
        this.published = published;
        this.images = images;
        this.path = path;
        this.next = next;
        this.previous = previous;
    }



    public ArticleSummary() {
    }

    public String title() {
        return title;
    }


    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUri() {
        return uri;
    }

    public String getTag() {
        return tag;
    }

    public String getDate() {
        return date;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isPublished() {
        return published;
    }

    public Images getImages() {
        return images;
    }

    public String getPath() {
        return path;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUri(String uri) {
        this.uri = uri;
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

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticleSummary that = (ArticleSummary) o;
        return published == that.published && Objects.equals(title, that.title) && Objects.equals(description, that.description) && Objects.equals(uri, that.uri) && Objects.equals(tag, that.tag) && Objects.equals(date, that.date) && Objects.equals(author, that.author) && Objects.equals(images, that.images) && Objects.equals(path, that.path) && Objects.equals(next, that.next) && Objects.equals(previous, that.previous);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, uri, tag, date, author, published, images, path, next, previous);
    }
}
