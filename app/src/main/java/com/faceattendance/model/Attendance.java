package com.faceattendance.model;

public class Attendance {

    String roll_no;
    String attendance_date;
    String status;
    String created_at;
    String subject;

    // NEW FIELD
    String prof_email;

    public String getRoll_no() {
        return roll_no;
    }

    public void setRoll_no(String roll_no) {
        this.roll_no = roll_no;
    }

    public String getAttendance_date() {
        return attendance_date;
    }

    public void setAttendance_date(String attendance_date) {
        this.attendance_date = attendance_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    // NEW GETTER & SETTER
    public String getProf_email() {
        return prof_email;
    }

    public void setProf_email(String prof_email) {
        this.prof_email = prof_email;
    }
}