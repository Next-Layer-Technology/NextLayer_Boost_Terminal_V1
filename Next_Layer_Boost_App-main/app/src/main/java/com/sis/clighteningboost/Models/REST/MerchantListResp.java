package com.sis.clighteningboost.Models.REST;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MerchantListResp {
    @SerializedName("message")
    @Expose
    String message;
    @SerializedName("data")
    @Expose
    ArrayList<MerchantData> merchantDataList;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<MerchantData> getMerchantDataList() {
        return merchantDataList;
    }

    public void setMerchantDataList(ArrayList<MerchantData> merchantDataList) {
        this.merchantDataList = merchantDataList;
    }
}


