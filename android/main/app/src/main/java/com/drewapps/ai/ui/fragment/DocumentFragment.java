package com.drewapps.ai.ui.fragment;

import static android.view.View.GONE;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.drewapps.ai.MyApplication;
import com.drewapps.ai.R;
import com.drewapps.ai.adapters.DocumentAdapter;
import com.drewapps.ai.databinding.FragmentDocumentBinding;
import com.drewapps.ai.items.Document;
import com.drewapps.ai.ui.activity.EditorActivity;
import com.drewapps.ai.ui.dialogs.DialogMsg;
import com.drewapps.ai.utils.Constants;
import com.drewapps.ai.utils.Util;
import com.drewapps.ai.viewmodel.DocumentViewModel;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import io.github.mddanishansari.html_to_pdf.HtmlToPdfConvertor;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class DocumentFragment extends Fragment {

    public DocumentFragment() {
        // Required empty public constructor
    }

    FragmentDocumentBinding binding;
    DocumentViewModel documentViewModel;
    DocumentAdapter documentAdapter;

    DialogMsg dialogMsg;
    ProgressDialog prgDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDocumentBinding.inflate(getLayoutInflater());

        prgDialog = new ProgressDialog(getActivity());
        prgDialog.setMessage("Loading...");
        prgDialog.setCancelable(false);

        setUpUI();
        setUpViewModel();

        return binding.getRoot();
    }

    private void setUpViewModel() {
        documentViewModel = new ViewModelProvider(getActivity()).get(DocumentViewModel.class);

        documentViewModel.setDocumentDataObj(MyApplication.prefManager().getInt(Constants.USER_ID));
        documentViewModel.getAllDocuments().observe(getActivity(), resource -> {
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
        binding.swipeRefresh.setRefreshing(false);
        if (isVisible) {
            binding.textView2.setVisibility(View.VISIBLE);
            binding.rvDocument.setVisibility(View.VISIBLE);
            binding.shimmerViewContainer.stopShimmer();
            binding.shimmerViewContainer.setVisibility(GONE);
            binding.animationView.setVisibility(View.GONE);
        } else {
            binding.textView2.setVisibility(GONE);
            binding.rvDocument.setVisibility(GONE);
            binding.animationView.setVisibility(View.VISIBLE);
            binding.shimmerViewContainer.stopShimmer();
            binding.shimmerViewContainer.setVisibility(GONE);
        }
    }

    private void setData(List<Document> data) {

        documentAdapter.setDocumentList(data);

    }

    private void setUpUI() {

        dialogMsg = new DialogMsg(getActivity(), false);

        documentAdapter = new DocumentAdapter(getContext(), new DocumentAdapter.OnClick() {
            @Override
            public void onClick(Document document) {
                Intent intent = new Intent(getActivity(), EditorActivity.class);
                intent.putExtra("document", (Serializable) document);
                startActivity(intent);
            }

            @Override
            public void onDownload(Document document) {
                HtmlToPdfConvertor htmlToPdfConvertor = new HtmlToPdfConvertor(getContext());
                htmlToPdfConvertor.setBaseUrl("file:///android_asset/images/");

                String html = "<h2 style=\"text-align: center;\"><strong>" + document.documentName + "</strong></h2>"
                        + document.content;

                htmlToPdfConvertor.convert(new File(getPdfFilePath(), document.documentName + "_" + System.currentTimeMillis() + ".pdf"), html,
                        new Function1<Exception, Unit>() {
                            @Override
                            public Unit invoke(Exception e) {
                                Util.showErrorLog(e.getMessage(), e);
                                Util.showToast(getContext(), "Check logs for error");
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

            @Override
            public void onDelete(Document document) {

                prgDialog.show();
                documentViewModel.deleteDocument(document.documentId).observe(getActivity(), result -> {

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
        });
        binding.rvDocument.setAdapter(documentAdapter);

        binding.swipeRefresh.setOnRefreshListener(() -> {
            binding.rvDocument.setVisibility(GONE);
            binding.shimmerViewContainer.startShimmer();
            binding.shimmerViewContainer.setVisibility(View.VISIBLE);
            documentViewModel.setDocumentDataObj(MyApplication.prefManager().getInt(Constants.USER_ID));
        });
    }

    private void openPdf(File file) {
        try {
            Context context = getContext();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(getContext().getPackageName());
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
                Toast.makeText(getContext(),
                        "Directory Error",
                        Toast.LENGTH_LONG).show();
            }
        }
        return file;
    }
}