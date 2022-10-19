package com.sis.clighteningboost.Models.REST;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MerchantNearbyClientsData {

    @SerializedName("id")
    @Expose
    int id;
    @SerializedName("client_id")
    @Expose
    String client_id;
    @SerializedName("client_name")
    @Expose
    String client_name;
    @SerializedName("client_image_url")
    @Expose
    String client_image_url;
    @SerializedName("updated_at")
    @Expose
    String updated_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getClient_image_url() {
        return client_image_url;
    }

    public void setClient_image_url(String client_image_url) {
        this.client_image_url = client_image_url;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
