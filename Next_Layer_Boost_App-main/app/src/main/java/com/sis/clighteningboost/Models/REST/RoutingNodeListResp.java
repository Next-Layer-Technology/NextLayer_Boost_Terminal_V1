package com.sis.clighteningboost.Models.REST;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RoutingNodeListResp {
    @SerializedName("message")
    @Expose
    String message;
    @SerializedName("data")
    @Expose
    ArrayList<RoutingNode> routingNodesList;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<RoutingNode> getRoutingNodesList() {
        return routingNodesList;
    }

    public void setRoutingNodesList(ArrayList<RoutingNode> routingNodesList) {
        this.routingNodesList = routingNodesList;
    }
}
