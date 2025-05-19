package com.reksti.mobile.model;

public class AttendRequest {
    private String class_uid;
    private String foto_wajah;

    public AttendRequest(String class_uid, String foto_wajah) {
        this.class_uid = class_uid;
        this.foto_wajah = foto_wajah;
    }

    public String getClass_uid() { return class_uid; }
    public String getFoto_wajah() { return foto_wajah; }
}
