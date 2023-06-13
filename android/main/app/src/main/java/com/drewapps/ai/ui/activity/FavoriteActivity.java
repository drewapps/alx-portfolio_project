package com.drewapps.ai.ui.activity;

import static android.view.View.GONE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.drewapps.ai.MyApplication;
import com.drewapps.ai.R;
import com.drewapps.ai.adapters.AdapterAllTemplate;
import com.drewapps.ai.databinding.ActivityFavoriteBinding;
import com.drewapps.ai.items.Template;
import com.drewapps.ai.items.UserData;
import com.drewapps.ai.utils.Connectivity;
import com.drewapps.ai.utils.Constants;
import com.drewapps.ai.utils.Tools;
import com.drewapps.ai.utils.Util;
import com.drewapps.ai.viewmodel.TemplateViewModel;
import com.drewapps.ai.viewmodel.UserDataViewModel;

public class FavoriteActivity extends AppCompatActivity {
    ActivityFavoriteBinding binding;
    AdapterAllTemplate adapterAllTemplate;
    TemplateViewModel templateViewModel;
    Connectivity connectivity;
    ProgressDialog prgDialog;
    UserDataViewModel userDataViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        connectivity = new Connectivity(this);
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Loading...");
        prgDialog.setCancelable(false);

        setUpUI();
        initViewModel();
    }

    private void initViewModel() {
        userDataViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);
        templateViewModel = new ViewModelProvider(this).get(TemplateViewModel.class);
        userDataViewModel.getUserData().observe(this, resource -> {
            if (resource != null) {

                Util.showLog("Got Data" + resource.message + " " + resource.data);

                switch (resource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        if (resource.data != null) {
                            setDataToUi(resource.data);
                            showVisibility(true);
                        } else {
                            showVisibility(false);
                        }
                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server
                        if (resource.data != null) {
                            setDataToUi(resource.data);
                            showVisibility(true);
                        } else {
                            showVisibility(false);
                        }

                        break;
                    case ERROR:
                        // Error State
                        break;
                    default:
                        // Default

                        break;
                }

            } else {

                // Init Object or Empty Data
                showVisibility(false);
                Util.showLog("Empty Data");

            }
        });
        userDataViewModel.setUserObj(MyApplication.prefManager().getInt(Constants.USER_ID));
    }

    private void showVisibility(boolean isVisible) {
        if (isVisible) {
            binding.rvFavorite.setVisibility(View.VISIBLE);
            binding.animationView.setVisibility(GONE);
        } else {
            binding.rvFavorite.setVisibility(GONE);
            binding.animationView.setVisibility(View.VISIBLE);
        }
    }

    private void setDataToUi(UserData data) {
        MyApplication.prefManager().setBoolean(Constants.SUBSCRIBED, data.isSubscribe !=null ? data.isSubscribe : false);
        Constants.IS_SUBSCRIBED = MyApplication.prefManager().getBoolean(Constants.SUBSCRIBED);
        adapterAllTemplate.setDetailTemplate(data.favoriteTemplate);
    }

    private void setUpUI() {
        binding.toolbar.toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(binding.toolbar.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Favorite Templates");

        adapterAllTemplate = new AdapterAllTemplate(this, false, categorys -> {

        }, template -> {
            favoriteTemplate(template);
        }, template->{
            Tools.gotoTemplate(this, template);
        });
        binding.rvFavorite.setAdapter(adapterAllTemplate);
    }

    private void favoriteTemplate(Template template) {
        prgDialog.show();
        templateViewModel.favoriteTemplate(template.templateId, template.isFavorite ? false : true).observe(this,
                result -> {

                    if (result != null) {
                        switch (result.status) {
                            case SUCCESS:

                                prgDialog.cancel();
                                userDataViewModel.setUserObj(MyApplication.prefManager().getInt(Constants.USER_ID));
                                //add offer text
                                break;

                            case ERROR:
                                prgDialog.cancel();
                                Util.showToast(this, "Fail to Favorite");
                                break;
                        }
                    }
                });
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