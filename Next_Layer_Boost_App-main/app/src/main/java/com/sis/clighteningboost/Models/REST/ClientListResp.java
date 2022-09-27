package com.sis.clighteningboost.Models.REST;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ClientListResp {
    @SerializedName("message")
    @Expose
    String message;
    @SerializedName("data")
    @Expose
    ArrayList<ClientData> clientDataList;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<ClientData> getClientDataList() {
        return clientDataList;
    }

    public void setClientDataList(ArrayList<ClientData> clientDataList) {
        this.clientDataList = clientDataList;
    }



}
