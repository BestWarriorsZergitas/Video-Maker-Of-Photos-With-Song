package com.videomaker.photowithsong.aplication;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.adobe.creativesdk.foundation.AdobeCSDKFoundation;
import com.adobe.creativesdk.foundation.auth.IAdobeAuthClientCredentials;
import com.zer.android.ZAndroidSDK;


/**
 * Created by Peih Gnaoh on 8/22/2017.
 */

public class Application extends android.app.Application implements IAdobeAuthClientCredentials {
    /* Be sure to fill in the two strings below. */
    private static final String CREATIVE_SDK_CLIENT_ID = "e9eb933510b04b9ab4c7f1286eb9259b";
    private static final String CREATIVE_SDK_CLIENT_SECRET = "08412199-5eb6-4669-9cc3-331ccff9ca71";
    private static final String CREATIVE_SDK_REDIRECT_URI = "ams+560554a81df45ef4ee3a62910e938052b1551fc2://adobeid/e9eb933510b04b9ab4c7f1286eb9259b";
    private static final String[] CREATIVE_SDK_SCOPES = {"email", "profile", "address"};

    @Override
    public void onCreate() {
        super.onCreate();
        AdobeCSDKFoundation.initializeCSDKFoundation(getApplicationContext());
        ZAndroidSDK.initApplication(this, getPackageName());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public String getClientID() {
        return CREATIVE_SDK_CLIENT_ID;
    }

    @Override
    public String getClientSecret() {
        return CREATIVE_SDK_CLIENT_SECRET;
    }

    @Override
    public String[] getAdditionalScopesList() {
        return CREATIVE_SDK_SCOPES;
    }

    @Override
    public String getRedirectURI() {
        return CREATIVE_SDK_REDIRECT_URI;
    }
}
