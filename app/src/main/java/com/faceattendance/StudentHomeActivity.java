package com.faceattendance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class StudentHomeActivity extends AppCompatActivity {
    Button btnMyprofile, btnReports, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);
        btnMyprofile = findViewById(R.id.btnMyprofile);
        btnReports = findViewById(R.id.btnReports);

        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentHomeActivity.this, StudentAttendanceReports.class);
                startActivity(intent);
            }
        });

        btnMyprofile = (Button) findViewById(R.id.btnMyprofile);
        btnMyprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentHomeActivity.this, StudentProfileActivity.class);
                startActivity(intent);
            }
        });

        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentHomeActivity.this, StartActivity.class);
                startActivity(intent);
            }
        });
    }
}