package com.videomaker.photowithsong.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.videomaker.photowithsong.R;
import com.videomaker.photowithsong.adapters.AdapterAblbumVideo;
import com.videomaker.photowithsong.objects.MyVideo;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;


public class MyVideoActivity extends AppCompatActivity {
    private RecyclerView recycleView;
    private AdapterAblbumVideo adapterAblbumVideo;
    private ArrayList<MyVideo> arrVideo = new ArrayList<>();
    private TextView textsave, titleappbar;
    private ImageView ivBack,ivNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my_video);
        init();
        new loadVideo().execute();
    }

    public void init() {
        recycleView = (RecyclerView) findViewById(R.id.rv_fr_albumvideo);
        textsave = (TextView) findViewById(R.id.tv_next);
        textsave.setVisibility(View.INVISIBLE);
        titleappbar = (TextView) findViewById(R.id.titleappbar);
        ivBack= (ImageView) findViewById(R.id.iv_back);
        ivNext= (ImageView) findViewById(R.id.iv_next);
        ivNext.setVisibility(View.INVISIBLE);
        titleappbar.setText(getString(R.string.my_video));
        arrVideo = new ArrayList<>();
        recycleView.setLayoutManager(new GridLayoutManager(getBaseContext(), 2));
        adapterAblbumVideo = new AdapterAblbumVideo(getBaseContext(), arrVideo);
        recycleView.setAdapter(adapterAblbumVideo);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    finish();
                }
        });
        titleappbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    public class loadVideo extends AsyncTask<Void, Void, ArrayList<MyVideo>> {
        @Override
        protected ArrayList<MyVideo> doInBackground(Void... voids) {
            return getPlayListvideo();
        }

        @Override
        protected void onPostExecute(ArrayList<MyVideo> objectMusics) {
            adapterAblbumVideo.setLsMyvideo(arrVideo);
            adapterAblbumVideo.notifyDataSetChanged();
            this.cancel(true);

        }

        public ArrayList<MyVideo> getPlayListvideo() {
            arrVideo.clear();
            File home = new File(Environment.getExternalStorageDirectory().getPath() + "/Make Video/My video");
            for (File file : home.listFiles()) {
                if (file.isDirectory()) {
                    for (File file1 : file.listFiles(new FileExtensionFilter())) {
                        HashMap<String, String> song = new HashMap<>();
                        song.put("file_name", file1.getName());
                        song.put("file_path", file1.getPath());
                        // Adding each song to SongList
                        arrVideo.add(new MyVideo(song.get("file_name"), song.get("file_path")));

                    }
                } else {
                    if (file.getName().endsWith(".mp4") || file.getName().endsWith(".MP4")) {
                        HashMap<String, String> song = new HashMap<>();
                        song.put("file_name", file.getName());
                        song.put("file_path", file.getPath());
                        // Adding each song to SongList
                        arrVideo.add(new MyVideo(song.get("file_name"), song.get("file_path")));
                    }
                }
            }

            // return songs list array
            return arrVideo;
        }

        /**
         * Class to filter files which are having .mp3 extension
         */
        //you can choose the filter for me i put .mp3
        class FileExtensionFilter implements FilenameFilter {
            public boolean accept(File dir, String name) {
                return (name.endsWith(".mp4") || name.endsWith(".MP4"));
            }
        }
    }
}
