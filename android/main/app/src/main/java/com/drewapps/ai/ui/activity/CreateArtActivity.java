package com.drewapps.ai.ui.activity;

import static com.drewapps.ai.MyApplication.prefManager;
import static com.drewapps.ai.utils.Constants.PERMISSIONS;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.drewapps.ai.Ads.BannerAdManager;
import com.drewapps.ai.Ads.InterstitialAdManager;
import com.drewapps.ai.R;
import com.drewapps.ai.adapters.MagicArtAdapter;
import com.drewapps.ai.databinding.ActivityCreateArtBinding;
import com.drewapps.ai.items.ItemGenMagicArt;
import com.drewapps.ai.items.ItemMagicArt;
import com.drewapps.ai.ui.dialogs.DialogMsg;
import com.drewapps.ai.ui.dialogs.PreviewDialog;
import com.drewapps.ai.utils.Connectivity;
import com.drewapps.ai.utils.Constants;
import com.drewapps.ai.utils.Tools;
import com.drewapps.ai.utils.Util;
import com.drewapps.ai.viewmodel.MagicArtViewModel;
import com.drewapps.ai.viewmodel.UserDataViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CreateArtActivity extends AppCompatActivity implements PreviewDialog.ClickListener, InterstitialAdManager.Listener {

    ActivityCreateArtBinding binding;

    UserDataViewModel userDataViewModel;

    MagicArtViewModel magicArtViewModel;
    DialogMsg dialogMsg;
    Connectivity connectivity;
    MagicArtAdapter magicArtAdapter;

    PreviewDialog previewDialog;
    ProgressDialog prgDialog;

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
                                } else if (!hasPermission(CreateArtActivity.this, PERMISSIONS[i])) {
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
        binding = ActivityCreateArtBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialogMsg = new DialogMsg(this, false);
        connectivity = new Connectivity(this);
        previewDialog = new PreviewDialog(this, true, this);
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Loading...");
        prgDialog.setCancelable(false);

        permissionsList = new ArrayList<>();
        permissionsList.addAll(Arrays.asList(PERMISSIONS));
        askForPermissions(permissionsList);

        intiViewModel();
        setUpUI();
        BannerAdManager.showBannerAds(this, binding.adView);
        InterstitialAdManager.Interstitial(this, this);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private void intiViewModel() {
        userDataViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);
        magicArtViewModel = new ViewModelProvider(this).get(MagicArtViewModel.class);

        userDataViewModel.getAvailableImages().observe(this, value->{
            availableImage = value;
            binding.tvAvailable.setText("Your balance: " + availableImage + " Images");
        });

        magicArtViewModel.getGeneratedData().observe(this, resource -> {
            if (resource != null) {

                Util.showLog("Got Data" + resource.message + resource.toString());

                switch (resource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (resource.data != null) {
                            Util.showLog(" " + resource.data.size());
                            setDataToUi(resource.data);
                            showVisibility(true);
                        } else {
                            showVisibility(false);
                        }

                        break;
                    case ERROR:
                        // Error State
                        dialogMsg.cancel();
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
    }

    private void showVisibility(boolean isVisible) {
        if (isVisible) {
            binding.rvMagic.setVisibility(View.VISIBLE);
            binding.ivTick.setImageDrawable(getDrawable(R.drawable.ic_tick));
        } else {
            binding.rvMagic.setVisibility(View.GONE);
            binding.ivTick.setImageDrawable(getDrawable(R.drawable.ic_tick_grey));
        }

    }

    private void setDataToUi(List<ItemGenMagicArt> data) {
//        userDataViewModel.updateImage(data.size());
        dialogMsg.cancel();
        List<ItemMagicArt> list = new ArrayList<ItemMagicArt>();
        for (ItemGenMagicArt item : data) {
            list.add(new ItemMagicArt(item.id, item.image, item.querys, item.resolution));
        }
        magicArtAdapter.setMagicArt(list);
    }

    private void setUpUI() {
        binding.toolbar.toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(binding.toolbar.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create Art");

        binding.etNoImage.setText("1");
        binding.etNoImage.setOnClickListener(v -> {
            Tools.ShowPopUpMenu(CreateArtActivity.this, v, binding.etNoImage,
                    binding.tilNoImage, R.menu.outputs);
        });
        binding.tilNoImage.setEndIconOnClickListener(v -> {
            Tools.ShowPopUpMenu(CreateArtActivity.this, v, binding.etNoImage,
                    binding.tilNoImage, R.menu.outputs);
        });

        binding.tilMedium.setEndIconOnClickListener(v -> {
            Tools.ShowPopUpMenu(CreateArtActivity.this, v, binding.etMedium,
                    binding.tilMedium, R.menu.medium_menu);
        });
        binding.etMedium.setOnClickListener(v -> {
            Tools.ShowPopUpMenu(CreateArtActivity.this, v, binding.etMedium,
                    binding.tilMedium, R.menu.medium_menu);
        });

        binding.etStyle.setOnClickListener(v -> {
            Tools.ShowPopUpMenu(CreateArtActivity.this, v, binding.etStyle,
                    binding.tilStyle, R.menu.style_menu);
        });

        binding.tilStyle.setEndIconOnClickListener(v -> {
            Tools.ShowPopUpMenu(CreateArtActivity.this, v, binding.etStyle,
                    binding.tilStyle, R.menu.style_menu);
        });

        binding.etResolution.setOnClickListener(v -> {
            Tools.ShowPopUpMenu(CreateArtActivity.this, v, binding.etResolution,
                    binding.tilResolution, R.menu.resolution);
        });

        binding.tilResolution.setEndIconOnClickListener(v -> {
            Tools.ShowPopUpMenu(CreateArtActivity.this, v, binding.etResolution,
                    binding.tilResolution, R.menu.resolution);
        });

        binding.btnGenerate.setOnClickListener(v -> {
            if (!connectivity.isConnected()) {
                dialogMsg.showErrorDialog("Please Connect Internet", "Ok");
                dialogMsg.show();
                return;
            }
            binding.tilInstruction.setErrorEnabled(false);
            if (validate()) {
                if (InterstitialAdManager.isLoaded() && prefManager().getInt(Constants.CLICK) >=
                        Integer.valueOf(prefManager().getString(Constants.INTERSTITIAL_AD_CLICK))) {
                    prefManager().setInt(Constants.CLICK, 0);
                    InterstitialAdManager.showAds();
                } else {
                    prefManager().setInt(Constants.CLICK, prefManager().getInt(Constants.CLICK) + 1);
                    startGenerating();
                }
            }
        });
        binding.btnClear.setOnClickListener(v -> {
            binding.etInstruction.setText("");
            binding.etResolution.setText("[256x256] Small Image");
            binding.etStyle.setText("None");
            binding.etMedium.setText("None");
            binding.etNoImage.setText("1");
        });

        binding.etInstruction.setOnClickListener(v -> {
            binding.tilInstruction.setErrorEnabled(false);
        });

        magicArtAdapter = new MagicArtAdapter(this, magicArt -> {
            previewDialog.showDialog(magicArt);
        }, 3, getResources().getDimension(com.intuit.sdp.R.dimen._5sdp));
        binding.rvMagic.setAdapter(magicArtAdapter);

    }

    private void startGenerating() {

        String description = binding.etInstruction.getText().toString();
        String style = binding.etStyle.getText().toString().equals("None") ? "" : binding.etStyle.getText().toString();
        String medium = binding.etMedium.getText().toString().equals("None") ? "" : binding.etMedium.getText().toString();
        String noOfImage = binding.etNoImage.getText().toString();

        String resolution = "";
        if (binding.etResolution.getText().toString().equals("[256x256] Small Image")) {
            resolution = "256x256";
        } else if (binding.etResolution.getText().toString().equals("[512x512] Medium Image")) {
            resolution = "512x512";
        } else {
            resolution = "1024x1024";
        }

        dialogMsg.showLoadingDialog();
        magicArtViewModel.setGeneratedDataObj(prefManager().getInt(Constants.USER_ID),
                description, style, medium, noOfImage, resolution);
    }

    private boolean validate() {
        if (binding.etInstruction.getText().toString().isEmpty()) {
            binding.tilInstruction.setError("Instruction is required");
            binding.tilInstruction.setErrorEnabled(true);
            binding.etInstruction.requestFocus();
            return false;
        }else if (Integer.parseInt(binding.etNoImage.getText().toString()) > availableImage) {
            Util.showToast(this, "You Have Insufficient Image Credit");
            return false;
        }

        return true;
    }

    @Override
    public void onDownload(ItemMagicArt itemMagicArt) {
        previewDialog.dialog.dismiss();
        prgDialog.show();
        String name = "MagicArt_" + System.currentTimeMillis();
        LoadDownload loadLogo = new LoadDownload(itemMagicArt.image, getFilePath().getAbsolutePath(), name);
        loadLogo.execute();

    }

    @Override
    public void onDelete(ItemMagicArt itemMagicArt) {
        previewDialog.dialog.dismiss();
        prgDialog.show();
        magicArtViewModel.deleteArt(itemMagicArt.id).observe(this, result -> {

            if (result != null) {
                switch (result.status) {
                    case SUCCESS:

                        prgDialog.cancel();
                        //add offer text
                        Util.showToast(this, "Successfully Delete");
                        break;

                    case ERROR:
                        prgDialog.cancel();
                        Util.showToast(this, "Fail to Delete");
                        break;
                }
            }
        });
    }

    private File getFilePath() {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS
        ), "/" + getResources().getString(R.string.app_name));
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Util.showLog("Can't create directory to save image.");
                Toast.makeText(this,
                        "Directory Error",
                        Toast.LENGTH_LONG).show();
            }
        }
        return file;
    }

    @Override
    public void onAdFailedToLoad() {

    }

    @Override
    public void onAdDismissed() {
        startGenerating();
    }

    private class LoadDownload extends AsyncTask<String, String, String> {

        private String urls, directory, name;
        Bitmap bitmap2;

        public LoadDownload(String urls, String directory, String name) {
            this.urls = urls;
            this.directory = directory;
            this.name = name;
        }

        @Override
        protected String doInBackground(String... strings) {
            InputStream inputStream;
            try {
                inputStream = new java.net.URL(urls).openStream();
                bitmap2 = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
                prgDialog.dismiss();
            }
            return "0";
        }

        @SuppressLint("WrongThread")
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            File file = new File(directory);

            if (!file.exists()) {
                file.mkdirs();
            }
            try {
                String filePath = directory + "/" + name + ".png";
                FileOutputStream fileOutputStream = new FileOutputStream(filePath);
                bitmap2.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                fileOutputStream.close();
                MediaScannerConnection.scanFile(CreateArtActivity.this, new String[]{filePath},
                        (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String str, Uri uri) {
                                Util.showLog("ExternalStorage " + "Scanned " + str + ":");
                                StringBuilder sb = new StringBuilder();
                                sb.append("-> uri=");
                                sb.append(uri);
                                sb.append("-> FILE=");
                                sb.append(filePath);
                            }
                        });
                prgDialog.dismiss();
                Util.showToast(CreateArtActivity.this, "Successfully Download");
            } catch (Exception e) {
                e.printStackTrace();
                Util.showErrorLog(e.getMessage(), e);
                Util.showToast(CreateArtActivity.this, e.getMessage());
                prgDialog.dismiss();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!InterstitialAdManager.isLoaded()) {
            InterstitialAdManager.LoadAds();
        }
    }
}