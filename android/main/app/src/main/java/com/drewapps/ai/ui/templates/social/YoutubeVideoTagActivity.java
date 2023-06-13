package com.drewapps.ai.ui.templates.social;

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
import com.drewapps.ai.databinding.ActivityYoutubeVideoTagBinding;
import com.drewapps.ai.items.Document;
import com.drewapps.ai.items.Template;
import com.drewapps.ai.ui.activity.EditorActivity;
import com.drewapps.ai.ui.dialogs.DialogMsg;
import com.drewapps.ai.utils.Constants;
import com.drewapps.ai.utils.Util;
import com.drewapps.ai.viewmodel.UserDataViewModel;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;

public class YoutubeVideoTagActivity extends AppCompatActivity {

    ActivityYoutubeVideoTagBinding binding;

    Template template;
    UserDataViewModel userDataViewModel;

    DialogMsg dialogMsg;

    AppDatabase db;
    DocumentDao documentDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityYoutubeVideoTagBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialogMsg = new DialogMsg(this, false);
        db = AppDatabase.getInstance(this);
        documentDao = db.getDocumentDao();

        template = (Template) getIntent().getSerializableExtra(Constants.TEMPLATE);
        userDataViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);
        userDataViewModel.getAvailableWords().observe(this, value->{
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

        binding.toolbar.toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(binding.toolbar.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create Template");


        binding.btnGenerate.setOnClickListener(v -> {
            if (validate()) {
                binding.btnGenerate.setText("Generating...");
                binding.btnGenerate.setClickable(false);
                startGenerating();
            }
        });

        binding.btnClear.setOnClickListener(v -> {
            binding.etDocumentName.setText("");
            binding.etInstruction.setText("");
            binding.etNoHashtag.setText("");
        });
        binding.etDocumentName.setOnClickListener(v->{
            binding.tilDocumentName.setErrorEnabled(false);
        });
        binding.etNoHashtag.setOnClickListener(v->{
            binding.tilNoHashtag.setErrorEnabled(false);
        });
        binding.etInstruction.setOnClickListener(v->{
            binding.tilInstruction.setErrorEnabled(false);
        });
    }

    private void startGenerating() {

        dialogMsg.showLoadingDialog();
        String documentName = binding.etDocumentName.getText().toString();
        String instruction = binding.etInstruction.getText().toString();
        String output = binding.etNoHashtag.getText().toString();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            try {

                // Call the API Service
                Response<Document> response = ApiClient.getApiService().instagram_hashtags(Config.API_KEY,
                        documentName, template.templateId, MyApplication.prefManager().getInt(Constants.USER_ID),
                        instruction, output).execute();


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
                        Intent intent = new Intent(YoutubeVideoTagActivity.this, EditorActivity.class);
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

                        dialogMsg.cancel();
                        Util.showErrorLog("EEE: " + e.getMessage(), e);
                        dialogMsg.showErrorDialog(e.getMessage(), "Ok");
                        dialogMsg.show();

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
            binding.tilInstruction.setError("Video Title is required");
            binding.tilInstruction.setErrorEnabled(true);
            binding.etInstruction.requestFocus();
            return false;
        } else if (binding.etNoHashtag.getText().toString().isEmpty()) {
            binding.tilNoHashtag.setError("No of Tag is required");
            binding.tilNoHashtag.setErrorEnabled(true);
            binding.tilNoHashtag.requestFocus();
            return false;
        } else if(Integer.valueOf(binding.etNoHashtag.getText().toString()) > 30){
            binding.tilNoHashtag.setError("Max 30 Tag");
            binding.tilNoHashtag.setErrorEnabled(true);
            binding.tilNoHashtag.requestFocus();
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