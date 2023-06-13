package com.drewapps.ai.ui.activity;

import static com.drewapps.ai.utils.Constants.PERMISSIONS;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;

import com.drewapps.ai.MyApplication;
import com.drewapps.ai.R;
import com.drewapps.ai.binding.GlideBinding;
import com.drewapps.ai.databinding.ActivityAccountBinding;
import com.drewapps.ai.ui.dialogs.DialogMsg;
import com.drewapps.ai.utils.Connectivity;
import com.drewapps.ai.utils.Constants;
import com.drewapps.ai.utils.Util;
import com.drewapps.ai.utils.ViewAnimation;
import com.drewapps.ai.viewmodel.UserDataViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class AccountActivity extends AppCompatActivity {

    ActivityAccountBinding binding;
    UserDataViewModel userDataViewModel;
    ProgressDialog prgDialog;
    Connectivity connectivity;
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
                                } else if (!hasPermission(AccountActivity.this, PERMISSIONS[i])) {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialogMsg = new DialogMsg(this, false);
        connectivity = new Connectivity(this);
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Loading...");
        prgDialog.setCancelable(false);

        setUpUI();
        initViewModel();
    }

    private void initViewModel() {
        userDataViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);
        userDataViewModel.getUserData().observe(this, resource -> {
            if (resource != null) {
                if (resource.data != null) {
                    MyApplication.prefManager().setBoolean(Constants.SUBSCRIBED, resource.data.isSubscribe !=null ? resource.data.isSubscribe : false);
                    Constants.IS_SUBSCRIBED = MyApplication.prefManager().getBoolean(Constants.SUBSCRIBED);
                    binding.tvUserName.setText(resource.data.userName);
                    binding.etDocumentName.setText(resource.data.userName);
                    binding.tvEmail.setText(resource.data.emailId);
                    GlideBinding.bindImage(binding.riUserImage, resource.data.profileImage);
                }
            }
        });
        userDataViewModel.setUserObj(MyApplication.prefManager().getInt(Constants.USER_ID));
    }

    private void setUpUI() {
        binding.toolbar.toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(binding.toolbar.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Account Details");

        binding.ivEdite.setOnClickListener(v -> {
            binding.tilDocumentName.setErrorEnabled(false);
            if (binding.tvUserName.getVisibility() == View.VISIBLE) {
                ViewAnimation.hideScale(binding.tvUserName);
                binding.tvUserName.setVisibility(View.GONE);
                ViewAnimation.showScale(binding.tilDocumentName);
                binding.tilDocumentName.setVisibility(View.VISIBLE);
                binding.ivEdite.setImageDrawable(getDrawable(R.drawable.ic_done));
                binding.etDocumentName.setText(binding.tvUserName.getText().toString());
            } else {
                if (validate()) {
                    if (!connectivity.isConnected()) {
                        Util.showToast(this, "Please Connect Internet");
                        return;
                    }
                    startUpdate();
                }
            }
        });
        binding.btnDelete.setOnClickListener(v -> {
            if (!connectivity.isConnected()) {
                Util.showToast(this, "Please Connect Internet");
                return;
            }
            startDelete();
        });
        binding.btnLogout.setOnClickListener(v -> {
            dialogMsg.showConfirmDialog("Logout", "Do you really want to logout?",
                    "Yes",
                    "Cancel");
            dialogMsg.show();

            dialogMsg.okBtn.setOnClickListener(view -> {
                Util.showToast(this, "Success Logout");
                dialogMsg.cancel();
                userDataViewModel.logout().observe(this, result -> {
                    if (result != null) {
                        switch (result.status) {
                            case SUCCESS:
                                Util.showToast(this, "Success Logout");
                                MyApplication.prefManager().setBoolean(Constants.IS_LOGGED_IN, false);
                                MyApplication.prefManager().setInt(Constants.USER_ID, 0);
                                startActivity(new Intent(this, LoginActivity.class));
                                finish();
                                break;

                            case ERROR:
                                prgDialog.cancel();
                                dialogMsg.showErrorDialog(result.message, "Ok");
                                dialogMsg.show();
                                break;
                        }
                    }
                });
            });

            dialogMsg.cancelBtn.setOnClickListener(view -> dialogMsg.cancel());

        });

        binding.plusIcon.setOnClickListener(view ->{
            permissionsList = new ArrayList<>();
            permissionsList.addAll(Arrays.asList(PERMISSIONS));
            askForPermissions(permissionsList);

            selectImage();
        });
    }

    private void selectImage() {
        Intent i = new Intent(
                Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        someActivityResultLauncher.launch(i);
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
                                    GlideBinding.bindImage(binding.riUserImage, profileImagePath);
                                    startUpdate();
                                }
                            }
                        }
                    }
                }
            });

    private void startUpdate() {
        prgDialog.show();
        userDataViewModel.updateAccount(MyApplication.prefManager().getInt(Constants.USER_ID),
                binding.etDocumentName.getText().toString(), binding.tvEmail.getText().toString(), profileImagePath, imageUri).observe(this, result -> {

            if (result != null) {
                switch (result.status) {
                    case SUCCESS:

                        prgDialog.cancel();
                        //add offer text
                        binding.tilDocumentName.setVisibility(View.GONE);
                        binding.tvUserName.setVisibility(View.VISIBLE);
                        binding.tvUserName.setAlpha(1);
                        binding.tvUserName.setText(binding.etDocumentName.getText().toString());
                        binding.ivEdite.setImageDrawable(getDrawable(R.drawable.ic_edite));
                        Util.showToast(this, "Successfully Update");
                        break;

                    case ERROR:
                        prgDialog.cancel();
                        dialogMsg.showErrorDialog(result.message, "Ok");
                        dialogMsg.show();
                        break;
                }
            }
        });
    }

    private void startDelete() {
        prgDialog.show();
        userDataViewModel.deleteAccount(MyApplication.prefManager().getInt(Constants.USER_ID)).observe(this, result -> {

            if (result != null) {
                switch (result.status) {
                    case SUCCESS:

                        prgDialog.cancel();
                        //add offer text
                        dialogMsg.showSuccessDialog("Your request has been send successfully\nfor delete account.", "Ok");
                        dialogMsg.show();
                        break;

                    case ERROR:
                        prgDialog.cancel();
                        dialogMsg.showErrorDialog(result.message, "Ok");
                        dialogMsg.show();
                        break;
                }
            }
        });
    }

    private boolean validate() {
        if (binding.etDocumentName.getText().toString().isEmpty()) {
            binding.tilDocumentName.setError("User Name required");
            binding.tilDocumentName.setHelperTextColor(ColorStateList.valueOf(getColor(R.color.white)));
            binding.tilDocumentName.setErrorEnabled(true);
            binding.etDocumentName.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}