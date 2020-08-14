package com.example.movie.adapter;

import android.content.Context;
import android.graphics.Color;
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
import com.example.movie.contact.ItemCategory;
import com.example.movie.contact.Video;
import com.example.movie.inter_face.IOnClickVideo;
import com.example.movie.inter_face.PutInSQL;

import java.util.ArrayList;
import java.util.List;

public class ItemCategoryAdapter extends RecyclerView.Adapter<ItemCategoryAdapter.ViewHoder> {
    List<ItemCategory> items;
    Context context;
    PutInSQL putInSQL;
    ArrayList<ItemCategory>  categoryArrayList;
    IOnClickVideo clickVideo;
    int choise=-1;
    int auto=-1;

    public void setPutInSQL(PutInSQL putInSQL) {
        this.putInSQL = putInSQL;
    }

    public void setClickVideo(IOnClickVideo clickVideo) {
        this.clickVideo = clickVideo;
    }

    public ItemCategoryAdapter(List<ItemCategory> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemCategoryAdapter.ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_category, parent, false);
        ViewHoder viewHoder = new ViewHoder(view);
        context = parent.getContext();
        return viewHoder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemCategoryAdapter.ViewHoder holder, final int position) {
        final ItemCategory itemCategory = items.get(position);
        holder.tvNameVideo.setText(itemCategory.getName());
        holder.tvTimeCreate.setText(itemCategory.getDate());
        Glide.with(context).load(itemCategory.getAvatar()).into(holder.imgAvatar);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickVideo.playVideo(itemCategory.getUrl());
                choise=position;
                notifyDataSetChanged();
            }
        });

        // highlight choise
        if(choise==position)
        {
            holder.layout.setBackgroundColor(Color.parseColor("#979393"));
        }
        else
        {
            holder.layout.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        //highlight auto
//        if(auto==position)
//        {
//            holder.layout.setBackgroundColor(Color.parseColor("#979393"));
//        }
//        else
//        {
//            holder.layout.setBackgroundColor(Color.parseColor("#ffffff"));
//        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder {
        ImageView imgAvatar;
        TextView tvNameVideo;
        TextView tvTimeCreate;
        RelativeLayout layout;

        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            tvNameVideo = itemView.findViewById(R.id.tvNameItem);
            tvTimeCreate = itemView.findViewById(R.id.tvTime);
            imgAvatar = itemView.findViewById(R.id.imgItemCategory);
            layout = itemView.findViewById(R.id.relativeItem);
        }
    }
}
