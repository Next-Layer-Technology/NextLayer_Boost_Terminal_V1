package com.sis.clighteningboost.Api

import com.sis.clighteningboost.Models.ARoutingAPIAuthResponse
import com.sis.clighteningboost.BitCoinPojo.CurrentAllRate
import okhttp3.RequestBody
import com.sis.clighteningboost.Models.REST.RegistrationClientResp
import com.sis.clighteningboost.Models.REST.ClientLoginResp
import com.google.gson.JsonObject
import com.sis.clighteningboost.Models.REST.MerchantLoginResp
import com.sis.clighteningboost.Models.REST.BaseIDRes
import com.sis.clighteningboost.Models.REST.ClientUpdateResp
import com.sis.clighteningboost.Models.REST.MerchantUpdateResp
import com.sis.clighteningboost.Models.REST.MerchantListResp
import com.sis.clighteningboost.Models.REST.ClientListResp
import com.sis.clighteningboost.Models.REST.FundingNodeListResp
import com.sis.clighteningboost.Models.REST.RoutingNodeListResp
import com.sis.clighteningboost.Models.REST.TransactionResp
import com.sis.clighteningboost.Models.REST.MerchantNearbyClientResp
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {
    @POST("routingapiauth1")
    fun getRoutingAPIAuth1(@Body body: Map<String?, String?>?): Call<ARoutingAPIAuthResponse?>?

    @POST("routingapiauth2")
    fun getRoutingAPIAuth2(@Body body: Map<String?, String?>?): Call<ARoutingAPIAuthResponse?>?

    @get:POST("ticker")
    val bitCoin: Call<CurrentAllRate?>?

    @Multipart
    @POST("add-client")
    fun  //ok
            client_Registration(
        @Part("client_type") client_type: RequestBody?,
        @Part("client_name") client_name: RequestBody?,
        @Part("client_id") client_id: RequestBody?,
        @Part("merchant_id") merchant_id: RequestBody?,
        @Part("national_id") national_id: RequestBody?,
        @Part("address") address: RequestBody?,
        @Part("dob") dob: RequestBody?,
        @Part("is_gamma_user") is_gamma_user: RequestBody?,
        @Part("registered_at") registered_at: RequestBody?,
        @Part("is_active") is_active: RequestBody?,
        @Part cliet_image_id: MultipartBody.Part?,
        @Part card_image_id: MultipartBody.Part?,
        @Part("email") email: RequestBody?,
        @Part("maxboost_limit") maxboost_limit: RequestBody?,
        @Part("base_id") base_id: RequestBody?,
        @Part("per_boost_limit") per_boost_limit: RequestBody?,
        @Part("max_daily_boost") max_daily_boost: RequestBody?
    ): Call<RegistrationClientResp?>?

    //TODO: Client Login APi
    @FormUrlEncoded //@POST("clients_login") //okclients_admin_login
    @POST("clients_admin_login")
    fun client_Loging(
        @Header("Authorization") token: String?,
        @Field("client_id") client_id: String?
    ): Call<ClientLoginResp?>?

    //TODO: Merchant Login APi
    @Headers("Accept: application/json") //@POST("merchants_login") //ok//merchants_admin_login
    @POST("merchants_admin_login")
    fun merchant_Loging(
        @Body body: JsonObject?
    ): Call<MerchantLoginResp?>?

    //TODO: Merchant Check BaseID APi
    //@Headers("Accept: application/json")
    //@Headers("Content-Type: application/json")
    @POST("user/baseid/exists")
    fun  //ok
            merchant_check_baseID(
        @Header("Authorization") token: String?,
        @Body body: JsonObject?
    ): Call<BaseIDRes?>?

    /* @FormUrlEncoded
    @POST("merchants_login") //ok
    Call<MerchantLoginResp> merchant_Loging(
            @Field("merchant_id") String merchant_id,
            @Field("password") String password
    );*/
    //TODO: Client Update APi
    @FormUrlEncoded
    @POST("clients_Edit/{id}?")
    fun  //ok
            client_Update(
        @Path("id") id: Int,
        @Field("client_maxboost") merchant_id: String?
    ): Call<ClientUpdateResp?>?

    //TODO: Merchant Update APi
    @FormUrlEncoded
    @POST("merchants_Edit/{id}?")
    fun  //ok
            merchant_Update(
        @Path("id") id: Int,
        @Field("merchant_maxboost") merchant_id: String?
    ): Call<MerchantUpdateResp?>?

    //TODO: Get  Merchant List  APi
    @GET("get-merchants")
    fun  //ok
            get_Merchant_List(): Call<MerchantListResp?>?

    //TODO: Get  Client List  APi
    @GET("get-clients")
    fun  //ok
            get_Client_List(): Call<ClientListResp?>?

    //TODO: Get  Funding Node List  APi
    @GET("get-funding-nodes")
    fun  //ok
            get_Funding_Node_List(): Call<FundingNodeListResp?>?

    //TODO: Get  Routing Node List  APi
    @GET("get-routing-nodes")
    fun  //ok
            get_Routing_Node_List(): Call<RoutingNodeListResp?>?

    //TODO: Trasacntion Add APi
    @FormUrlEncoded
    @POST("add-transction")
    fun  //ok
            transction_add(
        @Field("transaction_label") transaction_label: String?,
        @Field("transaction_id") transaction_id: String?,
        @Field("transaction_amountBTC") transaction_amountBTC: String?,
        @Field("transaction_amountUSD") transaction_amountUSD: String?,
        @Field("transaction_clientId") transaction_clientId: String?,
        @Field("transaction_merchantId") transaction_merchantId: String?,
        @Field("transaction_timestamp") transaction_timestamp: String?,
        @Field("conversion_rate") conversion_rate: String?
    ): Call<TransactionResp?>?

    //TODO: Instance Trasacntion Add APi
    @FormUrlEncoded
    @POST("add-instance")
    fun  //ok
            instance_transction_add(
        @Field("transaction_label") transaction_label: String?,
        @Field("transaction_id") transaction_id: String?,
        @Field("transaction_amountBTC") transaction_amountBTC: String?,
        @Field("transaction_amountUSD") transaction_amountUSD: String?,
        @Field("transaction_clientId") transaction_clientId: String?,
        @Field("transaction_merchantId") transaction_merchantId: String?,
        @Field("transaction_timestamp") transaction_timestamp: String?,
        @Field("conversion_rate") conversion_rate: String?
    ): Call<TransactionResp?>?

    //TODO: Merchant Near by Clients API
    @Headers("Accept: application/json")
    @GET("merchant_nearby_clients")
    fun merchant_nearby_clients(
        @Header("Authorization") token: String?
    ): Call<MerchantNearbyClientResp?>?
}