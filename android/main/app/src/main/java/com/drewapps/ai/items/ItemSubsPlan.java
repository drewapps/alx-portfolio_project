package com.drewapps.ai.items;

import androidx.room.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "subscriptions", primaryKeys = "id")
public class ItemSubsPlan {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("planName")
    @Expose
    public String planName;
    @SerializedName("planPrice")
    @Expose
    public String planPrice;
    @SerializedName("include_Words")
    @Expose
    public Integer includeWords;
    @SerializedName("include_images")
    @Expose
    public Integer includeImages;
    @SerializedName("google_product_id")
    @Expose
    public String googleProductId;
    @SerializedName("most_popular")
    @Expose
    public Integer mostPopular;

    @SerializedName("rewarded_enable")
    @Expose
    public Integer rewardedEnable;
    @SerializedName("duration")
    @Expose
    public String duration;
    @SerializedName("google_product_enable")
    @Expose
    public Integer googleProductEnable;
    public String gPrice = "";


}
