package com.reksti.mobile.network;

import com.reksti.mobile.model.Student;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("student")
    Call<Student> getStudent();

    @GET("face_logs")
    Call<List<Object>> getFaceLogs(); // Ganti Object dengan model FaceLog jika nanti kamu buat
}
