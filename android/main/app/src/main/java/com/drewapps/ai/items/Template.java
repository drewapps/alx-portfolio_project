package com.drewapps.ai.items;

import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "template", primaryKeys = "templateId")
public class Template implements Serializable {

    @SerializedName("templateId")
    public Integer templateId;
    @SerializedName("templateName")
    public String templateName;
    @SerializedName("templateImage")
    public String templateImage;
    @SerializedName("templateDescription")
    public String templateDescription;
    @SerializedName("category")
    public String category;
    @SerializedName("favorite")
    public boolean isFavorite;

    public Template(Integer templateId, String templateName, String templateImage, String templateDescription,
                    String category, boolean isFavorite) {
        this.templateId = templateId;
        this.templateName = templateName;
        this.templateImage = templateImage;
        this.templateDescription = templateDescription;
        this.category = category;
        this.isFavorite = isFavorite;
    }

}
