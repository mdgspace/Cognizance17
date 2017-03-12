package com.sdsmdg.cognizance2017.Models;

import io.realm.RealmObject;

public class Event extends RealmObject {
    private String title, theme, location;
    private String description;
    private int startDay, startHour, startMinute, imageId;
    private int event,endDay, endHour, endMinute;
    private boolean continuous;

    public Event(String title, String theme, String location, String description, int startDay,
                 int startHour, int startMinute, int imageId, int endDay, int endHour, int endMinute, boolean continuous) {
        this.title = title;
        this.theme = theme;
        this.location = location;
        this.description = description;
        this.startDay = startDay;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.imageId = imageId;
        this.endDay = endDay;
        this.endHour = endHour;
        this.endMinute = endMinute;
        this.continuous = continuous;
    }

    public Event() {
    }

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStartDay() {
        return startDay;
    }

    public void setStartDay(int startDay) {
        this.startDay = startDay;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getEndDay() {
        return endDay;
    }

    public void setEndDay(int endDay) {
        this.endDay = endDay;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public int getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }

    public boolean isContinuous() {
        return continuous;
    }

    public void setContinuous(boolean continuous) {
        this.continuous = continuous;
    }
    public String getTime(){
        String startMin = startMinute/10==0?"0"+startMinute:""+startMinute;
        String endMin = startMinute/10==0?"0"+startMinute:""+startMinute;
        return startHour + ":" + startMin+ "-" + endHour + ":" + endMin;
    }
}
