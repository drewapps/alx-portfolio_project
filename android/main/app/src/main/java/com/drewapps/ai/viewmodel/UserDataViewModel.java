package com.drewapps.ai.viewmodel;

import android.app.Activity;
import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.drewapps.ai.MyApplication;
import com.drewapps.ai.api.common.common.Resource;
import com.drewapps.ai.items.AppInfo;
import com.drewapps.ai.items.ChatItem;
import com.drewapps.ai.items.ItemChatHistory;
import com.drewapps.ai.items.ItemSubsPlan;
import com.drewapps.ai.items.LoginUser;
import com.drewapps.ai.items.PurchaseHistory;
import com.drewapps.ai.items.UserData;
import com.drewapps.ai.repository.UserDataRepository;
import com.drewapps.ai.utils.AbsentLiveData;
import com.drewapps.ai.utils.Constants;

import java.util.List;

public class UserDataViewModel extends AndroidViewModel {

    UserDataRepository repository;

    public LiveData<Resource<UserData>> results;
    public MutableLiveData<Integer> userObj = new MutableLiveData<>();
    public MutableLiveData<Integer> deleteObj = new MutableLiveData<>();
    public MutableLiveData<String> purchaseCreateObj = new MutableLiveData<>();
    public MutableLiveData<Integer> updateObj = new MutableLiveData<>();
    public MutableLiveData<String> logoutObj = new MutableLiveData<>();
    public MutableLiveData<Integer> purchaseObj = new MutableLiveData<>();
    public MutableLiveData<String> planObj = new MutableLiveData<>();
    public MutableLiveData<String> contactObj = new MutableLiveData<>();

    public MutableLiveData<TmpData> loginObj = new MutableLiveData<>();
    public LiveData<Resource<LoginUser>> loginData;

    public MutableLiveData<String> appInfoObj = new MutableLiveData<>();
    public LiveData<Resource<AppInfo>> appData;

    public MutableLiveData<TmpChatData> chatObj = new MutableLiveData<>();
    public MutableLiveData<TmpRwData> rewardObj = new MutableLiveData<>();
    public LiveData<Resource<Boolean>> rewardData;
    public LiveData<Resource<List<ChatItem>>> chatData;

    LiveData<Resource<List<ItemSubsPlan>>> planData;
    private final MutableLiveData<Boolean> loadingState = new MutableLiveData<>();

    public MutableLiveData<String> chatHistoryObj = new MutableLiveData<>();
    public MutableLiveData<String> chatDDObj = new MutableLiveData<>();

    public int offset = 0;

    public boolean isLoading = false;

    public void setLoadingState(Boolean state) {
        isLoading = state;
        loadingState.setValue(state);
    }

    public MutableLiveData<Boolean> getLoadingState() {
        return loadingState;
    }


    public UserDataViewModel(@NonNull Application application) {
        super(application);
        repository = new UserDataRepository(application);

        results = Transformations.switchMap(userObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return repository.getUserData(MyApplication.prefManager().getString(Constants.api_key), obj);
        });

