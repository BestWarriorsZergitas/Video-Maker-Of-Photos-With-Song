package com.videomaker.photowithsong.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.videomaker.photowithsong.R;
import com.videomaker.photowithsong.utils.Utils;

public class MainActivity extends AppCompatActivity {
    private static final String[] PERMISSION =
            {Manifest.permission.READ_EXTERNAL_STORAGE
                    , Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private Button btCreateVideo, btMyVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Utils.checkPermission(PERMISSION, MainActivity.this) == PackageManager.PERMISSION_GRANTED) {

            } else {
                MainActivity.this.requestPermissions(PERMISSION, 1);
            }
        }
        btCreateVideo = (Button) findViewById(R.id.bt_new_video);
        btMyVideo = (Button) findViewById(R.id.bt_my_video);

        btCreateVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ImagePickerActivity.class));
            }
        });
//        btMyVideo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this,MyVideo.class));
//            }
//        });
    }
}
