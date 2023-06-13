package com.drewapps.ai.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;

import com.drewapps.ai.MyApplication;
import com.drewapps.ai.R;
import com.drewapps.ai.adapters.AdapterCategory;
import com.drewapps.ai.databinding.ActivityContactBinding;
import com.drewapps.ai.items.UserData;
import com.drewapps.ai.ui.dialogs.DialogMsg;
import com.drewapps.ai.utils.Connectivity;
import com.drewapps.ai.utils.Constants;
import com.drewapps.ai.utils.Util;
import com.drewapps.ai.viewmodel.UserDataViewModel;

import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends AppCompatActivity {

    ActivityContactBinding binding;
    UserDataViewModel userDataViewModel;
    UserData userData;
    Connectivity connectivity;
    AdapterCategory adapterCategory;
    String category = "";
    ProgressDialog prgDialog;
    DialogMsg dialogMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        connectivity = new Connectivity(this);
        dialogMsg = new DialogMsg(this, false);
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Loading...");
        prgDialog.setCancelable(false);

        userDataViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);

        userDataViewModel.getUserData().observe(this, resource -> {
            if (resource != null) {
                if (resource.data != null) {
                    MyApplication.prefManager().setBoolean(Constants.SUBSCRIBED, resource.data.isSubscribe !=null ? resource.data.isSubscribe : false);
                    Constants.IS_SUBSCRIBED = MyApplication.prefManager().getBoolean(Constants.SUBSCRIBED);
                    userData = resource.data;
                    binding.etName.setText(userData.userName);
                    binding.etEmail.setText(userData.emailId);
                    if (!Constants.MESSAGE.equals("")) {
                        binding.etMessage.setText(Constants.MESSAGE);
                        Constants.MESSAGE = "";
                    }
                }
            }
        });
        userDataViewModel.setUserObj(MyApplication.prefManager().getInt(Constants.USER_ID));

        setUpUI();
    }

    private void setUpUI() {
        binding.toolbar.toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(binding.toolbar.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Contact Us");

        binding.btnSend.setOnClickListener(view -> {
            if (!connectivity.isConnected()) {
                Util.showToast(this, "Please Connect Internet");
                return;
            }
            if (validate()) {
                startSend();
            }
        });

        adapterCategory = new AdapterCategory(this, category -> {
            this.category = category;
        });
        binding.rvSubject.setAdapter(adapterCategory);

        List<String> categories = new ArrayList<String>();
        categories.add("Purchase Error");
        categories.add("Technical Error");
        categories.add("App issue");
        categories.add("Feedback");

        adapterCategory.setCategories(categories);
        this.category = categories.get(0);
    }

    private void startSend() {
        prgDialog.show();

        String name = binding.etName.getText().toString();
        String email = binding.etEmail.getText().toString();
        String message = binding.etMessage.getText().toString();

        userDataViewModel.setContactUs(name, email, message, category).observe(this, result -> {
            if (result != null) {
                switch (result.status) {
                    case SUCCESS:

                        prgDialog.cancel();
                        //add offer text
                        dialogMsg.showSuccessDialog("Thanks for Contact Us\nWe will contact you shortly", "Ok");
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

    private boolean validate() {
        if (binding.etName.getText().toString().isEmpty()) {
            binding.etName.setError("Enter a Name");
            binding.etName.requestFocus();
            return false;
        } else if (binding.etEmail.getText().toString().isEmpty()) {
            binding.etEmail.setError("Enter a Email");
            binding.etEmail.requestFocus();
            return false;
        } else if (binding.etMessage.getText().toString().isEmpty()) {
            binding.etMessage.setError("Enter a Message");
            binding.etMessage.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (getIntent().getExtras() != null) {
                if (getIntent().hasExtra(Constants.DISABLE)) {
                    finish();
                }
            }
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}