package com.sdsmdg.cognizance2017.models;

import io.realm.RealmObject;


public class Type extends RealmObject{
    private String category, name;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
