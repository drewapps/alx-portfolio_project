package com.drewapps.ai.ui.activity;

import static com.drewapps.ai.MyApplication.ShowOpenAds;
import static com.drewapps.ai.utils.Constants.ADMOB_OPEN_AD_ENABLE;
import static com.drewapps.ai.utils.Constants.ADMOB_OPEN_AD_ID;
import static com.drewapps.ai.utils.Constants.ADMOB_PUBLISHER_ID;
import static com.drewapps.ai.utils.Constants.AVL_REWARD;
import static com.drewapps.ai.utils.Constants.BANNER_AD_ID;
import static com.drewapps.ai.utils.Constants.BANNER_AD_TYPE;
import static com.drewapps.ai.utils.Constants.CURRENCY;
import static com.drewapps.ai.utils.Constants.CURRENT_REWARD;
import static com.drewapps.ai.utils.Constants.DAILY_REWARD_LIMIT;
import static com.drewapps.ai.utils.Constants.INTERSTITIAL_AD_CLICK;
import static com.drewapps.ai.utils.Constants.INTERSTITIAL_AD_ID;
import static com.drewapps.ai.utils.Constants.INTERSTITIAL_AD_TYPE;
import static com.drewapps.ai.utils.Constants.NATIVE_AD_ID;
import static com.drewapps.ai.utils.Constants.NATIVE_AD_TYPE;
import static com.drewapps.ai.utils.Constants.PRIVACY_POLICY;
import static com.drewapps.ai.utils.Constants.RAZOR_PAY_ENABLE;
import static com.drewapps.ai.utils.Constants.RAZOR_PAY_ID;
import static com.drewapps.ai.utils.Constants.RAZOR_PAY_SECRET;
import static com.drewapps.ai.utils.Constants.REWARD_AD_ID;
import static com.drewapps.ai.utils.Constants.REWARD_AD_TYPE;
import static com.drewapps.ai.utils.Constants.REWARD_IMAGE;
import static com.drewapps.ai.utils.Constants.REWARD_WORD;
import static com.drewapps.ai.utils.Constants.STRIPE_ENABLE;
import static com.drewapps.ai.utils.Constants.STRIPE_ID;
import static com.drewapps.ai.utils.Constants.STRIPE_SECRET;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.drewapps.ai.Ads.GDPRChecker;
import com.drewapps.ai.utils.Tools;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.drewapps.ai.Config;
import com.drewapps.ai.MyApplication;
import com.drewapps.ai.R;
import com.drewapps.ai.databinding.ActivityLoginBinding;
import com.drewapps.ai.items.AppVersion;
import com.drewapps.ai.ui.dialogs.DialogMsg;
import com.drewapps.ai.utils.Connectivity;
import com.drewapps.ai.utils.Constants;
import com.drewapps.ai.utils.PrefManager;
import com.drewapps.ai.utils.Util;
import com.drewapps.ai.viewmodel.UserDataViewModel;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    AlphaAnimation alphaAnimation, alphaAnimation2;
    UserDataViewModel userDataViewModel;
    PrefManager prefManager;
    DialogMsg dialogMsg;
    ProgressDialog prgDialog;
    Connectivity connectivity;

    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialogMsg = new DialogMsg(this, false);
        prefManager = new PrefManager(this);
        prgDialog = new ProgressDialog(this);
        connectivity = new Connectivity(this);
        prgDialog.setMessage("Please Wait...");
        prgDialog.setCancelable(false);

        initViewModel();
        setUpUi();
        initData();
    }

    private void initViewModel() {
        userDataViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);
    }

    private void setUpUi() {
        alphaAnimation = new AlphaAnimation(0.2f, 1.0f);
        alphaAnimation.setDuration(600);
        alphaAnimation.setRepeatCount(-1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        binding.ivIcon.startAnimation(alphaAnimation);

        alphaAnimation2 = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation2.setDuration(500);

        binding.btnGoogle.setOnClickListener(v -> {

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


            try {
                mGoogleSignInClient.signOut();
            } catch (Exception e) {
            }

            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

            if (account != null) {

                String fname = "" + account.getGivenName();
                String lname = "" + account.getFamilyName();
                String email = "" + account.getEmail();
                String auth_tokon = "" + account.getIdToken();
                String image = "" + account.getPhotoUrl();

                String auth_token = "" + account.getIdToken();
                userDataViewModel.setGoogleLogin(fname, email, image);
            } else {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                someActivityResultLauncher.launch(signInIntent);
            }
        });

        binding.txtClickHere.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, PrivacyActivity.class);
            intent.putExtra("type", Constants.PRIVACY_POLICY);
            intent.putExtra(Constants.DISABLE, "YES");
            startActivity(intent);
        });
    }

    private void initData() {
        if (connectivity.isConnected()) {
            Util.showLog("Internet connected");

            FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
            FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                    .setMinimumFetchIntervalInSeconds(60)
                    .build();
            mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
            mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);
            mFirebaseRemoteConfig.fetchAndActivate()
                    .addOnCompleteListener(new OnCompleteListener<Boolean>() {
                        @Override
                        public void onComplete(@NonNull Task<Boolean> task) {
                            if (task.isSuccessful()) {
                                boolean updated = task.getResult();
                                Util.showLog("Config params updated: " + updated);

                            } else {

                            }
                            Util.showLog("API_KEY: " + mFirebaseRemoteConfig.getString("apikey"));
                            prefManager.setString(Constants.api_key, mFirebaseRemoteConfig.getString("apikey"));

                            Config.API_KEY = prefManager.getString(Constants.api_key);
                            Integer ID = prefManager.getInt(Constants.USER_ID);
                            userDataViewModel.setAppInfoObj(ID == 0 ? "NoUser" : String.valueOf(ID));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialogMsg.showErrorDialog("Error Loading API, Please try again", "Try Again");
                            dialogMsg.show();
                            dialogMsg.okBtn.setOnClickListener(v -> {
                                dialogMsg.cancel();
                                initData();
                            });
                        }
                    });

            userDataViewModel.getAppInfo().observe(this, result -> {
                if (result != null) {
                    switch (result.status) {
                        case SUCCESS:

                            if (result.data.userStatus.equals(Constants.DELETED) && prefManager.getInt(Constants.USER_ID) != 0) {
                                dialogMsg.showErrorDialog("Your Account Was Deleted\nUse another account", "Ok");
                                dialogMsg.show();
                                dialogMsg.okBtn.setOnClickListener(v -> {
                                    prefManager.setBoolean(Constants.IS_LOGGED_IN, false);
                                    userDataViewModel.logout().observe(this, results -> {
                                        if (results != null) {
                                            switch (results.status) {
                                                case SUCCESS:
                                                    MyApplication.prefManager().setInt(Constants.USER_ID, 0);
                                                    finish();
                                                    break;

                                                case ERROR:
                                                    prgDialog.cancel();
                                                    dialogMsg.showErrorDialog(results.message, "Ok");
                                                    dialogMsg.show();
                                                    break;
                                            }
                                        }
                                    });
                                    dialogMsg.cancel();
                                    finish();
                                });
                            } else if (result.data.userStatus.equals(Constants.DISABLE)) {
                                dialogMsg.showErrorDialog("Your Account Was Disabled\nContact Us", "Ok");
                                dialogMsg.show();
                                dialogMsg.okBtn.setOnClickListener(v -> {
                                    dialogMsg.cancel();
                                    Intent intent = new Intent(LoginActivity.this, ContactActivity.class);
                                    intent.putExtra(Constants.DISABLE, "YES");
                                    startActivity(intent);
                                    finish();
                                });
                            } else {
                                prefManager.setString(PRIVACY_POLICY, result.data.privacyPolicy);
                                prefManager.setString(Constants.TERM_CONDITION, result.data.termsCondition);
                                prefManager.setString(Constants.REFUND_POLICY, result.data.refundPolicy);
                                checkVersionNo(result.data.appVersion);

                                prefManager.setString(ADMOB_PUBLISHER_ID, result.data.admobPublisherId);
                                prefManager.setString(ADMOB_OPEN_AD_ID, result.data.appOpenAdsId);
                                prefManager.setString(ADMOB_OPEN_AD_ENABLE, result.data.appOpensAdsEnable);
                                prefManager.setString(BANNER_AD_ID, result.data.bannerId);
                                prefManager.setString(BANNER_AD_TYPE, result.data.bannerDisplayType);
                                prefManager.setString(INTERSTITIAL_AD_ID, result.data.interstitialId);
                                prefManager.setString(INTERSTITIAL_AD_TYPE, result.data.interstitialDisplayType);
                                prefManager.setString(INTERSTITIAL_AD_CLICK, result.data.clickInterstitialAd);
                                prefManager.setString(REWARD_AD_ID, result.data.rewardedId);
                                prefManager.setString(REWARD_AD_TYPE, result.data.rewardedDisplayType);
                                prefManager.setString(DAILY_REWARD_LIMIT, result.data.dailyLimitRewarded);
                                prefManager.setString(REWARD_WORD, result.data.rewardedWord);
                                prefManager.setString(REWARD_IMAGE, result.data.rewardedImage);
                                prefManager.setString(NATIVE_AD_ID, result.data.nativeId);
                                prefManager.setString(NATIVE_AD_TYPE, result.data.nativeDisplayType);

                                prefManager.setString(RAZOR_PAY_ID, result.data.razorpayKeyId);
                                prefManager.setString(RAZOR_PAY_SECRET, result.data.razorpayKeySecret);
                                prefManager.setString(RAZOR_PAY_ENABLE, result.data.razorpayEnable);

                                prefManager.setString(STRIPE_ID, result.data.stripeSecretKey);
                                prefManager.setString(STRIPE_SECRET, result.data.stripeSecretKey);
                                prefManager.setString(STRIPE_ENABLE, result.data.stripeEnable);

                                prefManager.setString(CURRENCY, result.data.currency);

                                Tools.TodayRewardAvl(this);

                                if (prefManager.getInt(CURRENT_REWARD) >= Integer.valueOf(prefManager.getString(DAILY_REWARD_LIMIT))) {
                                    prefManager.setBoolean(AVL_REWARD, false);
                                } else {
                                    prefManager.setBoolean(AVL_REWARD, true);
                                }


                                new GDPRChecker()
                                        .withContext(LoginActivity.this)
                                        .withPrivacyUrl(prefManager.getString(PRIVACY_POLICY))
                                        .withPublisherIds(prefManager.getString(ADMOB_PUBLISHER_ID))
                                        .check();
                                ShowOpenAds();
                            }

                            break;

                        case ERROR:

                            if (prefManager.getString(Constants.api_key).equals("demoKey")) {
                                dialogMsg.showErrorDialog("Wrong ApiKey", "Try Again");
                                dialogMsg.show();
                                dialogMsg.okBtn.setOnClickListener(v -> {
                                    dialogMsg.cancel();
                                    initData();
                                });
                            } else {
                                dialogMsg.showErrorDialog(result.message, "Ok");
                                dialogMsg.show();
                                dialogMsg.okBtn.setOnClickListener(v -> {
                                    dialogMsg.cancel();
                                    finish();
                                });
                            }
                            break;
                    }
                }
            });

            userDataViewModel.googleLoginData().observe(this, result -> {
                if (result != null) {
                    switch (result.status) {
                        case SUCCESS:

                            prefManager.setBoolean(Constants.IS_LOGGED_IN, true);
                            prefManager.setInt(Constants.USER_ID, result.data.userId);
                            prefManager.setString(Constants.USER_NAME, result.data.userName);
                            prefManager.setString(Constants.USER_EMAIL, result.data.emailId);
                            prefManager.setString(Constants.USER_IMAGE, result.data.profileImage);

                            dialogMsg.showSuccessDialog("Successfully Login Your Account", "Next");
                            dialogMsg.show();
                            dialogMsg.okBtn.setOnClickListener(v -> {
                                dialogMsg.cancel();
                                if (prefManager.getBoolean(Constants.IS_FIRST_TIME)) {
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                } else {
                                    prefManager.setBoolean(Constants.IS_FIRST_TIME, true);
                                    startActivity(new Intent(LoginActivity.this, IntroActivity.class)
                                            .putExtra("FROM_LOGIN", "true"));
                                    finish();
                                }
                            });

                            break;

                        case ERROR:
                            prgDialog.dismiss();
                            prefManager.setBoolean(Constants.IS_LOGGED_IN, false);
                            dialogMsg.showErrorDialog(result.message, "Ok");
                            dialogMsg.show();

                            break;
                    }
                }
            });
        } else {
            Util.showLog("Internet is not connected");
            dialogMsg.showNoInternetDialog("Your Internet is not Connected.\nPlease connect internet", "Ok");
            dialogMsg.okBtn.setOnClickListener(v -> {
                dialogMsg.cancel();
                finish();
            });
            dialogMsg.show();
        }
    }

    private void checkVersionNo(AppVersion appVersion) {
        if (appVersion.updatePopupShow.equals(Config.ONE) && !appVersion.newAppVersionCode.equals(Config.APP_VERSION)) {

            dialogMsg.showAppInfoDialog("Update", "Cancel",
                    "Update is Available", appVersion.versionMessage);
            dialogMsg.show();

            if (appVersion.cancelOption.equals(Config.ZERO)) {
                dialogMsg.cancelBtn.setVisibility(View.GONE);
            }
            dialogMsg.cancelBtn.setOnClickListener(v -> {
                dialogMsg.cancel();
                gotoMainActivity();
            });

            dialogMsg.okBtn.setOnClickListener(v -> {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(appVersion.appLink)));
            });

        } else {
            gotoMainActivity();
        }
    }

    private void gotoMainActivity() {
        if (prefManager.getBoolean(Constants.IS_LOGGED_IN)) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        } else {
            binding.ivIcon.clearAnimation();
            binding.ivIcon.animate().translationY(-(MyApplication.getScreenHeight() / 2) * 0.6f)
                    .scaleY(1.5f)
                    .scaleX(1.5f)
                    .setInterpolator(new LinearOutSlowInInterpolator())
                    .setDuration(1000).start();

            binding.lvForm.setVisibility(View.VISIBLE);
            binding.lvForm.startAnimation(alphaAnimation2);
        }
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Here, no request code
                        if (result.getData() != null) {
                            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                            try {
                                // Google Sign In was successful, authenticate with Firebase
                                GoogleSignInAccount account = task.getResult(ApiException.class);
                                if (account != null) {
                                    prgDialog.show();
                                    Util.showLog("Google sign in success ");
                                    String fname = "" + account.getGivenName();
                                    String lname = "" + account.getFamilyName();
                                    String email = "" + account.getEmail();
                                    String auth_tokon = "" + account.getIdToken();
                                    Uri image = account.getPhotoUrl();
                                    String path;
                                    if (image != null) {
                                        Util.showLog("Image NotNull");
                                        path = "" + account.getPhotoUrl();
                                    } else {
                                        path = "https://lh3.googleusercontent.com/-XdUIqdMkCWA/AAAAAAAAAAI/AAAAAAAAAAA/4252rscbv5M/photo.jpg";
                                    }
                                    Util.showLog("Image: " + path);

                                    String auth_token = "" + account.getIdToken();
                                    userDataViewModel.setGoogleLogin(fname, email, path);
                                }
                            } catch (ApiException e) {
                                // Google Sign In failed, update UI appropriately
                                Util.showLog("Google sign in failed: " + e);
                                prgDialog.dismiss();
                                dialogMsg.showErrorDialog("Google sign in failed", "Ok");
                                dialogMsg.show();
                                // ...
                            }
                        }
                    }
                }
            });

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        super.onBackPressed();
        finish();
        System.exit(0);
    }
}