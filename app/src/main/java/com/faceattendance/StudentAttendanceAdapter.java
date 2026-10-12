package com.faceattendance;


import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.faceattendance.model.StudentAttendanceModel;

import java.util.List;

public class StudentAttendanceAdapter extends RecyclerView.Adapter<StudentAttendanceAdapter.ViewHolder> {

    private List<StudentAttendanceModel> list;

    public StudentAttendanceAdapter(List<StudentAttendanceModel> list) {
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        CheckBox checkAttendance;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvName.setTextColor(Color.BLACK);
            checkAttendance = itemView.findViewById(R.id.checkAttendance);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        StudentAttendanceModel student = list.get(position);

        holder.tvName.setText(student.getId());

        // IMPORTANT: remove old listener (recycler issue fix)

        holder.checkAttendance.setChecked(student.isChecked());
        holder.checkAttendance.setEnabled(false);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
