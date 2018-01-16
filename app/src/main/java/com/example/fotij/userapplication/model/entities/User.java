package com.example.fotij.userapplication.model.entities;

/**
 * Created by fotij on 09/01/2018.
 */

public class User {
    private String password;
    private String username;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User() {
    }

    public User(String password, String username) {
        this.password = password;
        this.username = username;
    }
    public User(User other) {
        this.password=other.password;
        this.username=other.username;
    }





}
