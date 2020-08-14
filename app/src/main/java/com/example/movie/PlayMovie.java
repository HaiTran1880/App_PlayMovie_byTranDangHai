package com.example.movie;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie.adapter.ItemCategoryAdapter;
import com.example.movie.contact.ItemCategory;
import com.example.movie.contact.Video;
import com.example.movie.define.DefineURL;
import com.example.movie.define.Define_Methods;
import com.example.movie.inter_face.IOnClickVideo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Timer;

public class PlayMovie extends AppCompatActivity {
    private static final String TAG = "StartVideo";
    String url = DefineURL.ITEM_CATEGORY_URL;
    ArrayList<ItemCategory> list;
    ItemCategoryAdapter adapter;
    VideoView videoView;
    RecyclerView recyclerView;

    SeekBar seekBar;
    ImageView btFull, btSmall, btPause, btPlay, btNext, btPrevious;
    RelativeLayout layout, controlPlay;
    TextView tvTimeEnd, tvTimeStart;
    ItemCategory itemPlaying;

    LinearLayout lnViewChangeVol, lnViewChangePosition;
    TextView tvCurrentVol;
    TextView tvgetCurrentPosision, tvDuration;

    float x1, y1;
    AudioManager audioManager;
    boolean reChangeVol = true;
    boolean reChangePosition = true;
    int maxVol, stepVol, currentVol;


    int getTimeCurrent;
    int position = 0;

    Switch autoPlay;
    Handler handler = new Handler();
    ActionBar actionBar;
    WindowManager windowManager;

    int timeCurrent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        //Action Bar
        actionBar = getSupportActionBar();
        actionBar.hide();

        //Video
        videoView = findViewById(R.id.vv);

        //Time Video Play
        tvTimeStart = findViewById(R.id.tvTimeBegin);
        tvTimeEnd = findViewById(R.id.tvTimeFinish);

        //Next List
        autoPlay = findViewById(R.id.swAuto);
        recyclerView = findViewById(R.id.rvPlayVideo);
        layout = findViewById(R.id.rlVideo);
        list = new ArrayList<>();

        //See Bar
        seekBar = findViewById(R.id.seekBar);
        btFull = findViewById(R.id.btFull);
        btSmall = findViewById(R.id.btSmall);
        btSmall.setVisibility(View.INVISIBLE);

        //Volume
        lnViewChangeVol = findViewById(R.id.lnViewChangeVol);
        tvCurrentVol = findViewById(R.id.tvCurrentVol);

        //Position
        lnViewChangePosition = findViewById(R.id.lnViewChangePosition);
        tvgetCurrentPosision = findViewById(R.id.tvCurrentPosition);
        tvDuration = findViewById(R.id.tvDuration);

        //Controller Play
        controlPlay = findViewById(R.id.controller);
        btPause = findViewById(R.id.btPause);
        btPlay = findViewById(R.id.btPlay);
        btNext = findViewById(R.id.btNext);
        btPrevious = findViewById(R.id.btPre);
        btPlay.setVisibility(View.INVISIBLE);

        //Start Video
        new getData(url).execute();
        String urlPlay = getIntent().getStringExtra("url");
        Uri uri = Uri.parse(urlPlay);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        //Show Controller
        ShowController_within_5s showController_delay = new ShowController_within_5s();
        handler.postDelayed(showController_delay, 5000);

        //SeeBar
        runnable.run();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    videoView.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Set Time Video
        tvTimeStart.setText(millisecondsToString(0));
        tvTimeEnd.setText(millisecondsToString(videoView.getDuration()));

