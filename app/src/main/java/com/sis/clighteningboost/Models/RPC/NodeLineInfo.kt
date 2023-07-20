package com.sis.clighteningboost.Models.RPC

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import com.sis.clighteningboost.Models.REST.ClientData
import com.sis.clighteningboost.Models.REST.TransactionInfo
import com.sis.clighteningboost.Models.REST.MerchantData
import com.sis.clighteningboost.Models.REST.FundingNode
import com.sis.clighteningboost.Models.REST.RoutingNode
import com.sis.clighteningboost.Models.REST.MerchantNearbyClientsData
import com.sis.clighteningboost.Models.TradeData
import com.sis.clighteningboost.Models.ARoutingAPIAuthData
import com.sis.clighteningboost.Models.NodesDataWithExecuteData
import com.sis.clighteningboost.Models.DecodeBolt112WithExecuteData
import java.util.ArrayList

class NodeLineInfo {
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("connected")
    @Expose
    var connected = false

    @SerializedName("netaddr")
    @Expose
    var netaddr: List<String>? = null

    @SerializedName("features")
    @Expose
    var features: String? = null

    @SerializedName("channels")
    @Expose
    var channels = ArrayList<Channels>()


    var isOn = false

}