package com.example.movie.contact;

public class Categories {
    String imgCategory, name;
    String url;

    public Categories(String imgCategory, String name, String url) {
        this.imgCategory = imgCategory;
        this.name = name;
        this.url = url;
    }

    public String getImgCategory() {
        return imgCategory;
    }

    public void setImgCategory(String imgCategory) {
        this.imgCategory = imgCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
