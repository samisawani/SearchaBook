package com.weezee.searchabook;

public class Book {
    private String description;
    private float rating;
    private String thumbnailUrl;
    private String title;
    private String infoUrl;

    public Book(String description, float rating, String thumbnailUrl, String title, String infoUrl) {
        this.description = description;
        this.rating = rating;
        this.thumbnailUrl = thumbnailUrl;
        this.title = title;
        this.infoUrl=infoUrl;
    }

    public String getDescription() {
        return description;
    }

    public float getRating() {
        return rating;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getInfoUrl() {
        return infoUrl;
    }
}
