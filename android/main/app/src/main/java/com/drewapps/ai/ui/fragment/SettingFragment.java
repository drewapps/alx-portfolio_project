package com.drewapps.ai.ui.fragment;

import static com.drewapps.ai.utils.Constants.MESSAGE;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.drewapps.ai.BuildConfig;
import com.drewapps.ai.R;
import com.drewapps.ai.databinding.DialogAddReviewBinding;
import com.drewapps.ai.databinding.FragmentSettingBinding;
import com.drewapps.ai.ui.activity.AccountActivity;
import com.drewapps.ai.ui.activity.ContactActivity;
import com.drewapps.ai.ui.activity.IntroActivity;
import com.drewapps.ai.ui.activity.PurchaseHistoryActivity;
import com.drewapps.ai.ui.activity.SupportRequestActivity;
import com.drewapps.ai.utils.Util;

public class SettingFragment extends Fragment {


    public SettingFragment() {
        // Required empty public constructor
    }

    FragmentSettingBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(getLayoutInflater());

        setUpUI();

        return binding.getRoot();
    }

    private void setUpUI() {
        binding.lvIntroduction.setOnClickListener(view -> {
            getContext().startActivity(new Intent(getContext(), IntroActivity.class));
        });
        binding.lvAccount.setOnClickListener(view -> {
            getContext().startActivity(new Intent(getContext(), AccountActivity.class));
        });

        binding.lvPurchase.setOnClickListener(view -> {
            getContext().startActivity(new Intent(getContext(), PurchaseHistoryActivity.class));
        });
        binding.lvSupport.setOnClickListener(view -> {
            getContext().startActivity(new Intent(getContext(), SupportRequestActivity.class));
        });
        binding.lvContact.setOnClickListener(view -> {
            getContext().startActivity(new Intent(getContext(), ContactActivity.class));
        });
        binding.lvShare.setOnClickListener(view -> {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                String shareMessage = getString(R.string.share_msg);
                shareMessage = shareMessage + " " + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch (Exception e) {
                //e.toString();
            }
        });

        binding.lvLeaveReview.setOnClickListener(v -> {
            Dialog dialog = new Dialog(getContext());
            DialogAddReviewBinding binding1 = DialogAddReviewBinding.inflate(getLayoutInflater());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(binding1.getRoot());

            if (dialog.getWindow() != null) {
                dialog.getWindow().setAttributes(getLayoutParams(dialog));

                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setCancelable(true);
            }
            binding1.btCancel.setOnClickListener(v3 -> {
                dialog.dismiss();
            });
            binding1.btSubmit.setOnClickListener(vs -> {
                if (binding1.etPost.getText().toString().isEmpty()) {
                    binding1.etPost.setError("Please Write Somthing");
                    binding1.etPost.requestFocus();
                    return;
                }
                if (binding1.ratingBar.getRating() == 0) {
                    Util.showToast(getContext(), "Please Give a rating");
                    return;
                }

                if (binding1.ratingBar.getRating() > 3) {
                    dialog.dismiss();
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getContext().getPackageName())));
                } else {
                    dialog.dismiss();
                    MESSAGE = binding1.etPost.getText().toString();
                    startActivity(new Intent(getContext(), ContactActivity.class));
                }

            });

            dialog.show();
        });
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

}