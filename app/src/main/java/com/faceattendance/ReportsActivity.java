package com.faceattendance;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.faceattendance.model.Attendance;
import com.faceattendance.server.ApiService;
import com.faceattendance.server.RetroClient;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.app.DatePickerDialog;
import android.widget.ImageView;
import java.util.Calendar;
import java.util.Locale;

public class ReportsActivity extends AppCompatActivity {

    ProgressDialog loading;
    ListView lv_attendance;
    EditText etSearch;
    ImageView ivCalendar;
    List<Attendance> data;
    AttendanceAdapter adapter; PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reports);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        findViewById(R.id.btnWeekly).setOnClickListener(v -> showWeeklyReport());
        findViewById(R.id.btnMonthly).setOnClickListener(v -> showMonthlyReport());
        lv_attendance = findViewById(R.id.lv_attendance);
        ivCalendar = findViewById(R.id.ivCalendar);
        etSearch = findViewById(R.id.etSearch);
        pieChart = findViewById(R.id.pieChart);
        data = new ArrayList<>();

        attendanceData();

        // Search filter
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adapter != null) {
                    adapter.filter(s.toString()); // 👈 custom filter method
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        ivCalendar.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    ReportsActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {

                        // ✅ Format date (match your API)
                        String date = selectedYear + "-" +
                                String.format("%02d", (selectedMonth + 1)) + "-" +
                                String.format("%02d", selectedDay);

                        // 👇 Put date in search box
                        etSearch.setText(date);

                        // 👇 Trigger filter
                        if (adapter != null) {
                            adapter.filter(date);
                        }
                    },
                    year, month, day
            );

            dialog.show();
        });
    }


    public void attendanceData() {

        loading = new ProgressDialog(ReportsActivity.this);
        loading.setTitle("Please wait, Data is Loading...");
        loading.show();

        ApiService service = RetroClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Attendance>> call = service.getAttendance();

        call.enqueue(new Callback<List<Attendance>>() {
            @Override
            public void onResponse(Call<List<Attendance>> call, Response<List<Attendance>> response) {
                loading.dismiss();

                if (response.body() == null || response.body().isEmpty()) {
                    Toast.makeText(ReportsActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                } else {
                    data = response.body();

                    adapter = new AttendanceAdapter(data, ReportsActivity.this);
                    lv_attendance.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Attendance>> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(ReportsActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showWeeklyReport() {

        List<Attendance> filtered = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, -7);
        Date weekAgo = calendar.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        for (Attendance item : data) {
            try {
                Date itemDate = sdf.parse(item.getAttendance_date());

                if (itemDate != null && itemDate.after(weekAgo) && itemDate.before(today)) {
                    filtered.add(item);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        updateChart(filtered);
    }

    private void showMonthlyReport() {

        List<Attendance> filtered = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        for (Attendance item : data) {
            try {
                Date itemDate = sdf.parse(item.getAttendance_date());

                if (itemDate != null) {
                    Calendar itemCal = Calendar.getInstance();
                    itemCal.setTime(itemDate);

                    if (itemCal.get(Calendar.MONTH) == currentMonth &&
                            itemCal.get(Calendar.YEAR) == currentYear) {

                        filtered.add(item);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        updateChart(filtered);
    }

    private void updateChart(List<Attendance> list) {

        int present = 0;
        int absent = 0;

        for (Attendance item : list) {
            if (item.getStatus().equals("1")) {
                present++;
            } else {
                absent++;
            }
        }

        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(present, "Present"));
        entries.add(new PieEntry(absent, "Absent"));

        PieDataSet dataSet = new PieDataSet(entries, "Attendance Report");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData pieData = new PieData(dataSet);
        pieData.setValueTextSize(14f);

        pieChart.setData(pieData);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.invalidate(); // refresh
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}