package com.drewapps.ai.ui.templates.generalWriting;

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
import com.drewapps.ai.databinding.ActivityCompanyBioBinding;
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

public class CompanyBioActivity extends AppCompatActivity {

    ActivityCompanyBioBinding binding;

    Template template;
    UserDataViewModel userDataViewModel;
    DialogMsg dialogMsg;

    AppDatabase db;
    DocumentDao documentDao;
    boolean isCompanyMission = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCompanyBioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialogMsg = new DialogMsg(this, false);
        db = AppDatabase.getInstance(this);
        documentDao = db.getDocumentDao();

        template = (Template) getIntent().getSerializableExtra(Constants.TEMPLATE);
        userDataViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);
        userDataViewModel.getAvailableWords().observe(this, value -> {
            binding.tvAvailable.setText("Your balance: " + value + " Words");
        });
        initUI();
        BannerAdManager.showBannerAds(this, binding.adView);
    }

    private void initUI() {

        GlideBinding.bindImage(binding.ivTemplateIcon, template.templateImage);
        binding.tvTitle.setText(template.templateName);
        binding.tvDescription.setText(template.templateDescription);

        binding.tvAvailable.setText("Your balance: " + userDataViewModel.getAvailableWords() + " Words");

        if(template.templateName.equals("Company Mission") || template.templateName.equals("Company Vision")){
            isCompanyMission = true;
            binding.tiPlatform.setVisibility(View.GONE);
            binding.tilInstruction.setHint("Company About");
            binding.tilInstruction.setHelperText("Company About*");
        }

        binding.toolbar.toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(binding.toolbar.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create Template");

        binding.etOutput.setText("1");
        binding.etOutput.setOnClickListener(v -> {
            Tools.ShowPopUpMenu(CompanyBioActivity.this, v, binding.etOutput,
                    binding.tilOutput, R.menu.outputs);
        });
        binding.tilOutput.setEndIconOnClickListener(v -> {
            Tools.ShowPopUpMenu(CompanyBioActivity.this, v, binding.etOutput,
                    binding.tilOutput, R.menu.outputs);
        });

        binding.etPlatform.setText(Constants.DEFAULT_PLATFORM);
        binding.tiPlatform.setEndIconOnClickListener(v -> {
            Tools.ShowPopUpMenu(CompanyBioActivity.this, v, binding.etPlatform,
                    binding.tiPlatform, R.menu.platform);
        });
        binding.etPlatform.setOnClickListener(v -> {
            Tools.ShowPopUpMenu(CompanyBioActivity.this, v, binding.etPlatform,
                    binding.tiPlatform, R.menu.platform);
        });

        binding.etLanguage.setText(Constants.LANGUAGE);
        binding.tiLanguage.setEndIconOnClickListener(v -> {
            Tools.ShowPopUpMenu(CompanyBioActivity.this, v, binding.etLanguage,
                    binding.tiLanguage, R.menu.languages);
        });
        binding.etLanguage.setOnClickListener(v -> {
            Tools.ShowPopUpMenu(CompanyBioActivity.this, v, binding.etLanguage,
                    binding.tiLanguage, R.menu.languages);
        });

        binding.btnGenerate.setOnClickListener(v -> {
            binding.tilDocumentName.setErrorEnabled(false);
            binding.tilInstruction.setErrorEnabled(false);
            binding.tilCompanyName.setErrorEnabled(false);
            if (validate()) {
                binding.btnGenerate.setText("Generating...");
                binding.btnGenerate.setClickable(false);
                startGenerating();
            }
        });
        binding.btnClear.setOnClickListener(v -> {
            binding.etDocumentName.setText("");
            binding.etInstruction.setText("");
            binding.etCompanyName.setText("");
        });

        binding.etDocumentName.setOnClickListener(v -> {
            binding.tilDocumentName.setErrorEnabled(false);
        });
        binding.etInstruction.setOnClickListener(v -> {
            binding.tilInstruction.setErrorEnabled(false);
        });
        binding.etCompanyName.setOnClickListener(v -> {
            binding.tilCompanyName.setErrorEnabled(false);
        });
    }

    private void startGenerating() {
        dialogMsg.showLoadingDialog();
        String documentName = binding.etDocumentName.getText().toString();
        String company = binding.etCompanyName.getText().toString();
        String instruction = binding.etInstruction.getText().toString();
        String output = binding.etOutput.getText().toString();
        String platform = binding.etPlatform.getText().toString();
        String language = binding.etLanguage.getText().toString();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            try {

                // Call the API Service
                Response<Document> response;

                if(isCompanyMission){
                    response = ApiClient.getApiService().company_mission(Config.API_KEY,
                            documentName, template.templateId, MyApplication.prefManager().getInt(Constants.USER_ID),
                            instruction, company, "", output, language).execute();
                }else {
                    response = ApiClient.getApiService().company_bio(Config.API_KEY,
                            documentName, template.templateId, MyApplication.prefManager().getInt(Constants.USER_ID),
                            instruction, company, platform, output, language).execute();
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
                        Intent intent = new Intent(CompanyBioActivity.this, EditorActivity.class);
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
        } else if (binding.etInstruction.getText().toString().isEmpty()) {
            binding.tilInstruction.setError(isCompanyMission ? "Company About is required" : "Information is required");
            binding.tilInstruction.setErrorEnabled(true);
            binding.etInstruction.requestFocus();
            return false;
        } else if (binding.etCompanyName.getText().toString().isEmpty()) {
            binding.tilCompanyName.setError("Company is required");
            binding.tilCompanyName.setErrorEnabled(true);
            binding.etCompanyName.requestFocus();
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