package com.faceattendance;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.faceattendance.model.SDetails;
import com.faceattendance.server.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DashboardActivity extends AppCompatActivity {
    Button btnMyprofile,btnStudentRegistration, btnAttendance,btnReports,btnLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        btnStudentRegistration = findViewById(R.id.btnStudentRegistration);
        btnAttendance = findViewById(R.id.btnAttendance);
        btnReports = findViewById(R.id.btnReports);

        btnLogout=(Button)findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(DashboardActivity.this, StartActivity.class);
                startActivity(intent);
            }
        });

        btnMyprofile=(Button)findViewById(R.id.btnMyprofile);
        btnMyprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(DashboardActivity.this, ProfProfile.class);
                startActivity(intent);
            }
        });


        btnStudentRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(DashboardActivity.this, StudentRegisterActivity.class);
                startActivity(intent);
            }
        });
        btnAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //val arrayList: ArrayList<SDetails> = ArrayList(list)
                ArrayList<SDetails>  students = new ArrayList(list);

                Intent intent= new Intent(DashboardActivity.this, MainActivity.class);
                intent.putParcelableArrayListExtra("student_list", students);
                startActivity(intent);
            }
        });
        btnReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(DashboardActivity.this, ReportsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStudents();
    }

    List<SDetails> list;
    private void loadStudents(){
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Getting Data...");
        pd.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://finalproject.live/") // your domain
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);

        Call<List<SDetails>> call = service.getStudents();

        call.enqueue(new Callback<List<SDetails>>() {
            @Override
            public void onResponse(Call<List<SDetails>> call, Response<List<SDetails>> response) {
                pd.hide();
                if (response.body() != null) {
                    list = response.body();
                    Toast.makeText(DashboardActivity.this, "Student : "+list.size(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(DashboardActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<SDetails>> call, Throwable t) {
                pd.hide();
                Toast.makeText(DashboardActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
