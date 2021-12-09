package com.example.telegraph.model;

public class UserInfo {

    public String user_id;
    public String user_name;
    public String user_password;
    public String token;
    public String status;
    public String uri;
    public String lastmessage;

    public String getToken() {
        return token;
    }

    public UserInfo(String user_id, String user_name, String user_password, String token, String status, String uri, String lastmessage) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_password = user_password;
        this.token = token;
        this.status = status;
        this.uri = uri;
        this.lastmessage = lastmessage;
    }

    public UserInfo() {
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_password() {
        return user_password;
    }

    public String getUri() {
        return uri;
    }

    public String getLastmessage() {
        return lastmessage;
    }

    public String getStatus() {
        return status;
    }

}