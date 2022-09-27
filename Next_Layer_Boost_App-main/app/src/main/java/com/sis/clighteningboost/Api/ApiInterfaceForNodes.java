package com.sis.clighteningboost.Api;

import com.sis.clighteningboost.Models.DecodeBolt112WithExecuteResponse;
import com.sis.clighteningboost.Models.NodesDataWithExecuteResponse;
import com.sis.clighteningboost.Models.REST.RegistrationClientResp;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiInterfaceForNodes {
    @FormUrlEncoded
    @POST("connect")
        //ok
    Call<ResponseBody> getNodesData(
            @Field("merchantId") String merchantId,
            @Field("merchantBackendPassword") String merchantBackendPassword,
            @Field("boost2faPassword") String boost2faPassword,
            @Field("command") String command);

    @POST("connect")
    Call<Object> getNodes(@Body Map<String, String> body);


    @POST("execute")
    Call<NodesDataWithExecuteResponse> getNodesDataWithExecute(
            @Header("Authorization")String token,
            @Body Map<String, String> body);

    @POST("execute")
    Call<DecodeBolt112WithExecuteResponse> getDecodeBolt112WithExecute(
            @Header("Authorization")String token,
            @Body Map<String, String> body);

    @POST("execute")
    Call<DecodeBolt112WithExecuteResponse> callRefundApiRequestWithExecute(
            @Header("Authorization")String token,
            @Body Map<String, String> body);

    @POST("/pay")
    Call<DecodeBolt112WithExecuteResponse> callPayApiRequestWithExecute(
            @Header("Authorization")String token,
            @Body Map<String, String> body);
}
