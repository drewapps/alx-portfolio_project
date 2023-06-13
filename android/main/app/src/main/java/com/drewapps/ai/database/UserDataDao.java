package com.drewapps.ai.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.drewapps.ai.items.AppInfo;
import com.drewapps.ai.items.ChatItem;
import com.drewapps.ai.items.ItemChatHistory;
import com.drewapps.ai.items.LoginUser;
import com.drewapps.ai.items.UserData;

import java.util.List;

@Dao
public abstract class UserDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(UserData userData);

    @Query("SELECT * FROM user_data ")
    public abstract LiveData<UserData> getUserData();

    @Query("DELETE FROM user_data")
    public abstract void delete();

    @Query("SELECT availableWord FROM user_data")
    public abstract LiveData<Integer> gertAvailableWord();

    @Query("SELECT availableImage FROM user_data")
    public abstract LiveData<Integer> gertAvailableImage();

    @Query("UPDATE user_data SET availableImage = (availableImage - :value)")
    public abstract void updateAvailableImage(Integer value);

    @Query("UPDATE user_data SET availableWord = (availableWord - :value)")
    public abstract void updateAvailableWord(Integer value);

    @Query("UPDATE user_data SET availableWord = (availableWord + :includeWords) , availableImage = availableImage + :includeImages WHERE userId =:userId")
    public abstract void updateData(Integer userId, Integer includeWords, Integer includeImages);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertLogin(LoginUser loginUser);

    @Query("SELECT * FROM login_user")
    public abstract LiveData<LoginUser> getLoginUser();

    @Query("DELETE FROM login_user")
    public abstract void deleteLoginUser();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAppInfo(AppInfo appInfo);

    @Query("SELECT * FROM app_about")
    public abstract LiveData<AppInfo> getAppInfo();

    @Query("DELETE FROM app_about")
    public abstract void deleteAppInfo();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertChat(ChatItem chatItem);

    @Query("SELECT * FROM chat WHERE userId = :userId AND chatId = :chatId")
    public abstract LiveData<List<ChatItem>> getChats(Integer userId, String chatId);

    @Query("SELECT * FROM chat WHERE userId = :userId AND chatId = :chatID")
    public abstract LiveData<List<ChatItem>> getChatsList(Integer userId, String chatID);

    @Query("DELETE FROM chat WHERE userId = :userId AND chatId = :chatID")
    public abstract void deleteChats(Integer userId, String chatID);


    @Query("UPDATE chat SET isAnimated = :bool WHERE userId = :userId AND id = :id")
    public abstract void updateChat(int id, String userId, boolean bool);

    @Query("DELETE FROM chat WHERE id =:id AND userId = :userId AND text = :text AND chatId = :chatId")
    public abstract void delete2Chats(int id, String userId, String text, String chatId);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAll(List<ItemChatHistory> list);

    @Query("SELECT * FROM chat_history")
    public abstract LiveData<List<ItemChatHistory>> getChatHistory();

    @Query("DELETE FROM chat_history ")
    public abstract void deleteChatHistory();
}
