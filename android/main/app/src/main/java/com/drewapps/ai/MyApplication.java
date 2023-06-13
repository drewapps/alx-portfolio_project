package com.drewapps.ai;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.drewapps.ai.Ads.AppOpenManager;
import com.drewapps.ai.utils.Connectivity;
import com.drewapps.ai.utils.Constants;
import com.drewapps.ai.utils.PrefManager;
import com.drewapps.ai.utils.Util;
import com.google.android.gms.ads.MobileAds;
import com.unity3d.ads.UnityAds;

public class MyApplication extends Application {

    Connectivity connectivity;

    PrefManager prefManager;
    public static Context context;

    private static final String ONESIGNAL_APP_ID = "55705d09-f531-48d4-965a-b3be6258f341";

    public static MyApplication myApplication;
    public static AppOpenManager appOpenAdManager;

    @Override
    public void onCreate() {
        super.onCreate();

        myApplication = this;

        connectivity = new Connectivity(this);
        prefManager = new PrefManager(this);

        context = this;
        if (connectivity.isConnected()) {
            Config.IS_CONNECTED = true;
        } else {
            Config.IS_CONNECTED = false;
        }

        MobileAds.initialize(this, initializationStatus -> {});
        UnityAds.initialize (this, getResources().getString(R.string.unity_ads_app_id));
        AppLovinSdk.initializeSdk(this);
        AppLovinSdk.getInstance( this ).setMediationProvider( "max" );
        AppLovinSdk.initializeSdk( this, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration configuration)
            {
            }
        } );

    }

    public static int getScreenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static Display getDefaultDisplay() {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        return display;
    }

    public static int getScreenHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static int getColumnWidth(int column, float grid_padding) {
        Resources r = context.getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, grid_padding, r.getDisplayMetrics());
        return (int) ((getScreenWidth() - ((column + 1) * padding)) / column);
    }

    public static PrefManager prefManager() {
        return new PrefManager(myApplication);
    }

    public static void ShowOpenAds() {
        try {
            if(prefManager().getString(Constants.ADMOB_OPEN_AD_ENABLE).equals(Config.ONE) && !Constants.IS_SUBSCRIBED) {
                appOpenAdManager = new AppOpenManager(myApplication);
            }
        }catch (Exception e) {
            Util.showErrorLog(e.getMessage(), e);
        }
    }
}
