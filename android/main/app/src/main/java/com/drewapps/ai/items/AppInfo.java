package com.drewapps.ai.items;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "app_about")
public class AppInfo implements Serializable {

    @PrimaryKey
    @SerializedName("email")
    @NonNull
    @Expose
    public String email;
    @SerializedName("phoneNo")
    @Expose
    public String phoneNo;
    @SerializedName("address")
    @Expose
    public String address;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("facebook")
    @Expose
    public String facebook;
    @SerializedName("instagram")
    @Expose
    public String instagram;
    @SerializedName("youtube")
    @Expose
    public String youtube;
    @SerializedName("appName")
    @Expose
    public String appName;
    @SerializedName("linkedin")
    @Expose
    public String linkedin;
    @SerializedName("twitter")
    @Expose
    public String twitter;

    @Embedded(prefix = "update_")
    @SerializedName("appUpdate")
    @Expose
    public AppVersion appVersion;

    @SerializedName("privacyPolicy")
    @Expose
    public String privacyPolicy;
    @SerializedName("refundPolicy")
    @Expose
    public String refundPolicy;
    @SerializedName("termsCondition")
    @Expose
    public String termsCondition;
    @SerializedName("userStatus")
    @Expose
    public String userStatus;

    @SerializedName("admobPublisherId")
    @Expose
    public String admobPublisherId;
    @SerializedName("admobAppId")
    @Expose
    public String admobAppId;
    @SerializedName("rewardedId")
    @Expose
    public String rewardedId;
    @SerializedName("rewardedDisplayType")
    @Expose
    public String rewardedDisplayType;
    @SerializedName("bannerId")
    @Expose
    public String bannerId;
    @SerializedName("bannerDisplayType")
    @Expose
    public String bannerDisplayType;
    @SerializedName("interstitialId")
    @Expose
    public String interstitialId;
    @SerializedName("interstitialDisplayType")
    @Expose
    public String interstitialDisplayType;
    @SerializedName("clickInterstitialAd")
    @Expose
    public String clickInterstitialAd;
    @SerializedName("nativeId")
    @Expose
    public String nativeId;
    @SerializedName("nativeDisplayType")
    @Expose
    public String nativeDisplayType;
    @SerializedName("dailyLimitRewarded")
    @Expose
    public String dailyLimitRewarded;
    @SerializedName("rewardedWord")
    @Expose
    public String rewardedWord;
    @SerializedName("rewardedImage")
    @Expose
    public String rewardedImage;
    @SerializedName("appOpensAdsEnable")
    @Expose
    public String appOpensAdsEnable;
    @SerializedName("appOpenAdsId")
    @Expose
    public String appOpenAdsId;
    @SerializedName("razorpayKeyId")
    @Expose
    public String razorpayKeyId;
    @SerializedName("razorpayKeySecret")
    @Expose
    public String razorpayKeySecret;
    @SerializedName("razorpayEnable")
    @Expose
    public String razorpayEnable;
    @SerializedName("stripePublishableKey")
    @Expose
    public String stripePublishableKey;
    @SerializedName("stripeSecretKey")
    @Expose
    public String stripeSecretKey;
    @SerializedName("stripeEnable")
    @Expose
    public String stripeEnable;
    @SerializedName("currency")
    @Expose
    public String currency;

