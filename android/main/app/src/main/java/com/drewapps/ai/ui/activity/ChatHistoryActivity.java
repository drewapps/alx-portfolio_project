package com.drewapps.ai.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.drewapps.ai.Ads.BannerAdManager;
import com.drewapps.ai.R;
import com.drewapps.ai.adapters.ChatHistoryAdapter;
import com.drewapps.ai.databinding.ActivityChatHistoryBinding;
import com.drewapps.ai.items.ItemChatHistory;
import com.drewapps.ai.ui.dialogs.DialogMsg;
import com.drewapps.ai.utils.Util;
import com.drewapps.ai.viewmodel.UserDataViewModel;

import java.util.Collections;
import java.util.List;

public class ChatHistoryActivity extends AppCompatActivity {

    ActivityChatHistoryBinding binding;
    UserDataViewModel userDataViewModel;
    DialogMsg dialogMsg;

    ChatHistoryAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUpUI();
        intiViewModel();

        BannerAdManager.showBannerAds(this, binding.adView);
    }

    private void setUpUI() {
        binding.toolbar.toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(binding.toolbar.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Chat History");

        adapter = new ChatHistoryAdapter(item -> {
            Intent intent = new Intent(this, ChatDetailActivity.class);
            intent.putExtra("chatID", item.chatId);
            startActivity(intent);
        });
        binding.rvChat.setAdapter(adapter);
    }

    private void intiViewModel() {
        userDataViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);

        userDataViewModel.getChatHistory().observe(this, resource -> {
            if (resource != null) {

                Util.showLog("Got Data" + resource.message + resource.toString());

                switch (resource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB
                        if (resource.data != null) {
                            Util.showLog(" " + resource.data.size());
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
                            Util.showLog(" " + resource.data.size());
                            setDataToUi(resource.data);
                            showVisibility(true);
                        } else {
                            showVisibility(false);
                        }

                        break;
                    case ERROR:
                        // Error State
                        dialogMsg.cancel();
                        dialogMsg.showErrorDialog(resource.message, "Ok");
                        dialogMsg.show();
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

    private void setDataToUi(List<ItemChatHistory> data) {
        Collections.reverse(data);
        adapter.setChatHistoryList(data);
    }

    private void showVisibility(boolean b) {
        if (b) {
            binding.rvChat.setVisibility(View.VISIBLE);
            binding.animationView.setVisibility(View.GONE);
        } else {
            binding.animationView.setVisibility(View.VISIBLE);
            binding.rvChat.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}