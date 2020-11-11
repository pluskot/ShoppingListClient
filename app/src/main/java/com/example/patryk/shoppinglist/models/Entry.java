package com.example.patryk.shoppinglist.models;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class Entry {
    @Expose
    private int id;
    @Expose
    private String product;
    private ShoppingList shopping_list;
    @Expose
    private Unit unit;
    @Expose
    private int quantity;

    public static List<Entry> currentEntries = new ArrayList<>();

    public Entry(String product, ShoppingList shopping_list, Unit unit, int quantity, int id) {
        this.product = product;
        this.shopping_list = shopping_list;
        this.unit = unit;
        this.quantity = quantity;
        this.id = id;
    }

    public Entry(String product, Unit unit, int quantity) {
        this.product = product;
        this.shopping_list = null;
        this.unit = unit;
        this.quantity = quantity;
    }
    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public ShoppingList getShopping_list() {
        return shopping_list;
    }

    public void setShopping_list(ShoppingList shopping_list) {
        this.shopping_list = shopping_list;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
