package com.faceattendance;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.faceattendance.model.Professor;
import com.faceattendance.model.Utils;
import com.faceattendance.server.ApiService;
import com.faceattendance.server.RetroClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfProfile extends AppCompatActivity {

    ProgressDialog progressDialog;
    List<Professor> a1;
    EditText et_name, et_email, et_phone, et_password, et_designation;
    SharedPreferences sharedPreferences;
    String session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_profile);
        sharedPreferences = getSharedPreferences(Utils.SHREF, Context.MODE_PRIVATE);
        session = sharedPreferences.getString("user_uname", "def-val");
        et_name = (EditText) findViewById(R.id.et_name);
        et_email = (EditText) findViewById(R.id.et_email);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_password = (EditText) findViewById(R.id.et_password);
        et_designation = (EditText) findViewById(R.id.et_designation);

        progressDialog = new ProgressDialog(ProfProfile.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        ApiService service = RetroClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Professor>> call = service.profprofile(session);

        call.enqueue(new Callback<List<Professor>>() {
            @Override
            public void onResponse(Call<List<Professor>> call, Response<List<Professor>> response) {

                progressDialog.dismiss();
                a1 = response.body();
                Professor user = a1.get(0);

                et_name.setText(user.getName());
                et_email.setText(user.getEmail());
                et_password.setText(user.getPass());
                et_phone.setText(user.getPhone());
                et_designation.setText(user.getDesignation());

            }

            @Override
            public void onFailure(Call<List<Professor>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ProfProfile.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}