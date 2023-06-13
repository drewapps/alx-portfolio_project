package com.drewapps.ai.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.drewapps.ai.MyApplication;
import com.drewapps.ai.api.common.common.Resource;
import com.drewapps.ai.items.ItemGenMagicArt;
import com.drewapps.ai.items.ItemMagicArt;
import com.drewapps.ai.repository.MagicArtRepository;
import com.drewapps.ai.utils.AbsentLiveData;
import com.drewapps.ai.utils.Constants;

import java.util.List;

public class MagicArtViewModel extends AndroidViewModel {

    MagicArtRepository repository;

    public LiveData<Resource<List<ItemMagicArt>>> magicData;
    public MutableLiveData<Integer> magicDataObj = new MutableLiveData<>();

    public LiveData<Resource<List<ItemGenMagicArt>>> generatedData;

    public MutableLiveData<TmpData> generatedDataObj = new MutableLiveData<>();

    public MutableLiveData<Integer> deleteObj = new MutableLiveData<>();

    public MagicArtViewModel(@NonNull Application application) {
        super(application);

        repository = new MagicArtRepository(application);

        magicData = Transformations.switchMap(magicDataObj, obj->{
            if(obj == null){
                return AbsentLiveData.create();
            }
            return repository.getAllMagicArt(MyApplication.prefManager().getString(Constants.api_key), obj);
        });

        generatedData = Transformations.switchMap(generatedDataObj, obj->{
            if(obj == null){
                return AbsentLiveData.create();
            }
            return repository.createMagicArt(MyApplication.prefManager().getString(Constants.api_key),
                    obj.userId, obj.description, obj.style, obj.medium, obj.noOfImage, obj.resolution);
        });
    }

    public LiveData<Resource<List<ItemMagicArt>>> getMagicData(){
        return magicData;
    }
    public void setMagicDataObj(Integer userId){
        magicDataObj.setValue(userId);
    }

    public LiveData<Resource<Boolean>> deleteArt(Integer magicId) {
        deleteObj.setValue(magicId);
        return Transformations.switchMap(deleteObj, obj->{
            if(obj==null){
                return AbsentLiveData.create();
            }
            return repository.deleteArt(MyApplication.prefManager().getString(Constants.api_key), obj);
        });
    }

    public class TmpData {
        public Integer userId = 0;
        public String description = "";
        public String style = "";
        public String medium = "";
        public String noOfImage = "";
        public String resolution = "";
    }

    public void setGeneratedDataObj(Integer userId, String description,
                                    String style, String medium, String noOfImage, String resolution) {
        TmpData tmpData = new TmpData();
        tmpData.userId = userId;
        tmpData.description = description;
        tmpData.style = style;
        tmpData.medium = medium;
        tmpData.noOfImage = noOfImage;
        tmpData.resolution =resolution;
        generatedDataObj.setValue(tmpData);
    }

    public LiveData<Resource<List<ItemGenMagicArt>>> getGeneratedData(){
        return generatedData;
    }
}
