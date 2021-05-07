package com.example.hangman.data.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.hangman.MainActivity;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String userId;
    private String displayName;
    private String email;
    public SharedPreferences sharedPreferences;

    public Context applicationContext = MainActivity.getContextOfApplication();

    public LoggedInUser(String userId, String displayName){
        this(userId, displayName,"");
    }

    public LoggedInUser(String userId, String displayName, String email) {
        this.userId = userId;
        this.displayName = displayName;
        this.email = email;

        sharedPreferences = applicationContext.getSharedPreferences("username", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", displayName);
        editor.apply();

        sharedPreferences = applicationContext.getSharedPreferences("uid", Context.MODE_PRIVATE);
        editor.putString("uid", userId);
        editor.apply();
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail(){ return email; }
}