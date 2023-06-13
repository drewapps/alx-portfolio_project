package com.drewapps.ai.api;

import com.google.gson.annotations.SerializedName;

public class ApiStatus {

    @SerializedName("status")
    public final String status;

    @SerializedName("message")
    public final String message;

    public ApiStatus(String status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public String toString() {
        return "ApiStatus{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}