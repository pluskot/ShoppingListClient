package com.example.patryk.shoppinglist.models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShoppingList implements Serializable {
    @Expose
    private int id;
    @Expose
    private String name;
    private User user;
    public static List<ShoppingList> currentShoppingLists = new ArrayList<>();

    public ShoppingList(String name, User user, int id) {
        this.name = name;
        this.user = user;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
