package com.sis.clighteningboost.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DecodeBolt112WithExecuteResponse implements Serializable {
    @SerializedName("success")
    public boolean success;

    @SerializedName("message")
    public String message;

    @SerializedName("data")
    public DecodeBolt112WithExecuteData decodeBolt112WithExecuteData;
}
