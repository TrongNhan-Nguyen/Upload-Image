package com.example.upload_image;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.upload_image.Api.Api_Server;
import com.example.upload_image.Api.Retrofit_Client;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btn_refresh_token.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btn_public_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPublicData();
            }
        });

        btn_auth_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        getAuthData();
            }
        });
    }


    private void login() {
        String email = "user-test@keep-exploring.com";
        String pass = "123456";
        HashMap<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("pass", pass);

        Call<String> call = api_server.signIn(map);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    if (response.errorBody() != null) {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        clg(jObjError.getString("error"));
                    } else {
                        JSONObject responseData = new JSONObject(response.body());

                        if (responseData.has("error")){
                            JSONObject err = responseData.getJSONObject("error");
                            clg(err.toString());
                        }else {
                            JSONObject data = responseData.getJSONObject("data");
                            clg(data.toString());
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                clg(t.getMessage());
            }
        });

    }

    private void logout() {

    }

    private void refreshToken() {

    }

    private void getPublicData() {
        Call<String> call = api_server.getPublicData();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {

                    if (response.errorBody() != null) {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        clg(jObjError.getString("error"));
                    } else {
                        JSONObject responseData = new JSONObject(response.body());
                        clg(responseData.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                clg(t.getMessage());
            }
        });
    }

    private void getAuthData() {
        Call<String> call = api_server.getAuthData();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    if (response.errorBody() != null) {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        clg(jObjError.getString("error"));
                    } else {
                        JSONObject responseData = new JSONObject(response.body());
                        clg(responseData.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                clg(t.getMessage());
            }
        });
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