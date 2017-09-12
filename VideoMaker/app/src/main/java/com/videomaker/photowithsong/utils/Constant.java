package com.videomaker.photowithsong.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.View;
import android.widget.RelativeLayout;

import com.videomaker.photowithsong.Ads;

/**
 * Created by Peih Gnaoh on 8/21/2017.
 */

public class Constant {

    public static final String IMAGE = "IMAGE";
    public static final String IMAGE_ARR = "IMAGE_ARR";
    public static final String PATH_VIDEO = Environment.getExternalStorageDirectory().getPath() + "/Make Video/My video/";
    public static final String PATH_TEMP = Environment.getExternalStorageDirectory().getPath() + "/Make Video/.Temp/";
    public static final String PATH = Environment.getExternalStorageDirectory().getPath() + "/Make Video/";

    public class Intents {
    }

    public class SharedPreferences {
    }

    public static Bitmap getBitmapFromLocalPath(String path) {
        try {
            return BitmapFactory.decodeFile(path);
        } catch (Exception e) {
            //  Logger.e(e.toString());
            return null;
        }
    }

    public static void showAds(Activity activity, RelativeLayout relativeLayout) {
       Ads.b(activity, relativeLayout, new Ads.OnAdsListener() {
            @Override
            public void onError() {
                relativeLayout.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAdLoaded() {
                relativeLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdOpened() {
                relativeLayout.setVisibility(View.VISIBLE);
            }
        });
    }
}
