package com.example.movie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movie.R;
import com.example.movie.contact.Video;
import com.example.movie.inter_face.IOnClickVideo;

import java.util.List;

public class AdapterHomeVideo extends RecyclerView.Adapter<AdapterHomeVideo.ViewHoder> {
    List<Video> videos;
    Context context;
    IOnClickVideo onClickVideo;

    public AdapterHomeVideo(List<Video> videos, Context context) {
        this.videos = videos;
        this.context = context;
    }

    public void setOnClickVideo(IOnClickVideo onClickVideo) {
        this.onClickVideo = onClickVideo;
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_video_home, parent, false);
        ViewHoder viewHoder=new ViewHoder(view);
        context = parent.getContext();
        return viewHoder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
        final Video video = videos.get(position);
        holder.tvNameVideo.setText(video.getNameVideo());
        holder.tvTimeCreate.setText(video.getTimeCreat());
        Glide.with(context).load(video.getAvatar()).into(holder.imgAvatar);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickVideo.playVideo(video.getUrl());
                onClickVideo.putInSql(video);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder {
        ImageView imgAvatar, btStart;
        TextView tvNameVideo;
        TextView tvTimeCreate;
        RelativeLayout layout;
        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            tvNameVideo = itemView.findViewById(R.id.tvNameHomeVideo);
            tvTimeCreate = itemView.findViewById(R.id.tvHomeTimeCreate);
            //btStart = itemView.findViewById(R.id.btStart);
            imgAvatar = itemView.findViewById(R.id.imgHomeMovie);
            layout = itemView.findViewById(R.id.rlHometMovie);
        }
    }
}
