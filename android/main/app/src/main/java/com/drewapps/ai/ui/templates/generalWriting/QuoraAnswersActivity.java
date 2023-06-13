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
import com.drewapps.ai.databinding.ActivityQuoraAnswersBinding;
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

public class QuoraAnswersActivity extends AppCompatActivity {

    ActivityQuoraAnswersBinding binding;

    Template template;
    UserDataViewModel userDataViewModel;

    DialogMsg dialogMsg;

    AppDatabase db;
    DocumentDao documentDao;
    Integer availableWords = 0;
    boolean isBullet = false;
    boolean isQuestion = false;
    boolean isListicle = false;

    boolean isStartUp = false;

    boolean isDefinition = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuoraAnswersBinding.inflate(getLayoutInflater());
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

        if (template.templateName.equals("Bullet Point Answers")) {
            isBullet = true;
            binding.tilOutput.setHint("No of Points");
        }

        if (template.templateName.equals("Answers")) {
            binding.tilOutput.setVisibility(View.GONE);
        }

        if (template.templateName.equals("Generate Question")) {
            isQuestion = true;
            binding.tilOutput.setHint("No of Questions");
            binding.tilQuestion.setHint("Topic");
            binding.tilQuestion.setHelperText("Topic*");
        }

        if (template.templateName.equals("Listicle Ideas")) {
            isListicle = true;
            binding.tilQuestion.setHint("Topic");
            binding.tilQuestion.setHelperText("Topic*");
        }

        if (template.templateName.equals("Startup Ideas")) {
            isStartUp = true;
            binding.tilQuestion.setHint("Instruction");
            binding.tilQuestion.setHelperText("Instruction*");
            binding.tilOutput.setHint("No of Ideas");
        }

        if (template.templateName.equals("Definition")) {
            isDefinition = true;
            binding.tilQuestion.setHint("Words");
            binding.tilQuestion.setHelperText("Words*");
            binding.tilOutput.setHint("No of Definition");
        }

        binding.toolbar.toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(binding.toolbar.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create Template");

        binding.etOutput.setText("1");
        binding.etOutput.setOnClickListener(v -> {
            Tools.ShowPopUpMenu(QuoraAnswersActivity.this, v, binding.etOutput,
                    binding.tilOutput, isBullet ? R.menu.bullets :
                            isQuestion ? R.menu.bullets :
                                    isStartUp ? R.menu.bullets : R.menu.outputs);
        });
        binding.tilOutput.setEndIconOnClickListener(v -> {
            Tools.ShowPopUpMenu(QuoraAnswersActivity.this, v, binding.etOutput,
                    binding.tilOutput, isBullet ? R.menu.bullets :
                            isQuestion ? R.menu.bullets :
                                    isStartUp ? R.menu.bullets : R.menu.outputs);
        });

        binding.etLanguage.setText(Constants.LANGUAGE);
        binding.etLanguage.setOnClickListener(v -> {
            Tools.ShowPopUpMenu(QuoraAnswersActivity.this, v, binding.etLanguage,
                    binding.tiLanguage, R.menu.languages);
        });

        binding.tiLanguage.setEndIconOnClickListener(v -> {
            Tools.ShowPopUpMenu(QuoraAnswersActivity.this, v, binding.etLanguage,
                    binding.tiLanguage, R.menu.languages);
        });

        binding.btnGenerate.setOnClickListener(v -> {
            binding.tilDocumentName.setErrorEnabled(false);
            binding.tilQuestion.setErrorEnabled(false);
            if (validate()) {
                binding.btnGenerate.setText("Generating...");
                binding.btnGenerate.setClickable(false);
                startGenerating();
            }
        });

        binding.btnClear.setOnClickListener(v -> {
            binding.etDocumentName.setText("");
            binding.etQuestion.setText("");
        });

        binding.etDocumentName.setOnClickListener(v -> {
            binding.tilDocumentName.setErrorEnabled(false);
        });
        binding.etQuestion.setOnClickListener(v -> {
            binding.tilQuestion.setErrorEnabled(false);
        });

    }

    private void startGenerating() {

        dialogMsg.showLoadingDialog();
        String documentName = binding.etDocumentName.getText().toString();
        String question = binding.etQuestion.getText().toString();
        String output = binding.etOutput.getText().toString();
        String language = binding.etLanguage.getText().toString();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            try {

                // Call the API Service
                Response<Document> response;

                if (isBullet) {
                    response = ApiClient.getApiService().bullet_point_answer(Config.API_KEY,
                            documentName, template.templateId, MyApplication.prefManager().getInt(Constants.USER_ID),
                            question, output, language).execute();
                } else if (template.templateName.equals("Answers")) {
                    response = ApiClient.getApiService().answers(Config.API_KEY,
                            documentName, template.templateId, MyApplication.prefManager().getInt(Constants.USER_ID),
                            question, language).execute();
                } else if (isQuestion) {
                    response = ApiClient.getApiService().question_gen(Config.API_KEY,
                            documentName, template.templateId, MyApplication.prefManager().getInt(Constants.USER_ID),
                            question, output, language).execute();
                } else if (isListicle) {
                    response = ApiClient.getApiService().listicle_idea(Config.API_KEY,
                            documentName, template.templateId, MyApplication.prefManager().getInt(Constants.USER_ID),
                            question, output, language).execute();
                }else if (isStartUp) {
                    response = ApiClient.getApiService().startup_idea(Config.API_KEY,
                            documentName, template.templateId, MyApplication.prefManager().getInt(Constants.USER_ID),
                            question, output, language).execute();
                }else if (isDefinition) {
                    response = ApiClient.getApiService().definition(Config.API_KEY,
                            documentName, template.templateId, MyApplication.prefManager().getInt(Constants.USER_ID),
                            question, output, language).execute();
                } else {
                    response = ApiClient.getApiService().quora_answers(Config.API_KEY,
                            documentName, template.templateId, MyApplication.prefManager().getInt(Constants.USER_ID),
                            question, output, language).execute();
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
                        Intent intent = new Intent(QuoraAnswersActivity.this, EditorActivity.class);
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
        } else if (binding.etQuestion.getText().toString().isEmpty()) {
            binding.tilQuestion.setError(isQuestion ? "Topic is required" :
                    isListicle ? "Topic is required" :
                            isStartUp ? "Instruction is required" :
                                    isDefinition ? "Word is required" :"Question is required");
            binding.tilQuestion.setErrorEnabled(true);
            binding.etQuestion.requestFocus();
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