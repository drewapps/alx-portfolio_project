package com.drewapps.ai.ui.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.drewapps.ai.MyApplication;
import com.drewapps.ai.binding.GlideBinding;
import com.drewapps.ai.databinding.DialogPreviewBinding;
import com.drewapps.ai.items.ItemMagicArt;

public class PreviewDialog {

    public Activity activity;
    public Dialog dialog;
    public boolean cancelable = false;
    public ClickListener clickListener;

    DialogPreviewBinding binding;

    public PreviewDialog(Activity activity, boolean cancelable, ClickListener clickListener) {
        this.activity = activity;
        this.cancelable = cancelable;
        this.clickListener = clickListener;
        this.dialog = new Dialog(activity);
        this.dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    public void showDialog(ItemMagicArt itemMagicArt) {

        binding = DialogPreviewBinding.inflate(activity.getLayoutInflater());
        dialog.setContentView(binding.getRoot());

        int width = MyApplication.getScreenWidth();

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.ivPreview.getLayoutParams();
        params.width = (int) (width*0.7f);
        params.height = (int) (width*0.7f);
        binding.ivPreview.setLayoutParams(params);

        binding.tvDescription.setText(itemMagicArt.querys);
        binding.tvSize.setText(itemMagicArt.resolution);
        GlideBinding.bindImage(binding.ivPreview, itemMagicArt.image);

        binding.btnDownload.setOnClickListener(v -> {
            clickListener.onDownload(itemMagicArt);
        });

        binding.btnDelete.setOnClickListener(v -> {
            clickListener.onDelete(itemMagicArt);
        });

        if (dialog.getWindow() != null) {
            dialog.getWindow().setAttributes(getLayoutParams(dialog));

            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setCancelable(cancelable);
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

    public interface ClickListener {
        public void onDownload(ItemMagicArt itemMagicArt);

        public void onDelete(ItemMagicArt itemMagicArt);
    }
}
