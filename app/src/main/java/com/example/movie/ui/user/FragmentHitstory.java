package com.example.movie.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie.PlayMovie;
import com.example.movie.R;
import com.example.movie.SQLHelper;
import com.example.movie.adapter.ItemCategoryAdapter;
import com.example.movie.contact.ItemCategory;
import com.example.movie.contact.Video;
import com.example.movie.inter_face.IOnClickVideo;

import java.util.ArrayList;

public class FragmentHitstory extends Fragment {
    TextView btRefesh;
    ArrayList<ItemCategory> categoryArrayList;
    ArrayList<ItemCategory> list;
    TextView btRefresh;
    ItemCategoryAdapter adapter;
    RecyclerView recyclerView;
    SQLHelper sqlHelper;

    public static FragmentHitstory newInstance() {

        Bundle args = new Bundle();
        FragmentHitstory fragment = new FragmentHitstory();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_hitstory, container, false);
        btRefesh = view.findViewById(R.id.btRefresh);
        recyclerView = view.findViewById(R.id.listHistory);
        sqlHelper = new SQLHelper(getContext());
        //categoryArrayList = sqlHelper.getAll();
        list = new ArrayList<>();
        btRefesh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlHelper.deleteAll();
                categoryArrayList = sqlHelper.getAll();
                adapter = new ItemCategoryAdapter(categoryArrayList, getContext());
                recyclerView.setAdapter(adapter);
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1, RecyclerView.VERTICAL, false);
                recyclerView.setLayoutManager(layoutManager);
            }
        });

        /*int size = categoryArrayList.size();
        for (int i = size - 1; i >= 0; i--) {
            list.add(categoryArrayList.get(i));
        }*/

        adapter = new ItemCategoryAdapter(categoryArrayList, getContext());
        adapter.setClickVideo(new IOnClickVideo() {
            @Override
            public void playVideo(String url) {
                Intent intent = new Intent(getContext(), PlayMovie.class);
                String urlVideo = url;
                intent.putExtra("url", urlVideo);
                startActivity(intent);
            }

            @Override
            public void putInSql(Video video) {
            }
        });
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        return view;
    }
}
