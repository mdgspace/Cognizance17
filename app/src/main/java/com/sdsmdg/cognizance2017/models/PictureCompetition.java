package com.sdsmdg.cognizance2017.models;

/**
 * Created by samagra on 22-03-2017.
 */

public class PictureCompetition {
    private String cogniId,rating;
    public PictureCompetition(String cogniId, String rating){
        this.cogniId = cogniId;
        this.rating = rating;
    }
    public String getCogniId(){return cogniId;}
    public String getRating(){return rating;}

}
