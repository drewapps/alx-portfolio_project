package com.drewapps.ai.ui.activity;

import static com.drewapps.ai.utils.Constants.PERMISSIONS;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import com.drewapps.ai.MyApplication;
import com.drewapps.ai.R;
import com.drewapps.ai.adapters.AdapterChat;
import com.drewapps.ai.binding.GlideBinding;
import com.drewapps.ai.databinding.ActivitySupportDetailBinding;
import com.drewapps.ai.items.ItemSupportReq;
import com.drewapps.ai.items.SupportDetails;
import com.drewapps.ai.ui.dialogs.DialogMsg;
import com.drewapps.ai.utils.Connectivity;
import com.drewapps.ai.utils.Constants;
import com.drewapps.ai.utils.Util;
import com.drewapps.ai.viewmodel.SupportViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class SupportDetailActivity extends AppCompatActivity {

    ActivitySupportDetailBinding binding;
    SupportViewModel supportViewModel;
    AdapterChat adapterChat;
    ItemSupportReq itemSupportReq;
    DialogMsg dialogMsg;

    Uri imageUri;
    String profileImagePath = "";

    int permissionsCount = 0;
    Integer availableImage;
    ActivityResultLauncher<String[]> permissionsLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
                    new ActivityResultCallback<Map<String, Boolean>>() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onActivityResult(Map<String, Boolean> result) {
                            ArrayList<Boolean> list = new ArrayList<>(result.values());
                            permissionsList = new ArrayList<>();
                            permissionsCount = 0;
                            for (int i = 0; i < list.size(); i++) {
                                if (shouldShowRequestPermissionRationale(PERMISSIONS[i])) {
                                    permissionsList.add(PERMISSIONS[i]);
                                } else if (!hasPermission(SupportDetailActivity.this, PERMISSIONS[i])) {
                                    permissionsCount++;
                                }
                            }
                            if (permissionsList.size() > 0) {
                                //Some permissions are denied and can be asked again.
                                askForPermissions(permissionsList);
                            } else if (permissionsCount > 0) {
                                //Show alert dialog
//                                showPermissionDialog();
                            } else {
                                //All permissions granted. Do your stuff 🤞
                                Util.showLog("All permissions granted. Do your stuff \uD83E\uDD1E");
                            }
                        }
                    });

    private void askForPermissions(ArrayList<String> permissionsList) {
        String[] newPermissionStr = new String[permissionsList.size()];
        for (int i = 0; i < newPermissionStr.length; i++) {
            newPermissionStr[i] = permissionsList.get(i);
        }
        if (newPermissionStr.length > 0) {
            permissionsLauncher.launch(newPermissionStr);
        } else {
            //showPermissionDialog();
        }
    }

    AlertDialog alertDialog;

    private void showPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permission required")
                .setMessage("Some permissions are needed to be allowed to use this app without any problems.")
                .setPositiveButton("Ok", (dialog, which) -> {
                    dialog.dismiss();
                });
        if (alertDialog == null) {
            alertDialog = builder.create();
            if (!alertDialog.isShowing()) {
                alertDialog.show();
            }
        }
    }


    private boolean hasPermission(Context context, String permissionStr) {
        return ContextCompat.checkSelfPermission(context, permissionStr) == PackageManager.PERMISSION_GRANTED;
    }

    ArrayList<String> permissionsList;

    Connectivity connectivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySupportDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialogMsg = new DialogMsg(this, false);
        connectivity = new Connectivity(this);

        setUpUI();
        initViewModel();
    }

    private void initViewModel() {
        supportViewModel = new ViewModelProvider(this).get(SupportViewModel.class);
        supportViewModel.getSupportDetails().observe(this, resource->{
            if (resource != null) {

                Util.showLog("Got Data" + resource.message + resource.toString());

                switch (resource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        if (resource.data != null) {
                                setData(resource.data);
                                showVisibility(true);
                        } else {
                            showVisibility(false);
                        }
                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server
                        if (resource.data != null) {
                            binding.progressBar.setVisibility(View.GONE);
                            binding.btRefresh.setAlpha(1f);
                            setData(resource.data);
                            showVisibility(true);
                        } else {
                            showVisibility(false);
                        }

                        break;
                    case ERROR:
                        // Error State
                        showVisibility(false);
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
        supportViewModel.setDetails(itemSupportReq.ticketId);
    }

    private void showVisibility(boolean b) {

    }

    private void setData(SupportDetails data) {

        adapterChat.setMessageList(data.message);
        binding.tvStatus.setText(data.status);
        if (data.status.equals("Open")) {
            binding.tvStatus.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.blue_600)));
        } else if (data.status.equals("Replied")) {
            binding.tvStatus.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.yellow_700)));
        } else if (data.status.equals("Pending")) {
            binding.tvStatus.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.pink_700)));
        } else if (data.status.equals("Resolved")) {
            binding.tvStatus.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.green_600)));
        } else if (data.status.equals("Closed")) {
            //Closed
            binding.tvStatus.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.grey_80)));
            binding.tvStatus.setAlpha(0.4f);
            binding.lvSendLayout.setVisibility(View.GONE);

        }
        binding.rvMassage.smoothScrollToPosition(data.message.size());
    }

    private void setUpUI() {

        itemSupportReq = (ItemSupportReq) getIntent().getSerializableExtra("SUPPORT");

        binding.tvTitle.setText(itemSupportReq.subject);
        binding.tvTicketID.setText(itemSupportReq.ticketId);
        binding.tvStatus.setText(itemSupportReq.status);

        if (itemSupportReq.status.equals("Open")) {
            binding.tvStatus.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.blue_600)));
        } else if (itemSupportReq.status.equals("Replied")) {
            binding.tvStatus.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.yellow_700)));
        } else if (itemSupportReq.status.equals("Pending")) {
            binding.tvStatus.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.pink_700)));
        } else if (itemSupportReq.status.equals("Resolved")) {
            binding.tvStatus.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.green_600)));
        } else if (itemSupportReq.status.equals("Closed")) {
            //Closed
            binding.tvStatus.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.grey_80)));
            binding.tvStatus.setAlpha(0.4f);

            binding.lvSendLayout.setVisibility(View.GONE);
        }

        if (itemSupportReq.priority.equals("High")) {
            binding.ivPriority.setImageTintList(ColorStateList.valueOf(getColor(R.color.red_700)));
        } else if (itemSupportReq.priority.equals("Normal")) {
            binding.ivPriority.setImageTintList(ColorStateList.valueOf(getColor(R.color.cyan_500)));
        } else if (itemSupportReq.priority.equals("Low")) {
            binding.ivPriority.setImageTintList(ColorStateList.valueOf(getColor(R.color.green_600)));
        }

        adapterChat = new AdapterChat(this, msg->{

        });
        binding.rvMassage.setAdapter(adapterChat);

        binding.btnAttach.setOnClickListener(v->{
            permissionsList = new ArrayList<>();
            permissionsList.addAll(Arrays.asList(PERMISSIONS));
            askForPermissions(permissionsList);

            Intent i = new Intent(
                    Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            someActivityResultLauncher.launch(i);
        });

        binding.btnSend.setOnClickListener(v->{

            if(!connectivity.isConnected()){
                Util.showToast(this, "Please Connect Internet");
                return;
            }
            if(binding.textContent.getText().toString().isEmpty()){
                Util.showToast(this, "Please Write Message");
                return;
            }

            startSend();

        });
        binding.btClose.setOnClickListener(v->{
            onBackPressed();
        });

        binding.btRefresh.setOnClickListener(v->{
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.btRefresh.setAlpha(0f);
            supportViewModel.setDetails(itemSupportReq.ticketId);
        });
    }

    private void startSend() {
        String message = binding.textContent.getText().toString();
        binding.btnSend.setVisibility(View.GONE);
        binding.pbSend.setVisibility(View.VISIBLE);
        supportViewModel.sendMessage(this, profileImagePath, imageUri,
                MyApplication.prefManager().getInt(Constants.USER_ID),
                message, itemSupportReq.ticketId,getContentResolver()).observe(this,
                result->{

                    if (result != null) {
                        switch (result.status) {
                            case SUCCESS:

                                binding.btnSend.setVisibility(View.VISIBLE);
                                binding.pbSend.setVisibility(View.GONE);
                                //add offer text

                                adapterChat.setMessageList(result.data.message);
                                binding.rvMassage.smoothScrollToPosition(result.data.message.size());
                                binding.textContent.setText("");
                                imageUri = null;
                                profileImagePath = "";
                                break;

                            case ERROR:
                                binding.btnSend.setVisibility(View.VISIBLE);
                                binding.pbSend.setVisibility(View.GONE);

                                dialogMsg.showErrorDialog(result.message, "Ok");
                                dialogMsg.show();

                                break;
                        }
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
                            Uri selectedImage = result.getData().getData();
                            String[] filePathColumn = {MediaStore.Images.Media.DATA};

                            if (selectedImage != null) {
                                Cursor cursor = getContentResolver().query(selectedImage,
                                        null, null, null, null);

                                if (cursor != null) {
                                    cursor.moveToFirst();

                                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                    profileImagePath = cursor.getString(columnIndex);
                                    cursor.close();

                                    imageUri = selectedImage;
                                    GlideBinding.bindImage(binding.btnAttach, profileImagePath);
                                }
                            }
                        }
                    }
                }
            });

}