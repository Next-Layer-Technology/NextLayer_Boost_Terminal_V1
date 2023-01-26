package com.sis.clighteningboost.Models

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
import java.io.Serializable

class TradeData : Serializable {
    @SerializedName("id")
    var id: Long = 0

    @SerializedName("timestamp")
    var timestamp: String? = null

    @SerializedName("amount")
    var amount = 0.0

    @SerializedName("amount_str")
    var amount_str = 0.0

    @SerializedName("price")
    var price = 0

    @SerializedName("price_str")
    var price_str: String? = null

    @SerializedName("microtimestamp")
    var microtimestamp: String? = null

    @SerializedName("buy_order_id")
    var buy_order_id: Long = 0

    @SerializedName("sell_order_id")
    var sell_order_id: Long = 0
}