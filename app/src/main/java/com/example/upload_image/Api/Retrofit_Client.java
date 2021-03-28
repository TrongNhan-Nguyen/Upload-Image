package com.example.upload_image.Api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;



public class Retrofit_Client {
    static OkHttpClient.Builder httpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request original = chain.request();
            Request request = original.newBuilder()
                    .header("Accept","application/json")
                    .method(original.method(),original.body())
                    .build();
            return chain.proceed(request);
        }
    });
    static OkHttpClient client = httpClient.build();



    private static Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build();

    //     .baseUrl("http://ec2-18-223-15-195.us-east-2.compute.amazonaws.com:3000")

}
