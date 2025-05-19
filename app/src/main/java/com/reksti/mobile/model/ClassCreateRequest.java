package com.reksti.mobile.model;

import java.util.List;

public class ClassCreateRequest {
    private List<String> student;

    public ClassCreateRequest(List<String> student) {
        this.student = student;
    }

    public List<String> getStudent() {
        return student;
    }

    public void setStudent(List<String> student) {
        this.student = student;
    }
}
