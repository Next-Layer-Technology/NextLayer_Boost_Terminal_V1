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

class Funding_allocation_msat {
    @SerializedName("03b51aefc422fe99ea4f435ea6fddb7758df885afec3e90efaec7962655bc97397")
    @Expose
    var _03b51aefc422fe99ea4f435ea6fddb7758df885afec3e90efaec7962655bc97397: String? = null

    @SerializedName("03e34f9975cdc4a58bf9abe8b2b2328f9399507151b5193e3b53da3845291f5548")
    @Expose
    var _03e34f9975cdc4a58bf9abe8b2b2328f9399507151b5193e3b53da3845291f5548: String? = null
}