        loginData = Transformations.switchMap(loginObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return repository.googleLogin(MyApplication.prefManager().getString(Constants.api_key), obj.name, obj.email, obj.imageUrl);
        });

        appData = Transformations.switchMap(appInfoObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return repository.getAppInfo(MyApplication.prefManager().getString(Constants.api_key), obj);
        });

        planData = Transformations.switchMap(planObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return repository.getSubsPlan(MyApplication.prefManager().getString(Constants.api_key));
        });

        chatData = Transformations.switchMap(chatObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return repository.getChatItems(MyApplication.prefManager().getString(Constants.api_key), obj.text, obj.chatID,
                    MyApplication.prefManager().getInt(Constants.USER_ID));
        });

        rewardData = Transformations.switchMap(rewardObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return repository.uploadReward(MyApplication.prefManager().getString(Constants.api_key),
                    MyApplication.prefManager().getInt(Constants.USER_ID), obj.word, obj.image);
        });
    }

    public void setChatObj(String text, String chatId) {
        TmpChatData tmpChatData = new TmpChatData();
        tmpChatData.text = text;
        tmpChatData.chatID = chatId;
        chatObj.setValue(tmpChatData);
    }

    public LiveData<Resource<List<ChatItem>>> getChatData() {
        return chatData;
    }

    public LiveData<List<ChatItem>> getLocalChatData(String chatID) {
        chatDDObj.setValue(chatID);
        return Transformations.switchMap(chatDDObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return repository.getLocalChats(obj);
        });
    }

    public void setUserObj(Integer userObj) {
        this.userObj.setValue(userObj);
    }

    public LiveData<Resource<UserData>> getUserData() {
        return results;
    }

    public LiveData<Integer> getAvailableWords() {
        return repository.getAvailableWords();
    }

    public LiveData<Integer> getAvailableImages() {
        return repository.getAvailableImages();
    }

    public void updateWord(Integer value) {
        repository.updateWord(value);
    }

    public void updateImage(Integer value) {
        repository.updateImage(value);
    }

    public LiveData<Resource<Boolean>> deleteAccount(Integer userId) {
        deleteObj.setValue(userId);
        return Transformations.switchMap(deleteObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return repository.deleteAccount(MyApplication.prefManager().getString(Constants.api_key), obj);
        });
    }

    public LiveData<Resource<Boolean>> updateAccount(Integer userId, String userName, String email, String filePath, Uri uri) {
        updateObj.setValue(userId);
        return Transformations.switchMap(updateObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return repository.updateAccount(MyApplication.prefManager().getString(Constants.api_key), String.valueOf(userId),
                    userName, email, filePath, uri);
        });
    }

    public LiveData<Resource<Boolean>> logout() {
        logoutObj.setValue("IQ");
        return Transformations.switchMap(logoutObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return repository.logout();
        });
    }

    public LiveData<Resource<List<PurchaseHistory>>> getPurchaseHistory(Integer userId) {
        purchaseObj.setValue(userId);
        return Transformations.switchMap(purchaseObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return repository.getPurchaseHistory(MyApplication.prefManager().getString(Constants.api_key), obj);
        });
    }

    public LiveData<Resource<List<ItemSubsPlan>>> getSubsPlan(Activity activity) {
        return planData;
    }

    public void setPlanObj() {
        setLoadingState(true);
        planObj.setValue("IQ");
    }

    public void updatePrice(String price, String id) {
        repository.updatePrice(price, id);
    }

    public LiveData<Resource<Boolean>> purchaseData(Integer userId, ItemSubsPlan planId, String paymentId, String type) {
        purchaseCreateObj.setValue("IQ");
        return Transformations.switchMap(purchaseCreateObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return repository.purchaseData(MyApplication.prefManager().getString(Constants.api_key), userId, planId, paymentId, type);
        });
    }

    public LiveData<Resource<Boolean>> setContactUs(String name, String email, String message, String category) {
        contactObj.setValue("IQ");
        return Transformations.switchMap(contactObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return repository.setContactUs(MyApplication.prefManager().getString(Constants.api_key), name,
                    email, message, category);
        });
    }

    public void setGoogleLogin(String name, String email, String imageUrl) {
        TmpData tmpData = new TmpData();
        tmpData.name = name;
        tmpData.email = email;
        tmpData.imageUrl = imageUrl;
        loginObj.setValue(tmpData);
    }

    public LiveData<Resource<LoginUser>> googleLoginData() {
        return loginData;
    }

    public void updateData(ChatItem item) {
        repository.updateData(item);
    }

    public void delete2Chat(ChatItem chatItem, String chatId) {
        repository.delete2Chat(chatItem.id, chatItem.userId, chatItem.text, chatId);
    }

    public class TmpData {
        public String name = "";
        public String email = "";
        public String imageUrl = "";
    }

    public class TmpRwData {
        public int word = 0;
        public int image = 0;
    }

    public class TmpChatData {
        public String text = "";
        public String chatID = "";
    }

    public LiveData<Resource<AppInfo>> getAppInfo() {
        return appData;
    }

    public void setAppInfoObj(String Id) {
        appInfoObj.setValue(Id);
    }

    public void uploadReward(int includeWords, int includeImages) {
        TmpRwData tmpRwData = new TmpRwData();
        tmpRwData.word = includeWords;
        tmpRwData.image = includeImages;
        rewardObj.setValue(tmpRwData);
    }

    public LiveData<Resource<Boolean>> getRewardRes() {
        return rewardData;
    }

    public LiveData<Resource<List<ItemChatHistory>>> getChatHistory() {
        chatHistoryObj.setValue("IQ");
        return Transformations.switchMap(chatHistoryObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return repository.getChatHistory(MyApplication.prefManager().getString(Constants.api_key));
        });
    }
}

