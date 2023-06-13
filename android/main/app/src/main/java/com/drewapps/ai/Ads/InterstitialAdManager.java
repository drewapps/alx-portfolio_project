package com.drewapps.ai.Ads;


import static com.drewapps.ai.MyApplication.prefManager;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.applovin.adview.AppLovinInterstitialAd;
import com.applovin.adview.AppLovinInterstitialAdDialog;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.sdk.AppLovinAd;
import com.applovin.sdk.AppLovinAdDisplayListener;
import com.applovin.sdk.AppLovinAdLoadListener;
import com.applovin.sdk.AppLovinAdSize;
import com.applovin.sdk.AppLovinAdVideoPlaybackListener;
import com.applovin.sdk.AppLovinSdk;
import com.drewapps.ai.utils.Constants;
import com.drewapps.ai.utils.Util;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.ump.ConsentInformation;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.sdk.InterstitialListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;

public class InterstitialAdManager {
    private static InterstitialAd mInterstitialAd = null;

    private static AppLovinInterstitialAdDialog applovinInterstitialAd;
    private static AppLovinAd applovinInterstitialAdBlock;
    private static MaxInterstitialAd maxInterstitialAd;
    private static Activity mContext;
    private static Listener listener;

    private static boolean isLoaded = false;

    public static void Interstitial(Activity context, Listener mlistener) {
        mContext = context;
        listener = mlistener;
        LoadAds();
    }

