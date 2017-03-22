package com.sdsmdg.cognizance2017;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.sdsmdg.cognizance2017.activities.LoginActivity;

import java.util.HashMap;


public class SessionManager {
    //shared preferences
    SharedPreferences preferences;
    //editor for shared preferences
    SharedPreferences.Editor editor;
    //context
    Context context;

    //Shared pref mode
    int PRIVATE_MODE = 0;
    //Sharedpref filename
    private static final String PREF_NAME = "UserPref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";

    // Email address (make variable public to access from outside)
    public static final String KEY_NUMBER = "number";

    //constructor
    public SessionManager(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = preferences.edit();
    }

    /**
     * create login session
     */
    public void createLoginSession(String userName, String userNumber) {
        //Storing login value as true
        editor.putBoolean(IS_LOGIN, true);

        //storing name in pref
        editor.putString(KEY_NAME, userName);

        //storing nmber in pref
        editor.putString(KEY_NUMBER, userNumber);

        //commit changer
        editor.commit();


    }

    /**
     * get stored session data
     */

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_NAME, preferences.getString(KEY_NAME, null));

        // user email id
        user.put(KEY_NUMBER, preferences.getString(KEY_NUMBER, null));

        // return user
        return user;
    }

    public String getUserCogniId() {
        return preferences.getString(KEY_NUMBER, null);
    }

    /**
     * checkLogin method will check if the user is logged in or not
     * and then redirect the user to login activity
     * else wont do anything
     */
    public void checkLogIn() {
        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            context.startActivity(i);
        }
    }

    /**
     * clear session details to logout the user
     * this method can be called whenever we wish to logout the user
     */

    public void logoutUser() {
        //clearing all data from shared preferences
        editor.clear();
        editor.commit();
        //after logging out the user must be directed to the login activity
        Intent intent = new Intent(context, LoginActivity.class);
        //after this close all the running activities and background services
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //add new flag to start new activity
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //starting login activity
        context.startActivity(intent);
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return preferences.getBoolean(IS_LOGIN, false);
    }


}
