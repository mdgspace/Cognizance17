package com.sdsmdg.cognizance2017.models;

/**
 * Created by samagra on 22-03-2017.
 */

public class LoginAuthModelClass {
    private String name;
    private String cogniId;
    LoginAuthModelClass(String name, String cogniId){
        this.name = name;
        this.cogniId = cogniId;
    }
    public String getName(){
        return  name;
    }
    public String getCogniId(){
        return cogniId;
    }

}
