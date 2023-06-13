package com.drewapps.ai.ui.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.drewapps.ai.MyApplication;
import com.drewapps.ai.R;
import com.drewapps.ai.adapters.MagicArtAdapter;
import com.drewapps.ai.databinding.FragmentMagicBinding;
import com.drewapps.ai.items.ItemMagicArt;
import com.drewapps.ai.ui.activity.CreateArtActivity;
import com.drewapps.ai.ui.dialogs.PreviewDialog;
import com.drewapps.ai.utils.Constants;
import com.drewapps.ai.utils.Util;
import com.drewapps.ai.viewmodel.MagicArtViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MagicFragment extends Fragment implements PreviewDialog.ClickListener {

    FragmentMagicBinding binding;
    MagicArtViewModel magicArtViewModel;
    MagicArtAdapter artAdapter;

    PreviewDialog previewDialog;
    ProgressDialog prgDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentMagicBinding.inflate(getLayoutInflater());
        previewDialog = new PreviewDialog(getActivity(), true, this);
        prgDialog = new ProgressDialog(getActivity());
        prgDialog.setMessage("Loading...");
        prgDialog.setCancelable(false);

        setUpUI();
        initViewModel();
        return binding.getRoot();
    }

    private void initViewModel() {
        magicArtViewModel = new ViewModelProvider(getActivity()).get(MagicArtViewModel.class);
        magicArtViewModel.setMagicDataObj(MyApplication.prefManager().getInt(Constants.USER_ID));
        magicArtViewModel.getMagicData().observe(getActivity(), resource -> {
            if (resource != null) {

                Util.showLog("Got Data" + resource.message + resource.toString());

                switch (resource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        if (resource.data != null) {

                            if (resource.data.size() > 0) {
                                setData(resource.data);
                                showVisibility(true);
                            }
                        } else {
                            showVisibility(false);
                        }
                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server
                        if (resource.data != null && resource.data.size() > 0) {
                            setData(resource.data);
                            showVisibility(true);
                        } else {
                            showVisibility(false);
                        }

                        break;
                    case ERROR:
                        // Error State
                        showVisibility(false);
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
            binding.noData.setVisibility(View.GONE);
            binding.textView2.setVisibility(View.VISIBLE);
            binding.rvMagic.setVisibility(View.VISIBLE);
        } else {
            binding.textView2.setVisibility(View.GONE);
            binding.rvMagic.setVisibility(View.GONE);
            binding.noData.setVisibility(View.VISIBLE);
        }
    }

    private void setData(List<ItemMagicArt> data) {
        artAdapter.setMagicArt(data);
    }

    private void setUpUI() {
        artAdapter = new MagicArtAdapter(getActivity(), magicArt -> {
            previewDialog.showDialog(magicArt);
        }, 3, getResources().getDimension(com.intuit.sdp.R.dimen._3sdp));
        binding.rvMagic.setAdapter(artAdapter);
        binding.btnNewArt.setOnClickListener(v -> {
            getActivity().startActivity(new Intent(getActivity(), CreateArtActivity.class));
        });
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
        magicArtViewModel.deleteArt(itemMagicArt.id).observe(getActivity(), result -> {

            if (result != null) {
                switch (result.status) {
                    case SUCCESS:

                        prgDialog.cancel();
                        //add offer text
                        Util.showToast(getActivity(), "Successfully Delete");
                        break;

                    case ERROR:
                        prgDialog.cancel();
                        Util.showToast(getActivity(), "Fail to Delete");
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
                Toast.makeText(getContext(),
                        "Directory Error",
                        Toast.LENGTH_LONG).show();
            }
        }
        return file;
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
                MediaScannerConnection.scanFile(getContext(), new String[]{filePath},
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
                Util.showToast(getActivity(), "Successfully Download");
            } catch (Exception e) {
                e.printStackTrace();
                Util.showErrorLog(e.getMessage(), e);
                Util.showToast(getActivity(), e.getMessage());
                prgDialog.dismiss();
            }
        }
    }
}