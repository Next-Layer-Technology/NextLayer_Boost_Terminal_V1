package com.sis.clighteningboost.Api;
import com.google.gson.JsonObject;
import com.sis.clighteningboost.BitCoinPojo.CurrentAllRate;

import com.sis.clighteningboost.Models.ARoutingAPIAuthResponse;
import com.sis.clighteningboost.Models.REST.BaseIDRes;
import com.sis.clighteningboost.Models.REST.ClientListResp;
import com.sis.clighteningboost.Models.REST.ClientLoginResp;
import com.sis.clighteningboost.Models.REST.ClientUpdateResp;
import com.sis.clighteningboost.Models.REST.FundingNodeListResp;
import com.sis.clighteningboost.Models.REST.MerchantListResp;
import com.sis.clighteningboost.Models.REST.MerchantLoginResp;
import com.sis.clighteningboost.Models.REST.MerchantUpdateResp;
import com.sis.clighteningboost.Models.REST.RegistrationClientResp;
import com.sis.clighteningboost.Models.REST.RoutingNodeListResp;
import com.sis.clighteningboost.Models.REST.TransactionResp;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
public interface ApiInterface {
    @POST("routingapiauth1")
    Call<ARoutingAPIAuthResponse> getRoutingAPIAuth1(@Body Map<String, String> body);

    @POST("routingapiauth2")
    Call<ARoutingAPIAuthResponse> getRoutingAPIAuth2(@Body Map<String, String> body);

    @POST("ticker")
    Call<CurrentAllRate> getBitCoin();
    @Multipart
    @POST("add-client") //ok
    Call<RegistrationClientResp> client_Registration(
             @Part("client_type") RequestBody client_type,
             @Part("client_name") RequestBody client_name,
             @Part("client_id")   RequestBody client_id,
             @Part("merchant_id") RequestBody merchant_id,
             @Part("national_id") RequestBody national_id,
             @Part("address")     RequestBody address,
             @Part("dob")         RequestBody dob,
             @Part("is_gamma_user") RequestBody is_gamma_user,
             @Part("registered_at") RequestBody registered_at,
             @Part("is_active")     RequestBody is_active,
             @Part MultipartBody.Part cliet_image_id,
             @Part MultipartBody.Part card_image_id,
             @Part("email")          RequestBody email,
             @Part("maxboost_limit") RequestBody maxboost_limit,
             @Part("base_id") RequestBody base_id,
             @Part("per_boost_limit") RequestBody per_boost_limit,
             @Part("max_daily_boost") RequestBody max_daily_boost
            );
    //TODO: Client Login APi
    @FormUrlEncoded
    //@POST("clients_login") //okclients_admin_login
    @POST("clients_admin_login")
    Call<ClientLoginResp> client_Loging(
            @Header("Authorization")String token,
            @Field("client_id") String client_id
    );

    //TODO: Merchant Login APi
    @Headers("Accept: application/json")
    //@POST("merchants_login") //ok//merchants_admin_login
    @POST("merchants_admin_login")
    Call<MerchantLoginResp> merchant_Loging(
            @Body JsonObject body
    );
    //TODO: Merchant Check BaseID APi
    //@Headers("Accept: application/json")
    //@Headers("Content-Type: application/json")
    @POST("user/baseid/exists") //ok
    Call<BaseIDRes> merchant_check_baseID(
            @Header("Authorization")String token,
            @Body JsonObject body
    );

   /* @FormUrlEncoded
    @POST("merchants_login") //ok
    Call<MerchantLoginResp> merchant_Loging(
            @Field("merchant_id") String merchant_id,
            @Field("password") String password
    );*/
    //TODO: Client Update APi
    @FormUrlEncoded
    @POST("clients_Edit/{id}?") //ok
    Call<ClientUpdateResp> client_Update(
            @Path("id") int id,
            @Field("client_maxboost") String merchant_id
    );

    //TODO: Merchant Update APi
    @FormUrlEncoded
    @POST("merchants_Edit/{id}?") //ok
    Call<MerchantUpdateResp> merchant_Update(
            @Path("id") int id,
            @Field("merchant_maxboost") String merchant_id
    );

    //TODO: Get  Merchant List  APi
    @GET("get-merchants") //ok
    Call<MerchantListResp> get_Merchant_List(
    );

    //TODO: Get  Client List  APi
    @GET("get-clients") //ok
    Call<ClientListResp> get_Client_List(
    );

    //TODO: Get  Funding Node List  APi
    @GET("get-funding-nodes") //ok
    Call<FundingNodeListResp> get_Funding_Node_List(
    );
    //TODO: Get  Routing Node List  APi
    @GET("get-routing-nodes") //ok
    Call<RoutingNodeListResp> get_Routing_Node_List(
    );

    //TODO: Trasacntion Add APi
    @FormUrlEncoded
    @POST("add-transction") //ok
    Call<TransactionResp> transction_add(
            @Field("transaction_label") String transaction_label,
            @Field("transaction_id") String transaction_id,
            @Field("transaction_amountBTC") String transaction_amountBTC,
            @Field("transaction_amountUSD") String transaction_amountUSD,
            @Field("transaction_clientId") String transaction_clientId,
            @Field("transaction_merchantId") String transaction_merchantId,
            @Field("transaction_timestamp") String transaction_timestamp,
            @Field("conversion_rate") String conversion_rate
    );
    //TODO: Instance Trasacntion Add APi
    @FormUrlEncoded
    @POST("add-instance") //ok
    Call<TransactionResp> instance_transction_add(
            @Field("transaction_label") String transaction_label,
            @Field("transaction_id") String transaction_id,
            @Field("transaction_amountBTC") String transaction_amountBTC,
            @Field("transaction_amountUSD") String transaction_amountUSD,
            @Field("transaction_clientId") String transaction_clientId,
            @Field("transaction_merchantId") String transaction_merchantId,
            @Field("transaction_timestamp") String transaction_timestamp,
            @Field("conversion_rate") String conversion_rate
    );
}