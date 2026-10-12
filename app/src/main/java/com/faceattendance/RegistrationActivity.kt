package com.faceattendance;

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File

class RegistrationActivity : AppCompatActivity() {
    private lateinit var etRollNo: EditText
    private lateinit var btnSubmit: Button
    private lateinit var tv1: TextView
    private lateinit var tv2: TextView
    private lateinit var tv3: TextView
    private val getImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            saveToPublicFolder(it)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        etRollNo = findViewById(R.id.etRollNo)
        btnSubmit = findViewById(R.id.btnSubmit)
        tv1 = findViewById(R.id.tv1)
        tv2 = findViewById(R.id.tv2)
        tv3 = findViewById(R.id.tv3)
        btnSubmit.setOnClickListener {
            val rollNo = etRollNo.text.toString().trim()

            if (rollNo.isEmpty()) {
                Toast.makeText(this, "Enter Roll Number", Toast.LENGTH_SHORT).show()
            } else {
                getImage.launch("image/*") // 👈 Opens gallery
            }
        }
    }


    private fun saveToPublicFolder(uri: Uri) {
        val rollNo = etRollNo.text.toString().trim()

        try {
            // Targets the root "Documents" folder visible to all file managers
            val publicDocs =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)

            // Create "Images" folder inside Documents
            val imagesFolder = File(publicDocs, "Images")
            if (!imagesFolder.exists()) imagesFolder.mkdirs()

            tv1.text = "ImgF${imagesFolder.exists()}"
            // Create Roll Number subfolder
            val rollFolder = File(imagesFolder, rollNo)
            if (!rollFolder.exists()) rollFolder.mkdirs()

            tv2.text = "ImgR${rollFolder.exists()}"

            val fileName = "img_${System.currentTimeMillis()}.jpg"
            val destFile = File(rollFolder, fileName)

            contentResolver.openInputStream(uri)?.use { input ->
                destFile.outputStream().use { output ->
                    input.copyTo(output)
                    tv3.text = "CopyTo}"
                }
            }
            Toast.makeText(this, "Saved to Documents/Images/$rollNo", Toast.LENGTH_LONG).show()

        } catch (e: Exception) {
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
        }
    }
}