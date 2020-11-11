package com.example.patryk.shoppinglist.models;

import com.google.gson.annotations.Expose;

public class SimpleEntry {
    @Expose
    private Unit unit;
    @Expose
    private String product;
    @Expose
    private int quantity;

    public SimpleEntry(Entry entry){
        this.product = entry.getProduct();
        this.quantity = entry.getQuantity();
        this.unit = entry.getUnit();
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
