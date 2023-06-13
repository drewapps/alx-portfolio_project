package com.drewapps.ai.ui.activity;

import static com.drewapps.ai.MyApplication.prefManager;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.drewapps.ai.Ads.BannerAdManager;
import com.drewapps.ai.Ads.InterstitialAdManager;
import com.drewapps.ai.BuildConfig;
import com.drewapps.ai.MyApplication;
import com.drewapps.ai.R;
import com.drewapps.ai.adapters.AdapterBackground;
import com.drewapps.ai.adapters.AdapterRoboChat;
import com.drewapps.ai.databinding.ActivityChatBinding;
import com.drewapps.ai.databinding.DialogPreviewBinding;
import com.drewapps.ai.databinding.ItemBackgroundDialogBinding;
import com.drewapps.ai.items.ChatItem;
import com.drewapps.ai.ui.dialogs.DialogMsg;
import com.drewapps.ai.utils.Connectivity;
import com.drewapps.ai.utils.Constants;
import com.drewapps.ai.utils.Util;
import com.drewapps.ai.utils.ViewAnimation;
import com.drewapps.ai.viewmodel.UserDataViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity implements InterstitialAdManager.Listener {

    ActivityChatBinding binding;
    AdapterRoboChat adapterRoboChat;
    UserDataViewModel userDataViewModel;
    Connectivity connectivity;
    DialogMsg dialogMsg;
    List<ChatItem> chatItemList;
    List<Drawable> drawableList;

    String query = "";
    boolean isDone = false;
    boolean isError = false;
    MenuItem wordMenuItem;

    String chatID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        connectivity = new Connectivity(this);
        dialogMsg = new DialogMsg(this, false);

        setUpUI();
        setUpViewModel();

        BannerAdManager.showBannerAds(this, binding.adView);
        InterstitialAdManager.Interstitial(this, this);

        chatID = "chat_" + System.currentTimeMillis();
    }

    private void setUpViewModel() {
        userDataViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);

        userDataViewModel.getChatData().observe(this, resource -> {
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
                            Util.showLog("Chat: " + resource.data.size());
                            setDataToUi(resource.data);
                            binding.btnSend.setEnabled(true);
                            userDataViewModel.setUserObj(prefManager().getInt(Constants.USER_ID));
                            showVisibility(true);
                        } else {
                            binding.btnSend.setEnabled(true);
                            showVisibility(false);
                        }

                        break;
                    case ERROR:
                        // Error State
                        if (isError) {
                            isError = false;
                            binding.btnSend.setEnabled(true);
                            showVisibility(true);
                            if (resource.data != null && resource.data.size() > 0) {
                                List<ChatItem> data = resource.data;
//                                Collections.reverse(data);
                                Util.showLog("0 " + data.get(data.size() - 1).text);
                                Util.showLog("1 " + data.get(data.size() - 2).text);
                                userDataViewModel.delete2Chat(data.get(data.size() - 1), chatID);
                                userDataViewModel.delete2Chat(data.get(data.size() - 2), chatID);
                                data.remove(data.get(data.size() - 1));
                                data.remove(data.get(data.size() - 1));
                                setDataToUi(data);
                            }
                            dialogMsg.cancel();
                            dialogMsg.showErrorDialog(resource.message, "Ok");
                            dialogMsg.show();
                        }
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

        userDataViewModel.getLocalChatData(chatID).observe(this, data -> {
            Util.showLog("User Chat Data: " + data.toString() + " " + chatID);
            if (data != null && data.size() > 0) {

            }
        });
        userDataViewModel.getUserData().observe(this, resource -> {
            if (resource != null) {

                Util.showLog("Got Data" + resource.message + " " + resource.data);

                switch (resource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server
                        prefManager().setBoolean(Constants.SUBSCRIBED, resource.data.isSubscribe != null ? resource.data.isSubscribe : false);
                        Constants.IS_SUBSCRIBED = prefManager().getBoolean(Constants.SUBSCRIBED);
                        setWords();
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

    }

    private void showVisibility(boolean b) {
        binding.btnSend.setVisibility(View.VISIBLE);
        binding.pbSend.setVisibility(View.GONE);
    }

    private void setDataToUi(List<ChatItem> data) {
        chatItemList = data;
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

        binding.lvMain.setBackground(drawableList.get(prefManager().getInt(Constants.BACKGROUND) > drawableList.size() ? 0
                : prefManager().getInt(Constants.BACKGROUND)));

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

        binding.btnSend.setOnClickListener(v -> {
            if (query.equals("")) {
                startSpeech();
            } else {
                if (!connectivity.isConnected()) {
                    Util.showToast(this, "Please Connect Internet");
                    return;
                }
                if (binding.textContent.getText().toString().isEmpty()) {
                    Util.showToast(this, "Please Ask Something");
                    return;
                }
                isError = true;
                if (InterstitialAdManager.isLoaded() && prefManager().getInt(Constants.CLICK) >=
                        Integer.valueOf(prefManager().getString(Constants.INTERSTITIAL_AD_CLICK))) {
                    prefManager().setInt(Constants.CLICK, 0);
                    InterstitialAdManager.showAds();
                } else {
                    prefManager().setInt(Constants.CLICK, prefManager().getInt(Constants.CLICK) + 1);
                    startSend();
                }
            }
        });

        binding.textContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (adapterRoboChat.getItemCount() > 0) {
                            binding.rvMassage.smoothScrollToPosition(adapterRoboChat.getItemCount());
                        }
                    }
                };
                handler.postDelayed(runnable, 200);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                query = s.toString();
                if (s.toString().equals("")) {
                    isDone = false;
                    binding.btnSend.setImageResource(R.drawable.ic_mic);
                    ViewAnimation.showScale(binding.btnSend);
                } else {
                    if (!isDone) {
                        isDone = true;
                        binding.btnSend.setImageResource(R.drawable.ic_send_outline);
                        ViewAnimation.showScale(binding.btnSend);
                    }
                }
            }
        });

    }

    private void startSpeech() {
        Intent intent
                = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

        try {
            someActivityResultLauncher.launch(intent);
        } catch (Exception e) {
            Toast.makeText(this, " " + e.getMessage(),
                            Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void startSend() {
        String message = binding.textContent.getText().toString();
        binding.textContent.setText("");
        binding.btnSend.setVisibility(View.GONE);
        binding.pbSend.setVisibility(View.VISIBLE);
        binding.btnSend.setEnabled(false);
        userDataViewModel.setChatObj(message, chatID);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        if (item.getItemId() == R.id.action_background) {
            startBackground();
        }

        if (item.getItemId() == R.id.action_history) {
            startActivity(new Intent(this, ChatHistoryActivity.class));
        }

        if (item.getItemId() == R.id.pointItem) {
            startActivity(new Intent(this, PremiumActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private void startBackground() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ItemBackgroundDialogBinding binding = ItemBackgroundDialogBinding.inflate(getLayoutInflater());
        dialog.setContentView(binding.getRoot());

        int width = MyApplication.getScreenWidth();

//        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.lvRoot.getLayoutParams();
//        params.width = (int) (width * 0.9f);
//        binding.lvRoot.setLayoutParams(params);


        AdapterBackground adapterBackground = new AdapterBackground(this, drawableList, position -> {
            this.binding.lvMain.setBackground(drawableList.get(position));
            prefManager().setInt(Constants.BACKGROUND, position);
            dialog.cancel();
        });
        binding.rvBg.setAdapter(adapterBackground);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setAttributes(getLayoutParams(dialog));

            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setCancelable(true);
        }
        dialog.show();
    }

    private WindowManager.LayoutParams getLayoutParams(@NonNull Dialog dialog) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        if (dialog.getWindow() != null) {
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
        }
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        return layoutParams;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        wordMenuItem = menu.findItem(R.id.pointItem);
        if (userDataViewModel != null) {
            setWords();
        }
        return super.onCreateOptionsMenu(menu);
    }

    private void setWords() {
        userDataViewModel.getAvailableWords().observe(this, value -> {
            if (wordMenuItem != null) {
                wordMenuItem.setTitle("" + value + " WRS");
            }
        });
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Here, no request code
                        if (result.getData() != null) {
                            ArrayList<String> resultData = result.getData().getStringArrayListExtra(
                                    RecognizerIntent.EXTRA_RESULTS);
                            binding.textContent.setText(Objects.requireNonNull(resultData).get(0));
                            binding.textContent.requestFocus();
                        }
                    }
                }
            });

    @Override
    public void onAdFailedToLoad() {

    }

    @Override
    public void onAdDismissed() {
        startSend();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!InterstitialAdManager.isLoaded()) {
            InterstitialAdManager.LoadAds();
        }
    }
}