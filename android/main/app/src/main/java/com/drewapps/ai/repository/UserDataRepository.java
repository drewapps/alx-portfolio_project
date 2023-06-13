package com.drewapps.ai.repository;

import android.app.Application;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.drewapps.ai.Config;
import com.drewapps.ai.MyApplication;
import com.drewapps.ai.api.ApiClient;
import com.drewapps.ai.api.ApiResponse;
import com.drewapps.ai.api.ApiStatus;
import com.drewapps.ai.api.common.NetworkBoundResource;
import com.drewapps.ai.api.common.common.Resource;
import com.drewapps.ai.billing.BillingManager;
import com.drewapps.ai.database.AppDatabase;
import com.drewapps.ai.database.PurchaseDao;
import com.drewapps.ai.database.SubscriptionDao;
import com.drewapps.ai.database.UserDataDao;
import com.drewapps.ai.items.AppInfo;
import com.drewapps.ai.items.ChatItem;
import com.drewapps.ai.items.ItemChatHistory;
import com.drewapps.ai.items.ItemSubsPlan;
import com.drewapps.ai.items.LoginUser;
import com.drewapps.ai.items.PurchaseHistory;
import com.drewapps.ai.items.UserData;
import com.drewapps.ai.utils.Constants;
import com.drewapps.ai.utils.Util;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class UserDataRepository {
    public Application application;

    AppDatabase db;
    UserDataDao userDataDao;
    PurchaseDao purchaseDao;

    SubscriptionDao subscriptionDao;
    BillingManager billingManager;
    List<ItemSubsPlan> planList;

    public UserDataRepository(Application application) {
        this.application = application;
        db = AppDatabase.getInstance(application);
        userDataDao = db.getUserDataDao();
        purchaseDao = db.getPurchaseDao();
        subscriptionDao = db.getSubscriptionDao();

    }

    public LiveData<Integer> getAvailableWords() {
        return userDataDao.gertAvailableWord();
    }

    public LiveData<Integer> getAvailableImages() {
        return userDataDao.gertAvailableImage();
    }

    public LiveData<Resource<UserData>> getUserData(String apiKey, Integer userId) {
        return new NetworkBoundResource<UserData, UserData>() {
            @Override
            protected void saveCallResult(@NonNull UserData item) {
                try {
                    db.runInTransaction(() -> {
                        userDataDao.delete();
                        userDataDao.insert(item);
                    });
                } catch (Exception ex) {
                    Util.showErrorLog("Error at ", ex);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable UserData data) {
                return Config.IS_CONNECTED;
            }

            @NonNull
            @Override
            protected LiveData<UserData> loadFromDb() {
                return userDataDao.getUserData();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<UserData>> createCall() {
                return ApiClient.getApiService().getUserData(apiKey, userId);
            }
        }.asLiveData();
    }

    public void updateWord(Integer value) {
        userDataDao.updateAvailableWord(value);
    }

    public void updateImage(Integer value) {
        userDataDao.updateAvailableImage(value);
    }

    public LiveData<Resource<Boolean>> deleteAccount(String apiKey, Integer userID) {
        final MutableLiveData<Resource<Boolean>> statusLiveData = new MutableLiveData<>();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {

            Response<ApiStatus> response;

            try {
                response = ApiClient.getApiService().deleteAccount(apiKey, userID).execute();

                if (response.isSuccessful()) {
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

    public LiveData<Resource<Boolean>> updateAccount(String apiKey, String userId, String userName, String email, String filePath, Uri uri) {

        MultipartBody.Part body = null;
        RequestBody fullName = null;
        if (filePath != null && !filePath.equals("")) {
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

        RequestBody useNameRB =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), userName);

        RequestBody useEmailRB =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), email);

        MultipartBody.Part finalBody = body;
        RequestBody finalFullName = fullName;

        final MutableLiveData<Resource<Boolean>> statusLiveData = new MutableLiveData<>();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {

            Response<ApiStatus> response;

            try {
                response = ApiClient.getApiService().updateAccount(apiKey, useIdRB, finalFullName, finalBody, useNameRB, useEmailRB).execute();

                if (response.isSuccessful()) {
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

    public LiveData<Resource<Boolean>> logout() {
        final MutableLiveData<Resource<Boolean>> statusLiveData = new MutableLiveData<>();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {

            try {
                db.runInTransaction(() -> {
                    userDataDao.delete();
                    userDataDao.deleteLoginUser();
                    db.getDocumentDao().deleteAll();
                    db.getTemplateDao().deleteAllTemplate();
                    db.getMagicArtDao().deleteAll();
                    statusLiveData.postValue(Resource.success(true));
                });
            } catch (Exception ex) {
                Util.showErrorLog("Error at ", ex);
                statusLiveData.postValue(Resource.error("error", false));

            }


            handler.post(() -> {
                //UI Thread work here

            });
        });

        return statusLiveData;
    }

    public LiveData<Resource<List<PurchaseHistory>>> getPurchaseHistory(String apiKey, Integer userId) {
        return new NetworkBoundResource<List<PurchaseHistory>, List<PurchaseHistory>>() {
            @Override
            protected void saveCallResult(@NonNull List<PurchaseHistory> item) {
                try {
                    db.runInTransaction(() -> {
                        purchaseDao.deleteAll();
                        purchaseDao.insertAll(item);
                    });
                } catch (Exception ex) {
                    Util.showErrorLog("Error at ", ex);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<PurchaseHistory> data) {
                return Config.IS_CONNECTED;
            }

            @NonNull
            @Override
            protected LiveData<List<PurchaseHistory>> loadFromDb() {
                return purchaseDao.getAllData();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<PurchaseHistory>>> createCall() {
                return ApiClient.getApiService().getPurchases(apiKey, userId);
            }
        }.asLiveData();
    }

    public LiveData<Resource<List<ItemSubsPlan>>> getSubsPlan(String apiKey) {
        return new NetworkBoundResource<List<ItemSubsPlan>, List<ItemSubsPlan>>() {
            @Override
            protected void saveCallResult(@NonNull List<ItemSubsPlan> item) {
                try {
                    db.runInTransaction(() -> {
                        planList = item;
                        subscriptionDao.deleteAll();
                        subscriptionDao.insert(item);
                        String[] data = new String[item.size()];
                        for (int i = 0; i < item.size(); i++) {
                            data[i] = item.get(i).googleProductId;
                        }
                    });
                } catch (Exception ex) {
                    Util.showErrorLog("Error at ", ex);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<ItemSubsPlan> data) {
                return Config.IS_CONNECTED;
            }

            @NonNull
            @Override
            protected LiveData<List<ItemSubsPlan>> loadFromDb() {
                return subscriptionDao.getAll();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<ItemSubsPlan>>> createCall() {
                return ApiClient.getApiService().getSubsPlans(apiKey);
            }
        }.asLiveData();
    }

    public void updatePrice(String price, String id) {
        subscriptionDao.updateData(price, id);
    }

   /* @Override
    public void onUpdatePurchase(String currentSku, String orderId) {

    }

    @Override
    public void onUpdatePrice(List<HashMap<String, String>> details) {
        for (int i = 0; i < details.size(); i++) {
            if (details.get(i) != null) {
                updatePrice(details.get(i).get(planList.get(i).googleProductId), planList.get(i).id);
            }
        }
    }

    @Override
    public void onError(String error) {

    }*/

    public LiveData<Resource<Boolean>> purchaseData(String apiKey, Integer userId, ItemSubsPlan plan, String paymentId, String type) {
        final MutableLiveData<Resource<Boolean>> statusLiveData = new MutableLiveData<>();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {

            Response<ApiStatus> response;

            try {
                response = ApiClient.getApiService().purchaseData(apiKey, userId, plan.id, paymentId, type).execute();
                if (response.isSuccessful()) {
                    userDataDao.updateData(userId, plan.includeWords, plan.includeImages);
                    if (plan.rewardedEnable == 1) {
                        MyApplication.prefManager().setBoolean(Constants.SUBSCRIBED, true);
                        Constants.IS_SUBSCRIBED = MyApplication.prefManager().getBoolean(Constants.SUBSCRIBED);
                    }
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

    public LiveData<Resource<Boolean>> setContactUs(String apiKey, String name, String email, String message, String category) {
        final MutableLiveData<Resource<Boolean>> statusLiveData = new MutableLiveData<>();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {

            Response<ApiStatus> response;

            try {
                response = ApiClient.getApiService().contactUs(apiKey, name, email, message, category).execute();

                if (response.isSuccessful()) {
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

    public LiveData<Resource<LoginUser>> googleLogin(String apiKey, String name, String email, String imageUrl) {
        return new NetworkBoundResource<LoginUser, LoginUser>() {
            @Override
            protected void saveCallResult(@NonNull LoginUser item) {
                try {
                    db.runInTransaction(() -> {
                        userDataDao.deleteLoginUser();
                        userDataDao.insertLogin(item);
                    });
                } catch (Exception ex) {
                    Util.showErrorLog("Error at ", ex);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable LoginUser data) {
                return Config.IS_CONNECTED;
            }

            @NonNull
            @Override
            protected LiveData<LoginUser> loadFromDb() {
                return userDataDao.getLoginUser();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<LoginUser>> createCall() {
                return ApiClient.getApiService().googleLogin(apiKey, name, email, imageUrl);
            }
        }.asLiveData();
    }

    public LiveData<Resource<AppInfo>> getAppInfo(String apiKey, String userId) {
        return new NetworkBoundResource<AppInfo, AppInfo>() {
            @Override
            protected void saveCallResult(@NonNull AppInfo item) {
                try {
                    db.runInTransaction(() -> {
                        userDataDao.deleteAppInfo();
                        userDataDao.insertAppInfo(item);
                    });
                } catch (Exception ex) {
                    Util.showErrorLog("Error at ", ex);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable AppInfo data) {
                return Config.IS_CONNECTED;
            }

            @NonNull
            @Override
            protected LiveData<AppInfo> loadFromDb() {
                return userDataDao.getAppInfo();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<AppInfo>> createCall() {
                return ApiClient.getApiService().getAppInfo(apiKey, userId);
            }
        }.asLiveData();
    }

    public LiveData<Resource<List<ChatItem>>> getChatItems(String apiKey, String text, String chatId, Integer userID) {
        if (!text.equals("")) {
            try {
                db.runInTransaction(() -> {
                    userDataDao.insertChat(new ChatItem("user", text, String.valueOf(userID), chatId,false));
                    userDataDao.insertChat(new ChatItem("assistant", "LOADING", String.valueOf(userID), chatId, false));
                });
            } catch (Exception ex) {
                Util.showErrorLog("Error at ", ex);
            }
        }
        return new NetworkBoundResource<List<ChatItem>, List<ChatItem>>() {

            @Override
            protected void saveCallResult(@NonNull List<ChatItem> item) {
                try {
                    db.runInTransaction(() -> {
                        userDataDao.deleteChats(userID, chatId);
                        for (int i = 0; i < item.size(); i++) {
                            if (text.equals("")) {
                                userDataDao.insertChat(new ChatItem(item.get(i).role, item.get(i).text, item.get(i).userId,
                                        chatId, false));
                            } else {
                                userDataDao.insertChat(new ChatItem(item.get(i).role, item.get(i).text, item.get(i).userId,
                                        chatId, i == (item.size() - 1) ? true : false));
                            }
                        }
                    });
                } catch (Exception ex) {
                    Util.showErrorLog("Error at ", ex);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<ChatItem> data) {
                return Config.IS_CONNECTED;
            }

            @NonNull
            @Override
            protected LiveData<List<ChatItem>> loadFromDb() {
                return userDataDao.getChats(userID, chatId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<ChatItem>>> createCall() {
                return ApiClient.getApiService().getChatItems(apiKey, userID, text, chatId);
            }
        }.asLiveData();
    }

    public LiveData<List<ChatItem>> getLocalChats(String chatID) {
        return userDataDao.getChatsList(MyApplication.prefManager().getInt(Constants.USER_ID), chatID);
    }

    public void updateData(ChatItem item) {
        try {
            db.runInTransaction(() -> {
                userDataDao.updateChat(item.id, item.userId, false);
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void delete2Chat(int id, String userId, String text, String chatId) {
        try {
            db.runInTransaction(() -> {
                userDataDao.delete2Chats(id, userId, text, chatId);
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public LiveData<Resource<Boolean>> uploadReward(String apiKey, int userID, int includeWords, int includeImages) {
        Util.showLog("SSS: Uploading");
        final MutableLiveData<Resource<Boolean>> statusLiveData = new MutableLiveData<>();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {

            Response<ApiStatus> response;

            try {
                response = ApiClient.getApiService().uploadReward(apiKey, userID).execute();

                if (response.isSuccessful()) {
                    userDataDao.updateData(userID, includeWords, includeImages);
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

    public LiveData<Resource<List<ItemChatHistory>>> getChatHistory(String apiKey) {
        return new NetworkBoundResource<List<ItemChatHistory>, List<ItemChatHistory>>() {
            @Override
            protected void saveCallResult(@NonNull List<ItemChatHistory> item) {
                try {
                    db.runInTransaction(()->{

                        userDataDao.deleteChatHistory();
                        userDataDao.insertAll(item);

                    });
                }catch (Exception e){

                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<ItemChatHistory> data) {
                return Config.IS_CONNECTED;
            }

            @NonNull
            @Override
            protected LiveData<List<ItemChatHistory>> loadFromDb() {
                return userDataDao.getChatHistory();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<ItemChatHistory>>> createCall() {
                return ApiClient.getApiService().getChatHistory(apiKey, MyApplication.prefManager().getInt(Constants.USER_ID));
            }
        }.asLiveData();
    }
}
