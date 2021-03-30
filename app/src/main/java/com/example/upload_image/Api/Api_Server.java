package com.example.upload_image.Api;

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Api_Server {
    @POST("/auth/sign-in")
    Call<String> signIn(@Body HashMap<String,String> map);
    @POST("/auth/sign-out")
    Call<String> signOut(@Body HashMap<String,String> map);
    @Multipart
    @POST("/auth/sign-up")
    Call<String> register(
            @Part("email") RequestBody username,
            @Part("pass") RequestBody password,
            @Part("displayName") RequestBody displayName,
            @Part MultipartBody.Part single);
    @POST("/auth/refresh-token")
    Call<String> refreshToken(@Body HashMap<String,String> map);
    @GET("/public/post")
    Call<String> getPublicData();
    @GET("/user")
    Call<String> getAuthData(@Header("Authorization") String accessToken);
}
