package com.sis.clighteningboost.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ARoutingAPIAuthData implements Serializable {
    @SerializedName("accessToken")
    public String accessToken;
}
