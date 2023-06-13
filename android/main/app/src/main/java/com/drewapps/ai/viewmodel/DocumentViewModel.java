package com.drewapps.ai.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.drewapps.ai.MyApplication;
import com.drewapps.ai.api.ApiStatus;
import com.drewapps.ai.api.common.common.Resource;
import com.drewapps.ai.items.Document;
import com.drewapps.ai.repository.DocumentRepository;
import com.drewapps.ai.utils.AbsentLiveData;
import com.drewapps.ai.utils.Constants;

import java.util.List;

public class DocumentViewModel extends AndroidViewModel {

    DocumentRepository repository;

    private LiveData<Resource<ApiStatus>> updateData;
    private MutableLiveData<TmpDataHolder> updateDataObj = new MutableLiveData<>();

    private LiveData<Resource<List<Document>>> documentData;
    private MutableLiveData<Integer> documentDataObj = new MutableLiveData<>();

    private MutableLiveData<Integer> deleteObj = new MutableLiveData<>();

    public DocumentViewModel(@NonNull Application application) {
        super(application);
        repository = new DocumentRepository(application);

        updateData = Transformations.switchMap(updateDataObj, obj->{
            if(obj==null){
                return AbsentLiveData.create();
            }
            return repository.updateDocument(MyApplication.prefManager().getString(Constants.api_key), obj.documentId, obj.documentTitle, obj.documentContent);
        });

        documentData = Transformations.switchMap(documentDataObj, obj->{
            if(obj==null){
                return AbsentLiveData.create();
            }
            return repository.getAllDocuments(MyApplication.prefManager().getString(Constants.api_key), obj);
        });
    }

    public LiveData<Resource<Boolean>> deleteDocument(Integer documentId) {
        deleteObj.setValue(documentId);
        return Transformations.switchMap(deleteObj, obj->{
            if(obj==null){
                return AbsentLiveData.create();
            }
            return repository.deleteDocument(MyApplication.prefManager().getString(Constants.api_key), obj);
        });
    }

    class TmpDataHolder {

        public Integer documentId = 0;
        public String documentTitle = "";
        public String documentContent = "";

    }

    public LiveData<Resource<ApiStatus>> getUpdateData(){
        return updateData;
    }
    public void setUpdateData(Integer documentId, String documentTitle, String documentContent){
        TmpDataHolder tmpDataHolder = new TmpDataHolder();
        tmpDataHolder.documentId = documentId;
        tmpDataHolder.documentTitle = documentTitle;
        tmpDataHolder.documentContent = documentContent;
        updateDataObj.postValue(tmpDataHolder);
    }

    public LiveData<Resource<List<Document>>> getAllDocuments(){
        return documentData;
    }
    public void setDocumentDataObj(Integer userID){
        documentDataObj.setValue(userID);
    }
}
