package com.example.movie;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toolbar;

import com.example.movie.inter_face.IOnClickItemCategories;
import com.example.movie.ui.categories.CategoriesFragment;
import com.example.movie.ui.home.HomeFragment;
import com.example.movie.ui.hotmovie.HotMovieFragment;
import com.example.movie.ui.item_category.ItemCategoryFragment;
import com.example.movie.ui.user.FragmentHitstory;
import com.example.movie.ui.user.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.nio.file.Files;
import java.util.function.Consumer;

public class MainActivity extends AppCompatActivity implements IOnClickItemCategories {
    private static final String TAG = "MainActivity";
    ProgressBar bar;
    ActionBar actionBar;
    ImageView btHome,btCategory,btHotMovie,btUser,btSearch;
    EditText etSearch;
    String kqSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        getFragment(HomeFragment.newInstance());
        actionBar=getSupportActionBar();
        bar = findViewById(R.id.prBar);

        actionBar.hide();

        //navigation
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home: {
                        bar.setVisibility(View.VISIBLE);
                        getFragment(HomeFragment.newInstance());
                        break;
                    }
                    case R.id.nav_categories: {
                        bar.setVisibility(View.VISIBLE);
                        getFragment(CategoriesFragment.newInstance());
                        break;
                    }
                    case R.id.nav_hot_movie: {
                        bar.setVisibility(View.VISIBLE);
                        getFragment(HotMovieFragment.newInstance());
                        break;
                    }
                    case R.id.nav_user: {
                        bar.setVisibility(View.INVISIBLE);
                        getFragment(UserFragment.newInstance());
                        break;
                    }
                }
                return false;
            }
        });

    }

    public void getFragment(Fragment fragment) {
        try {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "getFragment: " + e.getMessage());
        }
    }

    @Override
    public void getItemCategory(int a) {
        if (a == 1)
            actionBar.hide();
            getFragment(ItemCategoryFragment.newInstance());
    }

// checking internet
    @Override
    protected void onResume() {
        super.onResume();
        if(isOnline()==true)
        {
            getFragment(HomeFragment.newInstance());
        }
        else
        {
            loadDialog();
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
    public void loadDialog(){
        AlertDialog.Builder dialog= new AlertDialog.Builder(this);
        dialog.setTitle("Lost Internet");
        dialog.setMessage("Checking again");
        dialog.setPositiveButton("Go to Setting", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(Settings.ACTION_SETTINGS));
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.show();
    }

}

