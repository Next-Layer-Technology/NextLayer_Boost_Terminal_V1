package com.sis.clighteningboost.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AckModel {
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
