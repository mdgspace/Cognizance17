package com.sdsmdg.cognizance2017.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import static com.sdsmdg.cognizance2017.activities.MainActivity.curDay;


public class EventModel extends RealmObject {
    @PrimaryKey
    private int id;
    private String name, contact_person, contact_email, contact_phone, prize, description, contact_person2, contact_phone2, venue,day1,day2,day3;
    private Type type;
    private boolean isFav1, isFav2, isFav3;

    public String getDay1() {
        return day1;
    }

    public void setDay1(String day1) {
        this.day1 = day1;
    }

    public String getDay2() {
        return day2;
    }

    public void setDay2(String day2) {
        this.day2 = day2;
    }

    public String getDay3() {
        return day3;
    }

    public void setDay3(String day3) {
        this.day3 = day3;
    }

    public String getContact_person() {
        return contact_person;
    }

    public void setContact_person(String contact_person) {
        this.contact_person = contact_person;
    }

    public String getContact_email() {
        return contact_email;
    }

    public void setContact_email(String contact_email) {
        this.contact_email = contact_email;
    }

    public String getContact_phone() {
        return contact_phone;
    }

    public void setContact_phone(String contact_phone) {
        this.contact_phone = contact_phone;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContact_person2() {
        return contact_person2;
    }

    public void setContact_person2(String contact_person2) {
        this.contact_person2 = contact_person2;
    }

    public String getContact_phone2() {
        return contact_phone2;
    }

    public void setContact_phone2(String contact_phone2) {
        this.contact_phone2 = contact_phone2;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public boolean isFav1() {
        return isFav1;
    }

    public void setFav1(boolean fav1) {
        isFav1 = fav1;
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
    public String getCurDay(){
        if(curDay == 24){
            return getDay1();
        }else if(curDay == 25){
            return getDay2();
        }else {
            return getDay3();
        }
    }

    public boolean isFav2() {
        return isFav2;
    }

    public void setFav2(boolean fav2) {
        isFav2 = fav2;
    }

    public boolean isFav3() {
        return isFav3;
    }

    public void setFav3(boolean fav3) {
        isFav3 = fav3;
    }

    public String getTime(String time){
        String hr = time.substring(0, 2);
        String min = time.substring(2, 4);

        String hr2 = time.substring(5, 7);
        String min2 = time.substring(7, 9);
        String modifiedTime = hr + ":" + min + " - " + hr2 + ":" + min2;
        return modifiedTime;
    }
}