    public static void LoadAds() {
        if (!prefManager().getString(Constants.INTERSTITIAL_AD_TYPE).equals(Constants.FALSE) && !Constants.IS_SUBSCRIBED) {
            switch (prefManager().getString(Constants.INTERSTITIAL_AD_TYPE)) {
                case Constants.ADMOB:
                    if (mInterstitialAd == null) {
                        AdRequest.Builder builder = new AdRequest.Builder();
                        int request = GDPRChecker.getStatus();
                        if (request == ConsentInformation.ConsentStatus.NOT_REQUIRED) {
                            // load non Personalized ads
                            Bundle extras = new Bundle();
                            extras.putString("npa", "1");
                            builder.addNetworkExtrasBundle(AdMobAdapter.class, extras);
                        } // else do nothing , it will load PERSONALIZED ads
                        InterstitialAd.load(mContext, prefManager().getString(Constants.INTERSTITIAL_AD_ID), builder.build(),
                                new InterstitialAdLoadCallback() {
                                    @Override
                                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                        // The mInterstitialAd reference will be null until
                                        // an ad is loaded.
                                        mInterstitialAd = interstitialAd;
                                    }

                                    @Override
                                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                        // Handle the error
                                        mInterstitialAd = null;
                                        listener.onAdFailedToLoad();
                                    }
                                });
                    }
                    break;
                case Constants.APPLOVIN:
                    if (applovinInterstitialAdBlock == null) {

                        applovinInterstitialAd = AppLovinInterstitialAd.create(AppLovinSdk.getInstance(mContext), mContext);

                        applovinInterstitialAd.setAdLoadListener(new AppLovinAdLoadListener() {
                            @Override
                            public void adReceived(AppLovinAd ad) {
                                applovinInterstitialAdBlock = ad;
                            }

                            @Override
                            public void failedToReceiveAd(int errorCode) {
                                listener.onAdFailedToLoad();
                            }
                        });

                        AppLovinSdk.getInstance(mContext).getAdService().loadNextAd(AppLovinAdSize.INTERSTITIAL,
                                new AppLovinAdLoadListener() {
                                    @Override
                                    public void adReceived(AppLovinAd ad) {
                                        applovinInterstitialAdBlock = ad;
                                    }

                                    @Override
                                    public void failedToReceiveAd(int errorCode) {
                                        listener.onAdFailedToLoad();
                                    }
                                });


                    }
                    break;
                case Constants.MAX:
                    if (maxInterstitialAd == null) {

                        maxInterstitialAd = new MaxInterstitialAd(prefManager().getString(Constants.INTERSTITIAL_AD_ID),
                                mContext);

                        maxInterstitialAd.setListener(new MaxAdListener() {
                            @Override
                            public void onAdDisplayFailed(MaxAd ad, MaxError error) {

                            }

                            @Override
                            public void onAdLoaded(MaxAd ad) {

                            }

                            @Override
                            public void onAdLoadFailed(String adUnitId, MaxError error) {
                                maxInterstitialAd = null;
                                listener.onAdFailedToLoad();
                            }

                            @Override
                            public void onAdDisplayed(MaxAd ad) {

                            }

                            @Override
                            public void onAdClicked(MaxAd ad) {

                            }

                            @Override
                            public void onAdHidden(MaxAd ad) {
                                maxInterstitialAd = null;
                                listener.onAdDismissed();
                            }
                        });

                        maxInterstitialAd.loadAd();


                    }
                    break;

                case Constants.IRON_SOURCE:
                    if (!IronSource.isInterstitialReady()) {
                        IronSource.init(mContext, prefManager().getString(Constants.INTERSTITIAL_AD_ID), IronSource.AD_UNIT.INTERSTITIAL);
                        IronSource.setInterstitialListener(new InterstitialListener() {
                            @Override
                            public void onInterstitialAdReady() {
                                Util.showLog("IROUNSOURCE " + "onInterstitialAdReady");

                            }

                            @Override
                            public void onInterstitialAdLoadFailed(IronSourceError error) {
                                Util.showLog("IROUNSOURCE " + error.getErrorMessage());
                                listener.onAdFailedToLoad();

                            }

                            @Override
                            public void onInterstitialAdOpened() {

                            }

                            @Override
                            public void onInterstitialAdClosed() {

                                listener.onAdDismissed();

                            }

                            @Override
                            public void onInterstitialAdShowFailed(IronSourceError error) {
                                Log.v("IROUNSOURCE", error.getErrorMessage());

                            }

                            @Override
                            public void onInterstitialAdClicked() {

                            }

                            @Override
                            public void onInterstitialAdShowSucceeded() {

                            }
                        });
                        IronSource.loadInterstitial();
                    }
                    break;

                case Constants.UNITY:
                    if (!isLoaded) {
                        UnityAds.load(prefManager().getString(Constants.INTERSTITIAL_AD_ID), new IUnityAdsLoadListener() {
                            @Override
                            public void onUnityAdsAdLoaded(String placementId) {
                                isLoaded = true;
                            }

                            @Override
                            public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
                                isLoaded = false;
                                listener.onAdFailedToLoad();
                            }
                        });
                    }
                    break;
            }
        }
    }

    public static boolean isLoaded() {
        switch (prefManager().getString(Constants.INTERSTITIAL_AD_TYPE)) {
            case Constants.ADMOB:
                return mInterstitialAd == null ? false : true;
            case Constants.APPLOVIN:
                return applovinInterstitialAdBlock == null ? false : true;
            case Constants.MAX:
                return maxInterstitialAd == null ? false : maxInterstitialAd.isReady() ? true : false;
            case Constants.IRON_SOURCE:
                return IronSource.isInterstitialReady() ? true : false;
            case Constants.UNITY:
                return isLoaded;
        }
        return false;
    }

    public static void showAds() {
        if (!prefManager().getString(Constants.INTERSTITIAL_AD_TYPE).equals(Constants.FALSE) && !Constants.IS_SUBSCRIBED) {
            switch (prefManager().getString(Constants.INTERSTITIAL_AD_TYPE)) {
                case Constants.ADMOB:
                    if (mInterstitialAd != null) {
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdClicked() {
                                // Called when a click is recorded for an ad.
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                // Set the ad reference to null so you don't show the ad a second time.
                                mInterstitialAd = null;
                                listener.onAdDismissed();
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when ad fails to show.
                                mInterstitialAd = null;
                                listener.onAdFailedToLoad();
                            }

                            @Override
                            public void onAdImpression() {
                                // Called when an impression is recorded for an ad.
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                            }
                        });
                        if (mInterstitialAd != null) {
                            mInterstitialAd.show(mContext);
                        }
                    }
                    break;
                case Constants.APPLOVIN:
                    if (applovinInterstitialAdBlock != null) {
                        applovinInterstitialAd.setAdDisplayListener(new AppLovinAdDisplayListener() {
                            @Override
                            public void adDisplayed(AppLovinAd ad) {

                            }

                            @Override
                            public void adHidden(AppLovinAd ad) {
                                listener.onAdDismissed();
                            }
                        });
                        applovinInterstitialAd.setAdClickListener(ad -> {

                        });
                        applovinInterstitialAd.setAdVideoPlaybackListener(new AppLovinAdVideoPlaybackListener() {
                            @Override
                            public void videoPlaybackBegan(AppLovinAd ad) {

                            }

                            @Override
                            public void videoPlaybackEnded(AppLovinAd ad, double percentViewed, boolean fullyWatched) {

                            }
                        });
                        applovinInterstitialAd.showAndRender(applovinInterstitialAdBlock);
                    }
                    break;
                case Constants.MAX:
                    if (maxInterstitialAd != null) {
                        maxInterstitialAd.showAd();
                    }
                    break;
                case Constants.IRON_SOURCE:
                    if (IronSource.isInterstitialReady()) {
                        IronSource.showInterstitial();
                    }
                    break;
                case Constants.UNITY:
                    if (isLoaded) {
                        isLoaded = false;
                        UnityAds.show(mContext, prefManager().getString(Constants.INTERSTITIAL_AD_ID), new IUnityAdsShowListener() {
                            @Override
                            public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {

                            }

                            @Override
                            public void onUnityAdsShowStart(String placementId) {

                            }

                            @Override
                            public void onUnityAdsShowClick(String placementId) {

                            }

                            @Override
                            public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
                                listener.onAdDismissed();
                            }
                        });
                    }
            }
        }
    }

    public interface Listener {

        void onAdFailedToLoad();

        void onAdDismissed();

    }
}
