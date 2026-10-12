package com.faceattendance.model;

public class StudentAttendanceModel {

    private String id;
    private boolean isChecked;

    public StudentAttendanceModel(String id, boolean isChecked) {
        this.id = id;
        this.isChecked = isChecked;
    }

    public String getId() {
        return id;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
