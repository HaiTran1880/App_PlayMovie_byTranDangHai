package com.example.movie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movie.R;
import com.example.movie.contact.Video;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class AdapterSlide extends SliderViewAdapter<AdapterSlide.SliderAdapterVH> {
    Context context;

    public AdapterSlide(Context context) {
        this.context = context;
    }

    @Override
    public AdapterSlide.SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.slide_banner, null);
        return new AdapterSlide.SliderAdapterVH(inflate);

    }

    @Override
    public void onBindViewHolder(AdapterSlide.SliderAdapterVH viewHolder, int position) {
        switch (position) {
            case 0:
                Glide.with(context).load("https://myexperience673242261.files.wordpress.com/2018/05/7ca50-civil_war.jpg").into(viewHolder.imageView);
                break;
            case 1:
                Glide.with(context).load("https://designer.com.vn/wp-content/uploads/2017/07/poster-phim-hanh-dong.jpg").into(viewHolder.imageView);
                break;
            case 2:
                Glide.with(context).load("https://www.brandsvietnam.com/upload/forum2/2019/10/20171_1_1571803347.jpg").into(viewHolder.imageView);
                break;
            case 3:
                Glide.with(context).load("https://cdn.voh.com.vn/voh/Image/2019/08/03/1564652863f1fihv_20190803150512.jpg").into(viewHolder.imageView);
                break;
            case 4:
                Glide.with(context).load("https://designer.com.vn/wp-content/uploads/2017/07/poster-phim-kinh-di.jpg").into(viewHolder.imageView);
                break;
            default:
                Glide.with(context).load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSAi07qAkalxia21_rdnkcCtJSP9DTc0wGrTSgGStOCMBXTXhNX&s").into(viewHolder.imageView);
                break;

        }
    }

    @Override
    public int getCount() {
        return 5;
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {
        ImageView imageView;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageBanner);
        }
    }
}
