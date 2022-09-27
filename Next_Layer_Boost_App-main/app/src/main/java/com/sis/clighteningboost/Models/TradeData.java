package com.sis.clighteningboost.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TradeData implements Serializable {
    @SerializedName("id")
    public long id;

    @SerializedName("timestamp")
    public String timestamp;

    @SerializedName("amount")
    public double amount;

    @SerializedName("amount_str")
    public double amount_str;

    @SerializedName("price")
    public int price;

    @SerializedName("price_str")
    public String price_str;

    @SerializedName("microtimestamp")
    public String microtimestamp;

    @SerializedName("buy_order_id")
    public long buy_order_id;

    @SerializedName("sell_order_id")
    public long sell_order_id;
}
