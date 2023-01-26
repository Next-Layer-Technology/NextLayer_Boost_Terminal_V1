package com.sis.clighteningboost.Api

import okhttp3.ResponseBody
import com.sis.clighteningboost.Models.NodesDataWithExecuteResponse
import com.sis.clighteningboost.Models.DecodeBolt112WithExecuteResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiInterfaceForNodes {
    @FormUrlEncoded
    @POST("connect")
    fun  //ok
            getNodesData(
        @Field("merchantId") merchantId: String?,
        @Field("merchantBackendPassword") merchantBackendPassword: String?,
        @Field("boost2faPassword") boost2faPassword: String?,
        @Field("command") command: String?
    ): Call<ResponseBody?>?

    @POST("connect")
    fun getNodes(@Body body: Map<String?, String?>?): Call<Any?>?

    @POST("execute")
    fun getNodesDataWithExecute(
        @Header("Authorization") token: String?,
        @Body body: Map<String?, String?>?
    ): Call<NodesDataWithExecuteResponse?>?

    @POST("execute")
    fun getDecodeBolt112WithExecute(
        @Header("Authorization") token: String?,
        @Body body: Map<String?, String?>?
    ): Call<DecodeBolt112WithExecuteResponse?>?

    @POST("execute")
    fun callRefundApiRequestWithExecute(
        @Header("Authorization") token: String?,
        @Body body: Map<String?, String?>?
    ): Call<DecodeBolt112WithExecuteResponse?>?

    @POST("/pay")
    fun callPayApiRequestWithExecute(
        @Header("Authorization") token: String?,
        @Body body: Map<String?, String?>?
    ): Call<DecodeBolt112WithExecuteResponse?>?
}