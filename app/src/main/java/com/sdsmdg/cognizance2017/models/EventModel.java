package com.sdsmdg.cognizance2017.models;

import java.io.Serializable;

/**
 * Created by Arihant Jain on 3/18/2017.
 */

public class EventModel{
    private int id;
    private String name, type;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
