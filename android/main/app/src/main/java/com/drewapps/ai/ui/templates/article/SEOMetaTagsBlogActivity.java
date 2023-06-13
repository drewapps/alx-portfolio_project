package com.drewapps.ai.ui.templates.article;

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
import com.drewapps.ai.databinding.ActivitySeometaTagsBlogBinding;
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

public class SEOMetaTagsBlogActivity extends AppCompatActivity {

    ActivitySeometaTagsBlogBinding binding;

    Template template;
    UserDataViewModel userDataViewModel;

    DialogMsg dialogMsg;

    AppDatabase db;
    DocumentDao documentDao;
    Integer availableWords = 0;
    boolean isLanding = false;
    boolean isProduct = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySeometaTagsBlogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialogMsg = new DialogMsg(this, false);
        db = AppDatabase.getInstance(this);
        documentDao = db.getDocumentDao();

        template = (Template) getIntent().getSerializableExtra(Constants.TEMPLATE);
        userDataViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);
        userDataViewModel.getAvailableWords().observe(this, value -> {
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

        if (template.templateName.equals("Landing Page Headlines")) {
            isLanding = true;
            binding.tilSearchTerm.setVisibility(View.GONE);

            binding.tilTitle.setHint("Topic");
            binding.tilTitle.setHelperText("Topic*");

            binding.tilDescription.setHint("Description");
            binding.tilDescription.setHelperText("Description*");
        }

        if(template.templateName.equals("SEO Meta Tags (Product Page)")){
            isProduct = true;
            binding.tilTitle.setHint("Product Name");
            binding.tilTitle.setHelperText("Product Name*");

            binding.tilDescription.setHint("Product Description");
            binding.tilDescription.setHelperText("Product Description*");
        }

        binding.toolbar.toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(binding.toolbar.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create Template");

        binding.etOutput.setText("1");
        binding.etOutput.setOnClickListener(v -> {
            Tools.ShowPopUpMenu(SEOMetaTagsBlogActivity.this, v, binding.etOutput,
                    binding.tilOutput, R.menu.outputs);
        });
        binding.tilOutput.setEndIconOnClickListener(v -> {
            Tools.ShowPopUpMenu(SEOMetaTagsBlogActivity.this, v, binding.etOutput,
                    binding.tilOutput, R.menu.outputs);
        });

        binding.etLanguage.setText(Constants.LANGUAGE);
        binding.etLanguage.setOnClickListener(v -> {
            Tools.ShowPopUpMenu(SEOMetaTagsBlogActivity.this, v, binding.etLanguage,
                    binding.tiLanguage, R.menu.languages);
        });

        binding.tiLanguage.setEndIconOnClickListener(v -> {
            Tools.ShowPopUpMenu(SEOMetaTagsBlogActivity.this, v, binding.etLanguage,
                    binding.tiLanguage, R.menu.languages);
        });

        binding.btnGenerate.setOnClickListener(v -> {
            binding.tilDocumentName.setErrorEnabled(false);
            binding.tilDescription.setErrorEnabled(false);
            binding.tilTitle.setErrorEnabled(false);
            binding.tilSearchTerm.setErrorEnabled(false);
            if (validate()) {
                binding.btnGenerate.setText("Generating...");
                binding.btnGenerate.setClickable(false);
                startGenerating();
            }
        });

        binding.btnClear.setOnClickListener(v -> {
            binding.etDocumentName.setText("");
            binding.etSearchTerm.setText("");
            binding.etDescription.setText("");
            binding.etTitle.setText("");
        });

        binding.etDocumentName.setOnClickListener(v -> {
            binding.tilDocumentName.setErrorEnabled(false);
        });
        binding.etSearchTerm.setOnClickListener(v -> {
            binding.tilSearchTerm.setErrorEnabled(false);
        });
        binding.etTitle.setOnClickListener(v -> {
            binding.tilTitle.setErrorEnabled(false);
        });
        binding.etDescription.setOnClickListener(v -> {
            binding.tilDescription.setErrorEnabled(false);
        });
    }

    private void startGenerating() {

        dialogMsg.showLoadingDialog();
        String documentName = binding.etDocumentName.getText().toString();
        String title = binding.etTitle.getText().toString();
        String description = binding.etDescription.getText().toString();
        String search_term = binding.etSearchTerm.getText().toString();
        String output = binding.etOutput.getText().toString();
        String language = binding.etLanguage.getText().toString();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            try {

                // Call the API Service
                Response<Document> response;

                if (isLanding) {
                    response = ApiClient.getApiService().landing_page_headline(Config.API_KEY,
                            documentName, template.templateId, MyApplication.prefManager().getInt(Constants.USER_ID),
                            title, description, output,
                            language).execute();
                } else {
                    response = ApiClient.getApiService().seo_meta_tag_blog(Config.API_KEY,
                            documentName, template.templateId, MyApplication.prefManager().getInt(Constants.USER_ID),
                            title, description, search_term, output,
                            language).execute();
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
                        Intent intent = new Intent(SEOMetaTagsBlogActivity.this, EditorActivity.class);
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
        } else if (binding.etDescription.getText().toString().isEmpty()) {
            binding.tilDescription.setError(isLanding ? "Description is required" : isProduct ? "Product Description is required" : "Blog Description is required");
            binding.tilDescription.setErrorEnabled(true);
            binding.etDescription.requestFocus();
            return false;
        } else if (binding.etTitle.getText().toString().isEmpty()) {
            binding.tilTitle.setError(isLanding ? "Topic is required" : isProduct ? "Product Name is required" : "Blog Title is required");
            binding.tilTitle.setErrorEnabled(true);
            binding.etTitle.requestFocus();
            return false;
        } else if (binding.etSearchTerm.getText().toString().isEmpty() && !isLanding) {
            binding.tilSearchTerm.setError("Search Term is required");
            binding.tilSearchTerm.setErrorEnabled(true);
            binding.etSearchTerm.requestFocus();
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