package com.drewapps.ai.ui.templates.article;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;

import com.drewapps.ai.Ads.BannerAdManager;
import com.drewapps.ai.Config;
import com.drewapps.ai.MyApplication;
import com.drewapps.ai.R;
import com.drewapps.ai.api.ApiClient;
import com.drewapps.ai.api.ApiResponse;
import com.drewapps.ai.binding.GlideBinding;
import com.drewapps.ai.database.AppDatabase;
import com.drewapps.ai.database.DocumentDao;
import com.drewapps.ai.databinding.ActivityParagraphGeneratorBinding;
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

public class ParagraphGeneratorActivity extends AppCompatActivity {

    ActivityParagraphGeneratorBinding binding;

    Template template;
    UserDataViewModel userDataViewModel;

    DialogMsg dialogMsg;

    AppDatabase db;
    DocumentDao documentDao;
    Integer availableWords = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityParagraphGeneratorBinding.inflate(getLayoutInflater());
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

        binding.toolbar.toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(binding.toolbar.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create Template");


        binding.etLanguage.setText(Constants.LANGUAGE);
        binding.etLanguage.setOnClickListener(v -> {
            Tools.ShowPopUpMenu(ParagraphGeneratorActivity.this, v, binding.etLanguage,
                    binding.tiLanguage, R.menu.languages);
        });

        binding.tiLanguage.setEndIconOnClickListener(v -> {
            Tools.ShowPopUpMenu(ParagraphGeneratorActivity.this, v, binding.etLanguage,
                    binding.tiLanguage, R.menu.languages);
        });

        binding.etOutput.setText("1");
        binding.etOutput.setOnClickListener(v -> {
            Tools.ShowPopUpMenu(ParagraphGeneratorActivity.this, v, binding.etOutput,
                    binding.tilOutput, R.menu.bullets);
        });

        binding.tilOutput.setEndIconOnClickListener(v -> {
            Tools.ShowPopUpMenu(ParagraphGeneratorActivity.this, v, binding.etOutput,
                    binding.tilOutput, R.menu.bullets);
        });

        binding.btnGenerate.setOnClickListener(v -> {
            binding.tilDocumentName.setErrorEnabled(false);
            binding.tilKeywords.setErrorEnabled(false);
            binding.tilWords.setErrorEnabled(false);
            binding.tilTitle.setErrorEnabled(false);
            if (validate()) {
                binding.btnGenerate.setText("Generating...");
                binding.btnGenerate.setClickable(false);
                startGenerating();
            }
        });

        binding.btnClear.setOnClickListener(v -> {
            binding.etDocumentName.setText("");
            binding.etKeyword.setText("");
            binding.etTitle.setText("");
            binding.etWords.setText("100");
        });

        binding.etDocumentName.setOnClickListener(v->{
            binding.tilDocumentName.setErrorEnabled(false);
        });
        binding.etWords.setOnClickListener(v->{
            binding.tilWords.setErrorEnabled(false);
        });
        binding.etKeyword.setOnClickListener(v->{
            binding.tilKeywords.setErrorEnabled(false);
        });
        binding.etTitle.setOnClickListener(v->{
            binding.tilTitle.setErrorEnabled(false);
        });
    }

    private void startGenerating() {

        dialogMsg.showLoadingDialog();
        String documentName = binding.etDocumentName.getText().toString();
        String title = binding.etTitle.getText().toString();
        String keyword = binding.etKeyword.getText().toString();
        String words = binding.etWords.getText().toString();
        String language = binding.etLanguage.getText().toString();
        String frequency = binding.etOutput.getText().toString();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            try {

                // Call the API Service
                Response<Document> response = ApiClient.getApiService().paragraph_generator(Config.API_KEY,
                        documentName, template.templateId, MyApplication.prefManager().getInt(Constants.USER_ID),
                        title, keyword, frequency, words, language).execute();


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
                        Intent intent = new Intent(ParagraphGeneratorActivity.this, EditorActivity.class);
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
        } else if (binding.etTitle.getText().toString().isEmpty()) {
            binding.tilTitle.setError("Title is required");
            binding.tilTitle.setErrorEnabled(true);
            binding.etTitle.requestFocus();
            return false;
        }else if (binding.etKeyword.getText().toString().isEmpty()) {
            binding.tilKeywords.setError("Keyword is required");
            binding.tilKeywords.setErrorEnabled(true);
            binding.etKeyword.requestFocus();
            return false;
        } else if (Integer.parseInt(binding.etWords.getText().toString()) > 2500) {
            binding.tilWords.setError("Max - 2500 Words");
            binding.tilWords.setErrorEnabled(true);
            binding.tilWords.requestFocus();
            return false;
        } else if (Integer.parseInt(binding.etWords.getText().toString()) > availableWords) {
            Util.showToast(this, "You Have Insufficient Words");
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