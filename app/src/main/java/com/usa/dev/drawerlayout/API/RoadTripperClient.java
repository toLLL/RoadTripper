package com.usa.dev.drawerlayout.API;

import com.usa.dev.drawerlayout.Helper.RequestManager;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Header;
import retrofit.http.POST;

/**
 * Author : Philippe Volpe
 * Date: 13/01/2016
 * Purpose: Define a RoadTripper client
 */
public interface RoadTripperClient {

    @FormUrlEncoded
    @POST("/v1/auth/login")
    Call<RequestManager.User> login(@Header("Authorization")String token, @Field("facebookToken") String facebookToken);
    @FormUrlEncoded
    @POST("/v1/auth/facebooktoken")
    Call<Void>  updateFacebookToken(@Header("Authorization")String token, @Field("facebookToken") String facebookToken);
}
