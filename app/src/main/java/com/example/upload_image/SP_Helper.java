package com.example.upload_image;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.HashMap;
import java.util.Map;

public class SP_Helper {
    private Context context;

    public SP_Helper(Context context) {
        this.context = context;
    }

    public void setAccessToken(String accessToken) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("sp_token", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("accessToken", accessToken);
        editor.apply();
    }

    public String getAccessToken() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("sp_token", Context.MODE_PRIVATE);
        return sharedPreferences.getString("accessToken", null);
    }

    public void setRefreshToken(String refreshToken) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("sp_token", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("refreshToken", refreshToken);
        editor.apply();
    }

    public String getRefreshToken() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("sp_token", Context.MODE_PRIVATE);
        return sharedPreferences.getString("refreshToken", null);
    }

    public void setUserId(String userId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("sp_user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userId", userId);
        editor.apply();
    }

    public String getUserId() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("sp_user", Context.MODE_PRIVATE);
        return sharedPreferences.getString("userId", null);
    }

    public void clearSP() {
        context.getSharedPreferences("sp_token", Context.MODE_PRIVATE).edit().clear().apply();
        context.getSharedPreferences("sp_user", Context.MODE_PRIVATE).edit().clear().apply();
    }
}
