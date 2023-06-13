package com.drewapps.ai.ui.activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import static com.drewapps.ai.utils.Constants.AVL_REWARD;
import static com.drewapps.ai.utils.Constants.CURRENT_REWARD;
import static com.drewapps.ai.utils.Constants.DAILY_REWARD_LIMIT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.applovin.adview.AppLovinIncentivizedInterstitial;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.MaxReward;
import com.applovin.mediation.MaxRewardedAdListener;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.applovin.sdk.AppLovinAd;
import com.applovin.sdk.AppLovinAdDisplayListener;
import com.applovin.sdk.AppLovinAdLoadListener;
import com.drewapps.ai.Ads.GDPRChecker;
import com.drewapps.ai.Config;
import com.drewapps.ai.MyApplication;
import com.drewapps.ai.R;
import com.drewapps.ai.adapters.AdapterPlans;
import com.drewapps.ai.api.ApiClient;
import com.drewapps.ai.api.ApiResponse;
import com.drewapps.ai.billing.BillingEventListener;
import com.drewapps.ai.billing.BillingManager;
import com.drewapps.ai.billing.enums.ProductType;
import com.drewapps.ai.billing.models.BillingResponse;
import com.drewapps.ai.billing.models.ProductInfo;
import com.drewapps.ai.billing.models.PurchaseInfo;
import com.drewapps.ai.databinding.ActivityPremiumBinding;
import com.drewapps.ai.items.ItemSubsPlan;
import com.drewapps.ai.items.StripeResponse;
import com.drewapps.ai.ui.dialogs.DialogMsg;
import com.drewapps.ai.ui.fragment.PaymentDialogFragment;
import com.drewapps.ai.utils.Connectivity;
import com.drewapps.ai.utils.Constants;
import com.drewapps.ai.utils.PrefManager;
import com.drewapps.ai.utils.Util;
import com.drewapps.ai.viewmodel.UserDataViewModel;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.ump.ConsentInformation;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.model.Placement;
import com.ironsource.mediationsdk.sdk.RewardedVideoListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;

public class PremiumActivity extends AppCompatActivity implements PaymentResultListener {

    ActivityPremiumBinding binding;

    UserDataViewModel userDataViewModel;

    AdapterPlans adapterPlans;

    Connectivity connectivity;

    DialogMsg dialogMsg;

    List<ItemSubsPlan> planList;
    ItemSubsPlan currentItem;
    BillingManager billingManager;
    ProgressDialog prgDialog;

    String BASE_64KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxwbeDsKOxoe5Bzh40b0O/CHNcYI4jXT7s8HrITA+Vp21a4D5W7U1oxuuZJj6xggBHA0PWKi6cm+zHzfeZq+Y+Jtxo1S44URcdFmrNevikF9w83uwtb7tnnTl9i2qxRpQhso8weI9PLBVrsGI0n/ZqhxTCLS/pAYjBsQjMNu9tTcw0D4aNUEuT7sK8+a/ifMCzieGvAf5gQsQJhr+M7ESmF29k1v2D7dB4fAGju/97aYz5sFsgttbH2Rin61bbhVcC2yIr2BJPx+c6rTnD7Y7sUCx5f0BG7x+21WvlWG1zWXItG4kHzS7ZTQEOEebmC2zJE2uUF0x09WjffUYsr+s8QIDAQAB";

    String CURRENT_SKU = "";
    boolean isLoaded = false;

    String stripeOrderID = "";
    String publisherKey = "";
    PaymentSheet paymentSheet;
    String paymentIntentClientSecret;
    PaymentSheet.CustomerConfiguration customerConfig;

    RewardedAd rewardedAd;
    private MaxRewardedAd maxRewardedVideoAd;
    private AppLovinIncentivizedInterstitial applovinRewardedVideoAd;
    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPremiumBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prefManager = new PrefManager(this);
        connectivity = new Connectivity(this);
        dialogMsg = new DialogMsg(this, false);
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Loading...");
        prgDialog.setCancelable(false);

