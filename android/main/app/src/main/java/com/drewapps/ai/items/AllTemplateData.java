package com.drewapps.ai.items;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "all_template")
public class AllTemplateData {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @TypeConverters
    @SerializedName("category")
    @Expose
    public List<String> category;

    @TypeConverters
    @SerializedName("template")
    @Expose
    public List<AdapTemplateData> template;

    public AllTemplateData(int id, List<String> category, List<AdapTemplateData> template) {
        this.id = id;
        this.category = category;
        this.template = template;
    }
}
