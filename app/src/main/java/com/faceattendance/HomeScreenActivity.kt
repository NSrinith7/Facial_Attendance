package com.faceattendance;

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity


class HomeScreenActivity : AppCompatActivity() {

    private lateinit var btnStudentRegistration: Button
    private lateinit var btnAttendance: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btnStudentRegistration = findViewById(R.id.btnStudentRegistration)
        btnStudentRegistration.setOnClickListener {
           startActivity(Intent(this, StudentRegisterActivity::class.java))

        }
        btnAttendance = findViewById(R.id.btnAttendance)
        btnAttendance.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}

