package com.sis.clighteningboost.Models.REST;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegistrationClientResp {
    @SerializedName("message")
    @Expose
    String message;
    @SerializedName("data")
    @Expose
    ClientData clientData;
    @Override
    public String toString()
    {
        return "ClassPojo [data = "+clientData+", message = "+message+"]";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ClientData getClientData() {
        return clientData;
    }

    public void setClientData(ClientData clientData) {
        this.clientData = clientData;
    }
}
