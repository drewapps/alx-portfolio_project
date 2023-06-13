package com.drewapps.ai.items;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "user_data")
public class UserData {

    @PrimaryKey(autoGenerate = true)
    public int id;
    @SerializedName("userId")
    public Integer userId;
    @SerializedName("userName")
    public String userName;
    @SerializedName("emailId")
    public String emailId;
    @SerializedName("available_word")
    public Integer availableWord;
    @SerializedName("available_image")
    public Integer availableImage;
    @SerializedName("profileImage")
    public String profileImage;
    @SerializedName("document_created")
    public Integer documentCreated;
    @SerializedName("word_used")
    public Integer wordUsed;
    @SerializedName("image_created")
    public Integer imageCreated;
    @SerializedName("template_used")
    public Integer templateUsed;

    @SerializedName("isSubscribe")
    public Boolean isSubscribe;

    @SerializedName("start_date")
    public String startDate;

    @SerializedName("end_date")
    public String endDate;

    @SerializedName("current_plan")
    public String currentPlan;
    @TypeConverters
    @SerializedName("favorite_template")
    public List<Template> favoriteTemplate;

    @TypeConverters
    @SerializedName("user_monthly_usage_word")
    public List<Integer> userMonthlyUsageWord;

    @TypeConverters
    @SerializedName("user_monthly_usage_image")
    public List<Integer> userMonthlyUsageImage;

    public UserData(int id, Integer userId, String userName, String emailId, Integer availableWord, Integer availableImage, String profileImage, Integer documentCreated, Integer wordUsed, Integer imageCreated, Integer templateUsed, Boolean isSubscribe, String startDate, String endDate, String currentPlan, List<Template> favoriteTemplate, List<Integer> userMonthlyUsageWord, List<Integer> userMonthlyUsageImage) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.emailId = emailId;
        this.availableWord = availableWord;
        this.availableImage = availableImage;
        this.profileImage = profileImage;
        this.documentCreated = documentCreated;
        this.wordUsed = wordUsed;
        this.imageCreated = imageCreated;
        this.templateUsed = templateUsed;
        this.isSubscribe = isSubscribe;
        this.startDate = startDate;
        this.endDate = endDate;
        this.currentPlan = currentPlan;
        this.favoriteTemplate = favoriteTemplate;
        this.userMonthlyUsageWord = userMonthlyUsageWord;
        this.userMonthlyUsageImage = userMonthlyUsageImage;
    }
}
