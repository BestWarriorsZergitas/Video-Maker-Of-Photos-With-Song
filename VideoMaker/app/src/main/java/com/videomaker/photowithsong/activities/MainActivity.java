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
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.videomaker.photowithsong.R;
import com.videomaker.photowithsong.utils.Constant;
import com.videomaker.photowithsong.utils.FileMover;
import com.videomaker.photowithsong.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String[] PERMISSION =
            {Manifest.permission.READ_EXTERNAL_STORAGE
                    , Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private LinearLayout btCreateVideo, btMyVideo;
    private ImageView iv_creat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btCreateVideo = (LinearLayout) findViewById(R.id.bt_new_video);
        btMyVideo = (LinearLayout) findViewById(R.id.bt_my_video);
        turnPermiss();
    }

    public void init() {
        btCreateVideo.setOnClickListener(this);
        btMyVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MyVideoActivity.class));
            }
        });
    }

    public void turnPermiss() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Utils.checkPermission(PERMISSION, MainActivity.this) == PackageManager.PERMISSION_GRANTED) {
                creatFolder();
                copyfilemusic();
                init();
            } else {
                MainActivity.this.requestPermissions(PERMISSION, 1);
            }
        } else {
            creatFolder();
            copyfilemusic();
            init();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0) {
                creatFolder();
                copyfilemusic();
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

    String[] libraryAssets = {"NhacNenGiangSinh01.aac",
            "NhacNenGiangSinh02.aac",
            "NhacNenSinhNhat01.aac",
            "NhacNenTinhYeu01.aac",
            "NhacNenTinhYeu02.aac",
            "NhacNenHappyNewYear01.aac",
            "KissTheRain-Yiruma_.aac"};


    public void copyfilemusic() {
        ArrayList<File> file = new ArrayList<>();
        file.add(new File(getCacheDir() + "/NhacNenGiangSinh01.aac"));
        file.add(new File(getCacheDir() + "/NhacNenGiangSinh02.aac"));
        file.add(new File(getCacheDir() + "/NhacNenSinhNhat01.aac"));
        file.add(new File(getCacheDir() + "/NhacNenTinhYeu01.aac"));
        file.add(new File(getCacheDir() + "/NhacNenTinhYeu02.aac"));
        file.add(new File(getCacheDir() + "/NhacNenHappyNewYear01.aac"));
        file.add(new File(getCacheDir() + "/KissTheRain-Yiruma_.aac"));
        for (File f : file) {
            if (!f.exists()) {
                for (int i = 0; i < libraryAssets.length; i++) {
                    try {
                        InputStream audioinput = this.getAssets().open(libraryAssets[i]);
                        FileMover fm = new FileMover(audioinput, getCacheDir() + "/" + libraryAssets[i]);
                        fm.moveIt();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Log.d("DEBUG", "tep chua ton tai");
                return;
            }
        }

        Log.d("DEBUG", "tep da ton tai");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_new_video:
            case R.id.iv_creat: {
                startActivity(new Intent(MainActivity.this, ImagePickerActivity.class));
            }
        }
    }
}
