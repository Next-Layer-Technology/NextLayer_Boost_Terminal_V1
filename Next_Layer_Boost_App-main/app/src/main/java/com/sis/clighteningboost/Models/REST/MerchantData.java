package com.sis.clighteningboost.Models.REST;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MerchantData {
    @SerializedName("id")
    @Expose
    int  id;
    @SerializedName("merchant_id")
    @Expose
    String merchant_name;
    @SerializedName("email")
    @Expose
    String email;
    @SerializedName("ssh")
    @Expose
    String ssh;
    @SerializedName("store_name")
    @Expose
    String store_name;
    @SerializedName("latitude")
    @Expose
    String latitude;
    @SerializedName("longitude")
    @Expose
    String longitude;
    @SerializedName("boost_2fa_password")
    @Expose
    String password;
    @SerializedName("per_boost_limit")
    //@SerializedName("maxboost_limit")

    @Expose
    String maxboost_limit;
    @SerializedName("max_daily_boost")
    //@SerializedName("merchant_maxboost")

    @Expose
    String merchant_maxboost;
    @SerializedName("merchant_backend_password")
    @Expose
    String merchant_boost_password;

    @SerializedName("created_at")
    @Expose
    String created_at;
    @SerializedName("updated_at")
    @Expose
    String updated_at;
    @SerializedName("accessToken")
    @Expose
    String accessToken;
    @SerializedName("refreshToken")
    @Expose
    String refreshToken;

    @SerializedName("remaining_daily_boost")
    @Expose
    String remaining_daily_boost;

    public String getRemaining_daily_boost() {
        return remaining_daily_boost;
    }

    public void setRemaining_daily_boost(String remaining_daily_boost) {
        this.remaining_daily_boost = remaining_daily_boost;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getMerchant_boost_password() {
        return merchant_boost_password;
    }

    public void setMerchant_boost_password(String merchant_boost_password) {
        this.merchant_boost_password = merchant_boost_password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSsh() {
        return ssh;
    }

    public void setSsh(String ssh) {
        this.ssh = ssh;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMaxboost_limit() {
        return maxboost_limit;
    }

    public void setMaxboost_limit(String maxboost_limit) {
        this.maxboost_limit = maxboost_limit;
    }

    public String getMerchant_maxboost() {
        return merchant_maxboost;
    }

    public void setMerchant_maxboost(String merchant_maxboost) {
        this.merchant_maxboost = merchant_maxboost;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
