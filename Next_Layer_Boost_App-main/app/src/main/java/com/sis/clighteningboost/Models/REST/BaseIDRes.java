package com.sis.clighteningboost.Models.REST;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseIDRes {
    @SerializedName("message")
    @Expose
    String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
