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
import com.example.movie.contact.Categories;
import com.example.movie.inter_face.IOnClickItemCategories;


import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHoder> {
    List<Categories> categories;
    Context context;
    IOnClickItemCategories onClickCategory;

    public void setIonClickCategories(IOnClickItemCategories onClickCategory) {
        this.onClickCategory = onClickCategory;
    }

    public CategoriesAdapter(List<Categories> categories, Context context) {
        this.categories = categories;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoriesAdapter.ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_categories, parent, false);
        ViewHoder viewHoder = new ViewHoder(view);
        context = parent.getContext();
        return viewHoder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
        final Categories cate = categories.get(position);
        holder.textView.setText(cate.getName());
        Glide.with(context).load(cate.getImgCategory()).into(holder.imageView);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCategory.getItemCategory(1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        RelativeLayout layout;

        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tvNameCategories);
            imageView = itemView.findViewById(R.id.imgCategories);
            layout = itemView.findViewById(R.id.rlCategories);
        }
    }
}
