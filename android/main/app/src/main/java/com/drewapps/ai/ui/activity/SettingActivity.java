package com.drewapps.ai.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.drewapps.ai.R;
import com.drewapps.ai.databinding.ActivitySettingBinding;
import com.drewapps.ai.ui.fragment.SettingFragment;
import com.drewapps.ai.utils.Util;

public class SettingActivity extends AppCompatActivity {

    ActivitySettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupFragment(new SettingFragment());
    }

    public void setupFragment(Fragment fragment) {
        try {
            this.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_main, fragment)
                    .addToBackStack(null)
                    .commit();
        } catch (Exception e) {
            Util.showLog("Error! Can't replace fragment.");
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}