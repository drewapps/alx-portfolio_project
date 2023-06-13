package com.drewapps.ai.items;

import androidx.room.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "login_user", primaryKeys = "userId")
public class LoginUser {

    @SerializedName("userId")
    @Expose
    public Integer userId;
    @SerializedName("userName")
    @Expose
    public String userName;
    @SerializedName("emailId")
    @Expose
    public String emailId;
    @SerializedName("available_word")
    @Expose
    public Integer availableWord;
    @SerializedName("available_image")
    @Expose
    public Integer availableImage;
    @SerializedName("profileImage")
    @Expose
    public String profileImage;

    public LoginUser(Integer userId, String userName, String emailId, Integer availableWord, Integer availableImage, String profileImage) {
        this.userId = userId;
        this.userName = userName;
        this.emailId = emailId;
        this.availableWord = availableWord;
        this.availableImage = availableImage;
        this.profileImage = profileImage;
    }
}
