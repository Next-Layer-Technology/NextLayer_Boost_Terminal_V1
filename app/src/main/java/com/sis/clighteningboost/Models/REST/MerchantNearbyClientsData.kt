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

class MerchantNearbyClientsData {
    @SerializedName("id")
    @Expose
    var id = 0

    @SerializedName("client_id")
    @Expose
    var client_id: String? = null

    @SerializedName("client_name")
    @Expose
    var client_name: String? = null

    @SerializedName("client_image_url")
    @Expose
    var client_image_url: String? = null

    @SerializedName("updated_at")
    @Expose
    var updated_at: String? = null

}