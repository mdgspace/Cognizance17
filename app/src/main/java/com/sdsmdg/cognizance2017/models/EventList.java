package com.sdsmdg.cognizance2017.models;

import io.realm.RealmList;
import io.realm.RealmObject;


public class EventList extends RealmObject {
    private RealmList<Event> events;
    private int id;
    private String title,description;

    public RealmList<Event> getEvents() {
        return events;
    }

    public void setEvents(RealmList<Event> events) {
        this.events = events;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
