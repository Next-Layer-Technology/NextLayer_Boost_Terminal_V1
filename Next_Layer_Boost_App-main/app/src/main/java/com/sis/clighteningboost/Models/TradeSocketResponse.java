package com.sis.clighteningboost.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TradeSocketResponse implements Serializable {
    @SerializedName("data")
    public TradeData tradeData;

    @SerializedName("channel")
    public String channel;

    @SerializedName("event")
    public String event;
}
