package com.faceattendance.server;

import com.faceattendance.model.Attendance;
import com.faceattendance.model.ProfResponseData;
import com.faceattendance.model.Professor;
import com.faceattendance.model.ResponseData;
import com.faceattendance.model.SDetails;
import com.faceattendance.model.Student;

import java.util.List;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface ApiService {

//    @FormUrlEncoded
//    @POST("/FaceAttendance/studentRegister.php")
//    Call<ResponseData> registerStudent(
//            @Field("roll_number") String roll,
//            @Field("name") String name,
//            @Field("email") String email,
//            @Field("phone") String phone,
//            @Field("department") String department,
//            @Field("course") String course,
//            @Field("face") String face);
//
@FormUrlEncoded
@POST("/FaceAttendance/studentAttandance.php")
Call<ResponseData> sendAttendance(@Field("attendance") String roll,
                                  @Field("subject") String subject,
                                  @Field("prof_email") String prof_email);

//    @FormUrlEncoded
//    @POST("/FaceAttendance/studentAttandance.php")
//    Call<ResponseData> sendAttendance(@Field("attendance") String roll);

    @Multipart
    @POST("/FaceAttendance/studentRegister.php")
    Call<ResponseData> registerStudent(
            @Part MultipartBody.Part file,
            @PartMap Map<String, String> partMap

    );

    @GET("/FaceAttendance/studentprofile.php")
    Call<List<Student>> getStudentProfile(@Query("roll_number") String roll_number);

//    @GET("/FaceAttendance/studentprofile.php")
//    Call<Student> getStudentProfile(@Query("roll_number") String roll_number);

    @GET("/FaceAttendance/studentlogin.php")
    Call<ResponseData> studentlogin(
            @Query("email") String email,
            @Query("pass") String pass
    );

    @GET("/FaceAttendance/profregister.php")
    Call<ResponseData> profregister(
            @Query("name") String name,
            @Query("email") String email,
            @Query("phone") String phone,
            @Query("pass") String pass,
            @Query("designation") String designation
    );

    @GET("/FaceAttendance/proflogin.php")
    Call<ProfResponseData> proflogin(
            @Query("email") String email,
            @Query("pass") String pass
    );

    @GET("/FaceAttendance/adminlogin.php")
    Call<ResponseData> adminlogin(
            @Query("email") String email,
            @Query("pass") String pass
    );

    @GET("/FaceAttendance/updateProfStatus.php")
    Call<ResponseData> updateProfStatus(
            @Query("id") String id,
            @Query("status") String status
    );

    @POST("group_attendance.php")
    Call<ResponseData> groupAttendance(
            @Body List<String> faces
    );
    @GET("FaceAttendance/profprofile.php")
    Call<List<Professor>> profprofile(@Query("email") String email);

    @GET("FaceAttendance/getAttendance.php")
    Call<List<Attendance>> getAttendance();

    @GET("FaceAttendance/getProfList.php")
    Call<List<Professor>> getProfList();


    @GET("FaceAttendance/getMyAttendance.php")
    Call<List<Attendance>> getMyAttendance(@Query("email") String email);


    @GET("FaceAttendance/getstudents.php")
    Call<List<SDetails>> getStudents();

}

