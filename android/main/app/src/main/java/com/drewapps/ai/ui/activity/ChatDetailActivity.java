package com.drewapps.ai.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.lifecycle.ViewModelProvider;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.MenuItem;

import com.drewapps.ai.Ads.BannerAdManager;
import com.drewapps.ai.BuildConfig;
import com.drewapps.ai.MyApplication;
import com.drewapps.ai.R;
import com.drewapps.ai.adapters.AdapterRoboChat;
import com.drewapps.ai.databinding.ActivityChatDetailBinding;
import com.drewapps.ai.databinding.ActivityChatHistoryBinding;
import com.drewapps.ai.items.ChatItem;
import com.drewapps.ai.utils.Constants;
import com.drewapps.ai.viewmodel.UserDataViewModel;

import java.util.ArrayList;
import java.util.List;

public class ChatDetailActivity extends AppCompatActivity {

    ActivityChatDetailBinding binding;
    AdapterRoboChat adapterRoboChat;
    UserDataViewModel userDataViewModel;
    String chatID;
    List<Drawable> drawableList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(getIntent().getExtras()!=null){
            chatID = getIntent().getExtras().getString("chatID");
        }

        setUpUI();
        setUpViewModel();

        BannerAdManager.showBannerAds(this, binding.adView);

    }

    private void setUpViewModel() {
        userDataViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);
        userDataViewModel.getLocalChatData(chatID).observe(this, data -> {
            if (data != null && data.size() > 0) {
                setDataToUi(data);
            }
        });
    }

    private void setDataToUi(List<ChatItem> data) {
        adapterRoboChat.setMessageList(data);
        binding.rvMassage.stopScroll();
        if (adapterRoboChat.getItemCount() > 0) {
            binding.rvMassage.smoothScrollToPosition(adapterRoboChat.getItemCount());
        }
    }

    private void setUpUI() {
        drawableList = new ArrayList<>();
        drawableList.add(getDrawable(R.drawable.bg_1));
        drawableList.add(getDrawable(R.drawable.bg_11));
        drawableList.add(getDrawable(R.drawable.bg_2));
        drawableList.add(getDrawable(R.drawable.bg_8));
        drawableList.add(getDrawable(R.drawable.bg_3));
        drawableList.add(getDrawable(R.drawable.bg_9));
        drawableList.add(getDrawable(R.drawable.bg_4));
        drawableList.add(getDrawable(R.drawable.bg_7));
        drawableList.add(getDrawable(R.drawable.bg_5));
        drawableList.add(getDrawable(R.drawable.bg_10));
        drawableList.add(getDrawable(R.drawable.bg_6));

        binding.lvMain.setBackground(drawableList.get(MyApplication.prefManager().getInt(Constants.BACKGROUND) > drawableList.size() ? 0
                : MyApplication.prefManager().getInt(Constants.BACKGROUND)));

        binding.toolbar.toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(binding.toolbar.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("AI Chat");

        adapterRoboChat = new AdapterRoboChat(this, items -> {
            PopupMenu popup = new PopupMenu(this, adapterRoboChat.getSelectedItemView(), Gravity.NO_GRAVITY, 0,
                    R.style.popupMenu);
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    if (item.getItemId() == R.id.menu_copy) {
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Copied", Html.fromHtml(items.text).toString());
                        clipboard.setPrimaryClip(clip);
                    } else if (item.getItemId() == R.id.menu_share) {
                        try {
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                            String shareMessage = Html.fromHtml(items.text).toString();
                            shareMessage = shareMessage + " \n\n *Download Now:* " + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                            startActivity(Intent.createChooser(shareIntent, "choose one"));
                        } catch (Exception e) {
                            //e.toString();
                        }
                    }

                    return true;
                }
            });
            popup.inflate(R.menu.msg_menu);
            popup.setForceShowIcon(true);
            popup.show();
        });
        binding.rvMassage.setAdapter(adapterRoboChat);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}