package com.sis.clighteningboost.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ARoutingAPIAuthResponse implements Serializable {

    @SerializedName("data")
    public ARoutingAPIAuthData aRoutingAPIAuthData;
}
