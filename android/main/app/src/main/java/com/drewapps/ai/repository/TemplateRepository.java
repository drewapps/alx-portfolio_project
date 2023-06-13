package com.drewapps.ai.repository;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.drewapps.ai.Config;
import com.drewapps.ai.api.ApiClient;
import com.drewapps.ai.api.ApiResponse;
import com.drewapps.ai.api.ApiStatus;
import com.drewapps.ai.api.common.NetworkBoundResource;
import com.drewapps.ai.api.common.common.Resource;
import com.drewapps.ai.database.AppDatabase;
import com.drewapps.ai.database.TemplateDao;
import com.drewapps.ai.items.AdapTemplateData;
import com.drewapps.ai.items.AllTemplateData;
import com.drewapps.ai.items.Template;
import com.drewapps.ai.utils.Util;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;

public class TemplateRepository {

    Application application;
    AppDatabase db;
    TemplateDao templateDao;

    public TemplateRepository(Application application) {
        this.application = application;
        db = AppDatabase.getInstance(application);
        templateDao = db.getTemplateDao();
    }

    public LiveData<Resource<AllTemplateData>> getAllTemplateData(String api_key, Integer userId){
     return new NetworkBoundResource<AllTemplateData, AllTemplateData>() {
         @Override
         protected void saveCallResult(@NonNull AllTemplateData item) {
             try {
                 db.runInTransaction(() -> {
                     templateDao.deleteAllTemplate();
                     templateDao.insertAllTemplate(item);

                     for(AdapTemplateData adapTemplateData : item.template){
                         templateDao.deleteTemplateByCategory(adapTemplateData.category);
                         templateDao.insertTemplate(adapTemplateData.templateList);
                     }

                 });
             } catch (Exception ex) {
                 Util.showErrorLog("Error at ", ex);
             }
         }

         @Override
         protected boolean shouldFetch(@Nullable AllTemplateData data) {
             return Config.IS_CONNECTED;
         }

         @NonNull
         @Override
         protected LiveData<AllTemplateData> loadFromDb() {
             return templateDao.getAllTemplate();
         }

         @NonNull
         @Override
         protected LiveData<ApiResponse<AllTemplateData>> createCall() {
             return ApiClient.getApiService().getAllTemplateData(api_key, userId);
         }
     }.asLiveData();
    }

    public List<Template> getTemplateByCategory(String category){
        return templateDao.getTemplateByCategory(category);
    }

    public LiveData<Resource<Boolean>> favoriteTemplate(String apiKey, Integer userID, Integer templateId, boolean isFavorite) {
        final MutableLiveData<Resource<Boolean>> statusLiveData = new MutableLiveData<>();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {

            Response<ApiStatus> response;

            try {
                response = ApiClient.getApiService().favoriteTemplate(apiKey, userID, templateId).execute();

                if (response.isSuccessful()) {
                    templateDao.favorite(templateId, isFavorite);
                    statusLiveData.postValue(Resource.success(true));
                } else {
                    statusLiveData.postValue(Resource.error("error", false));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            handler.post(() -> {
                //UI Thread work here

            });
        });

        return statusLiveData;
    }

    public LiveData<List<Template>> getSearchTemplate(String query) {
        return templateDao.getSearchTemplate(query);
    }
}
