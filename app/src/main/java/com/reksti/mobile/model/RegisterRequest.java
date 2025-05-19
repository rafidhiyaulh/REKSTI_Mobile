package com.reksti.mobile.model;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest {
    @SerializedName("NIM")
    private String nim;

    @SerializedName("nama_lengkap")
    private String namaLengkap;

    @SerializedName("foto_wajah")
    private String fotoWajah;

    public RegisterRequest(String nim, String namaLengkap, String fotoWajah) {
        this.nim = nim;
        this.namaLengkap = namaLengkap;
        this.fotoWajah = fotoWajah;
    }
}
