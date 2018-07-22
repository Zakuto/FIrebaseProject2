package com.example.aryantotaharuddin.firebaseproject;

/**
 * Created by aryanto.taharuddin on 6/5/2018.
 */

public class User {
    private String name;
    private String email;

    public User(){

    }

    public User(String name, String email){
        this.name = name;
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
