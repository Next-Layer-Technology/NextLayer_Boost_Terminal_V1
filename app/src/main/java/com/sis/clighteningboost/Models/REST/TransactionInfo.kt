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

class TransactionInfo {
    @SerializedName("transaction_label")
    @Expose
    var transaction_label: String? = null

    @SerializedName("transaction_id")
    @Expose
    var transaction_id: String? = null

    @SerializedName("transaction_amountBTC")
    @Expose
    var transaction_amountBTC: String? = null

    @SerializedName("transaction_amountUSD")
    @Expose
    var transaction_amountUSD: String? = null

    @SerializedName("transaction_clientId")
    @Expose
    var mestransaction_clientIdsage: String? = null

    @SerializedName("transaction_merchantId")
    @Expose
    var transaction_merchantId: String? = null

    @SerializedName("transaction_timestamp")
    @Expose
    var transaction_timestamp: String? = null

    @SerializedName("conversion_rate")
    @Expose
    var conversion_rate: String? = null

    @SerializedName("client_remaining")
    @Expose
    var client_remaining: String? = null

    @SerializedName("merchant_remaining")
    @Expose
    var merchant_remaining: String? = null

    @SerializedName("updated_at")
    @Expose
    var updated_at: String? = null

    @SerializedName("created_at")
    @Expose
    var created_at: String? = null

    @SerializedName("id")
    @Expose
    var id = 0

}