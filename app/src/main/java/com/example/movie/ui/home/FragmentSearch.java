package com.example.movie.ui.home;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie.PlayMovie;
import com.example.movie.R;
import com.example.movie.adapter.HotMovieAdapter;
import com.example.movie.adapter.ItemCategoryAdapter;
import com.example.movie.contact.ItemCategory;
import com.example.movie.contact.Video;
import com.example.movie.define.DefineURL;
import com.example.movie.inter_face.IOnClickVideo;
import com.example.movie.inter_face.PutInSQL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class FragmentSearch extends Fragment {
    ArrayList<Video> list;
    HotMovieAdapter adapter;
    RelativeLayout rlFound;
    RecyclerView recyclerView;
    boolean check = true;
    String url = DefineURL.HOT_VIDEO_URL;
    IOnClickVideo listener;
    TextView resultFind;
    private static final String TAG = "SearchFragment";


    public static FragmentSearch newInstance(String titleSearch) {

        Bundle args = new Bundle();
        args.putString("titleSearch", titleSearch);
        FragmentSearch fragment = new FragmentSearch();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragmnent_search, container, false);
        recyclerView = view.findViewById(R.id.listSearch);
        resultFind = view.findViewById(R.id.tvResult);
        rlFound = view.findViewById(R.id.rlFound);
        String titleSearch = getArguments().getString("titleSearch");

        list = new ArrayList<>();
        new getData(url, titleSearch).execute();

        return view;
    }

    class getData extends AsyncTask<Void, Void, Void> {
        String newurl;
        String titleSearch;
        String json = "";

        public getData(String newurl, String titleSearch) {
            this.newurl = newurl;
            this.titleSearch = titleSearch;
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
                    String time = object.getString("date_created");
                    String name = object.getString("title");
                    String url = object.getString("file_mp4");
                    if (name.contains(titleSearch) == true) {
                        list.add(new Video(avt, name, time, url));
                    }
                }
                if (list.size() != 0) {
                    adapter = new HotMovieAdapter(list, getContext());
                    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1, RecyclerView.VERTICAL, false);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(layoutManager);

                    adapter.setOnClickVideo(new IOnClickVideo() {
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
                } else {
                    check = false;
                }

                if (check == false) {
                    recyclerView.setVisibility(View.INVISIBLE);
                    rlFound.setVisibility(View.VISIBLE);
                }

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
