package com.sis.clighteningboost.Models.REST;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MerchantUpdateResp {
    @SerializedName("message")
    @Expose
    String message;
    @SerializedName("data")
    @Expose
    MerchantData merchantData;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MerchantData getMerchantData() {
        return merchantData;
    }

    public void setMerchantData(MerchantData merchantData) {
        this.merchantData = merchantData;
    }
}
