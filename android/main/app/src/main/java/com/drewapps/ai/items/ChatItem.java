package com.drewapps.ai.items;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "chat")
public class ChatItem {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @SerializedName("role")
    @Expose
    public String role;
    @SerializedName("text")
    @Expose
    public String text;
    @SerializedName("userId")
    @Expose
    public String userId;

    @SerializedName("chatId")
    @Expose
    public String chatId;

    public boolean isAnimated = false;

    public ChatItem(String role, String text, String userId, String chatId, boolean isAnimated) {
        this.role = role;
        this.text = text;
        this.userId = userId;
        this.chatId = chatId;
        this.isAnimated = isAnimated;
    }
}
