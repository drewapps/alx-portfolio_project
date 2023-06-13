package com.drewapps.ai.Ads;

import static com.drewapps.ai.MyApplication.prefManager;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.applovin.adview.AppLovinAdView;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdFormat;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.sdk.AppLovinAd;
import com.applovin.sdk.AppLovinAdLoadListener;
import com.applovin.sdk.AppLovinAdSize;
import com.applovin.sdk.AppLovinSdkUtils;
import com.drewapps.ai.MyApplication;
import com.drewapps.ai.utils.Constants;
import com.drewapps.ai.utils.Util;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.ump.ConsentInformation;
import com.ironsource.mediationsdk.ISBannerSize;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.IronSourceBannerLayout;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.sdk.BannerListener;
import com.unity3d.services.banners.BannerErrorInfo;
import com.unity3d.services.banners.BannerView;
import com.unity3d.services.banners.UnityBannerSize;

public class BannerAdManager {
    public static void showBannerAds(Activity context, LinearLayout mAdViewLayout) {
        Util.showLog("SHOW_BANNER: " + prefManager().getString(Constants.BANNER_AD_TYPE));
        if (!prefManager().getString(Constants.BANNER_AD_TYPE).equals(Constants.FALSE) && !Constants.IS_SUBSCRIBED) {
            switch (prefManager().getString(Constants.BANNER_AD_TYPE)) {
                case Constants.ADMOB:
                    AdView mAdView = new AdView(context);
                    mAdView.setAdSize(AdSize.BANNER);
                    mAdView.setAdUnitId(prefManager().getString(Constants.BANNER_AD_ID));
                    AdRequest.Builder builder = new AdRequest.Builder();
                    int request = GDPRChecker.getStatus();
                    if (request == ConsentInformation.ConsentStatus.NOT_REQUIRED) {
                        // load non Personalized ads
                        Bundle extras = new Bundle();
                        extras.putString("npa", "1");
                        builder.addNetworkExtrasBundle(AdMobAdapter.class, extras);
                    } // else do nothing , it will load PERSONALIZED ads
                    mAdView.loadAd(builder.build());
                    mAdViewLayout.removeAllViews();
                    mAdViewLayout.addView(mAdView);
                    mAdViewLayout.setGravity(Gravity.CENTER);
                    break;
                case Constants.APPLOVIN:
                    AppLovinAdView adView = new AppLovinAdView( AppLovinAdSize.BANNER, context);

                    boolean isTablet = AppLovinSdkUtils.isTablet( context );
                    int heightPx = AppLovinSdkUtils.dpToPx( context, isTablet ? 90 : 50 );
                    adView.setLayoutParams( new FrameLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, heightPx ) );

                    int width = ViewGroup.LayoutParams.MATCH_PARENT;
                    adView.setVisibility(View.GONE);

                    adView.setLayoutParams( new FrameLayout.LayoutParams( width, heightPx ) );
                    adView.setAdLoadListener(new AppLovinAdLoadListener() {
                        @Override
                        public void adReceived(AppLovinAd ad) {
                            adView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void failedToReceiveAd(int errorCode) {
                            adView.setVisibility(View.GONE);

                        }
                    });

                    mAdViewLayout.removeAllViews();
                    mAdViewLayout.addView(adView);
                    mAdViewLayout.setGravity(Gravity.CENTER);

                    adView.loadNextAd();
                    break;
                case Constants.MAX:
                    MaxAdView maxAdView = new MaxAdView( MyApplication.prefManager().getString(Constants.BANNER_AD_ID), context );
                    maxAdView.setListener(new MaxAdViewAdListener() {
                        @Override
                        public void onAdExpanded(MaxAd ad) {

                        }

                        @Override
                        public void onAdCollapsed(MaxAd ad) {

                        }

                        @Override
                        public void onAdLoaded(MaxAd ad) {
                            maxAdView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAdDisplayed(MaxAd ad) {

                        }

                        @Override
                        public void onAdHidden(MaxAd ad) {

                        }

                        @Override
                        public void onAdClicked(MaxAd ad) {

                        }

                        @Override
                        public void onAdLoadFailed(String adUnitId, MaxError error) {

                        }

                        @Override
                        public void onAdDisplayFailed(MaxAd ad, MaxError error) {

                        }
                    });

                    maxAdView.setVisibility(View.GONE);
                    int widthMax = ViewGroup.LayoutParams.MATCH_PARENT;

                    int heightDp = MaxAdFormat.BANNER.getAdaptiveSize(widthMax, context).getHeight();
                    int heightPxMax = AppLovinSdkUtils.dpToPx( context, heightDp );

                    maxAdView.setLayoutParams( new FrameLayout.LayoutParams( widthMax, heightPxMax ) );

                    mAdViewLayout.removeAllViews();
                    mAdViewLayout.addView(maxAdView);
                    mAdViewLayout.setGravity(Gravity.CENTER);

                    maxAdView.loadAd();
                    break;
                case Constants.IRON_SOURCE:
                    IronSource.setUserId(String.valueOf(prefManager().getInt(Constants.USER_ID)));

                    IronSource.init(context, prefManager().getString(Constants.BANNER_AD_ID), IronSource.AD_UNIT.BANNER);
                    IronSourceBannerLayout banner = IronSource.createBanner(context, ISBannerSize.BANNER);
                    mAdViewLayout.removeAllViews();
                    mAdViewLayout.addView(banner);
                    mAdViewLayout.setGravity(Gravity.CENTER);
                    banner.setBannerListener(new BannerListener() {
                        @Override
                        public void onBannerAdLoaded() {
                            banner.setVisibility(View.VISIBLE);

                        }
                        @Override
                        public void onBannerAdLoadFailed(IronSourceError error) {
                            Util.showLog("EE: " + error.getErrorMessage());
                            context.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdViewLayout.removeAllViews();
                                }
                            });
                        }
                        @Override
                        public void onBannerAdClicked() {

                        }
                        @Override
                        public void onBannerAdScreenPresented() {

                        }
                        @Override
                        public void onBannerAdScreenDismissed() {

                        }
                        @Override
                        public void onBannerAdLeftApplication() {

                        }
                    });
                    IronSource.loadBanner(banner);
                    break;
                case Constants.UNITY:
                    BannerView bannerView = new BannerView(context, prefManager().getString(Constants.BANNER_AD_ID),
                            new UnityBannerSize(320, 50));
                    bannerView.setListener(new BannerView.IListener() {
                        @Override
                        public void onBannerLoaded(BannerView bannerAdView) {

                        }

                        @Override
                        public void onBannerClick(BannerView bannerAdView) {

                        }

                        @Override
                        public void onBannerFailedToLoad(BannerView bannerAdView, BannerErrorInfo errorInfo) {
                            Util.showLog("EE: " + errorInfo.errorMessage);
                            context.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdViewLayout.removeAllViews();
                                }
                            });
                        }

                        @Override
                        public void onBannerLeftApplication(BannerView bannerView) {

                        }
                    });
                    bannerView.load();
                    mAdViewLayout.removeAllViews();
                    mAdViewLayout.addView(bannerView);
                    mAdViewLayout.setGravity(Gravity.CENTER);
            }
        } else {
            mAdViewLayout.setVisibility(View.GONE);
        }
    }
}
