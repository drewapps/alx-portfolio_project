package com.drewapps.ai.Ads;


import static com.drewapps.ai.MyApplication.prefManager;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.drewapps.ai.R;
import com.drewapps.ai.utils.Constants;
import com.drewapps.ai.utils.Util;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.ump.ConsentInformation;

public class NativeAdManager {

    public static void showAds(Context context, FrameLayout flNative) {

        if (!prefManager().getString(Constants.NATIVE_AD_TYPE).equals(Constants.FALSE) && !Constants.IS_SUBSCRIBED) {
            switch (prefManager().getString(Constants.NATIVE_AD_TYPE)) {
                case Constants.ADMOB:
                    NativeAdView nativeAdView = (NativeAdView) LayoutInflater.from(context).inflate(R.layout.row_native_ad, null, false);
                    AdLoader.Builder builder = new AdLoader.Builder(context, prefManager().getString(Constants.NATIVE_AD_ID));
                    builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                        @Override
                        public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                            Util.showLog("NATIVE: " + nativeAd.getHeadline());
                            flNative.setVisibility(View.VISIBLE);
                            populateNativeADView(nativeAd, nativeAdView, flNative);
                        }
                    });

                    AdLoader adLoader =
                            builder.withAdListener(
                                            new AdListener() {
                                                @Override
                                                public void onAdFailedToLoad(LoadAdError loadAdError) {
                                                Util.showLog("NATIVE: " + loadAdError.getMessage());
                                                    flNative.setVisibility(View.GONE);
                                                }
                                            })
                                    .build();

                    AdRequest.Builder m_builder = new AdRequest.Builder();
                    int request = GDPRChecker.getStatus();
                    if (request == ConsentInformation.ConsentStatus.NOT_REQUIRED) {
                        // load non Personalized ads
                        Bundle extras = new Bundle();
                        extras.putString("npa", "1");
                        m_builder.addNetworkExtrasBundle(AdMobAdapter.class, extras);
                    }

                    adLoader.loadAd(m_builder.build());
                    break;
                case Constants.MAX:
                   MaxNativeAdLoader nativeAdLoader = new MaxNativeAdLoader( prefManager().getString(Constants.NATIVE_AD_ID),
                           context );
                    nativeAdLoader.setNativeAdListener( new MaxNativeAdListener()
                    {
                        @Override
                        public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad)
                        {
                            flNative.setVisibility( View.VISIBLE );
                            flNative.removeAllViews();
                            flNative.addView( nativeAdView );
                        }

                        @Override
                        public void onNativeAdLoadFailed(final String adUnitId, final MaxError error)
                        {
                            // We recommend retrying with exponentially higher delays up to a maximum delay
                            Util.showLog("Error: " + error.getMessage());
                        }

                        @Override
                        public void onNativeAdClicked(final MaxAd ad)
                        {
                            // Optional click callback
                        }
                    } );

                    nativeAdLoader.loadAd();
                    break;
            }

        } else {
            flNative.setVisibility(View.GONE);
        }
    }

    private static void populateNativeADView(NativeAd nativeAd, NativeAdView adView, FrameLayout flNative) {
        adView.setMediaView((com.google.android.gms.ads.nativead.MediaView) adView.findViewById(R.id.ad_media));
        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline and mediaContent are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView()).setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }
        adView.setNativeAd(nativeAd);

        flNative.removeAllViews();
        flNative.addView(adView);
    }

}
