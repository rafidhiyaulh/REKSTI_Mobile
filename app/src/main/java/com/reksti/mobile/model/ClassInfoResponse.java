// ClassInfoResponse.java
package com.reksti.mobile.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;

public class ClassInfoResponse {
    @SerializedName("students")
    private List<Map<String, Object>> students;

    public List<Map<String, Object>> getStudents() {
        return students;
    }
}
