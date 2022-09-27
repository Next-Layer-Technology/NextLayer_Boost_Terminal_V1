package com.sis.clighteningboost.Models.REST;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClientData {
    /*
        "registered_at": "2020-10-10",
        "is_active": "0",
        "client_image_id": "16197767571.jpeg",
        "card_image_id": "1619776757.jpeg",

        "updated_at": "2021-04-30T09:59:17.000000Z",
        "created_at": "2021-04-30T09:59:17.000000Z",
        "id": 35
    }
    */
    @SerializedName("client_name")
    @Expose
    String client_name;
    @SerializedName("client_id")
    @Expose
    String client_id;
    @SerializedName("national_id")
    @Expose
    String national_id;
    @SerializedName("address")
    @Expose
    String address;
    @SerializedName("dob")
    @Expose
    String dob;
    @SerializedName("is_gamma_user")
    @Expose
    String is_gamma_user;
    @SerializedName("registered_at")
    @Expose
    String registered_at;
    @SerializedName("is_active")
    @Expose
    String is_active;
    @SerializedName("client_image_id")
    @Expose
    String client_image_id;
    @SerializedName("card_image_id")
    @Expose
    String card_image_id;
    @SerializedName("email")
    @Expose
    String email;
    @SerializedName("per_boost_limit")
 //   @SerializedName("maxboost_limit")

    @Expose
    String maxboost_limit;
    @SerializedName("client_backend_password")
    @Expose
    String client_2fa_password;
    @SerializedName("merchant_id")
    @Expose
    String merchant_id;
    @SerializedName("max_daily_boost")
    //@SerializedName("client_maxboost")

    @Expose
    String client_maxboost;
    @SerializedName("updated_at")
    @Expose
    String updated_at;
    @SerializedName("created_at")
    @Expose
    String created_at;
    @SerializedName("id")
    @Expose
    int id;
    @SerializedName("client_type")
    @Expose
    String client_type;

    @SerializedName("remaining_daily_boost")
    @Expose
    String remaining_daily_boost;

    @SerializedName("next_layer_email")
    @Expose
    public String next_layer_email;

    @SerializedName("next_layer_email_password")
    @Expose
    public String next_layer_email_password;

    public String getNext_layer_email() {
        return next_layer_email;
    }

    public void setNext_layer_email(String next_layer_email) {
        this.next_layer_email = next_layer_email;
    }

    public String getNext_layer_email_password() {
        return next_layer_email_password;
    }

    public void setNext_layer_email_password(String next_layer_email_password) {
        this.next_layer_email_password = next_layer_email_password;
    }

    public String getRemaining_daily_boost() {
        return remaining_daily_boost;
    }

    public void setRemaining_daily_boost(String remaining_daily_boost) {
        this.remaining_daily_boost = remaining_daily_boost;
    }

    public String getClient_2fa_password() {
        return client_2fa_password;
    }

    public void setClient_2fa_password(String client_2fa_password) {
        this.client_2fa_password = client_2fa_password;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getClient_maxboost() {
        return client_maxboost;
    }
    public void setClient_maxboost(String client_maxboost) {
        this.client_maxboost = client_maxboost;
    }
    public String getClient_name() {
        return client_name;
    }
    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }
    public String getClient_id() {
        return client_id;
    }
    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }
    public String getNational_id() {
        return national_id;
    }
    public void setNational_id(String national_id) {
        this.national_id = national_id;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getDob() {
        return dob;
    }
    public void setDob(String dob) {
        this.dob = dob;
    }
    public String getIs_gamma_user() {
        return is_gamma_user;
    }
    public void setIs_gamma_user(String is_gamma_user) {
        this.is_gamma_user = is_gamma_user;
    }
    public String getRegistered_at() {
        return registered_at;
    }
    public void setRegistered_at(String registered_at) {
        this.registered_at = registered_at;
    }
    public String getIs_active() {
        return is_active;
    }
    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }
    public String getClient_image_id() {
        return client_image_id;
    }
    public void setClient_image_id(String client_image_id) {
        this.client_image_id = client_image_id;
    }
    public String getCard_image_id() {
        return card_image_id;
    }
    public void setCard_image_id(String card_image_id) {
        this.card_image_id = card_image_id;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getMaxboost_limit() {
        return maxboost_limit;
    }
    public void setMaxboost_limit(String maxboost_limit) {
        this.maxboost_limit = maxboost_limit;
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

    public String getClient_type() {
        return client_type;
    }

    public void setClient_type(String client_type) {
        this.client_type = client_type;
    }
}
