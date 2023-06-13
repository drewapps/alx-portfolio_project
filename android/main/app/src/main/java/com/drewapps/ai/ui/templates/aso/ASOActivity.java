package com.drewapps.ai.ui.templates.aso;

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
import com.drewapps.ai.databinding.ActivityAsoactivityBinding;
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

public class ASOActivity extends AppCompatActivity {

    ActivityAsoactivityBinding binding;
    Template template;
    UserDataViewModel userDataViewModel;

    DialogMsg dialogMsg;

    AppDatabase db;
    DocumentDao documentDao;
    Integer availableWords = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAsoactivityBinding.inflate(getLayoutInflater());
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

        if(template.templateName.equals("PlayStore App Description")){
            binding.tilOutput.setVisibility(View.GONE);
            binding.swEmoji.setVisibility(View.VISIBLE);
        }

        if(template.templateName.equals("AppStore Description")){
            binding.tilOutput.setVisibility(View.GONE);
        }

        if(template.templateName.equals("App Reviews")){
            binding.tilTitle.setVisibility(View.VISIBLE);
            binding.tilAbout.setVisibility(View.GONE);
            binding.tilOutput.setHint("No of Reviews");
        }

        binding.toolbar.toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(binding.toolbar.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create Template");

        binding.etOutput.setText("1");
        binding.etOutput.setOnClickListener(v -> {
            Tools.ShowPopUpMenu(ASOActivity.this, v, binding.etOutput,
                    binding.tilOutput, R.menu.outputs);
        });
        binding.tilOutput.setEndIconOnClickListener(v -> {
            Tools.ShowPopUpMenu(ASOActivity.this, v, binding.etOutput,
                    binding.tilOutput, R.menu.outputs);
        });

        binding.etLanguage.setText(Constants.LANGUAGE);
        binding.etLanguage.setOnClickListener(v -> {
            Tools.ShowPopUpMenu(ASOActivity.this, v, binding.etLanguage,
                    binding.tiLanguage, R.menu.languages);
        });

        binding.tiLanguage.setEndIconOnClickListener(v -> {
            Tools.ShowPopUpMenu(ASOActivity.this, v, binding.etLanguage,
                    binding.tiLanguage, R.menu.languages);
        });

        binding.btnGenerate.setOnClickListener(v -> {
            binding.tilDocumentName.setErrorEnabled(false);
            binding.tilTitle.setErrorEnabled(false);
            binding.tilAbout.setErrorEnabled(false);
            binding.tilKeyword.setErrorEnabled(false);
            if (validate()) {
                binding.btnGenerate.setText("Generating...");
                binding.btnGenerate.setClickable(false);
                startGenerating();
            }
        });

        binding.btnClear.setOnClickListener(v -> {
            binding.etDocumentName.setText("");
            binding.etTitle.setText("");
            binding.etAbout.setText("");
            binding.etKeyword.setText("");
        });

        binding.etDocumentName.setOnClickListener(v->{
            binding.tilDocumentName.setErrorEnabled(false);
        });
        binding.etTitle.setOnClickListener(v->{
            binding.tilTitle.setErrorEnabled(false);
        });
        binding.etAbout.setOnClickListener(v->{
            binding.tilAbout.setErrorEnabled(false);
        });
        binding.etKeyword.setOnClickListener(v->{
            binding.tilKeyword.setErrorEnabled(false);
        });
    }

    private void startGenerating() {

        dialogMsg.showLoadingDialog();
        String documentName = binding.etDocumentName.getText().toString();
        String title = binding.etTitle.getText().toString();
        String about = binding.etAbout.getText().toString();
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
                if(template.templateName.equals("PlayStore App Description")){
                    response = ApiClient.getApiService().play_store_description(Config.API_KEY,
                            documentName, template.templateId, MyApplication.prefManager().getInt(Constants.USER_ID),
                            about, keyword, emoji, language).execute();
                }else if(template.templateName.equals("AppStore Description")){
                    response = ApiClient.getApiService().app_store_description(Config.API_KEY,
                            documentName, template.templateId, MyApplication.prefManager().getInt(Constants.USER_ID),
                            about, keyword, language).execute();
                }else if(template.templateName.equals("App Reviews")){
                    response = ApiClient.getApiService().app_review(Config.API_KEY,
                            documentName, template.templateId, MyApplication.prefManager().getInt(Constants.USER_ID),
                            about, keyword, output, language).execute();
                }else {
                    response = ApiClient.getApiService().play_store_title(Config.API_KEY,
                            documentName, template.templateId, MyApplication.prefManager().getInt(Constants.USER_ID),
                            about, keyword, output, language).execute();
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
                        Intent intent = new Intent(ASOActivity.this, EditorActivity.class);
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
        } else if (binding.etTitle.getText().toString().isEmpty() && binding.tilTitle.getVisibility() == View.VISIBLE) {
            binding.tilTitle.setError("Title is required");
            binding.tilTitle.setErrorEnabled(true);
            binding.tilTitle.requestFocus();
            return false;
        } else if (binding.etAbout.getText().toString().isEmpty() && binding.tilAbout.getVisibility() == View.VISIBLE) {
            binding.tilAbout.setError("About is required");
            binding.tilAbout.setErrorEnabled(true);
            binding.etAbout.requestFocus();
            return false;
        }else if (binding.etKeyword.getText().toString().isEmpty()) {
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