package com.example.movie.ui.item_category;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.example.movie.define.DefineURL;
import com.example.movie.define.Define_Methods;
import com.example.movie.inter_face.IOnClickItemCategories;
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

public class ItemCategoryFragment extends Fragment {
    ArrayList<ItemCategory> list;
    ItemCategoryAdapter adapter;
    RecyclerView recyclerView;
    String url = DefineURL.ITEM_CATEGORY_URL;
    IOnClickVideo listener;
    ActionBar actionBar;

    SQLHelper sqlHelper;
    ArrayList<ItemCategory> listSQL;
    Define_Methods define_methods = new Define_Methods();
    private static final String TAG = "FragmentItemCategory";

    public static ItemCategoryFragment newInstance() {

        Bundle args = new Bundle();
        ItemCategoryFragment fragment = new ItemCategoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_item_category, container, false);
        recyclerView = view.findViewById(R.id.rvListItemCategory);

        list = new ArrayList<>();
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
                    String time = object.getString("date_created");
                    String name = object.getString("title");
                    String url = object.getString("file_mp4");
                    list.add(new ItemCategory(avt, name, time, url));
                }
                adapter = new ItemCategoryAdapter(list, getContext());
                adapter.setPutInSQL(new PutInSQL() {
                    @Override
                    public void putintoSQL(ItemCategory itemCategory) {
//                        sqlHelper = new SQLHelper(getContext());
//                        listSQL = sqlHelper.getAll();
//                        ItemCategory item = new ItemCategory(itemCategory.getAvatar(), itemCategory.getName(), itemCategory.getDate(), itemCategory.getUrl());
//                        if (listSQL.isEmpty() == false && define_methods.CHECK(item.getName(), listSQL)) {
//                            sqlHelper.deleteItem(item.getName());
//                        }
//                        sqlHelper.insertItem(item);
                    }
                });
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
//                        sqlHelper = new SQLHelper(getContext());
//                        listSQL = sqlHelper.getAll();
//                        ItemCategory item = new ItemCategory(video.getAvatar(), video.getNameVideo(), video.getTimeCreat(), video.getUrl());
//                        if (listSQL.isEmpty() == false && define_methods.CHECK(item.getName(), listSQL)) {
//                            sqlHelper.deleteItem(item.getName());
//                        }
//                        sqlHelper.insertItem(item);
                    }
                });
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1, RecyclerView.VERTICAL, false);
                recyclerView.setAdapter(adapter);
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
