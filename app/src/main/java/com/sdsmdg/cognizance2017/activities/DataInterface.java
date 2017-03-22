package com.sdsmdg.cognizance2017.activities;

import android.telecom.Call;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sdsmdg.cognizance2017.models.EventModel;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;


public interface DataInterface {
    @GET("/json/getEventSummary")
    void getAllEvents(Callback<JsonArray> response);
    @GET("/json/getEventDetails/{id}")
    void getEventById(@Path("id") int id, Callback<JsonObject> response);
    @GET("/check_cogni_id/{id}")
    void checkUser(@Path("id") String id, Callback<String> response);
}
