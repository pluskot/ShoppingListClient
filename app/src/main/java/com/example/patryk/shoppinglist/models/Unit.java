package com.example.patryk.shoppinglist.models;

public enum Unit {
    L("l"),
    ML("ml"),
    PIECE("szt."),
    KG("kg"),
    G("g");
    private final String name;

    Unit(String name){
        this.name = name;
    }

    public boolean equalsName(String otherName) {
        return name.equals(otherName);
    }

    @Override
    public String toString(){
        return name;
    }

}
