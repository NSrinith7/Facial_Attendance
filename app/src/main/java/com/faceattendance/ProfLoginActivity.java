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
import androidx.cardview.widget.CardView;

import com.faceattendance.model.ProfResponseData;
import com.faceattendance.model.ResponseData;
import com.faceattendance.model.Utils;
import com.faceattendance.server.ApiService;
import com.faceattendance.server.RetroClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfLoginActivity extends AppCompatActivity {

    EditText email, pass;
    Button login_btn;

    TextView tv_profRegister;

    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_login);

        email=(EditText) findViewById(R.id.et_email);

        pass=(EditText) findViewById(R.id.et_password);

        tv_profRegister=(TextView) findViewById(R.id.tv_profRegister);
        tv_profRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go=new Intent(ProfLoginActivity.this, ProfRegisterActivity.class);
                startActivity(go);
            }
        });

        login_btn=(Button) findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().isEmpty()){
                    Toast.makeText(ProfLoginActivity.this, "Please enter email ID", LENGTH_SHORT).show();
                    return;
                }
                if(pass.getText().toString().isEmpty()){
                    Toast.makeText(ProfLoginActivity.this, "Please enter password", LENGTH_SHORT).show();
                    return;
                }

                pd= new ProgressDialog(ProfLoginActivity.this);
                pd.setTitle("Loading...");
                pd.show();

                ApiService apiService = RetroClient.getRetrofitInstance().create(ApiService.class);
                Call<ProfResponseData> call = apiService.proflogin(email.getText().toString(),pass.getText().toString());
                call.enqueue(new Callback<ProfResponseData>() {
                    @Override
                    public void onResponse(Call<ProfResponseData> call, Response<ProfResponseData> response) {
                        pd.dismiss();
                        if (response.body().getStatus().equals("true")) {
                            SharedPreferences sharedPreferences = getSharedPreferences(Utils.SHREF, Context.MODE_PRIVATE);
                            SharedPreferences.Editor et=sharedPreferences.edit();
                            et.putString("user_uname",email.getText().toString());
                            et.commit();
                            if (response.body().getIsActive().equals("yes")) {
                                Toast.makeText(ProfLoginActivity.this, "Login Successfully", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(ProfLoginActivity.this, DashboardActivity.class));
                                finish();
                            }else {
                                Toast.makeText(ProfLoginActivity.this, "You are blocked, please contact admin", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(ProfLoginActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ProfResponseData> call, Throwable t) {
                        pd.dismiss();
                        Toast.makeText(ProfLoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}