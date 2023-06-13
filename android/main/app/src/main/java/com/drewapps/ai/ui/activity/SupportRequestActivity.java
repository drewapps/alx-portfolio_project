package com.drewapps.ai.ui.activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.drewapps.ai.MyApplication;
import com.drewapps.ai.R;
import com.drewapps.ai.adapters.AdapterSupport;
import com.drewapps.ai.databinding.ActivitySupportRequestBinding;
import com.drewapps.ai.items.ItemSupportReq;
import com.drewapps.ai.ui.fragment.CreateSupportFragment;
import com.drewapps.ai.utils.Constants;
import com.drewapps.ai.utils.Util;
import com.drewapps.ai.viewmodel.SupportViewModel;

import java.util.List;

public class SupportRequestActivity extends AppCompatActivity {

    ActivitySupportRequestBinding binding;

    AdapterSupport adapterSupport;

    SupportViewModel supportViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySupportRequestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUpUi();
        initViewModel();
    }

    private void initViewModel() {
        supportViewModel = new ViewModelProvider(this).get(SupportViewModel.class);
        supportViewModel.getAll(MyApplication.prefManager().getInt(Constants.USER_ID)).observe(this, resource -> {

            if (resource != null) {

                Util.showLog("Got Data" + resource.message + resource.toString());

                switch (resource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        if (resource.data != null) {

                            setData(resource.data);

                        } else {
                            showVisibility(false);
                        }
                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server
                        if (resource.data != null) {
                            setData(resource.data);
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

    private void setData(List<ItemSupportReq> data) {
        if(data.size()>0){
            showVisibility(true);
        }else {
            showVisibility(false);
        }

        adapterSupport.setSupportReq(data);

        boolean isOpen = false;
        for (ItemSupportReq req : data) {
            if (req.status.equals("Open") || req.status.equals("Replied") || req.status.equals("Pending")) {
                isOpen = true;
                break;
            }
        }
        if (isOpen) {
            binding.btnNewArt.setVisibility(GONE);
        } else {
            binding.btnNewArt.setVisibility(VISIBLE);
        }
    }

    private void showVisibility(boolean isVisible) {
        if (isVisible) {
            binding.rvSupports.setVisibility(VISIBLE);
            binding.animationView.setVisibility(View.GONE);
        } else {
            binding.rvSupports.setVisibility(GONE);
            binding.animationView.setVisibility(VISIBLE);
        }
    }

    private void setUpUi() {
        binding.toolbar.toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(binding.toolbar.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Support Request");

        adapterSupport = new AdapterSupport(this, item -> {
            Intent intent = new Intent(this, SupportDetailActivity.class);
            intent.putExtra("SUPPORT", item);
            startActivity(intent);
        });

        binding.rvSupports.setAdapter(adapterSupport);

        binding.btnNewArt.setOnClickListener(v -> {
            showDialogFullscreen();
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDialogFullscreen() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        CreateSupportFragment newFragment = new CreateSupportFragment();
        newFragment.setRequestCode(300);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
        newFragment.setOnCallbackResult(new CreateSupportFragment.CallbackResult() {
            @Override
            public void sendResult(int requestCode, Object obj) {
                if (requestCode == 300) {
                    displayDataResult((ItemSupportReq) obj);
                }
            }
        });
    }

    private void displayDataResult(ItemSupportReq obj) {
        adapterSupport.notifyDataSetChanged();
        Util.showLog(obj.subject);
    }
}