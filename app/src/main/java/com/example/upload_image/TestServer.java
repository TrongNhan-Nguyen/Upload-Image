package com.example.upload_image;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.upload_image.Api.Api_Server;
import com.example.upload_image.Api.Retrofit_Client;

public class TestServer extends AppCompatActivity {
    private Button btn_login, btn_logout, btn_refresh_token, btn_public_data, btn_auth_data;
    private Api_Server api_server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_server);
        api_server = Retrofit_Client.retrofit.create(Api_Server.class);
        initView();
    }

    private void initView() {
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_logout = (Button) findViewById(R.id.btn_logout);
        btn_refresh_token = (Button) findViewById(R.id.btn_refresh_token);
        btn_public_data = (Button) findViewById(R.id.btn_public_data);
        btn_auth_data = (Button) findViewById(R.id.btn_auth_data);
    }


    private void login() {

    }

    private void logout() {

    }

    private void refreshToken() {

    }

    private void getPublicData() {

    }

    private void getAuthData() {
    }

    private void setToken() {

    }

    private void getToken() {
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void clg(String msg) {
        Log.d("api", msg);
    }
}