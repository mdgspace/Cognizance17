package com.sdsmdg.cognizance2017.models;

import io.realm.RealmObject;

public class Event extends RealmObject {
    private String title, theme, location;
    private String description;
    private int startDay, startHr, startMin, imageId;
    private int event, endDay, endHr, endMin;
    private boolean continuous, fav;
    private Double latitude, longitude;

    public Event(String title, String theme, String location, String description, int startDay,
                 int startHr, int startMin, int imageId, int endDay, int endHr, int endMin, boolean continuous) {
        this.title = title;
        this.theme = theme;
        this.location = location;
        this.description = description;
        this.startDay = startDay;
        this.startHr = startHr;
        this.startMin = startMin;
        this.imageId = imageId;
        this.endDay = endDay;
        this.endHr = endHr;
        this.endMin = endMin;
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

    public int getStartHr() {
        return startHr;
    }

    public void setStartHr(int startHr) {
        this.startHr = startHr;
    }

    public int getStartMin() {
        return startMin;
    }

    public void setStartMin(int startMin) {
        this.startMin = startMin;
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

    public int getEndHr() {
        return endHr;
    }

    public void setEndHr(int endHr) {
        this.endHr = endHr;
    }

    public int getEndMin() {
        return endMin;
    }

    public void setEndMin(int endMin) {
        this.endMin = endMin;
    }

    public boolean isContinuous() {
        return continuous;
    }

    public void setContinuous(boolean continuous) {
        this.continuous = continuous;
    }

    public boolean isFav() {
        return fav;
    }

    public void setFav(boolean fav) {
        this.fav = fav;
    }

    public String getTime() {
        String startMin = String.format("%02d", getStartMin());
        String endMin = String.format("%02d", getEndMin());
        String startHr = String.format("%02d", getStartHr());
        String endHr = String.format("%02d", getEndHr());
        return startHr + ":" + startMin + "-" + endHr + ":" + endMin;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
