package com.drewapps.ai.items;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "chat_history")
public class ItemChatHistory {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @SerializedName("chatId")
    public String chatId;

    @TypeConverters
    @SerializedName("data")
    public List<ChatItem> chatItemList;

    public ItemChatHistory(int id, String chatId, List<ChatItem> chatItemList) {
        this.id = id;
        this.chatId = chatId;
        this.chatItemList = chatItemList;
    }
}
