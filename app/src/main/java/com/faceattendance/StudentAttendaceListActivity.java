package com.faceattendance;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faceattendance.model.ResponseData;
import com.faceattendance.model.SDetails;
import com.faceattendance.model.StudentAttendanceModel;
import com.faceattendance.model.Utils;
import com.faceattendance.server.ApiService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StudentAttendaceListActivity extends AppCompatActivity {
    RecyclerView rv;
    Button btnSubmit;

    SharedPreferences sharedPreferences;
    String session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_attendace_list);
        String attendancies = getIntent().getStringExtra("attendencies");
        String subject = getIntent().getStringExtra("subject");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainll), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        rv = (RecyclerView)findViewById(R.id.rv);
        btnSubmit = (Button)findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitAttendance(attendancies,subject);
            }
        });

        List<StudentAttendanceModel> studentList = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(attendancies);

            Iterator<String> keys = jsonObject.keys();

            while (keys.hasNext()) {
                String key = keys.next();
                boolean value = jsonObject.getBoolean(key);

                studentList.add(new StudentAttendanceModel(key, value));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        StudentAttendanceAdapter adapter = new StudentAttendanceAdapter(studentList);

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
    }


    private void submitAttendance(String attedance,String subject){
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Submitting Data...");
        pd.show();

        sharedPreferences = getSharedPreferences(Utils.SHREF, Context.MODE_PRIVATE);
        session = sharedPreferences.getString("user_uname", "def-val");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://finalproject.live/") // your domain
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);

        Call<ResponseData> call = service.sendAttendance(attedance,subject,session);

        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                pd.hide();
                Toast.makeText(StudentAttendaceListActivity.this, "Student details are submitted successfully. ", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                pd.hide();
                Toast.makeText(StudentAttendaceListActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}

