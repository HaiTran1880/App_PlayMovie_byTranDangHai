package com.example.movie.contact;

import java.io.Serializable;

public class ItemCategory {
    String avatar, name, date, url;

    public ItemCategory(String avatar, String name, String date, String url) {
        this.avatar = avatar;
        this.name = name;
        this.date = date;
        this.url = url;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
