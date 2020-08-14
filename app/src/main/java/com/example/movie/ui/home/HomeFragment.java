package com.example.movie.ui.home;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie.PlayMovie;
import com.example.movie.R;
import com.example.movie.SQLHelper;
import com.example.movie.adapter.AdapterHomeVideo;
import com.example.movie.adapter.AdapterSlide;
import com.example.movie.adapter.HotMovieAdapter;
import com.example.movie.contact.ItemCategory;
import com.example.movie.contact.Video;
import com.example.movie.define.DefineURL;
import com.example.movie.define.Define_Methods;
import com.example.movie.inter_face.IOnClickVideo;
import com.example.movie.ui.hotmovie.HotMovieFragment;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;

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

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    SliderView sliderView;
    AdapterSlide adapterSlide;
    RecyclerView recyclerViewHot, recyclerViewNew;
    String url = DefineURL.HOT_VIDEO_URL;
    List<Video> videos;

    ImageView btSearch, logo;
    EditText editText;
    boolean search = true;
    boolean checkNull = true;

    //    SQLHelper sqlHelper;
//    ArrayList<ItemCategory> listSQL;
//    Define_Methods define_methods = new Define_Methods();
//    HotMovieAdapter hotMovieAdapter;
    AdapterHomeVideo homeAdapter;

    public static HomeFragment newInstance() {

        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        sliderView = view.findViewById(R.id.svBanner);
        recyclerViewHot = view.findViewById(R.id.rlHomeHot);
        recyclerViewNew = view.findViewById(R.id.rlHomeNew);

        //Search
        btSearch = view.findViewById(R.id.btfind);
        editText = view.findViewById(R.id.etSearch);
        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (search == true) {
                    editText.setVisibility(View.VISIBLE);
                    search = false;
                } else {
                    String titleSearch = editText.getText().toString();
                    search = true;
                    if (titleSearch.equals("")) {
                        checkNull = false;
                    }
                    else
                    {
                        getFragment(FragmentSearch.newInstance(titleSearch));
                    }

                }
            }
        });


        // Banner
        adapterSlide = new AdapterSlide(getContext());
        sliderView.setSliderAdapter(adapterSlide);
        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(5); //set scroll delay in seconds :
        sliderView.startAutoCycle();

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
                homeAdapter = new AdapterHomeVideo(videos, getContext());
                homeAdapter.setOnClickVideo(new IOnClickVideo() {
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
//                hotMovieAdapter = new HotMovieAdapter(videos, getContext());
//                hotMovieAdapter.setOnClickVideo(new IOnClickVideo() {
//                    @Override
//                    public void playVideo(String url) {
//                        Intent intent = new Intent(getContext(), PlayMovie.class);
//                        String urlVideo = url;
//                        intent.putExtra("url", urlVideo);
//                        startActivity(intent);
//                    }
//
//                    @Override
//                    public void putInSql(Video video) {
//                        sqlHelper = new SQLHelper(getContext());
//                        ItemCategory item = new ItemCategory(video.getAvatar(), video.getNameVideo(), video.getTimeCreat(), video.getUrl());
//                        /*if (listSQL.isEmpty() == false && define_methods.CHECK(item.getName(), listSQL)) {
//                            sqlHelper.deleteItem(item.getName());
//                        }*/
//                        sqlHelper.insertItem(item);
//                    }
//                });
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1, RecyclerView.HORIZONTAL, false);
                RecyclerView.LayoutManager layoutManagerNew = new GridLayoutManager(getContext(), 1, RecyclerView.HORIZONTAL, false);
                recyclerViewHot.setAdapter(homeAdapter);
                recyclerViewHot.setLayoutManager(layoutManager);

                recyclerViewNew.setAdapter(homeAdapter);
                recyclerViewNew.setLayoutManager(layoutManagerNew);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(aVoid);
        }
    }

    public void getFragment(Fragment fragment) {
        try {
            getFragmentManager().beginTransaction().replace(R.id.container, fragment)
                    .commit();

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "getFragment: " + e.getMessage());
        }
    }
}