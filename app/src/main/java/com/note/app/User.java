package com.note.app;

public class User {
    public String name,email,password;
    public int mobileNo;
    User(){}

    public User(String name,String email,int mobileNo,String password){
        this.name=name;
        this.email=email;
        this.mobileNo=mobileNo;
        this.password=password;
    }
}
