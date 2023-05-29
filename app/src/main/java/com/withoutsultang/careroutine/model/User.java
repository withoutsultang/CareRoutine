package com.withoutsultang.careroutine.model;

import androidx.databinding.ObservableField;

public class User {
    private String Email;
    private String PW;
    private String Name;
    private String Birth;

    public User() {
        // 기본 생성자 필요
    }

    public User(String s, String s1, String s2, String s3) {
        this.Email = s;
        this.PW = s1;
        this.Name = s2;
        this.Birth = s3;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPW() {
        return PW;
    }

    public void setPW(String PW) {
        this.PW = PW;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getBirth() {
        return Birth;
    }

    public void setBirth(String birth) {
        Birth = birth;
    }
}

