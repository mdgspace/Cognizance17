package com.sdsmdg.cognizance2017.activities;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sdsmdg.cognizance2017.models.EventModel;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Arihant Jain on 3/18/2017.
 */

public interface DataInterface {
    @GET("/json/getEventSummary")
    void getAllEvents(Callback<JsonArray> response);
    @GET("/json/getEventDetails/{id}")
    void getEventById(@Path("id") int id, Callback<JsonObject> response);
}
