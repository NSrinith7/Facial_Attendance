package com.faceattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.content.Intent;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.*;


import com.faceattendance.model.ResponseData;
import com.faceattendance.model.Utils;
import com.faceattendance.server.ApiService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.*;
import retrofit2.converter.gson.GsonConverterFactory;

public class StudentRegisterActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    EditText roll, name, email, phone, department, course;
    ImageView faceImage;
    Button select_image, registerBtn;

    private static final String TAG = "StudentRegister";
    private static final int REQUEST_GALLERY_CODE = 200;
    private static final int READ_REQUEST_CODE = 300;

    SharedPreferences sharedPreferences;
    String session;

    private static final String SERVER_PATH = "http://finalproject.live/";

    private Uri uri;
    private File file;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);

        roll = findViewById(R.id.roll);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        department = findViewById(R.id.department);
        course = findViewById(R.id.course);

        faceImage = findViewById(R.id.faceImage);
        select_image = findViewById(R.id.select_image);
        registerBtn = findViewById(R.id.btnregister);

        select_image.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_GALLERY_CODE);
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImageToServer();
            }
        });

    }

    private void saveToPublicFolder(Uri uri) {
        String rollNo = roll.getText().toString().trim();

        try {
            // Targets the root "Documents" folder
            File publicDocs = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS
            );

            // Create "Images" folder inside Documents
            File imagesFolder = new File(publicDocs, "Images");
            if (!imagesFolder.exists()) {
                imagesFolder.mkdirs();
            }

            // Create Roll Number subfolder
            File rollFolder = new File(imagesFolder, rollNo);
            if (!rollFolder.exists()) {
                rollFolder.mkdirs();
            }


            String fileName = "img_" + System.currentTimeMillis() + ".jpg";
            File destFile = new File(rollFolder, fileName);
            Bitmap bitmap = null;
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.Source source =
                            ImageDecoder.createSource(getContentResolver(), uri);
                    bitmap = ImageDecoder.decodeBitmap(source);
                } else {
                    bitmap = MediaStore.Images.Media.getBitmap(
                            getContentResolver(), uri
                    );
                }
                faceImage.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }

            InputStream inputStream = getContentResolver().openInputStream(uri);
            if (inputStream != null) {
                OutputStream outputStream = new FileOutputStream(destFile);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }

                outputStream.flush();
                outputStream.close();
                inputStream.close();

            }

            Toast.makeText(this,
                    "Saved to Documents/Images/" + rollNo,
                    Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GALLERY_CODE && resultCode == Activity.RESULT_OK && data != null) {
            uri = data.getData();

            // Show image preview
            faceImage.setImageURI(uri);
            String filePath = getRealPathFromURIPath(uri);
            if (filePath != null) {
                file = new File(filePath);
            }
            saveToPublicFolder(uri);

            if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                String filePath1 = getRealPathFromURIPath(uri);
                if (filePath1 != null) {
                    file = new File(filePath1);
                }
            } else {
                EasyPermissions.requestPermissions(
                        this,
                        "Permission needed to access gallery",
                        READ_REQUEST_CODE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                );
            }
        }
    }

    private String getRealPathFromURIPath(Uri contentURI) {
        String result = null;

        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor != null) {
            int index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            if (index != -1) {
                cursor.moveToFirst();
                result = cursor.getString(index);
            }
            cursor.close(); // IMPORTANT
        }
        return result;
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (uri != null) {
            String filePath = getRealPathFromURIPath(uri);
            if (filePath != null) {
                file = new File(filePath);
            }
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "Permission Denied");
    }

    private void uploadImageToServer() {

        if (file == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading...");
        pd.show();

        sharedPreferences = getSharedPreferences(Utils.SHREF, Context.MODE_PRIVATE);
        session = sharedPreferences.getString("user_uname", "def-val");

        Map<String, String> map = new HashMap<>();
        map.put("rollNo", roll.getText().toString().trim());
        map.put("studentName", name.getText().toString().trim());
        map.put("studentEmail", email.getText().toString().trim());
        map.put("studentPhone", phone.getText().toString().trim());
        map.put("studentDept", department.getText().toString().trim());
        map.put("studentCourse", course.getText().toString().trim());
        map.put("profEmail", session);

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("image/*"), file);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://finalproject.live/") // your domain
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);

        Call<ResponseData> call = service.registerStudent(body, map);

        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                pd.dismiss();

                if (response.body() != null) {
                    String message = response.body().getMessage();
                    Toast.makeText(StudentRegisterActivity.this, message, Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(StudentRegisterActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(StudentRegisterActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}