package com.drewapps.ai.ui.activity;

import static com.drewapps.ai.utils.Constants.PERMISSIONS;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.widget.Toast;

import com.drewapps.ai.Ads.BannerAdManager;
import com.drewapps.ai.R;
import com.drewapps.ai.databinding.ActivityEditorBinding;
import com.drewapps.ai.databinding.AddLinkDialogBinding;
import com.drewapps.ai.items.Document;
import com.drewapps.ai.ui.dialogs.DialogMsg;
import com.drewapps.ai.utils.Util;
import com.drewapps.ai.viewmodel.DocumentViewModel;
import com.drewapps.ai.viewmodel.UserDataViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import io.github.mddanishansari.html_to_pdf.HtmlToPdfConvertor;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class EditorActivity extends AppCompatActivity {

    ActivityEditorBinding binding;

    Document document;

    HtmlToPdfConvertor htmlToPdfConvertor;
    boolean visible = false;
    boolean isChanged = false;

    DialogMsg dialogMsg;

    DocumentViewModel documentViewModel;
    UserDataViewModel userDataViewModel;
    int permissionsCount = 0;
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
                                } else if (!hasPermission(EditorActivity.this, PERMISSIONS[i])) {
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
        binding = ActivityEditorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialogMsg = new DialogMsg(this, false);
        userDataViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);
        setUpViewModel();

        binding.toolbar.toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(binding.toolbar.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().getExtras() != null) {
            document = (Document) getIntent().getExtras().getSerializable("document");
            getSupportActionBar().setTitle(document.documentName);
        }
        binding.tvCharacter.setText("" + document.contentCharacter + " Char");
        binding.tvWords.setText("" + document.contentWord + " Words");

       // userDataViewModel.updateWord(document.contentWord);

        permissionsList = new ArrayList<>();
        permissionsList.addAll(Arrays.asList(PERMISSIONS));
        askForPermissions(permissionsList);

        binding.rlSave.setOnClickListener(v -> {
            saveDocument();
        });

        binding.rlPdf.setOnClickListener(v -> {
            downloadPDF();
        });

        setData();
        BannerAdManager.showBannerAds(this, binding.adView);
    }

    private void setUpViewModel() {
        documentViewModel = new ViewModelProvider(this).get(DocumentViewModel.class);
        documentViewModel.getUpdateData().observe(this, resource -> {
            if (resource != null) {
                switch (resource.status) {
                    case LOADING:

                        break;

                    case SUCCESS:

                        if (resource.data != null) {

                            dialogMsg.cancel();
                            dialogMsg.showSuccessDialog("Your Document has been Saved successfully", "Ok");
                            dialogMsg.show();

                        }

                        break;

                    case ERROR:
                        // Error State
                        dialogMsg.showErrorDialog(resource.message, "Ok");
                        dialogMsg.show();
//                            dialog.dismiss();

                        break;
                    default:
                        // Default

                        break;
                }

            }
        });
    }

    private void setData() {
        if (document != null) {
            binding.editor.setHtml(document.content);
        }
        binding.editor.setPadding(10, 10, 10, 10);
        binding.editor.setEditorFontSize(18);
        binding.editor.setBackgroundColor(Color.WHITE);
        binding.actionRedo.setOnClickListener(view -> {
            binding.editor.redo();
        });
        binding.actionUndo.setOnClickListener(view -> {
            binding.editor.undo();
        });
        binding.actionBold.setOnClickListener(view -> {
            binding.editor.setBold();
        });
        binding.actionItalic.setOnClickListener(view -> {
            binding.editor.setItalic();
        });
        binding.actionUnderline.setOnClickListener(view -> {
            binding.editor.setUnderline();
        });
        binding.actionHeading1.setOnClickListener(view -> {
            binding.editor.setHeading(1);
        });
        binding.actionHeading2.setOnClickListener(view -> {
            binding.editor.setHeading(2);
        });
        binding.actionHeading3.setOnClickListener(view -> {
            binding.editor.setHeading(3);
        });
        binding.actionTxtColor.setOnClickListener(view -> {
            binding.editor.setTextColor(isChanged ? Color.BLACK : Color.RED);
            isChanged = !isChanged;
        });
        binding.actionIndent.setOnClickListener(view -> {
            binding.editor.setIndent();
        });
        binding.actionOutdent.setOnClickListener(view -> {
            binding.editor.setOutdent();
        });
        binding.actionAlignLeft.setOnClickListener(view -> {
            binding.editor.setAlignLeft();
        });
        binding.actionAlignCenter.setOnClickListener(view -> {
            binding.editor.setAlignCenter();
        });
        binding.actionAlignRight.setOnClickListener(view -> {
            binding.editor.setAlignRight();
        });
        binding.actionInsertBullets.setOnClickListener(view -> {
            binding.editor.setBullets();
        });
        binding.actionInsertNumbers.setOnClickListener(view -> {
            binding.editor.setNumbers();
        });
        binding.actionInsertLink.setOnClickListener(view -> {
            showAddLink();
        });
        binding.actionBlockquote.setOnClickListener(view -> {
            binding.editor.setBlockquote();
        });
        binding.actionInsertCheckbox.setOnClickListener(view -> {
            binding.editor.insertTodo();
        });

        binding.insertCode.setOnClickListener(view -> {
            binding.editor.setCode();
        });
        binding.preview.setOnClickListener(view -> {
            if (!visible) {
                binding.editor.setInputEnabled(false);
                binding.preview.setImageResource(R.drawable.ic_nopreview);
            } else {
                binding.editor.setInputEnabled(true);
                binding.preview.setImageResource(R.drawable.ic_preview);
            }
            visible = !visible;
        });

    }

    private void showAddLink() {
        AddLinkDialogBinding binding1 = AddLinkDialogBinding.inflate(getLayoutInflater());
        final Dialog dialog = new Dialog(this, R.style.ThemeWithCorners);
        dialog.setContentView(binding1.getRoot());
        dialog.setCancelable(false);

        binding1.btnCancelDialog.setOnClickListener(view -> dialog.dismiss());
        binding1.btnAddTextSDialog.setOnClickListener(view -> {
            if (binding1.etDocumentName.getText().toString().trim().length() > 0) {
                binding.editor.insertLink(binding1.etDocumentName.getText().toString(),
                        "One Code");
                dialog.dismiss();
                return;
            }
            Toast.makeText(this, "Please enter text here.", Toast.LENGTH_SHORT).show();
        });
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveDocument() {
        dialogMsg.showLoadingDialog();
        documentViewModel.setUpdateData(document.documentId, document.documentName, binding.editor.getHtml().toString());
    }

    private void downloadPDF() {

        htmlToPdfConvertor = new HtmlToPdfConvertor(this);
        htmlToPdfConvertor.setBaseUrl("file:///android_asset/images/");

        String html = "<h2 style=\"text-align: center;\"><strong>" + document.documentName + "</strong></h2>" + binding.editor.getHtml();

        htmlToPdfConvertor.convert(new File(getPdfFilePath(), document.documentName + "_" + System.currentTimeMillis() + ".pdf"), html,
                new Function1<Exception, Unit>() {
                    @Override
                    public Unit invoke(Exception e) {
                        Util.showErrorLog(e.getMessage(), e);
                        Util.showToast(EditorActivity.this, "Check logs for error");
                        return null;
                    }
                }, new Function1<File, Unit>() {
                    @Override
                    public Unit invoke(File file) {

                        dialogMsg.showSuccessDialog("Your PDF file is successfully downloaded.", "Open PDF");
                        dialogMsg.okBtn.setOnClickListener(v -> {

                            dialogMsg.cancel();
                            openPdf(file);

                        });
                        dialogMsg.show();
                        return null;
                    }
                });
    }

    private void openPdf(File file) {
        try {
            Context context = (Context) this;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(getPackageName());
            stringBuilder.append(".provider");
            Uri uri = FileProvider.getUriForFile(context, stringBuilder.toString(), file);
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(uri, "application/pdf");
            startActivity(intent);
            return;
        } catch (ActivityNotFoundException activityNotFoundException) {
            activityNotFoundException.printStackTrace();
            dialogMsg.showErrorDialog("No application found to open pdf file", "Ok");
            dialogMsg.show();
            return;
        } catch (Exception exception) {
            exception.printStackTrace();
            Context context = (Context) this;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to open ");
            stringBuilder.append(", please try again later.");
            stringBuilder.append(exception.getMessage());
            dialogMsg.showErrorDialog(stringBuilder.toString(), "Ok");
            dialogMsg.show();
            return;
        }
    }

    private File getPdfFilePath() {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS
        ), "/" + getResources().getString(R.string.app_name));
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Util.showLog("Can't create directory to save image.");
                Toast.makeText(getApplicationContext(),
                        "Directory Error",
                        Toast.LENGTH_LONG).show();
            }
        }
        return file;
    }
}