        if (MyApplication.prefManager().getString(Constants.RAZOR_PAY_ENABLE).equals(Config.ONE)) {
            Checkout.preload(this);
        }

        if (MyApplication.prefManager().getString(Constants.STRIPE_ENABLE).equals(Config.ONE)) {
            paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);
        }

        setUpUI();
        intViewModel();

        if (prefManager.getBoolean(Constants.AVL_REWARD) && !prefManager.getString(Constants.REWARD_AD_TYPE).equals(Constants.FALSE)) {
            binding.fullScreenCardView.setVisibility(VISIBLE);
            LoadRewardAds();
        } else {
            binding.fullScreenCardView.setVisibility(GONE);
        }
    }

    public boolean isAdLoaded = false;

    public void LoadRewardAds() {
        if (!MyApplication.prefManager().getString(Constants.REWARD_AD_TYPE).equals(Constants.FALSE)) {
            switch (MyApplication.prefManager().getString(Constants.REWARD_AD_TYPE)) {
                case Constants.ADMOB:
                    AdRequest.Builder builder = new AdRequest.Builder();
                    int request = GDPRChecker.getStatus();
                    if (request == ConsentInformation.ConsentStatus.NOT_REQUIRED) {
                        // load non Personalized ads
                        Bundle extras = new Bundle();
                        extras.putString("npa", "1");
                        builder.addNetworkExtrasBundle(AdMobAdapter.class, extras);
                    } // else do nothing , it will load PERSONALIZED ads
                    RewardedAd.load(this, MyApplication.prefManager().getString(Constants.REWARD_AD_ID), builder.build(),
                            new RewardedAdLoadCallback() {
                                @Override
                                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                    // Handle the error.
                                    Util.showLog("Load Error" + loadAdError.toString());
                                    rewardedAd = null;
                                    onRewardedLoadFail();
                                }

                                @Override
                                public void onAdLoaded(@NonNull RewardedAd ad) {
                                    rewardedAd = ad;
                                    onRewardAdLoad();
                                }
                            });
                    break;
                case Constants.MAX:
                    maxRewardedVideoAd = MaxRewardedAd.getInstance(prefManager.getString(Constants.REWARD_AD_ID), this);
                    maxRewardedVideoAd.setListener(new MaxRewardedAdListener() {
                        @Override
                        public void onRewardedVideoStarted(MaxAd ad) {

                        }

                        @Override
                        public void onRewardedVideoCompleted(MaxAd ad) {

                        }

                        @Override
                        public void onUserRewarded(MaxAd ad, MaxReward reward) {

                        }

                        @Override
                        public void onAdLoaded(MaxAd ad) {
                            onRewardAdLoad();
                        }

                        @Override
                        public void onAdDisplayed(MaxAd ad) {

                        }

                        @Override
                        public void onAdHidden(MaxAd ad) {
                            maxRewardedVideoAd = null;
                            onRewardAdDismissed();
                        }

                        @Override
                        public void onAdClicked(MaxAd ad) {

                        }

                        @Override
                        public void onAdLoadFailed(String adUnitId, MaxError error) {
                            // Rewarded ad failed to load
                            // We recommend retrying with exponentially higher delays up to a maximum delay (in this case 64 seconds)
                            maxRewardedVideoAd = null;
                            onRewardedLoadFail();

                        }

                        @Override
                        public void onAdDisplayFailed(MaxAd ad, MaxError error) {

                        }
                    });

                    maxRewardedVideoAd.loadAd();

                    break;
                case Constants.APPLOVIN:
                    Util.showLog("AA");
                    if (applovinRewardedVideoAd == null) {
                        applovinRewardedVideoAd = AppLovinIncentivizedInterstitial.create(this);
                        applovinRewardedVideoAd.preload(new AppLovinAdLoadListener() {
                            @Override
                            public void adReceived(AppLovinAd ad) {
                                onRewardAdLoad();
                            }

                            @Override
                            public void failedToReceiveAd(int errorCode) {
                                applovinRewardedVideoAd = null;
                                Util.showLog("APPLOVIN: " + errorCode);
                                onRewardedLoadFail();
                            }
                        });
                    } else {
                        Util.showLog("AA E");
                        if (!applovinRewardedVideoAd.isAdReadyToDisplay()) {
                            applovinRewardedVideoAd.preload(new AppLovinAdLoadListener() {
                                @Override
                                public void adReceived(AppLovinAd ad) {
                                    onRewardAdLoad();
                                }

                                @Override
                                public void failedToReceiveAd(int errorCode) {
                                    applovinRewardedVideoAd = null;
                                    onRewardedLoadFail();
                                    Util.showLog("APPLOVIN: " + errorCode);
                                }
                            });
                        }
                    }
                    break;
                case Constants.IRON_SOURCE:
                    if (!IronSource.isRewardedVideoAvailable()) {
                        IronSource.init(this, prefManager.getString(Constants.REWARD_AD_ID), IronSource.AD_UNIT.REWARDED_VIDEO);
                        IronSource.setRewardedVideoListener(new RewardedVideoListener() {
                            @Override
                            public void onRewardedVideoAdOpened() {

                            }

                            @Override
                            public void onRewardedVideoAdClosed() {
                                onRewardAdDismissed();
                            }

                            @Override
                            public void onRewardedVideoAvailabilityChanged(boolean available) {
                                if (available) {
                                    onRewardAdLoad();
                                }
                            }

                            @Override
                            public void onRewardedVideoAdStarted() {

                            }

                            @Override
                            public void onRewardedVideoAdEnded() {

                            }

                            @Override
                            public void onRewardedVideoAdRewarded(Placement placement) {

                            }

                            @Override
                            public void onRewardedVideoAdShowFailed(IronSourceError ironSourceError) {
                                onRewardedLoadFail();
                            }

                            @Override
                            public void onRewardedVideoAdClicked(Placement placement) {

                            }
                        });
                        IronSource.loadRewardedVideo();
                    }
                    break;
                case Constants.UNITY:
                    if (!isAdLoaded) {
                        UnityAds.load(prefManager.getString(Constants.REWARD_AD_ID), new IUnityAdsLoadListener() {
                            @Override
                            public void onUnityAdsAdLoaded(String placementId) {
                                isAdLoaded = true;
                                onRewardAdLoad();
                            }

                            @Override
                            public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
                                isAdLoaded = false;
                                onRewardedLoadFail();
                            }
                        });
                    }
                    break;
            }
        } else {
            binding.fullScreenCardView.setVisibility(GONE);
        }
    }

    private void onRewardAdLoad() {
        changeTheBackgroundOfTheCardView(binding.fullScreenView, binding.fullScreenCardView, true);
    }

    private void onRewardedLoadFail() {
        changeTheBackgroundOfTheCardView(binding.fullScreenView, binding.fullScreenCardView, false);
    }

    public void showAd() {
        if (!MyApplication.prefManager().getString(Constants.REWARD_AD_TYPE).equals(Constants.FALSE)) {
            switch (MyApplication.prefManager().getString(Constants.REWARD_AD_TYPE)) {
                case Constants.ADMOB:
                    if (rewardedAd != null) {
                        rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdClicked() {
                                // Called when a click is recorded for an ad.
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                // Set the ad reference to null so you don't show the ad a second time.
                                rewardedAd = null;
                                onRewardAdDismissed();
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                Util.showLog("Ad failed to show");
                                // Called when ad fails to show.
                                rewardedAd = null;
                                onRewardedLoadFail();
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
                        if (rewardedAd != null) {
                            rewardedAd.show(this, new OnUserEarnedRewardListener() {
                                @Override
                                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                    Util.showLog("REWARDED: " + rewardItem);

                                }
                            });
                        }
                    }
                    break;
                case Constants.MAX:
                    if (maxRewardedVideoAd != null) {
                        maxRewardedVideoAd.showAd(prefManager.getString(Constants.REWARD_AD_ID));
                    }
                    break;
                case Constants.APPLOVIN:
                    if (applovinRewardedVideoAd != null) {
                        applovinRewardedVideoAd.show(this, null, null, new AppLovinAdDisplayListener() {
                            @Override
                            public void adDisplayed(AppLovinAd appLovinAd) {

                            }

                            @Override
                            public void adHidden(AppLovinAd appLovinAd) {
                                applovinRewardedVideoAd = null;
                                onRewardAdDismissed();
                            }
                        });
                    }
                    break;
                case Constants.IRON_SOURCE:
                    if (IronSource.isRewardedVideoAvailable()) {
                        IronSource.showRewardedVideo();
                    }
                    break;
                case Constants.UNITY:
                    if (isAdLoaded) {
                        UnityAds.show(this, prefManager.getString(Constants.REWARD_AD_ID), new IUnityAdsShowListener() {
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
                                isAdLoaded = false;
                                onRewardAdDismissed();
                            }
                        });
                    }
            }
        }
    }

    public boolean isLoaded() {
        switch (prefManager.getString(Constants.REWARD_AD_TYPE)) {
            case Constants.ADMOB:
                return rewardedAd == null ? false : true;
            case Constants.MAX:
                return maxRewardedVideoAd == null ? false : maxRewardedVideoAd.isReady() ? true : false;
            case Constants.APPLOVIN:
                return applovinRewardedVideoAd == null ? false : applovinRewardedVideoAd.isAdReadyToDisplay() ? true : false;
            case Constants.IRON_SOURCE:
                return IronSource.isRewardedVideoAvailable() ? true : false;
            case Constants.UNITY:
                return isAdLoaded;

        }
        return false;
    }

    public void onRewardAdDismissed() {
        onRewardedLoadFail();
        if (prefManager.getInt(CURRENT_REWARD) >= Integer.valueOf(prefManager.getString(DAILY_REWARD_LIMIT))) {
            prefManager.setBoolean(AVL_REWARD, false);
            binding.fullScreenCardView.setVisibility(GONE);
        } else {
            prefManager.setBoolean(AVL_REWARD, true);
        }
        prgDialog.show();
        try {
            userDataViewModel.uploadReward(Integer.valueOf(prefManager.getString(Constants.REWARD_WORD)),
                    Integer.valueOf(prefManager.getString(Constants.REWARD_IMAGE)));
        } catch (Exception e) {
            Util.showErrorLog(e.getMessage(), e);
        }
    }

    private void changeTheBackgroundOfTheCardView(View v, CardView cardView, boolean status) {

        if (v != null && cardView != null) {
            if (status) {
                Drawable drawable = getDrawable(R.drawable.gradient_background);

                v.setBackground(drawable);

                cardView.setClickable(true);
            } else {
                Drawable drawable = getDrawable(R.drawable.gradient_background_grey);

                v.setBackground(drawable);

                cardView.setClickable(false);
            }
        }
    }

    private void intViewModel() {
        userDataViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);

        userDataViewModel.getLoadingState().observe(this, loadingState -> {

            if (loadingState != null && !loadingState && !isLoaded) {
                isLoaded = true;
                startBilling();
            }

        });
        userDataViewModel.getSubsPlan(this).observe(this, resource -> {
            if (resource != null) {

                Util.showLog("Got Data" + resource.message + resource.toString());

                switch (resource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        if (resource.data != null) {

                            if (resource.data.size() > 0) {
                                setData(resource.data);
                                showVisibility(true);
                            }
                        } else {
                            showVisibility(false);
                        }
                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server
                        if (resource.data != null && resource.data.size() > 0) {
                            setData(resource.data);
                            userDataViewModel.setLoadingState(false);
                            showVisibility(true);
                        } else {
                            showVisibility(false);
                        }

                        break;
                    case ERROR:
                        // Error State
                        showVisibility(false);
                        userDataViewModel.setLoadingState(false);
                        break;
                    default:
                        // Default

                        break;
                }

            } else {

                // Init Object or Empty Data
                showVisibility(false);
                Util.showLog("Empty Data");

            }
        });
        userDataViewModel.setPlanObj();

        userDataViewModel.getRewardRes().observe(PremiumActivity.this, result -> {
            if (result != null) {
                switch (result.status) {
                    case SUCCESS:
                        prgDialog.cancel();
                        //add offer text
                        Util.showToast(PremiumActivity.this, "Successfully Reward Item");
                        LoadRewardAds();
                        break;
                    case ERROR:
                        prgDialog.cancel();
                        Util.showToast(PremiumActivity.this, "Fail to Reward Item");
                        break;
                }
            }
        });
    }

    private void startBilling() {
        if (planList != null) {
            List<String> consume = new ArrayList<>();

            for (int i = 0; i < planList.size(); i++) {
                Util.showLog("Consume: " + planList.get(i).googleProductEnable + " " + planList.get(i).planName);
                if (planList.get(i).googleProductEnable == 1) {
                    consume.add(planList.get(i).googleProductId);
                }
            }

            billingManager = new BillingManager(this, BASE_64KEY)
                    .setConsumableIds(consume)
                    .autoConsume()
                    .enableLogging()
                    .connect();

            billingManager.setBillingEventListener(new BillingEventListener() {
                @Override
                public void onProductsFetched(@NonNull List<ProductInfo> productDetails) {
                    String product;
                    String price;

                    for (ProductInfo productInfo : productDetails) {
                        product = productInfo.getProduct();
                        price = productInfo.getOneTimePurchaseOfferPrice();
                        userDataViewModel.updatePrice(price, product);
                    }
                }

                @Override
                public void onPurchasedProductsFetched(@NonNull ProductType productType, @NonNull List<PurchaseInfo> purchases) {

                }

                @Override
                public void onProductsPurchased(@NonNull List<PurchaseInfo> purchases) {

                }

                @Override
                public void onPurchaseAcknowledged(@NonNull PurchaseInfo purchase) {

                }

                @Override
                public void onPurchaseConsumed(@NonNull PurchaseInfo purchase) {
                    String consumedProduct = purchase.getProduct();

                    if (consumedProduct.equalsIgnoreCase(CURRENT_SKU)) {
                        CURRENT_SKU = "";
                        Util.showLog(purchase.getOrderId());
                        postPayment(purchase.getOrderId(), "IN-APP");
                    }
                }

                @Override
                public void onBillingError(@NonNull BillingManager billingConnector, @NonNull BillingResponse response) {
                    prgDialog.dismiss();
                    dialogMsg.cancel();
                    dialogMsg.showErrorDialog(response.getDebugMessage(), "Ok");
                    dialogMsg.show();
                }
            });
        }
    }

    private void postPayment(String orderId, String type) {
        userDataViewModel.purchaseData(MyApplication.prefManager().getInt(Constants.USER_ID),
                currentItem, orderId, type).observe(PremiumActivity.this,
                result -> {
                    if (result != null) {
                        switch (result.status) {
                            case SUCCESS:

                                prgDialog.cancel();
                                //add offer text
                                dialogMsg.showSuccessDialog("Your Transaction has been completed successfully", "Ok");
                                dialogMsg.show();
                                dialogMsg.okBtn.setOnClickListener(v -> {
                                    finish();
                                });
                                break;

                            case ERROR:
                                prgDialog.cancel();
                                dialogMsg.showErrorDialog(result.message, "Ok");
                                dialogMsg.show();
                                break;
                        }
                    }
                });
    }

    private void showVisibility(boolean b) {
        if (b) {
            binding.pbMain.setVisibility(View.GONE);
        } else {
            binding.pbMain.setVisibility(View.VISIBLE);
        }
    }

    private void setData(List<ItemSubsPlan> data) {
        planList = data;
        adapterPlans.setPlans(data);

        setButton(planList.get(0));
    }

    private void setButton(ItemSubsPlan itemSubsPlan) {
        currentItem = itemSubsPlan;
        binding.tvBtnTitle.setText(itemSubsPlan.planName);
        binding.tvBtnPrice.setText("" + MyApplication.prefManager().getString(Constants.CURRENCY) + " " + itemSubsPlan.planPrice);
        binding.tvWords.setText(itemSubsPlan.includeWords + " Words");
        binding.tvImage.setText(itemSubsPlan.includeImages + " Images");
        if (itemSubsPlan.rewardedEnable == 1) {
            binding.tvAds.setText("Remove Ads for " + itemSubsPlan.duration);
        }else {
            binding.tvAds.setText("Remove Ads Feature Not Available in this Plan");
        }
    }

    private void setUpUI() {
        adapterPlans = new AdapterPlans(this, plans -> {
            setButton(plans);
        });
        binding.rvPlans.setAdapter(adapterPlans);

        binding.lvPurchase.setOnClickListener(v -> {
            if(currentItem!=null) {
                if (!connectivity.isConnected()) {
                    Util.showToast(this, "Please Connect Internet");
                    return;
                }
                showPayDialog();
            }
        });

        binding.tvPrivacy.setOnClickListener(v -> {
            Intent intent = new Intent(PremiumActivity.this, PrivacyActivity.class);
            intent.putExtra("type", Constants.PRIVACY_POLICY);
            startActivity(intent);
        });

        binding.tvTerm.setOnClickListener(v -> {
            Intent intent = new Intent(PremiumActivity.this, PrivacyActivity.class);
            intent.putExtra("type", Constants.TERM_CONDITION);
            startActivity(intent);
        });

        binding.btClose.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.tvHelp.setOnClickListener(v -> {
            startActivity(new Intent(this, ContactActivity.class));
        });

        binding.fullScreenCardView.setOnClickListener(v -> {

            if (prefManager.getBoolean(AVL_REWARD) && isLoaded()) {
                prefManager.setInt(CURRENT_REWARD, prefManager.getInt(CURRENT_REWARD) + 1);
                showAd();
            } else {
                prefManager.setBoolean(AVL_REWARD, false);
            }
        });
    }

    private void showPayDialog() {
        new PaymentDialogFragment(currentItem, new PaymentDialogFragment.CallBack() {
            @Override
            public void getResponse(String type) {
                if (type.equals("razorpay")) {
                    prgDialog.show();
                    startPayment(Integer.parseInt(currentItem.planPrice), MyApplication.prefManager().getString(Constants.RAZOR_PAY_ID));
                }
                if (type.equals("stripe")) {
                    prgDialog.show();
                    createStripePayment(currentItem.planPrice);
                }
                if (type.equals("in_app")) {
                    prgDialog.show();
                    CURRENT_SKU = currentItem.googleProductId;
                    billingManager.purchase(PremiumActivity.this, currentItem.googleProductId);
                }
            }
        })
                .show(getSupportFragmentManager(), "");
    }

    //**** Razorpay
    private void startPayment(int planPrice, String key) {
        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();
        checkout.setKeyID(key);
        /**
         * Set your logo here
         */
        checkout.setImage(R.mipmap.ic_launcher_round);
        /**
         * Reference to current activity
         */
        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            options.put("name", MyApplication.prefManager().getString(Constants.USER_NAME));
            options.put("description", "Charge Of Plan");
            options.put("theme.color", "#f59614");
            options.put("send_sms_hash", true);
            options.put("allow_rotation", true);
            options.put("currency", MyApplication.prefManager().getString(Constants.CURRENCY));
            options.put("amount", (float) planPrice * 100);//pass amount in currency subunits
            options.put("prefill.email", MyApplication.prefManager().getString(Constants.USER_EMAIL));
            checkout.open(this, options);

        } catch (Exception e) {
            Util.showErrorLog("Error in starting Razorpay Checkout", e);
        }
    }


    @Override
    public void onPaymentSuccess(String s) {
        postPayment(s, "RAZORPAY");
    }

    @Override
    public void onPaymentError(int i, String s) {
        prgDialog.dismiss();
        String message = "";
        if (i == Checkout.PAYMENT_CANCELED) {
            message = "The user canceled the payment.";
        } else if (i == Checkout.NETWORK_ERROR) {
            message = "There was a network error, for example, loss of internet connectivity.";
        } else if (i == Checkout.INVALID_OPTIONS) {
            message = "An issue with options passed in checkout.open .";
        } else if (i == Checkout.TLS_ERROR) {
            message = "The device does not support TLS v1.1 or TLS v1.2.";
        } else {
            message = "Unknown Error";
        }
        dialogMsg.cancel();
        Util.showLog(i + " " + s);
        dialogMsg.showErrorDialog(message, "Ok");
        dialogMsg.show();
    }

    //*** Stripe

    private void createStripePayment(String price) {
        stripeOrderID = "STRIPE_" + System.currentTimeMillis();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            try {

                // Call the API Service
                Response<StripeResponse> response = ApiClient.getApiService().createStripePayment(
                        MyApplication.prefManager().getString(Constants.api_key),
                        price).execute();


                // Wrap with APIResponse Class
                ApiResponse<StripeResponse> apiResponse = new ApiResponse<>(response);

                // If response is successful
                if (apiResponse.isSuccessful()) {

                    Util.showLog("" + apiResponse.body + " " + apiResponse.body.toString());

                    this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            publisherKey = response.body().publishableKey;

                            Util.showLog("KEY: " + publisherKey + " " + response.body().customer + " "
                                    + response.body().ephemeralKey);

                            customerConfig = new PaymentSheet.CustomerConfiguration(
                                    response.body().customer,
                                    response.body().ephemeralKey
                            );
                            paymentIntentClientSecret = response.body().paymentIntent;
                            PaymentConfiguration.init(getApplicationContext(), publisherKey);
                            presentPaymentSheet();
                        }
                    });

                } else {
                    this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            prgDialog.dismiss();
                            dialogMsg.cancel();
                            dialogMsg.showErrorDialog(apiResponse.errorMessage, "Ok");
                            dialogMsg.show();
                        }
                    });
                }

            } catch (IOException e) {
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
            handler.post(() -> {
                //UI Thread work here

            });
        });
    }

    private void presentPaymentSheet() {
        final PaymentSheet.Configuration configuration = new PaymentSheet.Configuration.Builder(getString(R.string.app_name))
                .customer(customerConfig)
                // Set `allowsDelayedPaymentMethods` to true if your business can handle payment methods
                // that complete payment after a delay, like SEPA Debit and Sofort.
                .allowsDelayedPaymentMethods(true)
                .build();
        paymentSheet.presentWithPaymentIntent(
                paymentIntentClientSecret,
                configuration
        );
    }

    void onPaymentSheetResult(final PaymentSheetResult paymentSheetResult) {
        // implemented in the next steps
        if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            prgDialog.cancel();
            Util.showLog("Canceled");
            dialogMsg.cancel();
            dialogMsg.showErrorDialog("Payment Canceled By User", "Ok");
            dialogMsg.show();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            prgDialog.cancel();
            Util.showLog("Got error: " + ((PaymentSheetResult.Failed) paymentSheetResult).getError());
            dialogMsg.cancel();
            dialogMsg.showErrorDialog("" + ((PaymentSheetResult.Failed) paymentSheetResult).getError(), "Ok");
            dialogMsg.show();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            // Display for example, an order confirmation screen
            Util.showLog("Completed");
            postPayment(stripeOrderID, "Stripe");
        }
    }

}