package com.videomaker.photowithsong.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.videomaker.photowithsong.R;
import com.videomaker.photowithsong.objects.MusicMP3;
import com.videomaker.photowithsong.utils.Constant;
import com.videomaker.photowithsong.utils.VideoUilt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class SlideShowVideoActivity extends AppCompatActivity implements View.OnClickListener {
    private VideoUilt video;
    private VideoView videoView;
    private MediaController mediaController;
    private int position = 0;
    private TextView txtmusis, txtsong;
    private ImageView imgcontrolermusic, imgmusic;
    private MusicMP3 musicMP3;
    private LinearLayout lnpro;
    private ArrayList<Bitmap> lsBitmap;
    private ArrayList<String> paths;
    private String pathvideo, pathaudio, pathoutput;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_show);
        init();
        path=creatFile("test");
        imgcontrolermusic.setOnClickListener(this);
        loadbitmap();
        new AsynMakeVideo().execute(lsBitmap);

    }

    public void loadbitmap() {
        Intent data = getIntent();
        paths=data.getStringArrayListExtra(Constant.IMAGE_ARR);
        lsBitmap=new ArrayList<>();
//        for (int i=0;i<paths.size();i++){
//            lsBitmap.add(getBitmapFromLocalPath(paths.get(i),1));
//        }
        lsBitmap.add(BitmapFactory.decodeResource(getResources(), R.drawable.backgroundsinhnhat));
        lsBitmap.add(BitmapFactory.decodeResource(getResources(), R.drawable.backgroundvalentine));
        lsBitmap.add(BitmapFactory.decodeResource(getResources(), R.drawable.backgroundvalentine1));
        lsBitmap.add(BitmapFactory.decodeResource(getResources(), R.drawable.nennoel));
        lsBitmap.add(BitmapFactory.decodeResource(getResources(), R.drawable.nennoel1));

    }

    public String creatFile(String name) {
        File filevideo = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Make Video/" + name + ".mp4");
        try {
            FileOutputStream out = new FileOutputStream(filevideo);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return filevideo.getPath();
    }

    public void init() {
        videoView = (VideoView) findViewById(R.id.showvideo);
        txtmusis = (TextView) findViewById(R.id.txtnamemusic);
        txtsong = (TextView) findViewById(R.id.txtsong);
        imgmusic = (ImageView) findViewById(R.id.iconmusic);
        imgcontrolermusic = (ImageView) findViewById(R.id.img_controlmusic);
        lnpro = (LinearLayout) findViewById(R.id.lnprocess);
        mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                Log.d("DEBUG", videoView.getDuration() + "");
            }
        });

    }


    public void startIntentMusic() {
        Intent intent = new Intent(this, LoadMusicActivity.class);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (data != null) {
                musicMP3 = (MusicMP3) data.getSerializableExtra("music");
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_controlmusic: {
                startIntentMusic();
                break;
            }
        }
    }

    public class AsynMakeVideo extends AsyncTask<ArrayList<Bitmap>, Void, String> {

        @Override
        protected String doInBackground(ArrayList<Bitmap>... arrayLists) {
            ArrayList<Bitmap> bitmaps = arrayLists[0];
            video = new VideoUilt(getBaseContext(), bitmaps,path);
            return video.makeVideo();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            lnpro.setVisibility(View.INVISIBLE);
            showVideo(s);
        }

        private void showVideo(String path) {
            videoView.setVideoPath(path);
            videoView.start();
        }
    }
    public static Bitmap getBitmapFromLocalPath(String path, int sampleSize) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = sampleSize;
            return BitmapFactory.decodeFile(path, options);
        } catch (Exception e) {
            //  Logger.e(e.toString());
        }
        return null;
    }
}
