package com.sis.clighteningboost.Models.REST;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransactionInfo {


    @SerializedName("transaction_label")
    @Expose
    String transaction_label;
    @SerializedName("transaction_id")
    @Expose
    String transaction_id;
    @SerializedName("transaction_amountBTC")
    @Expose
    String transaction_amountBTC;
    @SerializedName("transaction_amountUSD")
    @Expose
    String transaction_amountUSD;
    @SerializedName("transaction_clientId")
    @Expose
    String mestransaction_clientIdsage;
    @SerializedName("transaction_merchantId")
    @Expose
    String transaction_merchantId;
    @SerializedName("transaction_timestamp")
    @Expose
    String transaction_timestamp;

    @SerializedName("conversion_rate")
    @Expose
    String conversion_rate;

    @SerializedName("client_remaining")
    @Expose
    String client_remaining;
    @SerializedName("merchant_remaining")
    @Expose
    String merchant_remaining;
    @SerializedName("updated_at")
    @Expose
    String updated_at;
    @SerializedName("created_at")
    @Expose
    String created_at;
    @SerializedName("id")
    @Expose
    int id;


    public String getTransaction_label() {
        return transaction_label;
    }

    public void setTransaction_label(String transaction_label) {
        this.transaction_label = transaction_label;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getTransaction_amountBTC() {
        return transaction_amountBTC;
    }

    public void setTransaction_amountBTC(String transaction_amountBTC) {
        this.transaction_amountBTC = transaction_amountBTC;
    }

    public String getTransaction_amountUSD() {
        return transaction_amountUSD;
    }

    public void setTransaction_amountUSD(String transaction_amountUSD) {
        this.transaction_amountUSD = transaction_amountUSD;
    }

    public String getMestransaction_clientIdsage() {
        return mestransaction_clientIdsage;
    }

    public void setMestransaction_clientIdsage(String mestransaction_clientIdsage) {
        this.mestransaction_clientIdsage = mestransaction_clientIdsage;
    }

    public String getTransaction_merchantId() {
        return transaction_merchantId;
    }

    public void setTransaction_merchantId(String transaction_merchantId) {
        this.transaction_merchantId = transaction_merchantId;
    }

    public String getTransaction_timestamp() {
        return transaction_timestamp;
    }

    public void setTransaction_timestamp(String transaction_timestamp) {
        this.transaction_timestamp = transaction_timestamp;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConversion_rate() {
        return conversion_rate;
    }

    public void setConversion_rate(String conversion_rate) {
        this.conversion_rate = conversion_rate;
    }

    public String getClient_remaining() {
        return client_remaining;
    }

    public void setClient_remaining(String client_remaining) {
        this.client_remaining = client_remaining;
    }

    public String getMerchant_remaining() {
        return merchant_remaining;
    }

    public void setMerchant_remaining(String merchant_remaining) {
        this.merchant_remaining = merchant_remaining;
    }
}
