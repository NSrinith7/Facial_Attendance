package com.faceattendance;

import static android.widget.Toast.LENGTH_SHORT;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.faceattendance.model.ResponseData;
import com.faceattendance.model.Utils;
import com.faceattendance.server.ApiService;
import com.faceattendance.server.RetroClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    Button login_btn;
    ProgressDialog pd;
    TextView tv_stuRegister;
    EditText etRollNumber,et_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etRollNumber = (EditText) findViewById(R.id.et_roll);
        et_password = (EditText) findViewById(R.id.et_password);
        tv_stuRegister=(TextView)findViewById(R.id.tv_stuRegister);
        tv_stuRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go=new Intent(LoginActivity.this, StudentRegisterActivity.class);
                startActivity(go);
            }
        });
        login_btn = findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etRollNumber.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please enter Student ID", LENGTH_SHORT).show();
                    return;
                }
                if(et_password.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please enter password", LENGTH_SHORT).show();
                    return;
                }

                pd= new ProgressDialog(LoginActivity.this);
                pd.setTitle("Loading...");
                pd.show();

                ApiService apiService = RetroClient.getRetrofitInstance().create(ApiService.class);
                Call<ResponseData> call = apiService.studentlogin(etRollNumber.getText().toString(),et_password.getText().toString());
                call.enqueue(new Callback<ResponseData>() {
                    @Override
                    public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                        pd.dismiss();
                        if (response.body().getStatus().equals("true")) {
                            SharedPreferences sharedPreferences = getSharedPreferences(Utils.SHREF, Context.MODE_PRIVATE);
                            SharedPreferences.Editor et=sharedPreferences.edit();
                            et.putString("user_uname",etRollNumber.getText().toString());
                            et.commit();
                            Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(LoginActivity.this, StudentHomeActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseData> call, Throwable t) {
                        pd.dismiss();
                        Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
