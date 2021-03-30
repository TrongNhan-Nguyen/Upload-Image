package com.example.upload_image;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.upload_image.Api.Api_Server;
import com.example.upload_image.Api.Retrofit_Client;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestServer extends AppCompatActivity {
    private Button btn_login, btn_logout, btn_register, btn_refresh_token, btn_public_data, btn_auth_data;
    private ImageView img_register;
    private Api_Server api_server;
    private SP_Helper sp_helper;
    private Image_Helper image_helper;
    private String realPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_server);
        api_server = Retrofit_Client.retrofit.create(Api_Server.class);
        sp_helper = new SP_Helper(this);
        image_helper = new Image_Helper(this);
        initView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && data.getData() != null) {
            Uri uri = data.getData();
            img_register.setImageURI(uri);
            realPath = image_helper.getPathFromUri(uri);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initView() {
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_logout = (Button) findViewById(R.id.btn_logout);
        btn_refresh_token = (Button) findViewById(R.id.btn_refresh_token);
        btn_public_data = (Button) findViewById(R.id.btn_public_data);
        btn_auth_data = (Button) findViewById(R.id.btn_auth_data);
        btn_register = (Button) findViewById(R.id.btn_register);
        img_register = (ImageView) findViewById(R.id.img_register);


        img_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        btn_refresh_token.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshToken();
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

                        if (responseData.has("error")) {
                            JSONObject err = responseData.getJSONObject("error");
                            clg(err.toString());
                        } else {
                            JSONObject data = responseData.getJSONObject("data");
                            String accessToken = data.getString("accessToken");
                            String refreshToken = data.getString("refreshToken");
                            String userId = data.getString("_id");

                            sp_helper.setAccessToken(accessToken);
                            sp_helper.setRefreshToken(refreshToken);
                            sp_helper.setUserId(userId);
                            clg("Access Token:\n" + sp_helper.getAccessToken());
                            clg("Refresh Token:\n" + sp_helper.getRefreshToken());
                            clg("User ID:\n" + sp_helper.getUserId());
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
        String userId = sp_helper.getUserId();
        HashMap<String, String> map = new HashMap<>();
        map.put("userId", userId);
        Call<String> call = api_server.signOut(map);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    if (response.errorBody() != null) {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        clg(jObjError.getString("error"));
                    } else {
                        JSONObject responseData = new JSONObject(response.body());

                        if (responseData.has("error")) {
                            JSONObject err = responseData.getJSONObject("error");
                            clg(err.toString());
                        } else {
                            sp_helper.clearSP();
                            clg(responseData.toString());
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

    private void register() {
        RequestBody requestBody;
        if (realPath == null) {
            requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), "");
        } else {
            File file = new File(realPath);
            requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        }
        MultipartBody.Part singleImage = MultipartBody.Part.createFormData("image_user", realPath, requestBody);
        RequestBody username = RequestBody.create(MediaType.parse("text/plain"), "user-android");
        RequestBody password = RequestBody.create(MediaType.parse("text/plain"), "123456");
        RequestBody displayName = RequestBody.create(MediaType.parse("text/plain"), "User Android");

        Call<String> call = api_server.register(username, password,displayName, singleImage);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    if (response.errorBody() != null) {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        clg(jObjError.getString("error"));
                    } else {
                        JSONObject responseData = new JSONObject(response.body());

                        if (responseData.has("error")) {
                            JSONObject err = responseData.getJSONObject("error");
                            clg(err.toString());
                        } else {
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
    private void refreshToken() {
        String refreshToken = sp_helper.getRefreshToken();
        String userId = sp_helper.getUserId();
        HashMap<String, String> map = new HashMap<>();
        map.put("refreshToken", refreshToken);
        map.put("userId", userId);

        Call<String> call = api_server.refreshToken(map);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    if (response.errorBody() != null) {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        clg(jObjError.getString("error"));
                    } else {
                        JSONObject responseData = new JSONObject(response.body());

                        if (responseData.has("error")) {
                            JSONObject err = responseData.getJSONObject("error");
                            clg(err.toString());
                        } else {
                            String newAccessToken = responseData.getString("data");
                            sp_helper.setAccessToken(newAccessToken);
                            clg("New Access Token:\n" + sp_helper.getAccessToken());
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
        Call<String> call = api_server.getAuthData(sp_helper.getAccessToken());
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

    private void clg(String msg) {
        Log.d("api", msg);
    }
}