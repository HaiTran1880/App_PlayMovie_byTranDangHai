package com.example.movie.contact;

import android.widget.Button;

public class Video {
    String avatar, timeCreat, nameVideo;
    String url;
    Button btStart;

    public Video(String avatar, String nameVideo, String timeCreat, String url) {
        this.avatar = avatar;
        this.nameVideo = nameVideo;
        this.timeCreat = timeCreat;
        this.url = url;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTimeCreat() {
        return timeCreat;
    }

    public void setTimeCreat(String timeCreat) {
        this.timeCreat = timeCreat;
    }

    public String getNameVideo() {
        return nameVideo;
    }

    public void setNameVideo(String nameVideo) {
        this.nameVideo = nameVideo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Button getBtStart() {
        return btStart;
    }

    public void setBtStart(Button btStart) {
        this.btStart = btStart;
    }
}
