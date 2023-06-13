package com.drewapps.ai.ui.fragment;

import static com.drewapps.ai.utils.Constants.PERMISSIONS;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.drewapps.ai.MyApplication;
import com.drewapps.ai.R;
import com.drewapps.ai.binding.GlideBinding;
import com.drewapps.ai.databinding.FragmentCreateSupportBinding;
import com.drewapps.ai.ui.dialogs.DialogMsg;
import com.drewapps.ai.utils.Constants;
import com.drewapps.ai.utils.Util;
import com.drewapps.ai.viewmodel.SupportViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;


public class CreateSupportFragment extends DialogFragment {


    public CreateSupportFragment() {
        // Required empty public constructor
    }

    public void setRequestCode(int request_code) {
        this.request_code = request_code;
    }

    FragmentCreateSupportBinding binding;

    public CallbackResult callbackResult;

    public void setOnCallbackResult(final CallbackResult callbackResult) {
        this.callbackResult = callbackResult;
    }

    private int request_code = 0;

    SupportViewModel supportViewModel;

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
                                } else if (!hasPermission(getContext(), PERMISSIONS[i])) {
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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

    ProgressDialog prgDialog;
    DialogMsg dialogMsg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateSupportBinding.inflate(getLayoutInflater());

        dialogMsg = new DialogMsg(getActivity(), false);
        prgDialog = new ProgressDialog(getContext());
        prgDialog.setMessage("Loading...");
        prgDialog.setCancelable(false);

        setUpUI();
        initViewModel();
        return binding.getRoot();
    }

    private void initViewModel() {
        supportViewModel = new ViewModelProvider(getActivity()).get(SupportViewModel.class);
    }

    private void setUpUI() {

        binding.btClose.setOnClickListener(v -> {
            dismiss();
        });

        binding.etCategory.setOnClickListener(v -> {
            ShowPopUpMenu(getContext(), v, binding.etCategory, R.menu.support_category);
        });
        binding.tilCategory.setOnClickListener(v -> {
            ShowPopUpMenu(getContext(), v, binding.etCategory, R.menu.support_category);
        });

        binding.etPriority.setOnClickListener(v -> {
            ShowPopUpMenu(getContext(), v, binding.etPriority, R.menu.support_priority);
        });
        binding.tilPriority.setOnClickListener(v -> {
            ShowPopUpMenu(getContext(), v, binding.etPriority, R.menu.support_priority);
        });

        binding.etMassage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.tilMassage.setErrorEnabled(false);
            }
        });

        binding.etSubjectName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.tilSubjectName.setErrorEnabled(false);
            }
        });

        binding.btSave.setOnClickListener(v -> {
            binding.tilMassage.setErrorEnabled(false);
            binding.tilSubjectName.setErrorEnabled(false);
            if (validate()) {
                sendRequest();
            }

        });

        binding.lvAdd.setOnClickListener(v -> {
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

    private void sendRequest() {

        String subject = binding.etSubjectName.getText().toString();
        String category = binding.etCategory.getText().toString();
        String priority = binding.etPriority.getText().toString();
        String massage = binding.etMassage.getText().toString();

        prgDialog.show();
        supportViewModel.createSupportReq(getContext(), profileImagePath, imageUri, MyApplication.prefManager().getInt(Constants.USER_ID),
                subject, category, priority, massage, getActivity().getContentResolver()).observe(getActivity(),
                result->{

                    if (result != null) {
                        switch (result.status) {
                            case SUCCESS:

                                prgDialog.cancel();
                                //add offer text

                                dialogMsg.showSuccessDialog("Your Support Request has been sent.", "Ok");
                                dialogMsg.show();
                                dialogMsg.okBtn.setOnClickListener(v->{
                                    if (callbackResult != null) {
                                        dialogMsg.cancel();
                                        callbackResult.sendResult(request_code, result.data);
                                        dismiss();
                                    }
                                });

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
        if (binding.etSubjectName.getText().toString().isEmpty()) {
            binding.tilSubjectName.setError("Document name is required");
            binding.tilSubjectName.setErrorEnabled(true);
            binding.tilSubjectName.requestFocus();
            return false;
        } else if (binding.etMassage.getText().toString().isEmpty()) {
            binding.tilMassage.setError("Instruction is required");
            binding.tilMassage.setErrorEnabled(true);
            binding.tilMassage.requestFocus();
            return false;
        }
        return true;
    }

    public interface CallbackResult {
        void sendResult(int requestCode, Object obj);
    }

    public void ShowPopUpMenu(Context context, View parentView, View mainView, int menu) {
        PopupMenu popup = new PopupMenu(context, parentView, Gravity.NO_GRAVITY, 0,
                R.style.popupMenu);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                ((MaterialAutoCompleteTextView) mainView).setText(item.getTitle());

                return true;
            }
        });
        popup.inflate(menu);
        popup.setForceShowIcon(false);
        popup.show();

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
                                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                                        null, null, null, null);

                                if (cursor != null) {
                                    cursor.moveToFirst();

                                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                    profileImagePath = cursor.getString(columnIndex);
                                    cursor.close();

                                    imageUri = selectedImage;
                                    GlideBinding.bindImage(binding.ivImage, profileImagePath);
                                }
                            }
                        }
                    }
                }
            });

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
}