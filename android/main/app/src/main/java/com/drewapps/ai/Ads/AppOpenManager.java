package com.drewapps.ai.Ads;

import static androidx.lifecycle.Lifecycle.Event.ON_START;

import static com.drewapps.ai.MyApplication.prefManager;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.drewapps.ai.MyApplication;
import com.drewapps.ai.utils.Constants;
import com.drewapps.ai.utils.Util;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.ump.ConsentInformation;

public class AppOpenManager implements Application.ActivityLifecycleCallbacks, LifecycleObserver {
    private static final String LOG_TAG = "AppOpenManager";
    private static AppOpenAd appOpenAd = null;

    private static AppOpenAd.AppOpenAdLoadCallback loadCallback;

    private static MyApplication myApplication;

    private static Activity currentActivity;

    private static boolean isShowingAd = false;

    /** Constructor */
    public AppOpenManager(MyApplication myApplication) {
        Util.showLog("AD_OPEN");
        this.myApplication = myApplication;
        this.myApplication.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    /** Request an ad */
    public static void fetchAd() {
        // We will implement this below.
        if (isAdAvailable()) {
            return;
        }

        loadCallback =
                new AppOpenAd.AppOpenAdLoadCallback() {
                    /**
                     * Called when an app open ad has loaded.
                     *
                     * @param ad the loaded app open ad.
                     */
                    @Override
                    public void onAdLoaded(AppOpenAd ad) {
                        appOpenAd = ad;
                    }

                    /**
                     * Called when an app open ad has failed to load.
                     *
                     * @param loadAdError the error.
                     */
                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        // Handle the error.
                        Util.showLog(loadAdError.getMessage());
                    }

                };

        AdRequest.Builder builder = new AdRequest.Builder();
        int request = GDPRChecker.getStatus();
        if (request == ConsentInformation.ConsentStatus.NOT_REQUIRED) {
            // load non Personalized ads
            Bundle extras = new Bundle();
            extras.putString("npa", "1");
            builder.addNetworkExtrasBundle(AdMobAdapter.class, extras);
        }

        AppOpenAd.load(
                myApplication, prefManager().getString(Constants.ADMOB_OPEN_AD_ID), builder.build(),
                loadCallback);
    }

    /** Creates and returns ad request. */
    private static AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }

    /** Utility method that checks if ad exists and can be shown. */
    public static boolean isAdAvailable() {
        return appOpenAd != null;
    }

    public static void showAdIfAvailable() {
        // Only show ad if there is not already an app open ad currently showing
        // and an ad is available.
        if (!isShowingAd && isAdAvailable()) {
            Log.e(LOG_TAG, "Will show ad.");

            FullScreenContentCallback fullScreenContentCallback =
                    new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Set the reference to null so isAdAvailable() returns false.
                            appOpenAd = null;
                            isShowingAd = false;
                            fetchAd();
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {}

                        @Override
                        public void onAdShowedFullScreenContent() {
                            isShowingAd = true;
                        }
                    };

            appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
            appOpenAd.show(currentActivity);

        } else {
            Log.e(LOG_TAG, "Can not show ad.");
            fetchAd();
        }
    }

    @Override
    public void onActivityPreCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        Application.ActivityLifecycleCallbacks.super.onActivityPreCreated(activity, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        currentActivity = null;
    }

    /** LifecycleObserver methods */
    @OnLifecycleEvent(ON_START)
    public void onStart() {
        Util.showLog("START");
        showAdIfAvailable();
    }

}
