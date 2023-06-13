package com.drewapps.ai.items;

import com.google.gson.annotations.SerializedName;

public class StripeResponse {

    @SerializedName("paymentIntent")
    public String paymentIntent;
    @SerializedName("ephemeralKey")
    public String ephemeralKey;
    @SerializedName("customer")
    public String customer;
    @SerializedName("publishableKey")
    public String publishableKey;

    public StripeResponse(String paymentIntent, String ephemeralKey, String customer, String publishableKey) {
        this.paymentIntent = paymentIntent;
        this.ephemeralKey = ephemeralKey;
        this.customer = customer;
        this.publishableKey = publishableKey;
    }

    public String getPaymentIntent() {
        return paymentIntent;
    }

    public void setPaymentIntent(String paymentIntent) {
        this.paymentIntent = paymentIntent;
    }

    public String getEphemeralKey() {
        return ephemeralKey;
    }

    public void setEphemeralKey(String ephemeralKey) {
        this.ephemeralKey = ephemeralKey;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getPublishableKey() {
        return publishableKey;
    }

    public void setPublishableKey(String publishableKey) {
        this.publishableKey = publishableKey;
    }

    @Override
    public String toString() {
        return "StripeResponse{" +
                "paymentIntent='" + paymentIntent + '\'' +
                ", ephemeralKey='" + ephemeralKey + '\'' +
                ", customer='" + customer + '\'' +
                ", publishableKey='" + publishableKey + '\'' +
                '}';
    }
}
