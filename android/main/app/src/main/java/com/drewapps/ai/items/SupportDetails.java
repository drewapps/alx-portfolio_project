package com.drewapps.ai.items;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "support_detail", primaryKeys = "ticketId")
public class SupportDetails {

    @SerializedName("ticket_id")
    @NonNull
    public String ticketId;

    @SerializedName("subject")
    @Expose
    public String subject;
    @SerializedName("status")
    @Expose
    public String status;

    @TypeConverters
    @SerializedName("message")
    @Expose
    public List<Message> message;

    public SupportDetails(@NonNull String ticketId, String subject, String status, List<Message> message) {
        this.ticketId = ticketId;
        this.subject = subject;
        this.status = status;
        this.message = message;
    }
}
