package com.example.patryk.shoppinglist.models;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class WholeShoppingList {
    @Expose
    private String shopping_list;
    @Expose
    private int user_id;
    @Expose
    private List<SimpleEntry> entries;

    public WholeShoppingList(User user, ShoppingList shoppingList, List<Entry> entries) {
        this.user_id = user.getId();
        this.shopping_list = shoppingList.getName();
        this.entries = new ArrayList<>();
        if (!entries.isEmpty()) {
            for (Entry entry : entries) {
                this.entries.add(new SimpleEntry(entry));
            }
        }
    }

    public String getShopping_list() {
        return shopping_list;
    }

    public void setShopping_list(String shopping_list) {
        this.shopping_list = shopping_list;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public List<SimpleEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<SimpleEntry> entries) {
        this.entries = entries;
    }
}
