package com.drewapps.ai.items;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


@Entity(tableName = "adaptemplate")
public class AdapTemplateData {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @TypeConverters
    @SerializedName("category")
    @Expose
    public String category;

    @TypeConverters
    @SerializedName("list")
    @Expose
    public List<Template> templateList;

    public AdapTemplateData(String category, List<Template> templateList) {
        this.category = category;
        this.templateList = templateList;
    }
}
