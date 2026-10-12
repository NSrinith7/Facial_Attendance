package com.faceattendance.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SDetails(

    @SerializedName("student_id")
    val studentId: String,

    @SerializedName("roll_number")
    val rollNumber: String,

    val name: String,
    val email: String,
    val phone: String,
    val department: String,
    val course: String,
    val face: String
) : Parcelable