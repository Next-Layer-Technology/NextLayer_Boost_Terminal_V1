package com.sis.clighteningboost.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NodesDataWithExecuteResponse implements Serializable {
    @SerializedName("success")
    public boolean success;

    @SerializedName("message")
    public String message;

    @SerializedName("data")
    public NodesDataWithExecuteData nodesDataWithExecuteData;
}
