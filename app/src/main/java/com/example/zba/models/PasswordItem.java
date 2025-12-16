package com.example.zba.models;

public class PasswordItem {


    private String appName;

    private String userName;
    private String password;
    private  String id;

    // REQUIRED by Firestore
    public PasswordItem() {
    }
    public PasswordItem(String id, String appName, String userName, String password) {
        this.id = id;
        this.appName = appName;
        this.userName = userName;
        this.password = password;
    }

    public String getUserName(){return  this.userName;}
    public  void setUserName (String username) { userName = username; }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id=id;
    }
}
