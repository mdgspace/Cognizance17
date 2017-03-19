package com.sdsmdg.cognizance2017.models;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by Arihant Jain on 3/18/2017.
 */

public class EventModel extends RealmObject{
    private int id;
    private String name;
    private Type type;
    private boolean isFav;

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
