package com.faceattendance;

import android.app.ProgressDialog;
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

import com.bumptech.glide.Glide;
import com.faceattendance.model.Professor;
import com.faceattendance.model.ResponseData;
import com.faceattendance.model.Student;
import com.faceattendance.model.StudentAttendanceModel;
import com.faceattendance.server.ApiService;
import com.faceattendance.server.RetroClient;

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


public class ProfessorListActivity extends AppCompatActivity {
    RecyclerView rv;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_list);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainll), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        loadProfessors();
    }



    Call<List<Professor>> call;
    public void loadProfessors() {

        ApiService service = RetroClient.getRetrofitInstance().create(ApiService.class);
        call = service.getProfList();

        call.enqueue(new Callback<List<Professor>>() {
            @Override
            public void onResponse(Call<List<Professor>> call, Response<List<Professor>> response) {

                List<Professor> professors = response.body();
                Log.i("FA88","List");
                ProfessorListAdapter adapter = new ProfessorListAdapter(professors,ProfessorListActivity.this);
                rv.setAdapter(adapter);
                Toast.makeText(ProfessorListActivity.this, "Suucess: ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<Professor>> call, Throwable t) {
                Log.i("FA88",t.getMessage());
                Toast.makeText(ProfessorListActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}