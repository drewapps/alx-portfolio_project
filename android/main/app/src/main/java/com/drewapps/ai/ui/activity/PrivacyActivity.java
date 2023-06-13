package com.drewapps.ai.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;

import com.drewapps.ai.R;
import com.drewapps.ai.databinding.ActivityPrivacyBinding;
import com.drewapps.ai.utils.Constants;
import com.drewapps.ai.utils.PrefManager;

public class PrivacyActivity extends AppCompatActivity {

    ActivityPrivacyBinding binding;
    String type, privacy;
    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrivacyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        prefManager = new PrefManager(this);

        binding.toolbar.toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(binding.toolbar.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().getExtras() != null) {

            type = getIntent().getExtras().getString("type");

            if (type.equals(Constants.PRIVACY_POLICY)) {
                privacy = prefManager.getString(Constants.PRIVACY_POLICY);
                getSupportActionBar().setTitle("Privacy Policy");
            } else if (type.equals(Constants.TERM_CONDITION)) {
                privacy = prefManager.getString(Constants.TERM_CONDITION);
                getSupportActionBar().setTitle("Term & Condition");
            } else {
                privacy = prefManager.getString(Constants.REFUND_POLICY);
                getSupportActionBar().setTitle("Refund Policy");
            }

            setData();

        }
    }

    private void setData() {
        binding.webview.getSettings().setJavaScriptEnabled(true);
        String encodedHtml = Base64.encodeToString(privacy.getBytes(),
                Base64.NO_PADDING);
        binding.webview.loadData(encodedHtml, "text/html", "base64");

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