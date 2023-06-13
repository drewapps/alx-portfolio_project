package com.drewapps.ai.items;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "message")
public class Message {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("date")
    @Expose
    public String date;
    @SerializedName("image")
    @Expose
    public String image;

    public Message(int id, String type, String message, String date, String image) {
        this.id = id;
        this.type = type;
        this.message = message;
        this.date = date;
        this.image = image;
    }
}
