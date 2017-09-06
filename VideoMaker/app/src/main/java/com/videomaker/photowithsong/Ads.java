package com.videomaker.photowithsong;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.zer.android.zah;

/**
 * Created by lequy on 7/13/2017.
 */

public class Ads {
    static InterstitialAd interstitialAd;

    public Ads() {
    }

    public interface OnAdsListener {
        void onError();

        void onAdLoaded();

        void onAdOpened();
    }

    public static void b(Activity activity, final RelativeLayout relativeLayout, final OnAdsListener onAdsListener) {
        try {
            if (com.zer.android.zu.s(activity) && !TextUtils.isEmpty(zah.getBannerAds(activity))) {
                final AdView adView = new AdView(activity);
                adView.setAdSize(AdSize.BANNER);
                adView.setAdUnitId(zah.getBannerAds(activity));
                AdRequest adRequest = (new AdRequest.Builder()).addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
                adView.setAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int i) {
                        onAdsListener.onError();
                        super.onAdFailedToLoad(i);
                    }

                    @Override
                    public void onAdLoaded() {
                        onAdsListener.onAdLoaded();
                        super.onAdLoaded();
                    }

                    @Override
                    public void onAdOpened() {
                        onAdsListener.onAdOpened();
                        super.onAdOpened();
                    }
                });
                adView.loadAd(adRequest);

                RelativeLayout.LayoutParams rLParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                rLParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
                relativeLayout.addView(adView, rLParams);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void t(Activity activity, RelativeLayout relativeLayout) {
        if (com.zer.android.zu.s(activity) && !TextUtils.isEmpty(zah.getBannerAds(activity))) {
            AdView adView = new AdView(activity);
            adView.setAdSize(AdSize.BANNER);
            adView.setAdUnitId(zah.getBannerAds(activity));
            AdRequest adRequest = (new AdRequest.Builder()).addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
            adView.loadAd(adRequest);

            RelativeLayout.LayoutParams rLParams =
                    new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            rLParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 1);
            relativeLayout.addView(adView, rLParams);
        }
    }

    public static void f(Context context) {
        if (com.zer.android.zu.s(context) && !TextUtils.isEmpty(zah.getInterAdsId(context))) {
            interstitialAd = new InterstitialAd(context);
            interstitialAd.setAdUnitId(zah.getInterAdsId(context));
            AdRequest adRequest = (new AdRequest.Builder()).build();
            interstitialAd.loadAd(adRequest);
            interstitialAd.setAdListener(new AdListener() {
                public void onAdLoaded() {
                    showInterstitial();
                }
            });
        }

    }

    private static void showInterstitial() {
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        }
    }
}

