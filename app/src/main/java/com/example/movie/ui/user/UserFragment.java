package com.example.movie.ui.user;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.movie.R;
import com.example.movie.adapter.HotMovieAdapter;
import com.example.movie.ui.categories.CategoriesFragment;
import com.example.movie.ui.home.HomeFragment;
import com.example.movie.ui.hotmovie.HotMovieFragment;

public class UserFragment extends Fragment {
    TextView textView;
    ImageView btMenu;
    Context context;
    private static final String TAG = "MainActivity";


    public static UserFragment newInstance() {

        Bundle args = new Bundle();
        UserFragment fragment = new UserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        btMenu=view.findViewById(R.id.btUser);

        btMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getContext(), v);
                popupMenu.inflate(R.menu.popup_menu);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.nav_home) {
                            getFragment(HomeFragment.newInstance());
                        }
                        if (item.getItemId() == R.id.nav_History) {
                            getFragment(FragmentHitstory.newInstance());

                        }
                        if (item.getItemId() == R.id.nav_Favorite) {
                            getFragment(CategoriesFragment.newInstance());
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        return view;
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
