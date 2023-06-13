package com.drewapps.ai.repository;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.drewapps.ai.Config;
import com.drewapps.ai.api.ApiClient;
import com.drewapps.ai.api.ApiResponse;
import com.drewapps.ai.api.common.NetworkBoundResource;
import com.drewapps.ai.api.common.common.Resource;
import com.drewapps.ai.database.AppDatabase;
import com.drewapps.ai.database.SupportDao;
import com.drewapps.ai.items.ItemSupportReq;
import com.drewapps.ai.items.SupportDetails;
import com.drewapps.ai.utils.AbsentLiveData;
import com.drewapps.ai.utils.Util;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class SupportRepository {

    Application application;
    AppDatabase db;
    SupportDao supportDao;

    public SupportRepository(Application application) {
        this.application = application;
        db = AppDatabase.getInstance(application);
        supportDao = db.getSupportDao();
    }

    public LiveData<Resource<List<ItemSupportReq>>> getAll(String apiKey, Integer userId){
        return new NetworkBoundResource<List<ItemSupportReq>, List<ItemSupportReq>>() {
            @Override
            protected void saveCallResult(@NonNull List<ItemSupportReq> item) {
                try {
                    try {
                        db.runInTransaction(() -> {
                            supportDao.deleteAll();
                            supportDao.insertAll(item);
                        });
                    } catch (Exception ex) {
                        Util.showErrorLog("Error at ", ex);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<ItemSupportReq> data) {
                return Config.IS_CONNECTED;
            }

            @NonNull
            @Override
            protected LiveData<List<ItemSupportReq>> loadFromDb() {
                return supportDao.getAllSupportReq();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<ItemSupportReq>>> createCall() {
                return ApiClient.getApiService().getSupportReqList(apiKey, userId);
            }
        }.asLiveData();
    }

    public LiveData<Resource<ItemSupportReq>> createSupportReq(String apiKey, Context context, String filePath,
                                                               Uri uri, String userId,
                                                               String subject, String category,
                                                               String priority, String massage,
                                                               ContentResolver contentResolver) {
        MultipartBody.Part body = null;
        RequestBody fullName = null;
        if (filePath!=null && !filePath.equals("")) {
            File file = new File(filePath);
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

            // MultipartBody.Part is used to send also the actual file news_title
            body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

            fullName =
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), file.getName());
        }
        RequestBody useIdRB =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), userId);

        RequestBody subjectRB =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), subject);

        RequestBody categoryRB =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), category);

        RequestBody priorityRB =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), priority);


        RequestBody massageRB =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), massage);

        MultipartBody.Part finalBody = body;
        RequestBody finalFullName = fullName;

        return new NetworkBoundResource<ItemSupportReq, ItemSupportReq>() {

            private ItemSupportReq resultsDb;
            String user_id = "";
            @Override
            protected void saveCallResult(@NonNull ItemSupportReq item) {
                try {
                    db.runInTransaction(() -> {
                        supportDao.insert(item);
                    });
                } catch (Exception ex) {
                    Util.showErrorLog("Error at ", ex);
                }

                resultsDb = item;
            }

            @Override
            protected boolean shouldFetch(@Nullable ItemSupportReq data) {
                return Config.IS_CONNECTED;
            }

            @NonNull
            @Override
            protected LiveData<ItemSupportReq> loadFromDb() {
                if (resultsDb == null) {
                    return AbsentLiveData.create();
                } else {
                    return new LiveData<ItemSupportReq>() {
                        @Override
                        protected void onActive() {
                            super.onActive();
                            setValue(resultsDb);
                        }
                    };
                }
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<ItemSupportReq>> createCall() {
                return ApiClient.getApiService().createSupport(apiKey, finalFullName, finalBody,
                        useIdRB, subjectRB, categoryRB, priorityRB, massageRB);
            }
        }.asLiveData();

    }

    public LiveData<Resource<SupportDetails>> getSupportDetails(String apiKey, String ticketId){
        return new NetworkBoundResource<SupportDetails, SupportDetails>() {
            @Override
            protected void saveCallResult(@NonNull SupportDetails item) {
                try {
                    try {
                        db.runInTransaction(() -> {
                            supportDao.deleteDetail(ticketId);
                            supportDao.insertDetail(item);
                        });
                    } catch (Exception ex) {
                        Util.showErrorLog("Error at ", ex);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable SupportDetails data) {
                return Config.IS_CONNECTED;
            }

            @NonNull
            @Override
            protected LiveData<SupportDetails> loadFromDb() {
                return supportDao.getDetail(ticketId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<SupportDetails>> createCall() {
                return ApiClient.getApiService().getSupportDetails(apiKey, ticketId);
            }
        }.asLiveData();
    }

    public LiveData<Resource<SupportDetails>> sendMessage(String apiKey, Context context, String filePath,
                                                               Uri uri, String userId,
                                                               String message, String ticketId,
                                                               ContentResolver contentResolver) {
        MultipartBody.Part body = null;
        RequestBody fullName = null;
        if (filePath!=null && !filePath.equals("")) {
            File file = new File(filePath);
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

            // MultipartBody.Part is used to send also the actual file news_title
            body = MultipartBody.Part.createFormData("attachment", file.getName(), requestFile);

            fullName =
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), file.getName());
        }
        RequestBody useIdRB =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), userId);

        RequestBody massageRB =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), message);

        RequestBody ticketRB =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), ticketId);


        MultipartBody.Part finalBody = body;
        RequestBody finalFullName = fullName;

        return new NetworkBoundResource<SupportDetails, SupportDetails>() {

            private SupportDetails resultsDb;
            String user_id = "";
            @Override
            protected void saveCallResult(@NonNull SupportDetails item) {
                try {
                    db.runInTransaction(() -> {
                        supportDao.update(item.message, item.ticketId, item.status);
                    });
                } catch (Exception ex) {
                    Util.showErrorLog("Error at ", ex);
                }

                resultsDb = item;
            }

            @Override
            protected boolean shouldFetch(@Nullable SupportDetails data) {
                return Config.IS_CONNECTED;
            }

            @NonNull
            @Override
            protected LiveData<SupportDetails> loadFromDb() {
                if (resultsDb == null) {
                    return AbsentLiveData.create();
                } else {
                    return new LiveData<SupportDetails>() {
                        @Override
                        protected void onActive() {
                            super.onActive();
                            setValue(resultsDb);
                        }
                    };
                }
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<SupportDetails>> createCall() {
                return ApiClient.getApiService().sendMessage(apiKey, finalFullName, finalBody,
                        useIdRB, massageRB, ticketRB);
            }
        }.asLiveData();

    }
}
