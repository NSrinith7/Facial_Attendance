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

public class ProfRegisterActivity  extends AppCompatActivity {
    Button register_btn;
    ProgressDialog pd;
    TextView tv_profLogin;
    EditText et_name, et_email,et_phone,et_password,et_designation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_register);

        et_name = (EditText) findViewById(R.id.et_name);
        et_email = (EditText) findViewById(R.id.et_email);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_password = (EditText) findViewById(R.id.et_password);
        et_designation = (EditText) findViewById(R.id.et_designation);

        register_btn=(Button) findViewById(R.id.register_btn);

        tv_profLogin=(TextView)findViewById(R.id.tv_profLogin);
        tv_profLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go=new Intent(ProfRegisterActivity.this, ProfLoginActivity.class);
                startActivity(go);
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_name.getText().toString().isEmpty()){
                    Toast.makeText(ProfRegisterActivity.this, "Please enter Name", LENGTH_SHORT).show();
                    return;
                }
                if(et_password.getText().toString().isEmpty()){
                    Toast.makeText(ProfRegisterActivity.this, "Please enter password", LENGTH_SHORT).show();
                    return;
                }
                if(et_email.getText().toString().isEmpty()){
                    Toast.makeText(ProfRegisterActivity.this, "Please enter Email", LENGTH_SHORT).show();
                    return;
                }
                if(et_phone.getText().toString().isEmpty()){
                    Toast.makeText(ProfRegisterActivity.this, "Please enter Phone", LENGTH_SHORT).show();
                    return;
                }
                if(et_designation.getText().toString().isEmpty()){
                    Toast.makeText(ProfRegisterActivity.this, "Please enter Designation", LENGTH_SHORT).show();
                    return;
                }


                pd= new ProgressDialog(ProfRegisterActivity.this);
                pd.setTitle("Loading...");
                pd.show();

                ApiService apiService = RetroClient.getRetrofitInstance().create(ApiService.class);
                Call<ResponseData> call = apiService.profregister(et_name.getText().toString(),et_email.getText().toString(),et_phone.getText().toString(),et_password.getText().toString(),et_designation.getText().toString());
                call.enqueue(new Callback<ResponseData>() {
                    @Override
                    public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                        pd.dismiss();
                        if (response.body().getStatus().equals("true")) {
                            Toast.makeText(ProfRegisterActivity.this, "Login Successfully", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(ProfRegisterActivity.this, ProfLoginActivity.class));
                        } else {
                            Toast.makeText(ProfRegisterActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseData> call, Throwable t) {
                        pd.dismiss();
                        Toast.makeText(ProfRegisterActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}