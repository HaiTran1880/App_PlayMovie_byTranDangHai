package com.example.movie.ui.categories;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie.R;
import com.example.movie.adapter.CategoriesAdapter;
import com.example.movie.contact.Categories;
import com.example.movie.define.DefineURL;
import com.example.movie.inter_face.IOnClickItemCategories;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class CategoriesFragment extends Fragment {
    private static final String TAG = "CategoriesFragment";
    RecyclerView recyclerView;
    ArrayList<Categories> categories;
    String url = DefineURL.CATEGORY_URL;
    CategoriesAdapter categoriesAdapter;
    IOnClickItemCategories listener;

    public static CategoriesFragment newInstance() {

        Bundle args = new Bundle();
        CategoriesFragment fragment = new CategoriesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        categories = new ArrayList<>();
        recyclerView = view.findViewById(R.id.rvCategories);
        new getData(url).execute();
        return view;
    }

    class getData extends AsyncTask<Void, Void, Void> {
        String newurl;
        String json = "";

        public getData(String url) {
            this.newurl = url;
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
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                JSONArray array = new JSONArray(json);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    String img = object.getString("thumb");
                    String name = object.getString("title");
                    categories.add(new Categories(img, name, ""));
                }
                categoriesAdapter = new CategoriesAdapter(categories, getContext());
                categoriesAdapter.setIonClickCategories(new IOnClickItemCategories() {
                    @Override
                    public void getItemCategory(int a) {
                        listener.getItemCategory(1);
                    }
                });
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1, RecyclerView.VERTICAL, false);
                recyclerView.setAdapter(categoriesAdapter);
                recyclerView.setLayoutManager(layoutManager);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IOnClickItemCategories) {
            listener = (IOnClickItemCategories) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement");
        }
    }
}