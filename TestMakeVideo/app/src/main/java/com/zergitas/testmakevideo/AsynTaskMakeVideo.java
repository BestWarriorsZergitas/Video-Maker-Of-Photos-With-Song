package com.zergitas.testmakevideo;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.github.hiteshsondhi88.libffmpeg.FFmpeg;

/**
 * Created by DaiPhongPC on 8/22/2017.
 */

public class AsynTaskMakeVideo extends AsyncTask<Void,Void,Void> {
    ProgressDialog dialog;
    FFmpeg recorder;
    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
