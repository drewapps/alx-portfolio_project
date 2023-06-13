package com.drewapps.ai.items;

import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "gen_magic_art", primaryKeys = "id")
public class ItemGenMagicArt {

    @SerializedName("id")
    public Integer id;

    @SerializedName("image")
    public String image;

    @SerializedName("query")
    public String querys;

    @SerializedName("resolution")
    public String resolution;

    public ItemGenMagicArt(Integer id, String image, String querys, String resolution) {
        this.id = id;
        this.image = image;
        this.querys = querys;
        this.resolution = resolution;
    }
}
