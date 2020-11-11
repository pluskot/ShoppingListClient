package com.example.patryk.shoppinglist.utils;

import com.example.patryk.shoppinglist.models.User;
import com.google.gson.annotations.Expose;

public class Token {
    @Expose
    private String token;
    @Expose
    private User user;

    private static Token activeToken = null;

    private Token(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static Token getInstance(){
        return activeToken;
    }

    public static void clearInstance() { activeToken = null;}

    public static void setActiveToken(Token token) { activeToken = token;}
}
