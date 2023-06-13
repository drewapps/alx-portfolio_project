package com.drewapps.ai.ui.fragment;

import static android.view.View.GONE;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.drewapps.ai.AAChartCoreLib.AAChartCreator.AAChartModel;
import com.drewapps.ai.AAChartCoreLib.AAChartCreator.AAMoveOverEventMessageModel;
import com.drewapps.ai.AAChartCoreLib.AAChartCreator.AASeriesElement;
import com.drewapps.ai.AAChartCoreLib.AAChartEnum.AAChartType;
import com.drewapps.ai.AAChartView;
import com.drewapps.ai.MyApplication;
import com.drewapps.ai.adapters.AdapterTemplate;
import com.drewapps.ai.binding.GlideBinding;
import com.drewapps.ai.databinding.FragmentDashboardBinding;
import com.drewapps.ai.items.UserData;
import com.drewapps.ai.ui.activity.FavoriteActivity;
import com.drewapps.ai.ui.activity.PremiumActivity;
import com.drewapps.ai.ui.activity.SettingActivity;
import com.drewapps.ai.utils.Connectivity;
import com.drewapps.ai.utils.Constants;
import com.drewapps.ai.utils.Tools;
import com.drewapps.ai.utils.Util;
import com.drewapps.ai.viewmodel.UserDataViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DashboardFragment extends Fragment implements AAChartView.AAChartViewCallBack {

    public DashboardFragment() {
        // Required empty public constructor
    }

    FragmentDashboardBinding binding;
    UserDataViewModel userDataViewModel;
    Connectivity connectivity;
    AdapterTemplate adapterTemplate;
    public AAChartModel aaChartModel, aaChartModel1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDashboardBinding.inflate(getLayoutInflater());

        connectivity = new Connectivity(getContext());

        initUi();
        initViewModel();

        return binding.getRoot();
    }

    private void initViewModel() {
        userDataViewModel = new ViewModelProvider(getActivity()).get(UserDataViewModel.class);
        userDataViewModel.setUserObj(MyApplication.prefManager().getInt(Constants.USER_ID));
        userDataViewModel.getUserData().observe(getActivity(), resource -> {
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

    private void setDataToUi(UserData data) {

        adapterTemplate.setTemplateData(data.favoriteTemplate);

        GlideBinding.bindImage(binding.riUserImage, data.profileImage);
        binding.tvAvailableWords.setText(Util.numberFormat(data.availableWord));
        binding.tvAvailableImages.setText(Util.numberFormat(data.availableImage));
        binding.tvUserName.setText(data.userName);
        binding.tvEmail.setText(data.emailId);
        binding.tvCreatedDoc.setText(Util.numberFormat(data.documentCreated));
        binding.tvCreatedWord.setText(Util.numberFormat(data.wordUsed));
        binding.tvCreatedImage.setText(Util.numberFormat(data.imageCreated));
        binding.tvCreatedTemplate.setText("" + data.templateUsed);

        MyApplication.prefManager().setBoolean(Constants.SUBSCRIBED, data.isSubscribe !=null ? data.isSubscribe : false);

        Constants.IS_SUBSCRIBED = MyApplication.prefManager().getBoolean(Constants.SUBSCRIBED);

        showVisibility(true);

        binding.charView.callBack = this;
        aaChartModel = configureWordChart(data.userMonthlyUsageWord);
        aaChartModel.chartType = AAChartType.Area;
        binding.charView.aa_drawChartWithChartModel(aaChartModel);

        binding.charViewImg.callBack = this;
        aaChartModel1 = configureImageChart(data.userMonthlyUsageImage);
        aaChartModel1.chartType = AAChartType.Area;
        binding.charViewImg.aa_drawChartWithChartModel(aaChartModel1);

        binding.tvPremium.setOnClickListener(v->{
            getContext().startActivity(new Intent(getContext(), PremiumActivity.class));
        });

        binding.tvFaveViewAll.setOnClickListener(v->{
            startActivity(new Intent(getContext(), FavoriteActivity.class));
        });

        binding.ivSettings.setOnClickListener(v->{
            startActivity(new Intent(getContext(), SettingActivity.class));
        });
    }

    public void showVisibility(boolean isVisible) {
        binding.swipeRefresh.setRefreshing(false);
        if (isVisible) {
            binding.nsMain.setVisibility(View.VISIBLE);
            binding.animationView.setVisibility(GONE);
            binding.shimmerViewContainer.stopShimmer();
            binding.shimmerViewContainer.setVisibility(GONE);
        } else {
            binding.nsMain.setVisibility(GONE);
            binding.shimmerViewContainer.stopShimmer();
            binding.animationView.setVisibility(View.VISIBLE);
            binding.shimmerViewContainer.setVisibility(GONE);
        }
    }

    private void initUi() {
        adapterTemplate = new AdapterTemplate(getContext(), template -> {
            Tools.gotoTemplate(getContext(), template);
        }, template -> {

        });

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.rvFavorite.setLayoutManager(llm);
        binding.rvFavorite.setAdapter(adapterTemplate);

        binding.swipeRefresh.setOnRefreshListener(() -> {
            binding.nsMain.setVisibility(View.GONE);
            binding.shimmerViewContainer.startShimmer();
            binding.shimmerViewContainer.setVisibility(View.VISIBLE);
            userDataViewModel.setUserObj(MyApplication.prefManager().getInt(Constants.USER_ID));
        });


    }

    public static AAChartModel configureWordChart(List<Integer> userMonthlyUsageWord) {

        String[] xData = new String[userMonthlyUsageWord.size()];
        Object[] yData = new Object[userMonthlyUsageWord.size()];

        DateFormat dateFormat = new SimpleDateFormat("MMM");
        Date date = new Date();
        String month = dateFormat.format(date);

        for (int i = 0; i < userMonthlyUsageWord.size(); i++) {
            xData[i] = "" + (i + 1);
            yData[i] = userMonthlyUsageWord.get(i);
        }

        AASeriesElement element1 = new AASeriesElement()
                .name("Words")
                .data(yData);

        return configureBasicOptions()
                .chartType(AAChartType.Area)
                .gradientColorEnable(true)
                .yAxisGridLineWidth(1)
                .colorsTheme(new String[]{"#ffc069"})
                .categories(xData)
                .series(new AASeriesElement[]{element1});
    }

    public static AAChartModel configureImageChart(List<Integer> userMonthlyUsageImage) {

        String[] xData = new String[userMonthlyUsageImage.size()];
        Object[] yData = new Object[userMonthlyUsageImage.size()];

        DateFormat dateFormat = new SimpleDateFormat("MMM");
        Date date = new Date();
        String month = dateFormat.format(date);

        for (int i = 0; i < userMonthlyUsageImage.size(); i++) {
            xData[i] = "" + (i + 1);
            yData[i] = userMonthlyUsageImage.get(i);
        }

        AASeriesElement element1 = new AASeriesElement()
                .name("Images")
                .data(yData);

        return configureBasicOptions()
                .chartType(AAChartType.Area)
                .colorsTheme(new String[]{"#7dffc0"})
                .yAxisGridLineWidth(1)
                .categories(xData)
                .series(new AASeriesElement[]{element1});
    }

    public static AAChartModel configureBasicOptions() {
        return new AAChartModel()
                .backgroundColor("#001320")
                .dataLabelsEnabled(false)
                .yAxisGridLineWidth(0)
                .axesTextColor("#FFFFFF")
                .touchEventEnabled(true);
    }

    @Override
    public void chartViewDidFinishLoad(AAChartView aaChartView) {

    }

    @Override
    public void chartViewMoveOverEventMessage(AAChartView aaChartView, AAMoveOverEventMessageModel messageModel) {

    }
}