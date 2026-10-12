package com.faceattendance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class StartActivity  extends AppCompatActivity {
    CardView cardProf,cardStudent,cardAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        cardProf=(CardView) findViewById(R.id.cardProf);
        cardProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go=new Intent(StartActivity.this, ProfLoginActivity.class);
                startActivity(go);

            }
        });

        cardStudent=(CardView) findViewById(R.id.cardStudent);
        cardStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go=new Intent(StartActivity.this, LoginActivity.class);
                startActivity(go);
            }
        });

        cardAdmin=(CardView) findViewById(R.id.cardAdmin);
        cardAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go=new Intent(StartActivity.this, AdminLoginActivity.class);
                startActivity(go);
            }
        });


    }
}
