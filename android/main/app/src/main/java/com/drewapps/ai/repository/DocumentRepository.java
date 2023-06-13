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
import com.drewapps.ai.database.DocumentDao;
import com.drewapps.ai.items.Document;
import com.drewapps.ai.utils.AbsentLiveData;
import com.drewapps.ai.utils.Util;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;

public class DocumentRepository {

    Application application;
    AppDatabase db;
    DocumentDao documentDao;
    public DocumentRepository(Application application) {
        this.application = application;
        db = AppDatabase.getInstance(application);
        documentDao = db.getDocumentDao();
    }

    public LiveData<Resource<ApiStatus>> updateDocument(String apiKey, Integer documentId, String documentTitle, String documentContent) {

        return new NetworkBoundResource<ApiStatus, ApiStatus>() {
            private ApiStatus resultsDb;

            @Override
            protected void saveCallResult(@NonNull ApiStatus item) {

                try {
                    db.runInTransaction(() -> {
                        documentDao.updateDocument(documentId, documentTitle, documentContent);
                    });
                } catch (Exception ex) {
                    Util.showErrorLog("Error at ", ex);
                }
                resultsDb = item;
            }

            @Override
            protected boolean shouldFetch(@Nullable ApiStatus data) {
                return Config.IS_CONNECTED;
            }

            @NonNull
            @Override
            protected LiveData<ApiStatus> loadFromDb() {
                if (resultsDb == null) {
                    return AbsentLiveData.create();
                }

                return new LiveData<ApiStatus>() {
                    @Override
                    protected void onActive() {
                        super.onActive();
                        setValue(resultsDb);
                    }
                };
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<ApiStatus>> createCall() {
                return ApiClient.getApiService().updateDocument(apiKey, documentId, documentTitle, documentContent);
            }
        }.asLiveData();
    }

    public LiveData<Resource<List<Document>>> getAllDocuments(String apiKey, Integer user_id) {
        return new NetworkBoundResource<List<Document>, List<Document>>() {
            @Override
            protected void saveCallResult(@NonNull List<Document> item) {
                try {
                    db.runInTransaction(() -> {
                        documentDao.deleteAll();
                        documentDao.insertAll(item);
                    });
                } catch (Exception ex) {
                    Util.showErrorLog("Error at ", ex);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Document> data) {
                return Config.IS_CONNECTED;
            }

            @NonNull
            @Override
            protected LiveData<List<Document>> loadFromDb() {
                return documentDao.getAllDocuments();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Document>>> createCall() {
                return ApiClient.getApiService().getAllDocuments(apiKey, user_id);
            }
        }.asLiveData();
    }

    public LiveData<Resource<Boolean>> deleteDocument(String api_key, Integer document_id) {
        final MutableLiveData<Resource<Boolean>> statusLiveData = new MutableLiveData<>();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {

            Response<ApiStatus> response;

            try {
                response = ApiClient.getApiService().deleteDocument(api_key, document_id).execute();

                if (response.isSuccessful()) {
                    documentDao.deleteById(document_id);
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
}
