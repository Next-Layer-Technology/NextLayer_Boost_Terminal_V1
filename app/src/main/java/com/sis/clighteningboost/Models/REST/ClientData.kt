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

class ClientData {
    /*
        "registered_at": "2020-10-10",
        "is_active": "0",
        "client_image_id": "16197767571.jpeg",
        "card_image_id": "1619776757.jpeg",

        "updated_at": "2021-04-30T09:59:17.000000Z",
        "created_at": "2021-04-30T09:59:17.000000Z",
        "id": 35
    }
    */
    @SerializedName("client_name")
    @Expose
    var client_name: String? = null

    @SerializedName("client_id")
    @Expose
    var client_id: String? = null

    @SerializedName("national_id")
    @Expose
    var national_id: String? = null

    @SerializedName("address")
    @Expose
    var address: String? = null

    @SerializedName("dob")
    @Expose
    var dob: String? = null

    @SerializedName("is_gamma_user")
    @Expose
    var is_gamma_user: String? = null

    @SerializedName("registered_at")
    @Expose
    var registered_at: String? = null

    @SerializedName("is_active")
    @Expose
    var is_active: String? = null

    @SerializedName("client_image_id")
    @Expose
    var client_image_id: String? = null

    @SerializedName("card_image_id")
    @Expose
    var card_image_id: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("per_boost_limit") //   @SerializedName("maxboost_limit")
    @Expose
    var maxboost_limit: String? = null

    @SerializedName("client_backend_password")
    @Expose
    var client_2fa_password: String? = null

    @SerializedName("merchant_id")
    @Expose
    var merchant_id: String? = null

    @SerializedName("max_daily_boost") //@SerializedName("client_maxboost")
    @Expose
    var client_maxboost: String? = null

    @SerializedName("updated_at")
    @Expose
    var updated_at: String? = null

    @SerializedName("created_at")
    @Expose
    var created_at: String? = null

    @SerializedName("id")
    @Expose
    var id = 0

    @SerializedName("client_type")
    @Expose
    var client_type: String? = null

    @SerializedName("remaining_daily_boost")
    @Expose
    var remaining_daily_boost: String? = null

    @SerializedName("next_layer_email")
    @Expose
    var next_layer_email: String? = null

    @SerializedName("next_layer_email_password")
    @Expose
    var next_layer_email_password: String? = null

}