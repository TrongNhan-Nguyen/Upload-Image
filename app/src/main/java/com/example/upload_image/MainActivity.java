package com.example.upload_image;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.upload_image.Api.Api;
import com.example.upload_image.Api.Retrofit_Client;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private Button btn_single, btn_multi, btn_submit;
    private TextView tv_username, tv_email;
    private ImageView img_upload;
    private Api api;
    private String realPath = "";
    private List<String> imagesEncodedList;
    String imageEncoded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        runtimePermission();
        initVew();
    }

    private void initVew() {
        api = Retrofit_Client.retrofit.create(Api.class);
        btn_single = (Button) findViewById(R.id.btn_single);
        btn_multi = (Button) findViewById(R.id.btn_multi);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_email = (TextView) findViewById(R.id.tv_email);
        img_upload = (ImageView) findViewById(R.id.img_upload);


        img_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });


        btn_single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadSingle();
            }
        });
        btn_multi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                toast("Upload Submit");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        imagesEncodedList = new ArrayList<>();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        if (requestCode == 1 && data.getData() != null) {
            Uri uri = data.getData();
            img_upload.setImageURI(uri);
            realPath = getPathFromUri(uri);
        } else if (requestCode == 1 && data.getClipData() != null) {
            ClipData mClipData = data.getClipData();
            ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
            for (int i = 0; i < mClipData.getItemCount(); i++) {

                ClipData.Item item = mClipData.getItemAt(i);
                Uri uri = item.getUri();
                realPath = getPathFromUri(uri);
                imagesEncodedList.add(realPath);
//                mArrayUri.add(uri);
//                Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
//                cursor.moveToFirst();
//
//                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                imageEncoded  = cursor.getString(columnIndex);
//                imagesEncodedList.add(imageEncoded);
//                cursor.close();
            }
            log("Selected Images" + imagesEncodedList.toString());

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void uploadSingle() {

        File file = new File(realPath);
        RequestBody requestBody;
        if (realPath.isEmpty()) {
            requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), "");
        } else {
            requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        }
        MultipartBody.Part singleImage = MultipartBody.Part.createFormData("single", realPath, requestBody);
        RequestBody username = RequestBody.create(MediaType.parse("text/plain"), "Nguyen Trong Nhan");
        RequestBody password = RequestBody.create(MediaType.parse("text/plain"), "!@#sfdewfe(K:WNN");
        Call<String> call = api.uploadSingle(username, password, singleImage);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.errorBody() == null) {
                    log("successfully Upload Single");
                } else {
                    log("failed Upload Single");
                    log(response.toString());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                log(t.getMessage());
            }
        });


    }

    private void uploadMulti() {
//        List<File> fileList = new ArrayList<>();
//        File file;
//        for (int i =0; i < imagesEncodedList.size(); i++){
//           file = new File(imagesEncodedList.get(i));
//           fileList.add(file);
//        }
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        for (int i = 0; i < imagesEncodedList.size(); i++) {
            File file = new File(imagesEncodedList.get(i));
            RequestBody requestBody = RequestBody.create(
                    MediaType.parse("image/*"),
                    file);
            builder.addFormDataPart("uploaded_file", file.getName(), requestBody);
        }
        MultipartBody requestBody = builder.build();
        RequestBody username = RequestBody.create(MediaType.parse("text/plain"), "Nguyen Trong Nhan");
        RequestBody password = RequestBody.create(MediaType.parse("text/plain"), "!@#sfdewfe(K:WNN");
        Call<String> call = api.uploadSingle(requestBody);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.errorBody() == null) {
                    log("successfully Upload Single");
                } else {
                    log("failed Upload Single");
                    log(response.toString());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                log(t.getMessage());
            }
        });


    }

    private void testApi() {
        Call<String> call = api.getDataFromServer();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {

                    if (response.errorBody() != null) {
//                        Bat loi cua server minh o day
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        log(jObjError.getString("error"));
                    } else {
//                        Neu khong co loi thi se nhay vao day
                        JSONObject responseData = new JSONObject(response.body());
                        log(responseData.toString());
                    }

//                    JSONObject responseData = new JSONObject(response.body());
//                    log(responseData.toString());
//                    JSONObject user = responseData.getJSONObject("user");
//                    JSONArray userList = responseData.getJSONArray("userList");
//                    log(user.toString());
//                    log(userList.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                log(t.getMessage());
            }
        });
    }

    private String getPathFromUri(Uri uri) {
        final String docId = DocumentsContract.getDocumentId(uri);
        final String[] split = docId.split(":");
        final String type = split[0];
        Uri contentUri = null;
        if ("image".equals(type)) {
            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else if ("video".equals(type)) {
            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        } else if ("audio".equals(type)) {
            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }
        final String selection = "_id=?";
        final String[] selectionArgs = new String[]{
                split[1]
        };
        return getDataColumn(contentUri, selection, selectionArgs);

    }

    public String getDataColumn(Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };
        try {
            cursor = this.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private void runtimePermission() {
        Dexter.withContext(this).withPermissions(
                Manifest.permission.INTERNET,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                if (multiplePermissionsReport.areAllPermissionsGranted()) {
                    log("All permissions granted");
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void log(String msg) {
        Log.d("Message", msg);
    }
}