package com.faceattendance;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.faceattendance.model.Student;
import com.faceattendance.model.Utils;
import com.faceattendance.server.ApiService;
import com.faceattendance.server.RetroClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentProfileActivity extends AppCompatActivity {

    // UI
    ImageView imgProfile;
    TextView tvName, tvRoll, tvEmail, tvPhone, tvDept, tvCourse;
    ProgressBar progressBar;

    // Session
    SharedPreferences sharedPreferences;
    String session;

    // API Call reference
    Call<List<Student>> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        // Initialize UI
        imgProfile = findViewById(R.id.imgProfile);
        tvName = findViewById(R.id.tvName);
        tvRoll = findViewById(R.id.tvRoll);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvDept = findViewById(R.id.tvDept);
        tvCourse = findViewById(R.id.tvCourse);
        progressBar = findViewById(R.id.progressBar);

        // Get session
        sharedPreferences = getSharedPreferences(Utils.SHREF, Context.MODE_PRIVATE);
        session = sharedPreferences.getString("user_uname", "");

        loadProfile(session);
    }

    private void loadProfile(String session) {

        if (session == null || session.isEmpty()) {
            Toast.makeText(this, "Session expired!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        ApiService service = RetroClient.getRetrofitInstance().create(ApiService.class);
        call = service.getStudentProfile(session);

        call.enqueue(new Callback<List<Student>>() {
            @Override
            public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {

                if (isFinishing() || isDestroyed()) return;

                progressBar.setVisibility(View.GONE);

                if (!response.isSuccessful() || response.body() == null || response.body().isEmpty()) {
                    Toast.makeText(StudentProfileActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                    return;
                }

                Student student = response.body().get(0);

                // Set Data
                tvName.setText("Name: " + student.getName());
                tvRoll.setText("Roll No: " + student.getRoll_number());
                tvEmail.setText("Email: " + student.getEmail());
                tvPhone.setText("Phone: " + student.getPhone());
                tvDept.setText("Department: " + student.getDepartment());
                tvCourse.setText("Course: " + student.getCourse());

                // Load Image
                if (student.getFace() != null && !student.getFace().isEmpty()) {
                    Glide.with(StudentProfileActivity.this)
                            .load("http://finalproject.live/FaceAttendance/" + student.getFace())
                            .placeholder(R.drawable.no_image)
                            .error(R.drawable.no_image)
                            .into(imgProfile);
                }
            }

            @Override
            public void onFailure(Call<List<Student>> call, Throwable t) {

                if (isFinishing() || isDestroyed()) return;

                progressBar.setVisibility(View.GONE);
                Toast.makeText(StudentProfileActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (call != null && !call.isCanceled()) {
            call.cancel();
        }
    }
}