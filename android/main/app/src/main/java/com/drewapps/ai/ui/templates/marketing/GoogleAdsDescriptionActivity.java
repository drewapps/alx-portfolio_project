package com.drewapps.ai.ui.templates.marketing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;

import com.drewapps.ai.Ads.BannerAdManager;
import com.drewapps.ai.Config;
import com.drewapps.ai.MyApplication;
import com.drewapps.ai.R;
import com.drewapps.ai.api.ApiClient;
import com.drewapps.ai.api.ApiResponse;
import com.drewapps.ai.binding.GlideBinding;
import com.drewapps.ai.database.AppDatabase;
import com.drewapps.ai.database.DocumentDao;
import com.drewapps.ai.databinding.ActivityGoogleAdsDescriptionBinding;
import com.drewapps.ai.items.Document;
import com.drewapps.ai.items.Template;
import com.drewapps.ai.ui.activity.EditorActivity;
import com.drewapps.ai.ui.dialogs.DialogMsg;
import com.drewapps.ai.utils.Constants;
import com.drewapps.ai.utils.Tools;
import com.drewapps.ai.utils.Util;
import com.drewapps.ai.viewmodel.UserDataViewModel;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;

public class GoogleAdsDescriptionActivity extends AppCompatActivity {

    ActivityGoogleAdsDescriptionBinding binding;

    Template template;
    UserDataViewModel userDataViewModel;

    DialogMsg dialogMsg;

    AppDatabase db;
    DocumentDao documentDao;
    Integer availableWords = 0;

    boolean isCall = false;
    boolean isPin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGoogleAdsDescriptionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dialogMsg = new DialogMsg(this, false);
        db = AppDatabase.getInstance(this);
        documentDao = db.getDocumentDao();

