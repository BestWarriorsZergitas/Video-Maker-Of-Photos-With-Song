package com.videomaker.photowithsong.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.videomaker.photowithsong.Ads;
import com.videomaker.photowithsong.R;
import com.videomaker.photowithsong.utils.AnimationTranslate;
import com.videomaker.photowithsong.utils.Constant;
import com.videomaker.photowithsong.utils.Utils;
import com.zer.android.ZAndroidSDK;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String[] PERMISSION =
            {Manifest.permission.READ_EXTERNAL_STORAGE
                    , Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private LinearLayout btCreateVideo, btMyVideo;
    private RelativeLayout rl_ads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        btCreateVideo = (LinearLayout) findViewById(R.id.bt_new_video);
        btMyVideo = (LinearLayout) findViewById(R.id.bt_my_video);
        rl_ads = (RelativeLayout) findViewById(R.id.adslayout);
        turnPermiss();
    }


    public void init() {
        ZAndroidSDK.init(this);
        Ads.f(this);
        Constant.showAds(this, rl_ads);
        btCreateVideo.setOnClickListener(this);
        btMyVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MyVideoActivity.class));
                AnimationTranslate.nextAnimation(MainActivity.this);
            }
        });
    }

    public void turnPermiss() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Utils.checkPermission(PERMISSION, MainActivity.this) == PackageManager.PERMISSION_GRANTED) {
                creatFolder();
                init();
            } else {
                MainActivity.this.requestPermissions(PERMISSION, 1);
            }
        } else {
            creatFolder();
            init();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0) {
                creatFolder();
                init();
            }
        }

    }

    public void creatFolder() {
        File file = new File(Constant.PATH);
        if (!file.exists()) {
            file.mkdirs();
            File filetemp = new File(Constant.PATH_TEMP);
            if (!filetemp.exists()) {
                filetemp.mkdirs();
                Log.d("DEBUG", "filetemp not creat");
            }
            File filevideo = new File(Constant.PATH_VIDEO);
            if (!filevideo.exists()) {
                filevideo.mkdirs();
                Log.d("DEBUG", "filevideo notcreate");
            }
        }
        creatFile();

    }

    public void creatFile() {
        File filevideo = new File(Constant.PATH_TEMP + "test.mp4");
        File filevideoaudio = new File(Constant.PATH_TEMP + "test1.mp4");
        if (filevideo.exists() && filevideoaudio.exists()) {
            Log.d("DEBUG", "file created");
        } else {
            try {
                FileOutputStream out = new FileOutputStream(filevideo);
                FileOutputStream out1 = new FileOutputStream(filevideoaudio);
                Log.d("DEBUG", "file created success");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_new_video:
            case R.id.iv_creat: {
                startActivity(new Intent(MainActivity.this, ImagePickerActivity.class));
                AnimationTranslate.nextAnimation(MainActivity.this);
            }
        }
    }

}
