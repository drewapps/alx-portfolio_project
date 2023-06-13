package com.drewapps.ai.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.drewapps.ai.ui.dialogs.DialogMsg;
import com.google.android.material.navigation.NavigationBarView;
import com.drewapps.ai.MyApplication;
import com.drewapps.ai.R;
import com.drewapps.ai.databinding.ActivityMainBinding;
import com.drewapps.ai.ui.fragment.DashboardFragment;
import com.drewapps.ai.ui.fragment.DocumentFragment;
import com.drewapps.ai.ui.fragment.MagicFragment;
import com.drewapps.ai.ui.fragment.TemplateFragment;
import com.drewapps.ai.utils.Constants;
import com.drewapps.ai.utils.Util;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(!MyApplication.prefManager().getBoolean(Constants.IS_LOGGED_IN)){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        initUI();
    }

    private void initUI() {

        binding.navigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menu_dashboard:
                        setupFragment(new DashboardFragment());
                        return true;
                    case R.id.menu_templates:
                        setupFragment(new TemplateFragment());
                        return true;
                    case R.id.menu_magic:
                        setupFragment(new MagicFragment());
                        return true;
                    case R.id.menu_document:
                        setupFragment(new DocumentFragment());
                        return true;
//                    case R.id.menu_setting:
//                        setupFragment(new SettingFragment());
//                        return true;
                    default:
                        return false;
                }
            }
        });

        binding.navigation.setSelectedItemId(R.id.menu_dashboard);
        binding.navigation.setBackground(null);
        binding.navigation.getMenu().getItem(2).setEnabled(false);
        binding.fab.setOnClickListener(view->{
            startActivity(new Intent(this, ChatActivity.class));
        });
    }

    public void setupFragment(Fragment fragment) {
        try {
            this.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_main, fragment)
                    .commitAllowingStateLoss();
        } catch (Exception e) {
            Util.showLog("Error! Can't replace fragment.");
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fl_main);

        if (fragment != null) {
            if (fragment instanceof DashboardFragment) {

                DialogMsg dialogMsg = new DialogMsg(this, false);
                dialogMsg.showWarningDialog("Exit", "Do You Want To Exit", "Yes", true);
                dialogMsg.show();
                dialogMsg.okBtn.setOnClickListener(v -> {
                    dialogMsg.cancel();
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    super.onBackPressed();
                    finish();
                    System.exit(0);
                });
            } else {
                binding.navigation.setSelectedItemId(R.id.menu_dashboard);
            }
        }
    }
}