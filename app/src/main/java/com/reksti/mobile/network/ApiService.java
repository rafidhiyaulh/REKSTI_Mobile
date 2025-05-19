package com.reksti.mobile.network;

import com.reksti.mobile.model.AttendRequest;
import com.reksti.mobile.model.AttendResponse;
import com.reksti.mobile.model.ClassCreateRequest;
import com.reksti.mobile.model.ClassCreateResponse;
import com.reksti.mobile.model.ClassInfoResponse;
import com.reksti.mobile.model.RegisterRequest;
import com.reksti.mobile.model.RegisterResponse;
import com.reksti.mobile.model.StudentResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {

    @POST("/register")
    Call<RegisterResponse> registerStudent(@Body RegisterRequest request);

    @GET("/student")
    Call<StudentResponse> getStudent(@Header("NIM") String nim);

    @POST("/attend")
    Call<AttendResponse> attendClass(@Body AttendRequest request);

    @POST("/class")
    Call<ClassCreateResponse> createClass(@Body ClassCreateRequest request);

    @GET("/class")
    Call<ClassInfoResponse> getClass(@Header("class-uid") String classUid);

}
