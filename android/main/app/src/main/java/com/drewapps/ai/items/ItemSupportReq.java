package com.drewapps.ai.items;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "support_request", primaryKeys = "ticketId")
public class ItemSupportReq implements Serializable {

    @SerializedName("ticketId")
    @Expose
    @NonNull
    public String ticketId;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("category")
    @Expose
    public String category;
    @SerializedName("subject")
    @Expose
    public String subject;
    @SerializedName("priority")
    @Expose
    public String priority;
    @SerializedName("lastUpdated")
    @Expose
    public String lastUpdated;

    public ItemSupportReq(String ticketId, String status, String category, String subject, String priority, String lastUpdated) {
        this.ticketId = ticketId;
        this.status = status;
        this.category = category;
        this.subject = subject;
        this.priority = priority;
        this.lastUpdated = lastUpdated;
    }
}
