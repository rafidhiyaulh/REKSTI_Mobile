// ClassCreateResponse.java
package com.reksti.mobile.model;

import com.google.gson.annotations.SerializedName;

public class ClassCreateResponse {
    @SerializedName("status")
    private boolean status;

    @SerializedName("class_uid")
    private String classUid;

    public boolean isStatus() {
        return status;
    }

    public String getClassUid() {
        return classUid;
    }
}
