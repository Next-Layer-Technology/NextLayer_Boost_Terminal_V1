package com.sis.clighteningboost.BitCoinPojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BitRate {

    @SerializedName("USD")
    @Expose
    private USD usd;

    public USD getUsd() {
        return usd;
    }

    public void setUsd(USD usd) {
        this.usd = usd;
    }
}
