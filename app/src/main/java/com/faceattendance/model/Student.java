package com.faceattendance.model;

import com.google.gson.annotations.SerializedName;

public class Student {

    @SerializedName("student_id")
    private String student_id;

    @SerializedName("roll_number")
    private String roll_number;

    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

    @SerializedName("phone")
    private String phone;

    @SerializedName("department")
    private String department;

    @SerializedName("course")
    private String course;

    @SerializedName("face")
    private String face;

    // 🔹 Empty Constructor (Required)
    public Student() {
    }

    // 🔹 Full Constructor
    public Student(String student_id, String roll_number, String name, String email,
                   String phone, String department, String course, String face) {
        this.student_id = student_id;
        this.roll_number = roll_number;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.course = course;
        this.face = face;
    }

    // 🔹 Getters & Setters

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getRoll_number() {
        return roll_number;
    }

    public void setRoll_number(String roll_number) {
        this.roll_number = roll_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }
}
