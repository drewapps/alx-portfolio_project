package com.drewapps.ai.items;

import androidx.room.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "purchase_history", primaryKeys = "transactionId")
public class PurchaseHistory {

    @SerializedName("transactionId")
    @Expose
    public Integer transactionId;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("amount")
    @Expose
    public String amount;
    @SerializedName("payment_id")
    @Expose
    public String paymentId;
    @SerializedName("date")
    @Expose
    public String date;

    public PurchaseHistory(Integer transactionId, String title, String amount, String paymentId, String date) {
        this.transactionId = transactionId;
        this.title = title;
        this.amount = amount;
        this.paymentId = paymentId;
        this.date = date;
    }
}
