package com.reksti.mobile.model;

import java.util.List;

public class Student {
    private String name;
    private List<ClassAttendance> classes;

    public String getName() {
        return name;
    }

    public List<ClassAttendance> getClasses() {
        return classes;
    }
}