        //Button Play Video
        btPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView.getCurrentPosition() == videoView.getDuration()) {
                    videoView.seekTo(videoView.getCurrentPosition());
                }
                videoView.start();
                btPlay.setVisibility(View.INVISIBLE);
                btPause.setVisibility(View.VISIBLE);
            }
        });

        //Button Pause Video
        btPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.pause();
                btPause.setVisibility(View.INVISIBLE);
                btPlay.setVisibility(View.VISIBLE);
            }
        });

        //Button Next Video
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btPlay.setVisibility(View.INVISIBLE);
                btPause.setVisibility(View.VISIBLE);
                ItemCategory item = list.get(0);
                if (position >= 0) {
                    Uri uri = Uri.parse(item.getUrl());
                    videoView.setVideoURI(uri);
                    videoView.requestFocus();
                    videoView.start();

                }
                list.remove(0);
                list.add(item);
                position++;
                itemPlaying = item;
            }
        });

        //Button Previous Video
        btPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btPlay.setVisibility(View.INVISIBLE);
                btPause.setVisibility(View.VISIBLE);
                if (position > 0) {
                    ItemCategory item = list.get(list.size() - 2);
                    Uri uri = Uri.parse(item.getUrl());
                    videoView.setVideoURI(uri);
                    videoView.requestFocus();
                    videoView.start();
                    position--;
                    itemPlaying = item;
                } else position = 0;
            }
        });

        //Button Full Screen
        btFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTimeCurrent = videoView.getCurrentPosition();
                btFull.setVisibility(View.INVISIBLE);
                btSmall.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) layout.getLayoutParams();
                params1.width = params.MATCH_PARENT;
                params1.height = params1.MATCH_PARENT;
                layout.setLayoutParams(params);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
                videoView.seekTo(getTimeCurrent);
            }
        });

        //Button Small Screen
        btSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btFull.setVisibility(View.VISIBLE);
                btSmall.setVisibility(View.INVISIBLE);


                getTimeCurrent = videoView.getCurrentPosition();
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) layout.getLayoutParams();
                params1.width = params.MATCH_PARENT;
                params1.height = 800;
                layout.setLayoutParams(params1);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                videoView.seekTo(getTimeCurrent);
            }
        });

        // setUp Volume and Position
        audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        stepVol = 30 / maxVol;
        currentVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        currentVol = currentVol * stepVol;
        tvCurrentVol.setText(String.valueOf(currentVol));

        //Touch Video
        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                switch (motionEvent.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = (int) motionEvent.getX();
                        y1 = (int) motionEvent.getY();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        tvDuration.setText(millisecondsToString(videoView.getDuration()));
                        if (Math.abs(motionEvent.getX() - x1) > 50) {
                            reChangeVol = false;
                        }
                        if (Math.abs(motionEvent.getY() - y1) > 50) {
                            reChangePosition = false;
                        }
                        if (Math.abs(motionEvent.getX() - x1) > 50 && reChangePosition) {
                            videoView.pause();
                            lnViewChangePosition.setVisibility(View.VISIBLE);
                            timeCurrent = videoView.getCurrentPosition()+ (int)motionEvent.getX()-(int)x1;
                            if(timeCurrent>=0&&timeCurrent<=videoView.getDuration())
                            {
                                tvgetCurrentPosision.setText(millisecondsToString(timeCurrent));
                            }
                            else if (timeCurrent<0)
                            {
                                tvgetCurrentPosision.setText("00:00");
                            }
                            else
                            {
                                tvgetCurrentPosision.setText(millisecondsToString(videoView.getDuration()));
                            }
                            videoView.seekTo(timeCurrent);

                        }

                        if (Math.abs(motionEvent.getY() - y1) > 50 && reChangeVol) {
                            lnViewChangeVol.setVisibility(View.VISIBLE);
                            if (motionEvent.getY() - y1 < 0 && currentVol < 30) {
                                currentVol++;
                                tvCurrentVol.setText(String.valueOf(currentVol));
                                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVol / stepVol, 0);
                            } else if (motionEvent.getY() - y1 > 0 && currentVol > 0) {
                                currentVol--;
                                tvCurrentVol.setText(String.valueOf(currentVol));
                                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVol / stepVol, 0);
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        videoView.start();
                        lnViewChangePosition.setVisibility(View.INVISIBLE);
                        lnViewChangeVol.setVisibility(View.INVISIBLE);
                        reChangeVol = true;
                        reChangePosition = true;
                        break;
                }
                controlPlay.setVisibility(View.VISIBLE);
                return false;
            }
        });

        //autoPlay list Video
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                adapter = new ItemCategoryAdapter(list, getBaseContext());
                if (autoPlay.isChecked() == true && list != null) {
                    ItemCategory item;
                    if (position >= 0 && position <= list.size()) {
                        item = list.get(position);
                        Uri uri = Uri.parse(item.getUrl());
                        videoView.setVideoURI(uri);
                        videoView.requestFocus();
                        videoView.start();
                        position++;
                    } else {
                        position = 0;
                        item = list.get(0);
                        Uri uri = Uri.parse(item.getUrl());
                        videoView.setVideoURI(uri);
                        videoView.requestFocus();
                        videoView.start();

                    }
                } else {

                }
            }
        });

    }

    // format time of video
    private String millisecondsToString(int milliseconds) {
        return new SimpleDateFormat("mm:ss").format(milliseconds);
    }

    //updating SeekBar
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (videoView != null) {
                int mCurrentPosition = videoView.getCurrentPosition();
                seekBar.setProgress(mCurrentPosition);
                int timeCurrent = videoView.getCurrentPosition();
                tvTimeStart.setText(millisecondsToString(timeCurrent));
                int timeEnd = videoView.getDuration();
                tvTimeEnd.setText(millisecondsToString(timeEnd));
                updateSeekBar();
            }
        }
    };

    private void updateSeekBar() {
        handler.postDelayed(runnable, 1000);
        seekBar.setMax(videoView.getDuration());
    }


    //List Next Video
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
                adapter = new ItemCategoryAdapter(list, getBaseContext());
                adapter.setClickVideo(new IOnClickVideo() {
                    @Override
                    public void playVideo(String url) {
                        Uri uri = Uri.parse(url);
                        videoView.setVideoURI(uri);
                        videoView.requestFocus();
                        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
                        videoView.start();
                    }

                    @Override
                    public void putInSql(Video video) {
                    }
                });
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getBaseContext(), 1, RecyclerView.VERTICAL, false);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(layoutManager);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(aVoid);
        }
    }

    // display Controller within 5s
    class ShowController_within_5s implements Runnable {
        @Override
        public void run() {
            handler.postDelayed(this, 5000);
            controlPlay.setVisibility(View.GONE);
        }
    }

}