    public AppInfo(@NonNull String email, String phoneNo, String address, String description, String facebook, String instagram, String youtube, String appName, String linkedin, String twitter, AppVersion appVersion, String privacyPolicy, String refundPolicy, String termsCondition, String userStatus, String admobPublisherId, String admobAppId, String rewardedId, String rewardedDisplayType, String bannerId, String bannerDisplayType, String interstitialId, String interstitialDisplayType, String clickInterstitialAd, String nativeId, String nativeDisplayType, String dailyLimitRewarded, String rewardedWord, String rewardedImage, String appOpensAdsEnable, String appOpenAdsId, String razorpayKeyId, String razorpayKeySecret, String razorpayEnable, String stripePublishableKey, String stripeSecretKey, String stripeEnable, String currency) {
        this.email = email;
        this.phoneNo = phoneNo;
        this.address = address;
        this.description = description;
        this.facebook = facebook;
        this.instagram = instagram;
        this.youtube = youtube;
        this.appName = appName;
        this.linkedin = linkedin;
        this.twitter = twitter;
        this.appVersion = appVersion;
        this.privacyPolicy = privacyPolicy;
        this.refundPolicy = refundPolicy;
        this.termsCondition = termsCondition;
        this.userStatus = userStatus;
        this.admobPublisherId = admobPublisherId;
        this.admobAppId = admobAppId;
        this.rewardedId = rewardedId;
        this.rewardedDisplayType = rewardedDisplayType;
        this.bannerId = bannerId;
        this.bannerDisplayType = bannerDisplayType;
        this.interstitialId = interstitialId;
        this.interstitialDisplayType = interstitialDisplayType;
        this.clickInterstitialAd = clickInterstitialAd;
        this.nativeId = nativeId;
        this.nativeDisplayType = nativeDisplayType;
        this.dailyLimitRewarded = dailyLimitRewarded;
        this.rewardedWord = rewardedWord;
        this.rewardedImage = rewardedImage;
        this.appOpensAdsEnable = appOpensAdsEnable;
        this.appOpenAdsId = appOpenAdsId;
        this.razorpayKeyId = razorpayKeyId;
        this.razorpayKeySecret = razorpayKeySecret;
        this.razorpayEnable = razorpayEnable;
        this.stripePublishableKey = stripePublishableKey;
        this.stripeSecretKey = stripeSecretKey;
        this.stripeEnable = stripeEnable;
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "email='" + email + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", facebook='" + facebook + '\'' +
                ", instagram='" + instagram + '\'' +
                ", youtube='" + youtube + '\'' +
                ", appName='" + appName + '\'' +
                ", linkedin='" + linkedin + '\'' +
                ", twitter='" + twitter + '\'' +
                ", appVersion=" + appVersion +
                ", privacyPolicy='" + privacyPolicy + '\'' +
                ", refundPolicy='" + refundPolicy + '\'' +
                ", termsCondition='" + termsCondition + '\'' +
                ", userStatus='" + userStatus + '\'' +
                ", admobPublisherId='" + admobPublisherId + '\'' +
                ", admobAppId='" + admobAppId + '\'' +
                ", rewardedId='" + rewardedId + '\'' +
                ", rewardedDisplayType='" + rewardedDisplayType + '\'' +
                ", bannerId='" + bannerId + '\'' +
                ", bannerDisplayType='" + bannerDisplayType + '\'' +
                ", interstitialId='" + interstitialId + '\'' +
                ", interstitialDisplayType='" + interstitialDisplayType + '\'' +
                ", clickInterstitialAd='" + clickInterstitialAd + '\'' +
                ", nativeId='" + nativeId + '\'' +
                ", nativeDisplayType='" + nativeDisplayType + '\'' +
                ", dailyLimitRewarded='" + dailyLimitRewarded + '\'' +
                ", rewardedWord='" + rewardedWord + '\'' +
                ", rewardedImage='" + rewardedImage + '\'' +
                ", appOpensAdsEnable='" + appOpensAdsEnable + '\'' +
                ", appOpenAdsId='" + appOpenAdsId + '\'' +
                ", razorpayKeyId='" + razorpayKeyId + '\'' +
                ", razorpayKeySecret='" + razorpayKeySecret + '\'' +
                ", razorpayEnable='" + razorpayEnable + '\'' +
                ", stripePublishableKey='" + stripePublishableKey + '\'' +
                ", stripeSecretKey='" + stripeSecretKey + '\'' +
                ", stripeEnable='" + stripeEnable + '\'' +
                ", currency='" + currency + '\'' +
                '}';
    }
}
