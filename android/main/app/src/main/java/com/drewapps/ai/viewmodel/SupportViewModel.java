package com.drewapps.ai.viewmodel;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.drewapps.ai.MyApplication;
import com.drewapps.ai.api.common.common.Resource;
import com.drewapps.ai.items.ItemSupportReq;
import com.drewapps.ai.items.SupportDetails;
import com.drewapps.ai.repository.SupportRepository;
import com.drewapps.ai.utils.AbsentLiveData;
import com.drewapps.ai.utils.Constants;

import java.util.List;

public class SupportViewModel extends AndroidViewModel {

    SupportRepository repository;
    MutableLiveData<Integer> supportObj = new MutableLiveData<>();
    MutableLiveData<String> createObj = new MutableLiveData<>();
    MutableLiveData<String> sendObj = new MutableLiveData<>();
    MutableLiveData<String> detailObj = new MutableLiveData<>();

    LiveData<Resource<SupportDetails>> detailData;

    public SupportViewModel(@NonNull Application application) {
        super(application);
        repository = new SupportRepository(application);

        detailData = Transformations.switchMap(detailObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return repository.getSupportDetails(MyApplication.prefManager().getString(Constants.api_key), obj);
        });
    }

    public LiveData<Resource<List<ItemSupportReq>>> getAll(Integer userId) {
        supportObj.setValue(userId);
        return Transformations.switchMap(supportObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return repository.getAll(MyApplication.prefManager().getString(Constants.api_key), obj);
        });
    }

    public LiveData<Resource<ItemSupportReq>> createSupportReq(Context context, String filePath, Uri uri, Integer userId,
                                                               String subject, String category,
                                                               String priority, String massage,
                                                               ContentResolver contentResolver) {

        createObj.setValue("IQ");

        return Transformations.switchMap(createObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return repository.createSupportReq(MyApplication.prefManager().getString(Constants.api_key),
                    context, filePath, uri, String.valueOf(userId), subject, category, priority, massage, contentResolver);
        });

    }

    public LiveData<Resource<SupportDetails>> sendMessage(Context context, String filePath,
                                                          Uri uri, Integer userId,
                                                          String message, String ticketId,
                                                          ContentResolver contentResolver) {
        sendObj.setValue("IQ");

        return Transformations.switchMap(sendObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return repository.sendMessage(MyApplication.prefManager().getString(Constants.api_key),
                    context, filePath, uri, String.valueOf(userId), message, ticketId, contentResolver);
        });
    }

    public LiveData<Resource<SupportDetails>> getSupportDetails() {
        return detailData;
    }

    public void setDetails(String ticketId) {
        detailObj.setValue(ticketId);
    }
}
