package dev.grzi.representations;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Images {
    private String miniatureUrl;
    private String highResolutionUrl;
    private String credits;

    public String getMiniatureUrl() {
        return miniatureUrl;
    }

    public void setMiniatureUrl(String miniatureUrl) {
        this.miniatureUrl = miniatureUrl;
    }

    public String getHighResolutionUrl() {
        return highResolutionUrl;
    }

    public void setHighResolutionUrl(String highResolutionUrl) {
        this.highResolutionUrl = highResolutionUrl;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Images images = (Images) o;
        return Objects.equals(miniatureUrl, images.miniatureUrl) && Objects.equals(highResolutionUrl, images.highResolutionUrl) && Objects.equals(credits, images.credits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(miniatureUrl, highResolutionUrl, credits);
    }
}
