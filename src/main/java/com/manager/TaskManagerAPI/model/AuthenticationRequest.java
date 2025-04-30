package com.manager.TaskManagerAPI.model;

public class AuthenticationRequest {

    private String Username;
    private String Password;

    public AuthenticationRequest() {}
    public AuthenticationRequest(String username, String password) {
        this.Username = username;
        this.Password = password;
    }

    // getters
    public String getUsername() {return Username;}
    public String getPassword() {return Password;}

    // setters
    public void setUsername(String username) {this.Username = username;}
    public void setPassword(String password) {this.Password = password;}
}
