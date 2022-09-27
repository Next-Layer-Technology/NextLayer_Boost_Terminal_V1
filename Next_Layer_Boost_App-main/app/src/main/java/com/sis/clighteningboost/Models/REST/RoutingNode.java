package com.sis.clighteningboost.Models.REST;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RoutingNode {
    @SerializedName("id")
    @Expose
    String id;
    @SerializedName("ip")
    @Expose
    String ip;
    @SerializedName("port")
    @Expose
    String port;
    @SerializedName("username")
    @Expose
    String username;
    @SerializedName("password")
    @Expose
    String password;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



}