        template = (Template) getIntent().getSerializableExtra(Constants.TEMPLATE);
        userDataViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);
        userDataViewModel.getAvailableWords().observe(this, value->{
            availableWords = value;
            binding.tvAvailable.setText("Your balance: " + availableWords + " Words");
        });
        initUI();
        BannerAdManager.showBannerAds(this, binding.adView);
    }

    private void initUI() {

        GlideBinding.bindImage(binding.ivTemplateIcon, template.templateImage);
        binding.tvTitle.setText(template.templateName);
        binding.tvDescription.setText(template.templateDescription);

        if(template.templateName.equals("Call To Action")){
            isCall = true;
            binding.tilKeyword.setVisibility(View.GONE);
            binding.swEmoji.setVisibility(View.GONE);

            binding.tilProductName.setHint("Product/Brand Name");
            binding.tilProductName.setHelperText("Product/Brand Name*");

            binding.tilInstruction.setHint("Product/Brand About");
            binding.tilInstruction.setHelperText("Product/Brand About*");
        }
        if(template.templateName.equals("Pinterest Ad Campaign")){
            isPin = true;
            binding.tilKeyword.setVisibility(View.GONE);
            binding.swEmoji.setVisibility(View.GONE);

            binding.tilProductName.setHint("Product Service");
            binding.tilProductName.setHelperText("Product Service*");

            binding.tilInstruction.setVisibility(View.GONE);
            binding.tilOutput.setVisibility(View.GONE);
        }

        binding.toolbar.toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(binding.toolbar.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create Template");

        binding.etOutput.setText("1");
        binding.etOutput.setOnClickListener(v -> {
            Tools.ShowPopUpMenu(GoogleAdsDescriptionActivity.this, v, binding.etOutput,
                    binding.tilOutput, R.menu.outputs);
        });
        binding.tilOutput.setEndIconOnClickListener(v -> {
            Tools.ShowPopUpMenu(GoogleAdsDescriptionActivity.this, v, binding.etOutput,
                    binding.tilOutput, R.menu.outputs);
        });

        binding.etLanguage.setText(Constants.LANGUAGE);
        binding.etLanguage.setOnClickListener(v -> {
            Tools.ShowPopUpMenu(GoogleAdsDescriptionActivity.this, v, binding.etLanguage,
                    binding.tiLanguage, R.menu.languages);
        });

        binding.tiLanguage.setEndIconOnClickListener(v -> {
            Tools.ShowPopUpMenu(GoogleAdsDescriptionActivity.this, v, binding.etLanguage,
                    binding.tiLanguage, R.menu.languages);
        });

        binding.btnGenerate.setOnClickListener(v -> {
            binding.tilDocumentName.setErrorEnabled(false);
            binding.tilInstruction.setErrorEnabled(false);
            binding.tilProductName.setErrorEnabled(false);
            binding.tilKeyword.setErrorEnabled(false);
            if (validate()) {
                binding.btnGenerate.setClickable(false);
                startGenerating();
            }
        });

        binding.btnClear.setOnClickListener(v -> {
            binding.etDocumentName.setText("");
            binding.etInstruction.setText("");
            binding.etKeyword.setText("");
            binding.etProductName.setText("");
        });

        binding.etDocumentName.setOnClickListener(v->{
            binding.tilDocumentName.setErrorEnabled(false);
        });
        binding.etKeyword.setOnClickListener(v->{
            binding.tilKeyword.setErrorEnabled(false);
        });
        binding.etProductName.setOnClickListener(v->{
            binding.tilProductName.setErrorEnabled(false);
        });
        binding.etInstruction.setOnClickListener(v->{
            binding.tilInstruction.setErrorEnabled(false);
        });
    }

    private void startGenerating() {

        dialogMsg.showLoadingDialog();
        String documentName = binding.etDocumentName.getText().toString();
        String instruction = binding.etInstruction.getText().toString();
        String product_name = binding.etProductName.getText().toString();
        String keyword = binding.etKeyword.getText().toString();
        String output = binding.etOutput.getText().toString();
        String language = binding.etLanguage.getText().toString();
        boolean emoji = binding.swEmoji.isChecked();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            try {

                // Call the API Service
                Response<Document> response;

                if(isCall){
                    response = ApiClient.getApiService().call_action(Config.API_KEY,
                            documentName, template.templateId, MyApplication.prefManager().getInt(Constants.USER_ID),
                            product_name, instruction, output, language).execute();
                }else if(isPin){
                    response = ApiClient.getApiService().pin_ads(Config.API_KEY,
                            documentName, template.templateId, MyApplication.prefManager().getInt(Constants.USER_ID),
                            product_name,  language).execute();
                }else {
                    response = ApiClient.getApiService().google_ads_description(Config.API_KEY,
                            documentName, template.templateId, MyApplication.prefManager().getInt(Constants.USER_ID),
                            instruction, output, product_name, keyword,
                            emoji, language).execute();
                }

                // Wrap with APIResponse Class
                ApiResponse<Document> apiResponse = new ApiResponse<>(response);

                // If response is successful
                if (apiResponse.isSuccessful()) {

                    try {
                        db.runInTransaction(() -> {
                            if (apiResponse.body != null) {
                                documentDao.insert(apiResponse.body);
                            }
                        });
                    } catch (Exception ex) {
                        Util.showErrorLog("Error at ", ex);
                    }

                    runOnUiThread(() -> {
                        binding.btnGenerate.setText("Generate");
                        binding.btnGenerate.setClickable(true);

                        dialogMsg.cancel();
                        Intent intent = new Intent(GoogleAdsDescriptionActivity.this, EditorActivity.class);
                        intent.putExtra("document", (Serializable) apiResponse.body);
                        startActivity(intent);

                    });

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.btnGenerate.setText("Generate");
                            binding.btnGenerate.setClickable(true);
                            dialogMsg.cancel();
                            Util.showLog("EEE: " + apiResponse.errorMessage);
                            dialogMsg.showErrorDialog(apiResponse.errorMessage, "Ok");
                            dialogMsg.show();

                        }
                    });
                }

            } catch (IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Util.showLog("EEE: " + "Coupon Code Not Valid");

                    }
                });
            }
            handler.post(() -> {
                //UI Thread work here

            });
        });

    }

    private boolean validate() {
        if (binding.etDocumentName.getText().toString().isEmpty()) {
            binding.tilDocumentName.setError("Document name is required");
            binding.tilDocumentName.setErrorEnabled(true);
            binding.tilDocumentName.requestFocus();
            return false;
        } else if (binding.etInstruction.getText().toString().isEmpty()  && !isPin) {
            binding.tilInstruction.setError(isCall ? "Product/Brand About is required" :
                    "Instruction is required");
            binding.tilInstruction.setErrorEnabled(true);
            binding.etInstruction.requestFocus();
            return false;
        } else if (binding.etProductName.getText().toString().isEmpty()) {
            binding.tilProductName.setError(isCall ? "Product/Brand Name is required" : isPin ? "Product Service is Required" : "Product / Service Name is required");
            binding.tilProductName.setErrorEnabled(true);
            binding.etProductName.requestFocus();
            return false;
        }else if (binding.etKeyword.getText().toString().isEmpty() && !isCall) {
            binding.tilKeyword.setError("Keyword is required");
            binding.tilKeyword.setErrorEnabled(true);
            binding.etKeyword.requestFocus();
            return false;
        }

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}