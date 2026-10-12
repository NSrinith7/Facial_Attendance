package com.faceattendance;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.faceattendance.model.Attendance;

import java.util.ArrayList;
import java.util.List;

public class AttendanceAdapter extends BaseAdapter {

    Context context;

    List<Attendance> attendance;      // filtered list
    List<Attendance> originalList;    // original data

    public AttendanceAdapter(List<Attendance> attendance, Context context) {
        this.context = context;
        this.attendance = new ArrayList<>(attendance);
        this.originalList = new ArrayList<>(attendance);
    }

    @Override
    public int getCount() {
        return attendance.size();
    }

    @Override
    public Object getItem(int position) {
        return attendance.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // ViewHolder
    static class ViewHolder {

        TextView tvRollNo;
        TextView tvDate;
        TextView tvStatus;
        TextView tvSubject;
        TextView tvProfessorEmail;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.child_attendance, parent, false);

            holder = new ViewHolder();

            holder.tvRollNo = convertView.findViewById(R.id.tvRollNo);
            holder.tvDate = convertView.findViewById(R.id.tvDate);
            holder.tvStatus = convertView.findViewById(R.id.tvStatus);
            holder.tvSubject = convertView.findViewById(R.id.tvSubject);

            // NEW
            holder.tvProfessorEmail = convertView.findViewById(R.id.tvProfessorEmail);

            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        Attendance item = attendance.get(position);

        // Roll Number
        holder.tvRollNo.setText("Roll No : " + item.getRoll_no());

        // Date
        holder.tvDate.setText("Date : " + item.getAttendance_date());

        // Subject
        holder.tvSubject.setText("Class : " + item.getSubject());

        // Professor Email
        holder.tvProfessorEmail.setText(
                "Attendance Taken By Professor Email : " + item.getProf_email()
        );

        // Status
        try {

            int status = Integer.parseInt(item.getStatus());

            if (status == 1) {

                holder.tvStatus.setText("Present");
                holder.tvStatus.setTextColor(Color.parseColor("#4CAF50"));

            } else {

                holder.tvStatus.setText("Absent");
                holder.tvStatus.setTextColor(Color.parseColor("#F44336"));
            }

        } catch (Exception e) {

            holder.tvStatus.setText("Unknown");
            holder.tvStatus.setTextColor(Color.GRAY);
        }

        return convertView;
    }

    // FILTER METHOD
    public void filter(String text) {

        text = text.toLowerCase().trim();

        attendance.clear();

        if (text.isEmpty()) {

            attendance.addAll(originalList);

        } else {

            for (Attendance item : originalList) {

                String rollNo = item.getRoll_no() != null
                        ? item.getRoll_no().toLowerCase()
                        : "";

                String subject = item.getSubject() != null
                        ? item.getSubject().toLowerCase()
                        : "";

                String date = item.getAttendance_date() != null
                        ? item.getAttendance_date().toLowerCase()
                        : "";

                String profEmail = item.getProf_email() != null
                        ? item.getProf_email().toLowerCase()
                        : "";

                if (rollNo.contains(text) ||
                        subject.contains(text) ||
                        date.contains(text) ||
                        profEmail.contains(text)) {

                    attendance.add(item);
                }
            }
        }

        notifyDataSetChanged();
    }
}