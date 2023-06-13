package com.drewapps.ai.ui.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;
import com.drewapps.ai.Ads.NativeAdManager;
import com.drewapps.ai.R;
import com.drewapps.ai.binding.GlideBinding;
import com.drewapps.ai.databinding.DialogLoaderBinding;
import com.drewapps.ai.databinding.DialogMessageBinding;

public class DialogMsg {

    public Activity activity;
    public Dialog dialog;
    public boolean cancelable = false;

    public LottieAnimationView successAnim, errorAnim, warningAnim, confirmAnim, noInternet;
    public Button okBtn, cancelBtn;

    public DialogMsg(Activity activity, Boolean cancelable) {
        this.activity = activity;
        this.dialog = new Dialog(activity);
        this.dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.cancelable = cancelable;
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

    public void show() {
        if (dialog != null) {
            dialog.show();
        }
    }

    public Dialog getDialog() {
        return dialog;
    }

    public void cancel() {
        if (dialog != null) {
            dialog.cancel();
        }
    }

    public void showLoadingDialog() {
        DialogLoaderBinding binding = DialogLoaderBinding.inflate(activity.getLayoutInflater());
        this.dialog.setContentView(binding.getRoot());

        if (dialog.getWindow() != null) {
            dialog.getWindow().setAttributes(getLayoutParams(dialog));

            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setCancelable(cancelable);
        }
        dialog.show();
    }

    public void showSuccessDialog(String message, String okTitle) {
        DialogMessageBinding binding = DialogMessageBinding.inflate(activity.getLayoutInflater());
        this.dialog.setContentView(binding.getRoot());
        successAnim = binding.successAnimation;
        successAnim.setVisibility(View.VISIBLE);

        okBtn = binding.dialogOkButton;

        okBtn.setBackgroundColor(dialog.getContext().getResources().getColor(R.color.green_A700));

        binding.dialogTitleTextView.setText("Success");
        binding.dialogMessageTextView.setText(message);
        okBtn.setText(okTitle);


        if (dialog.getWindow() != null) {
            dialog.getWindow().setAttributes(getLayoutParams(dialog));

            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setCancelable(cancelable);
            okBtn.setOnClickListener(view -> DialogMsg.this.cancel());
        }
    }

    public void showErrorDialog(String message, String okTitle) {
        DialogMessageBinding binding = DialogMessageBinding.inflate(activity.getLayoutInflater());
        this.dialog.setContentView(binding.getRoot());
        errorAnim = binding.errorAnimation;
        errorAnim.setVisibility(View.VISIBLE);

        okBtn = binding.dialogOkButton;

        okBtn.setBackgroundColor(dialog.getContext().getResources().getColor(R.color.red_A700));

        binding.dialogTitleTextView.setText("Error");
        binding.dialogMessageTextView.setText(message);
        okBtn.setText(okTitle);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setAttributes(getLayoutParams(dialog));

            this.dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            this.dialog.setCancelable(cancelable);
            okBtn.setOnClickListener(view -> DialogMsg.this.cancel());
        }
    }

    public void showWarningDialog(String title, String message, String okTitle, boolean cancelable) {
        DialogMessageBinding binding = DialogMessageBinding.inflate(activity.getLayoutInflater());
        this.dialog.setContentView(binding.getRoot());
        warningAnim = dialog.findViewById(R.id.warn_animation);
        warningAnim.setVisibility(View.VISIBLE);
        okBtn = binding.dialogOkButton;
        cancelBtn = binding.dialogCancelButton;

        if (cancelable) {
            cancelBtn.setVisibility(View.VISIBLE);
        }
        NativeAdManager.showAds(dialog.getContext(), dialog.findViewById(R.id.rl_native_ad));
        okBtn.setBackgroundColor(dialog.getContext().getResources().getColor(R.color.amber_800));

        binding.dialogTitleTextView.setText(title);
        binding.dialogMessageTextView.setText(message);
        okBtn.setText(okTitle);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setAttributes(getLayoutParams(dialog));

            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setCancelable(cancelable);
            okBtn.setOnClickListener(view -> DialogMsg.this.cancel());
            cancelBtn.setOnClickListener(view -> DialogMsg.this.cancel());
        }
    }

    public boolean isShowing() {
        return dialog.isShowing();
    }

    public void showConfirmDialog(String title, String message, String okTitle, String cancelTitle) {
        DialogMessageBinding binding = DialogMessageBinding.inflate(activity.getLayoutInflater());
        this.dialog.setContentView(binding.getRoot());
        confirmAnim = binding.confirmAnimation;
        confirmAnim.setVisibility(View.VISIBLE);

        cancelBtn = binding.dialogCancelButton;
        cancelBtn.setVisibility(View.VISIBLE);
        okBtn = binding.dialogOkButton;

        binding.dialogTitleTextView.setText(title);
        binding.dialogTitleTextView.setAllCaps(true);
        binding.dialogMessageTextView.setText(message);
        okBtn.setText(okTitle);
        cancelBtn.setText(cancelTitle);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setAttributes(getLayoutParams(dialog));

            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setCancelable(cancelable);
            okBtn.setOnClickListener(view -> DialogMsg.this.cancel());
            cancelBtn.setOnClickListener(view -> DialogMsg.this.cancel());
        }
    }

    public void showAppInfoDialog(String updateText, String cancelText, String title, String description) {
        this.dialog.setContentView(R.layout.dialog_app_info);
        TextView descriptionTextView = dialog.findViewById(R.id.descriptionTextView);
        cancelBtn = dialog.findViewById(R.id.dialogCancelButton);

        descriptionTextView.setMovementMethod(new ScrollingMovementMethod());

        TextView msgTextView = dialog.findViewById(R.id.titleTextView);
        okBtn = dialog.findViewById(R.id.dialogOkButton);

        msgTextView.setText(title);
        descriptionTextView.setText(description);
        okBtn.setText(updateText);
        cancelBtn.setText(cancelText);

        GlideBinding.setTextSize(msgTextView, "font_body_s_size");
        GlideBinding.setTextSize(descriptionTextView, "font_body_xs_size");
        GlideBinding.setTextSize(cancelBtn, "button_text_12");
        GlideBinding.setTextSize(okBtn, "button_text_12");

        GlideBinding.setFont(descriptionTextView, "bold");
        GlideBinding.setFont(msgTextView, "extra_bold");
        GlideBinding.setFont(cancelBtn, "medium");
        GlideBinding.setFont(okBtn, "medium");

        if (dialog.getWindow() != null) {
            dialog.getWindow().setAttributes(getLayoutParams(dialog));
            dialog.setCancelable(cancelable);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            okBtn.setOnClickListener(view -> DialogMsg.this.cancel());
        }
    }

    public void showNoInternetDialog(String message, String okTitle) {
        DialogMessageBinding binding = DialogMessageBinding.inflate(activity.getLayoutInflater());
        this.dialog.setContentView(binding.getRoot());
        noInternet = binding.noInterAnimation;
        noInternet.setVisibility(View.VISIBLE);

        okBtn = binding.dialogOkButton;

        okBtn.setBackgroundColor(dialog.getContext().getResources().getColor(R.color.blue_700));

        binding.dialogTitleTextView.setText("No Internet");
        binding.dialogMessageTextView.setText(message);
        okBtn.setText(okTitle);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setAttributes(getLayoutParams(dialog));

            this.dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            this.dialog.setCancelable(cancelable);
            okBtn.setOnClickListener(view -> DialogMsg.this.cancel());
        }
    }
}
