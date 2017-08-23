package com.videomaker.photowithsong.aplication;

import ly.img.android.PESDK;

/**
 * Created by Peih Gnaoh on 8/22/2017.
 */

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PESDK.init(this);
    }
}
