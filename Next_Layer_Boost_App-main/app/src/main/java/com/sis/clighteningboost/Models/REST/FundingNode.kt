package com.sis.clighteningboost.Models.REST

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

class FundingNode {
    @SerializedName("id")
    @Expose
    var id = 0

    @SerializedName("node_id")
    @Expose
    var node_id: String? = null

    @SerializedName("ip")
    @Expose
    var ip: String? = null

    @SerializedName("port")
    @Expose
    var port: String? = null

    @SerializedName("username")
    @Expose
    var username: String? = null

    @SerializedName("password")
    @Expose
    var password: String? = null

    @SerializedName("merchant_boost_fee")
    @Expose
    var merchant_boost_fee: String? = null

    @SerializedName("lightning_boost_fee")
    @Expose
    var lightning_boost_fee: String? = null

    @SerializedName("created_at")
    @Expose
    var created_at: String? = null

    @SerializedName("updated_at")
    @Expose
    var updated_at: String? = null

    @SerializedName("registration_fees")
    @Expose
    var registration_fees: String? = null

    @SerializedName("company_email")
    @Expose
    var company_email: String? = null

}