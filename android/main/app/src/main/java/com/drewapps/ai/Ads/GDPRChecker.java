package com.drewapps.ai.Ads;

import android.Manifest;
import android.app.Activity;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;

import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.FormError;
import com.google.android.ump.UserMessagingPlatform;

public class GDPRChecker {
    private static final String TAG = "GDPRChecker";
    private ConsentInformation consentInformation;
    private Activity context;
    private String privacyUrl;
    private String[] publisherIds;
    private static GDPRChecker instance;
    private ConsentForm consentForm;
    private static int status = ConsentInformation.ConsentStatus.REQUIRED;
    private boolean withAdFreeOption = false;

    protected GDPRChecker(Activity context) {
        this.context = context;
        this.consentInformation = UserMessagingPlatform.getConsentInformation(context);
    }

    public GDPRChecker() {
    }

    public GDPRChecker withContext(Activity context) {
        instance = new GDPRChecker(context);
        return instance;
    }

    public GDPRChecker withPrivacyUrl(String privacyUrl) {
        this.privacyUrl = privacyUrl;
        if (instance == null)
            throw new NullPointerException("Please call withContext first");
        return instance;
    }

    @RequiresPermission(Manifest.permission.INTERNET)
    private void initGDPR() {
        if (publisherIds == null)
            throw new NullPointerException("publisherIds is null, please call withPublisherIds first");

        ConsentRequestParameters params = new ConsentRequestParameters
                .Builder()
                .setTagForUnderAgeOfConsent(false)
                .build();

        consentInformation.requestConsentInfoUpdate(
                context,
                params,
                new ConsentInformation.OnConsentInfoUpdateSuccessListener() {
                    @Override
                    public void onConsentInfoUpdateSuccess() {

                        if (consentInformation.isConsentFormAvailable()) {

                            Log.w("PTag", "Ram 1");
                            loadForm();
                        }
                    }
                },
                new ConsentInformation.OnConsentInfoUpdateFailureListener() {
                    @Override
                    public void onConsentInfoUpdateFailure(FormError formError) {
                        Log.w("PTag", "Ram 3");
                        loadForm();
                        // initializeAds();
                    }
                });
    }

    private void loadForm() {
        UserMessagingPlatform.loadConsentForm(
                context,
                new UserMessagingPlatform.OnConsentFormLoadSuccessListener() {
                    @Override
                    public void onConsentFormLoadSuccess(ConsentForm consentForm1) {
                        consentForm = consentForm1;

                        if (consentInformation.getConsentStatus() == ConsentInformation.ConsentStatus.REQUIRED) {
                            consentForm.show(
                                    context,
                                    new ConsentForm.OnConsentFormDismissedListener() {
                                        @Override
                                        public void onConsentFormDismissed(@Nullable FormError formError) {
                                            // Handle dismissal by reloading form.
                                            Log.w("PTag", "Ram 4");
                                            loadForm();
                                            // initializeAds();
                                        }
                                    });

                        } else {
                            Log.w("PTag", "Ram 5");
                        }
                        status = consentInformation.getConsentStatus();
                    }
                },
                new UserMessagingPlatform.OnConsentFormLoadFailureListener() {
                    @Override
                    public void onConsentFormLoadFailure(FormError formError) {
                        Log.w("PTag", "Ram 6");

                    }
                }
        );
    }

    public void check() {
        initGDPR();
    }

    public GDPRChecker withPublisherIds(String... publisherIds) {
        this.publisherIds = publisherIds;
        if (instance == null)
            throw new NullPointerException("Please call withContext first");
        return instance;
    }


    public static int getStatus() {
        return status;
    }

}