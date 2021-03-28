package com.example.upload_image.Api;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Api {
    //TEST LOCAL
    @GET("/")
    Call<String> getData();

    @Multipart
    @POST("/upload/single")
    Call<String> uploadSingle(
            @Part("username") RequestBody username,
            @Part("password") RequestBody password,
            @Part MultipartBody.Part single);

    @Multipart
    @POST("/upload/multi")
    Call<String> uploadMulti(
            @Part("username") RequestBody username,
            @Part("password") RequestBody password,
            @Part List<MultipartBody.Part> multi
    );

    //TEST LOCAL

    //TEST SERVER
    @GET("/public/post")
    Call<String> getDataFromServer();
}
