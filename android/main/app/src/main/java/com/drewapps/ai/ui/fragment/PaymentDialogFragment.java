package com.drewapps.ai.ui.fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.drewapps.ai.Config;
import com.drewapps.ai.R;
import com.drewapps.ai.databinding.FragmentPaymentDialogBinding;
import com.drewapps.ai.items.ItemSubsPlan;
import com.drewapps.ai.utils.Constants;
import com.drewapps.ai.utils.PrefManager;
import com.drewapps.ai.utils.Util;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class PaymentDialogFragment extends BottomSheetDialogFragment {


    FragmentPaymentDialogBinding binding;

    String type = "";
    CallBack callBack;
    Activity context;
    PrefManager prefManager;
    ItemSubsPlan currentSubsPlan;

    public PaymentDialogFragment(ItemSubsPlan currentSubsPlan, CallBack callBack) {
        this.callBack = callBack;
        this.currentSubsPlan = currentSubsPlan;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPaymentDialogBinding.inflate(getLayoutInflater());
        prefManager = new PrefManager(getContext());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (prefManager.getString(Constants.RAZOR_PAY_ENABLE).equals(Config.ONE)) {
            binding.razorpayRb.setVisibility(View.VISIBLE);
        }
        if (prefManager.getString(Constants.STRIPE_ENABLE).equals(Config.ONE)) {
            binding.stripeRb.setVisibility(View.VISIBLE);
        }
        Util.showLog("" + currentSubsPlan.googleProductEnable);
        if(currentSubsPlan.googleProductEnable == 1){
            binding.inAppRb.setVisibility(View.VISIBLE);
        }

        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.razorpay_rb:
                        type = "razorpay";
                        break;
                    case R.id.stripe_rb:
                        type = "stripe";
                        break;
                    case R.id.in_app_rb:
                        type = "in_app";
                        break;
                }
            }
        });
        binding.cencelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        binding.buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("")) {
                    Toast.makeText(getContext(), "Please select payment type", Toast.LENGTH_SHORT).show();
                } else {
                    callBack.getResponse(type);
                    dismiss();
                }
            }
        });
    }

    public interface CallBack {
        void getResponse(String type);
    }
}