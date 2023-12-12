package com.client.api.request;

public class LoginRequest {

    private String username;

    private String password;

    public LoginRequest(String text, String s) {
        this.username = text;
        this.password = s;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
