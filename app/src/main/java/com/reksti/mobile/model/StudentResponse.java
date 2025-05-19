package com.reksti.mobile.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class StudentResponse {
    @SerializedName("nama")
    private String nama;

    @SerializedName("class_uids")
    private List<String> classUids;

    public String getNama() {
        return nama;
    }

    public List<String> getClassUids() {
        return classUids;
    }
}
