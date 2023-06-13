package com.drewapps.ai.ui.templates.generalWriting;

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
import com.drewapps.ai.databinding.ActivityFreelanceProjectProposalBinding;
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

public class FreelanceProjectProposalActivity extends AppCompatActivity {

    ActivityFreelanceProjectProposalBinding binding;
    Template template;
    UserDataViewModel userDataViewModel;
    DialogMsg dialogMsg;

    AppDatabase db;
    DocumentDao documentDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFreelanceProjectProposalBinding.inflate(getLayoutInflater());
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

        binding.toolbar.toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(binding.toolbar.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create Template");


        binding.etLanguage.setText(Constants.LANGUAGE);
        binding.tiLanguage.setEndIconOnClickListener(v -> {
            Tools.ShowPopUpMenu(FreelanceProjectProposalActivity.this, v, binding.etLanguage,
                    binding.tiLanguage, R.menu.languages);
        });
        binding.etLanguage.setOnClickListener(v -> {
            Tools.ShowPopUpMenu(FreelanceProjectProposalActivity.this, v, binding.etLanguage,
                    binding.tiLanguage, R.menu.languages);
        });

        binding.btnGenerate.setOnClickListener(v -> {
            binding.tilDocumentName.setErrorEnabled(false);
            binding.tilClientName.setErrorEnabled(false);
            binding.tilProject.setErrorEnabled(false);
            binding.tilClientAbout.setErrorEnabled(false);
            binding.tilFrellancer.setErrorEnabled(false);
            binding.tilGoalProject.setErrorEnabled(false);
            binding.tilExp.setErrorEnabled(false);
            if (validate()) {
                binding.btnGenerate.setText("Generating...");
                binding.btnGenerate.setClickable(false);
                startGenerating();
            }
        });
        binding.btnClear.setOnClickListener(v -> {
            binding.etDocumentName.setText("");
            binding.etClientName.setText("");
            binding.etClientAbout.setText("");
            binding.etProject.setText("");
            binding.etGoalProject.setText("");
            binding.etFreelancer.setText("");
            binding.etExp.setText("");
        });

        binding.etDocumentName.setOnClickListener(v -> {
            binding.tilDocumentName.setErrorEnabled(false);
        });
        binding.etClientName.setOnClickListener(v -> {
            binding.tilClientName.setErrorEnabled(false);
        });
        binding.etClientAbout.setOnClickListener(v -> {
            binding.tilClientAbout.setErrorEnabled(false);
        });
        binding.etProject.setOnClickListener(v -> {
            binding.tilProject.setErrorEnabled(false);
        });
        binding.etGoalProject.setOnClickListener(v -> {
            binding.tilGoalProject.setErrorEnabled(false);
        });
        binding.etFreelancer.setOnClickListener(v -> {
            binding.tilFrellancer.setErrorEnabled(false);
        });
        binding.etExp.setOnClickListener(v -> {
            binding.tilExp.setErrorEnabled(false);
        });
    }

    private void startGenerating() {
        dialogMsg.showLoadingDialog();
        String documentName = binding.etDocumentName.getText().toString();
        String clientName = binding.etClientName.getText().toString();
        String clientAbout = binding.etClientAbout.getText().toString();
        String project = binding.etProject.getText().toString();
        String goalProject = binding.etGoalProject.getText().toString();
        String freelancer = binding.etFreelancer.getText().toString();
        String exp = binding.etExp.getText().toString();
        String language = binding.etLanguage.getText().toString();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            try {

                // Call the API Service
                Response<Document> response = ApiClient.getApiService().freelancer_project(Config.API_KEY,
                            documentName, template.templateId, MyApplication.prefManager().getInt(Constants.USER_ID),
                            clientName, clientAbout, project, goalProject, freelancer, exp, language).execute();


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
                        Intent intent = new Intent(FreelanceProjectProposalActivity.this, EditorActivity.class);
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
        } else if (binding.etClientName.getText().toString().isEmpty()) {
            binding.tilClientName.setError("Client Name is required");
            binding.tilClientName.setErrorEnabled(true);
            binding.etClientName.requestFocus();
            return false;
        } else if (binding.etClientAbout.getText().toString().isEmpty()) {
            binding.tilClientAbout.setError("Client About is required");
            binding.tilClientAbout.setErrorEnabled(true);
            binding.etClientAbout.requestFocus();
            return false;
        } else if (binding.etProject.getText().toString().isEmpty()) {
            binding.tilProject.setError("Project is required");
            binding.tilProject.setErrorEnabled(true);
            binding.etProject.requestFocus();
            return false;
        } else if (binding.etGoalProject.getText().toString().isEmpty()) {
            binding.tilGoalProject.setError("Goal of Project is required");
            binding.tilGoalProject.setErrorEnabled(true);
            binding.etGoalProject.requestFocus();
            return false;
        } else if (binding.etFreelancer.getText().toString().isEmpty()) {
            binding.tilFrellancer.setError("Freelancer About is required");
            binding.tilFrellancer.setErrorEnabled(true);
            binding.etFreelancer.requestFocus();
            return false;
        } else if (binding.etExp.getText().toString().isEmpty()) {
            binding.tilExp.setError("Freelancer Experience is required");
            binding.tilExp.setErrorEnabled(true);
            binding.etExp.requestFocus();
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