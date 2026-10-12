package com.faceattendance;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FaceTrainingActivity extends AppCompatActivity {

    private static final String TAG = "FaceTrainingActivity";

    private FaceDetector faceDetector;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_training);
        ProgressBar progressBar = findViewById(R.id.progress_bar);
        TextView tvStatus = findViewById(R.id.tv_status);
        MaterialButton btnRetry = findViewById(R.id.btn_retry);

// Example usage in success/failure
        runOnUiThread(() -> {
            progressBar.setVisibility(View.GONE);
            tvStatus.setText("Face processing completed!\nReady for next step.");
            // btnRetry.setVisibility(View.VISIBLE); // if error
        });
        // Configure ML Kit Face Detector (high-accuracy mode for better landmarks/contours if needed)
        FaceDetectorOptions options = new FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)      // optional: eyes, nose, etc.
                .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)        // optional: face outline
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL) // smiling, eyes open
                .setMinFaceSize(0.15f)  // min face size relative to image
                .build();

        faceDetector = FaceDetection.getClient(options);

        Toast.makeText(this, "ML Kit Face Detector Ready", Toast.LENGTH_SHORT).show();

        // Start processing (replace with your real data loading)
        processTrainingImages();
    }

    private void processTrainingImages() {
        executor.execute(() -> {
            try {
                // In real app: fetch from API, database, or local files
                // Format: List of Base64 strings + person IDs
                List<String> base64Faces = new ArrayList<>();
                List<Integer> personIds = new ArrayList<>();

                // ------------------- REPLACE WITH REAL DATA -------------------
                // Add multiple images per person for better future recognition
                base64Faces.add("YOUR_BASE64_ENCODED_FACE_IMAGE_1"); // person 1
                personIds.add(1);
                base64Faces.add("YOUR_BASE64_ENCODED_FACE_IMAGE_2"); // person 1 again
                personIds.add(1);
                base64Faces.add("YOUR_BASE64_ENCODED_FACE_IMAGE_3"); // person 2
                personIds.add(2);
                // -------------------------------------------------------------

                if (base64Faces.isEmpty()) {
                    runOnUiThread(() -> Toast.makeText(this, "No images to process", Toast.LENGTH_LONG).show());
                    return;
                }

                int processed = 0;
                for (int i = 0; i < base64Faces.size(); i++) {
                    Bitmap bitmap = base64ToBitmap(base64Faces.get(i));
                    if (bitmap == null) {
                        Log.w(TAG, "Failed to decode Base64 at index " + i);
                        continue;
                    }

                    detectAndProcessFace(bitmap, personIds.get(i));
                    processed++;
                    bitmap.recycle(); // free memory
                }

                int finalProcessed = processed;
                runOnUiThread(() -> {
                    Toast.makeText(this, "Processed " + finalProcessed + " images. Ready for recognition setup.", Toast.LENGTH_LONG).show();
                    // Here: save cropped faces or embeddings to storage/DB
                    // e.g., saveCroppedFacesOrEmbeddings(...)
                });

            } catch (Exception e) {
                Log.e(TAG, "Processing failed", e);
                runOnUiThread(() -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        });
    }

    private void detectAndProcessFace(Bitmap bitmap, int personId) {
        InputImage image = InputImage.fromBitmap(bitmap, 0); // 0 = no rotation; adjust if needed

        faceDetector.process(image)
                .addOnSuccessListener(faces -> {
                    if (faces.isEmpty()) {
                        Log.w(TAG, "No face detected for person ID " + personId);
                        return;
                    }

                    for (Face face : faces) {
                        Rect bounds = face.getBoundingBox();
                        Log.i(TAG, "Face detected for ID " + personId + ": bounds=" + bounds);

                        // Optional: Crop the face region
                        Bitmap croppedFace = cropFace(bitmap, bounds);
                        if (croppedFace != null) {
                            // TODO: In future, generate embedding here (TFLite model)
                            // e.g., float[] embedding = getEmbeddingFromTFLite(croppedFace);
                            // Then save: saveEmbedding(personId, embedding);
                            croppedFace.recycle();
                        }

                        // Optional: Log extra info
                        if (face.getSmilingProbability() != null) {
                            Log.d(TAG, "Smiling prob: " + face.getSmilingProbability());
                        }
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Face detection failed", e));
    }

    private Bitmap base64ToBitmap(String base64) {
        try {
            byte[] decoded = Base64.decode(base64, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
        } catch (Exception e) {
            Log.e(TAG, "Base64 decode failed", e);
            return null;
        }
    }

    private Bitmap cropFace(Bitmap original, Rect bounds) {
        try {
            // Add some padding if needed
            int left = Math.max(0, bounds.left);
            int top = Math.max(0, bounds.top);
            int width = Math.min(bounds.width(), original.getWidth() - left);
            int height = Math.min(bounds.height(), original.getHeight() - top);

            return Bitmap.createBitmap(original, left, top, width, height);
        } catch (Exception e) {
            Log.e(TAG, "Crop failed", e);
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
        if (faceDetector != null) {
            faceDetector.close();
        }
    }
}