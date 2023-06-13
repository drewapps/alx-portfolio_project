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
import com.drewapps.ai.database.MagicArtDao;
import com.drewapps.ai.database.UserDataDao;
import com.drewapps.ai.items.ItemGenMagicArt;
import com.drewapps.ai.items.ItemMagicArt;
import com.drewapps.ai.utils.Util;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;

public class MagicArtRepository {

    Application application;
    AppDatabase db;
    MagicArtDao magicArtDao;
    UserDataDao userDataDao;

    public MagicArtRepository(Application application) {
        this.application = application;
        db = AppDatabase.getInstance(application);
        magicArtDao = db.getMagicArtDao();
        userDataDao = db.getUserDataDao();
    }

    public LiveData<Resource<List<ItemMagicArt>>> getAllMagicArt(String apiKey, Integer userId){
        return new NetworkBoundResource<List<ItemMagicArt>, List<ItemMagicArt>>() {
            @Override
            protected void saveCallResult(@NonNull List<ItemMagicArt> item) {

                try {
                    db.runInTransaction(() -> {
                        magicArtDao.deleteAll();
                        magicArtDao.insertAll(item);
                    });
                } catch (Exception ex) {
                    Util.showErrorLog("Error at ", ex);
                }

            }

            @Override
            protected boolean shouldFetch(@Nullable List<ItemMagicArt> data) {
                return Config.IS_CONNECTED;
            }

            @NonNull
            @Override
            protected LiveData<List<ItemMagicArt>> loadFromDb() {
                return magicArtDao.getAllMagicArt();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<ItemMagicArt>>> createCall() {
                return ApiClient.getApiService().getMagicArt(apiKey, userId);
            }
        }.asLiveData();
    }

    public LiveData<Resource<List<ItemGenMagicArt>>> createMagicArt(String apiKey, Integer userId, String description,
                                                                    String style, String medium, String noOfImage, String resolution){
        return new NetworkBoundResource<List<ItemGenMagicArt>, List<ItemGenMagicArt>>() {
            @Override
            protected void saveCallResult(@NonNull List<ItemGenMagicArt> item) {
                try {
                    db.runInTransaction(() -> {
                        magicArtDao.deleteGenAll();
                        magicArtDao.insertGenAll(item);
                        userDataDao.updateAvailableImage(item.size());
                        for(ItemGenMagicArt itemGen : item){
                            magicArtDao.insert(new ItemMagicArt(itemGen.id, itemGen.image, itemGen.querys, itemGen.resolution));
                        }
                    });
                } catch (Exception ex) {
                    Util.showErrorLog("Error at ", ex);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<ItemGenMagicArt> data) {
                return Config.IS_CONNECTED;
            }

            @NonNull
            @Override
            protected LiveData<List<ItemGenMagicArt>> loadFromDb() {
                return magicArtDao.getAllGenMagicArt();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<ItemGenMagicArt>>> createCall() {
                return ApiClient.getApiService().createMagicArt(apiKey, userId, description, style, medium, noOfImage, resolution);
            }
        }.asLiveData();
    }

    public LiveData<Resource<Boolean>> deleteArt(String apiKey, Integer artId) {

        final MutableLiveData<Resource<Boolean>> statusLiveData = new MutableLiveData<>();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {

            Response<ApiStatus> response;

            try {
                response = ApiClient.getApiService().deleteArt(apiKey, artId).execute();

                if (response.isSuccessful()) {
                    magicArtDao.deleteById(artId);
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
