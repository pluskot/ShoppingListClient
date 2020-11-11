package com.example.patryk.shoppinglist.models;

import com.google.gson.annotations.Expose;

public class User {
    @Expose
    private String password;
    @Expose
    private String username;
    @Expose
    private String email;
    @Expose
    private int id;

    public User(String username, String email, String password, int id) {
        this.password = password;
        this.username = username;
        this.email = email;
        this.id = id;
    }

    public User(int id) {
        this.id = id;
    }

    public User(String username, String email, String password) {
        this.password = password;
        this.username = username;
        this.email = email;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
