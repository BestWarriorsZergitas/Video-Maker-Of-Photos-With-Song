package com.videomaker.photowithsong.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by DaiPhongPC on 8/28/2017.
 */

public class Utils {
    public static int checkPermission(String[] permissions, Context context) {
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (String permission : permissions) {
            permissionCheck += ContextCompat.checkSelfPermission(context, permission);
        }
        return permissionCheck;
    }
}
