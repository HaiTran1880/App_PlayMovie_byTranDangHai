package com.example.movie.ui.hotmovie;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie.PlayMovie;
import com.example.movie.R;
import com.example.movie.SQLHelper;
import com.example.movie.adapter.HotMovieAdapter;
import com.example.movie.contact.ItemCategory;
import com.example.movie.contact.Video;
import com.example.movie.define.DefineURL;
import com.example.movie.define.Define_Methods;
import com.example.movie.inter_face.IOnClickItemCategories;
import com.example.movie.inter_face.IOnClickVideo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class HotMovieFragment extends Fragment {
    private static final String TAG = "HotMovieFragment";
    RecyclerView recyclerView;
    List<Video> videos;
    HotMovieAdapter hotMovieAdapter;
    String url = DefineURL.HOT_VIDEO_URL;
    IOnClickVideo listener;

    Define_Methods define_methods = new Define_Methods();
    SQLHelper sqlHelper;
    ArrayList<ItemCategory> listSQL;

    public static HotMovieFragment newInstance() {

        Bundle args = new Bundle();

        HotMovieFragment fragment = new HotMovieFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_hotmovie, container, false);
        recyclerView = view.findViewById(R.id.rvHotmovie);
        videos = new ArrayList<>();
        new getData(url).execute();
        return view;
    }

    class getData extends AsyncTask<Void, Void, Void> {
        String newurl;
        String json = "";

        public getData(String newurl) {
            this.newurl = newurl;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL(newurl);
                URLConnection connection = url.openConnection();
                InputStream inputStream = connection.getInputStream();
                int character;
                while ((character = inputStream.read()) != -1) {
                    json += (char) character;
                }
                Log.d(TAG, "doInBackground: " + json);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                JSONArray array = new JSONArray(json);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    String avt = object.getString("avatar");
                    String timeCreat = object.getString("date_created");
                    String name = object.getString("title");
                    String url = object.getString("file_mp4");
                    videos.add(new Video(avt, name, timeCreat, url));
                }
                hotMovieAdapter = new HotMovieAdapter(videos, getContext());
                hotMovieAdapter.setOnClickVideo(new IOnClickVideo() {
                    @Override
                    public void playVideo(String url) {
                        Intent intent = new Intent(getContext(), PlayMovie.class);
                        String urlVideo = url;
                        intent.putExtra("url", urlVideo);

                        startActivity(intent);
                    }

                    @Override
                    public void putInSql(Video video) {
                        sqlHelper = new SQLHelper(getContext());
//                        listSQL = sqlHelper.getAll();
//                        ItemCategory item = new ItemCategory(video.getAvatar(), video.getNameVideo(), video.getTimeCreat(), video.getUrl());
//                        if (listSQL.isEmpty() == false && define_methods.CHECK(item.getName(), listSQL)) {
//                            sqlHelper.deleteItem(item.getName());
//                        }
//                        sqlHelper.insertItem(item);

                    }
                });
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false);
                recyclerView.setAdapter(hotMovieAdapter);
                recyclerView.setLayoutManager(layoutManager);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(aVoid);
        }
    }

//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        if (context instanceof IOnClickVideo) {
//            listener = (IOnClickVideo) context;
//        } else {
//            throw new RuntimeException(context.toString() + "must implement");
//        }
//    }
}