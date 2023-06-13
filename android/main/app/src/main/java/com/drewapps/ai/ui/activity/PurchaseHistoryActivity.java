package com.drewapps.ai.ui.activity;

import static android.view.View.GONE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.drewapps.ai.MyApplication;
import com.drewapps.ai.R;
import com.drewapps.ai.adapters.PurchaseAdapter;
import com.drewapps.ai.databinding.ActivityPurchaseHistoryBinding;
import com.drewapps.ai.items.PurchaseHistory;
import com.drewapps.ai.utils.Constants;
import com.drewapps.ai.utils.Util;
import com.drewapps.ai.viewmodel.UserDataViewModel;

import java.util.List;

public class PurchaseHistoryActivity extends AppCompatActivity {

    ActivityPurchaseHistoryBinding binding;
    UserDataViewModel userDataViewModel;
    PurchaseAdapter purchaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPurchaseHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setUpUI();
        initViewModel();
    }

    private void initViewModel() {
        userDataViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);
        userDataViewModel.getPurchaseHistory(MyApplication.prefManager().getInt(Constants.USER_ID)).observe(this, resource->{
            if (resource != null) {

                Util.showLog("Got Data" + resource.message + resource.toString());

                switch (resource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        if (resource.data != null) {

                            if (resource.data.size() > 0) {
                                setData(resource.data);
                                showVisibility(true);
                            }
                        } else {
                            showVisibility(false);
                        }
                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server
                        if (resource.data != null && resource.data.size() > 0) {
                            setData(resource.data);
                            showVisibility(true);
                        } else {
                            showVisibility(false);
                        }

                        break;
                    case ERROR:
                        // Error State
                        showVisibility(false);
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
    }

    private void setData(List<PurchaseHistory> data) {
        purchaseAdapter.setPurchaseHistoryList(data);
    }

    private void showVisibility(boolean isVisible) {
        if (isVisible) {
            binding.rvPurchase.setVisibility(View.VISIBLE);
            binding.animationView.setVisibility(View.GONE);
        } else {
            binding.rvPurchase.setVisibility(GONE);
            binding.animationView.setVisibility(View.VISIBLE);
        }
    }


    private void setUpUI() {
        binding.toolbar.toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(binding.toolbar.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Purchase History");

        purchaseAdapter = new PurchaseAdapter(this, purchase->{

        });
        binding.rvPurchase.setAdapter(purchaseAdapter);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

}