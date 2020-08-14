package com.example.movie.inter_face;

import com.example.movie.contact.Video;

public interface IOnClickVideo {
    void playVideo(String url);
    void putInSql(Video video);
}
