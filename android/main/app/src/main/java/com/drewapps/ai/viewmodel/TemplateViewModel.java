package com.drewapps.ai.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.drewapps.ai.MyApplication;
import com.drewapps.ai.api.common.common.Resource;
import com.drewapps.ai.items.AllTemplateData;
import com.drewapps.ai.items.Template;
import com.drewapps.ai.repository.TemplateRepository;
import com.drewapps.ai.utils.AbsentLiveData;
import com.drewapps.ai.utils.Constants;

import java.util.List;

public class TemplateViewModel extends AndroidViewModel {

    TemplateRepository repository;

    private LiveData<Resource<AllTemplateData>> templateData;
    public MutableLiveData<Integer> templateObj = new MutableLiveData<>();
    public MutableLiveData<Integer> favObj = new MutableLiveData<>();
    public MutableLiveData<String> searchObj = new MutableLiveData<>();

    public TemplateViewModel(@NonNull Application application) {
        super(application);
        repository = new TemplateRepository(application);

        templateData = Transformations.switchMap(templateObj, obj->{
            if(obj==null){
                return AbsentLiveData.create();
            }
            return repository.getAllTemplateData(MyApplication.prefManager().getString(Constants.api_key), obj);
        });
    }

    public LiveData<Resource<AllTemplateData>> getAllTemplateData(){
        return templateData;
    }

    public void setTemplateObj(Integer userID){
        templateObj.setValue(userID);
    }

    public List<Template> getTemplateByCategory(String category){
        return repository.getTemplateByCategory(category);
    }

    public LiveData<Resource<Boolean>> favoriteTemplate(Integer templateId, boolean isFavorite) {
        favObj.setValue(templateId);
        return Transformations.switchMap(favObj, obj->{
            if(obj==null){
                return AbsentLiveData.create();
            }
            return repository.favoriteTemplate(MyApplication.prefManager().getString(Constants.api_key),
                    MyApplication.prefManager().getInt(Constants.USER_ID), obj, isFavorite);
        });
    }

    public LiveData<List<Template>> getSearchTemplate(String query) {
        searchObj.setValue(query);
        return Transformations.switchMap(searchObj, obj->{
            if(obj==null){
                return AbsentLiveData.create();
            }
            return repository.getSearchTemplate(obj);
        });
    }
}
