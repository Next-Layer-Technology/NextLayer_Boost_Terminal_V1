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

class MerchantData {
    @SerializedName("id")
    @Expose
    var id = 0

    @SerializedName("merchant_id")
    @Expose
    var merchant_name: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("ssh")
    @Expose
    var ssh: String? = null

    @SerializedName("store_name")
    @Expose
    var store_name: String? = null

    @SerializedName("latitude")
    @Expose
    var latitude: String? = null

    @SerializedName("longitude")
    @Expose
    var longitude: String? = null

    @SerializedName("boost_2fa_password")
    @Expose
    var password: String? = null

    @SerializedName("per_boost_limit") //@SerializedName("maxboost_limit")
    @Expose
    var maxboost_limit: String? = null

    @SerializedName("max_daily_boost") //@SerializedName("merchant_maxboost")
    @Expose
    var merchant_maxboost: String? = null

    @SerializedName("merchant_backend_password")
    @Expose
    var merchant_boost_password: String? = null

    @SerializedName("created_at")
    @Expose
    var created_at: String? = null

    @SerializedName("updated_at")
    @Expose
    var updated_at: String? = null

    @SerializedName("accessToken")
    @Expose
    var accessToken: String? = null

    @SerializedName("refreshToken")
    @Expose
    var refreshToken: String? = null

    @SerializedName("remaining_daily_boost")
    @Expose
    var remaining_daily_boost: String? = null



}