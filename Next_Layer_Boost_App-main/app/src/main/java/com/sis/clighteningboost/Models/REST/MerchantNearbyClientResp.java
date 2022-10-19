package com.sis.clighteningboost.Models.REST;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MerchantNearbyClientResp {
    @SerializedName("message")
    @Expose
    String message;
    @SerializedName("data")
    @Expose
    ArrayList<MerchantNearbyClientsData> merchantDataList;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<MerchantNearbyClientsData> getMerchantDataList() {
        return merchantDataList;
    }

    public void setMerchantDataList(ArrayList<MerchantNearbyClientsData> merchantDataList) {
        this.merchantDataList = merchantDataList;
    }
}
