package com.drewapps.ai.ui.fragment;

import static android.view.View.GONE;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.drewapps.ai.Ads.InterstitialAdManager;
import com.drewapps.ai.MyApplication;
import com.drewapps.ai.R;
import com.drewapps.ai.adapters.AdapterAllTemplate;
import com.drewapps.ai.adapters.AdapterCategory;
import com.drewapps.ai.databinding.FragmentTemplateBinding;
import com.drewapps.ai.items.AllTemplateData;
import com.drewapps.ai.items.Template;
import com.drewapps.ai.utils.Connectivity;
import com.drewapps.ai.utils.Constants;
import com.drewapps.ai.utils.PrefManager;
import com.drewapps.ai.utils.Tools;
import com.drewapps.ai.utils.Util;
import com.drewapps.ai.viewmodel.TemplateViewModel;

import java.util.ArrayList;
import java.util.List;

public class TemplateFragment extends Fragment implements InterstitialAdManager.Listener {

    public TemplateFragment() {
        // Required empty public constructor
    }

    FragmentTemplateBinding binding;

    AdapterAllTemplate adapterAllTemplate;
    AdapterCategory adapterCategory;

    AllTemplateData allTemplateData;
    TemplateViewModel templateViewModel;
    Connectivity connectivity;

    ProgressDialog prgDialog;
    Template currentTemplate;
    PrefManager prefManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTemplateBinding.inflate(getLayoutInflater());

        prefManager = new PrefManager(getActivity());
        connectivity = new Connectivity(getContext());
        prgDialog = new ProgressDialog(getActivity());
        prgDialog.setMessage("Loading...");
        prgDialog.setCancelable(false);

        setUpUI();
        initViewModel();
        InterstitialAdManager.Interstitial(getActivity(), this);

        return binding.getRoot();
    }

    private void initViewModel() {
        templateViewModel = new ViewModelProvider(getActivity()).get(TemplateViewModel.class);

        templateViewModel.setTemplateObj(MyApplication.prefManager().getInt(Constants.USER_ID));
        templateViewModel.getAllTemplateData().observe(getActivity(), resource -> {
            if (resource != null) {

                Util.showLog("Got Data" + resource.message + " " + resource.data);

                switch (resource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        if (resource.data != null) {
                            setDataToUi(resource.data);
                            showVisibility(true);
                        } else {
                            showVisibility(false);
                        }
                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server
                        if (resource.data != null) {
                            setDataToUi(resource.data);
                            showVisibility(true);
                        } else {
                            showVisibility(false);
                        }

                        break;
                    case ERROR:
                        // Error State
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
            binding.clMain.setVisibility(View.VISIBLE);
            binding.shimmerViewContainer.stopShimmer();
            binding.shimmerViewContainer.setVisibility(GONE);
            binding.animationView.setVisibility(GONE);
        } else {
            binding.clMain.setVisibility(GONE);
            binding.shimmerViewContainer.stopShimmer();
            binding.shimmerViewContainer.setVisibility(GONE);
            binding.animationView.setVisibility(View.VISIBLE);
        }
    }

    private void setDataToUi(AllTemplateData data) {
        prgDialog.dismiss();
        List<String> catList = new ArrayList<String>();
        catList.add("All");
        catList.addAll(data.category);
        adapterCategory.setCategories(catList);
        allTemplateData = data;
        setTemplateData(data);
    }

    private void setTemplateData(AllTemplateData data) {
        adapterAllTemplate = new AdapterAllTemplate(getContext(), true, category -> {
            loadCategoryWise(category, null);
            adapterCategory.setlectedCategory(category);
        }, template -> {
            favoriteTemplate(template);
        }, template -> {
            currentTemplate = template;
            clickOnTemplate();
        });
        binding.rvMain.setAdapter(adapterAllTemplate);

        adapterAllTemplate.setAllTemplate(data.template);
    }

    private void setUpUI() {
        adapterCategory = new AdapterCategory(getContext(), category -> {

            if (category.equals("All")) {

                setTemplateData(allTemplateData);

            } else {

                loadCategoryWise(category, null);

            }

        });
        binding.rvCategory.setAdapter(adapterCategory);

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                templateViewModel.getSearchTemplate("%" + s.toString() + "%").observe(getActivity(), resource -> {

                    if (resource != null) {

                        if (resource.size() > 0) {
                            loadCategoryWise("", resource);
                        } else {
//                            showVisibility(false);
                        }
                    }

                });
            }
        });

    }

    private void loadCategoryWise(String category, List<Template> list) {
        adapterAllTemplate = new AdapterAllTemplate(getContext(), false, categorys -> {

        }, template -> {
            favoriteTemplate(template);
        }, template -> {
            currentTemplate = template;
            clickOnTemplate();
        });
        binding.rvMain.setAdapter(adapterAllTemplate);

        if (list != null) {
            adapterAllTemplate.setDetailTemplate(list);
        } else {
            adapterAllTemplate.setDetailTemplate(templateViewModel.getTemplateByCategory(category));
        }
    }

    private void clickOnTemplate() {
        if (InterstitialAdManager.isLoaded() && prefManager.getInt(Constants.CLICK) >=
                Integer.valueOf(prefManager.getString(Constants.INTERSTITIAL_AD_CLICK))) {
            prefManager.setInt(Constants.CLICK, 0);
            InterstitialAdManager.showAds();
        } else {
            prefManager.setInt(Constants.CLICK, prefManager.getInt(Constants.CLICK) + 1);
            goNextTemplate();
        }
    }

    private void goNextTemplate() {
        Tools.gotoTemplate(getContext(), currentTemplate);
    }

    private void favoriteTemplate(Template template) {
        prgDialog.show();
        templateViewModel.favoriteTemplate(template.templateId, template.isFavorite ? false : true).observe(getActivity(),
                result -> {

                    if (result != null) {
                        switch (result.status) {
                            case SUCCESS:
                                templateViewModel.setTemplateObj(MyApplication.prefManager().getInt(Constants.USER_ID));
                                //add offer text
                                break;

                            case ERROR:
                                prgDialog.cancel();
                                Util.showToast(getActivity(), "Fail to Favorite");
                                break;
                        }
                    }
                });
    }

    @Override
    public void onAdFailedToLoad() {

    }

    @Override
    public void onAdDismissed() {
        goNextTemplate();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!InterstitialAdManager.isLoaded()) {
            InterstitialAdManager.LoadAds();
        }
    }
}