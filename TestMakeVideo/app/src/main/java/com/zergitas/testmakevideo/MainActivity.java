package com.zergitas.testmakevideo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            FFmpeg ffmpeg = FFmpeg.getInstance(this);
            String cmd = "-i " + videoFilePath + " -i " + audioFilePath + " -shortest -threads 0 -preset ultrafast -strict -2 " + outputFilePath
            ffmpeg.execute(cmd, mergeListener);
        } catch (FFmpegCommandAlreadyRunningException e) {
            e.printStackTrace();
        }
    }
}
