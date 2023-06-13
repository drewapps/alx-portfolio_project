package com.drewapps.ai.items;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity(primaryKeys = "newAppVersionCode")
public class AppVersion {

    @NonNull
    @SerializedName("newAppVersionCode")
    public String newAppVersionCode;

    @SerializedName("updatePopupShow")
    public String updatePopupShow;

    @SerializedName("appLink")
    public String appLink;

    @SerializedName("cancelOption")
    public String cancelOption;

    @SerializedName("description")
    public String versionMessage;


    public AppVersion(@NonNull String newAppVersionCode, String updatePopupShow, String appLink, String cancelOption, String versionMessage) {
        this.newAppVersionCode = newAppVersionCode;
        this.updatePopupShow = updatePopupShow;
        this.appLink = appLink;
        this.cancelOption = cancelOption;
        this.versionMessage = versionMessage;
    }

    @Override
    public String toString() {
        return "AppVersion{" +
                "newAppVersionCode='" + newAppVersionCode + '\'' +
                ", updatePopupShow='" + updatePopupShow + '\'' +
                ", appLink='" + appLink + '\'' +
                ", cancelOption='" + cancelOption + '\'' +
                ", versionMessage='" + versionMessage + '\'' +
                '}';
    }
}
