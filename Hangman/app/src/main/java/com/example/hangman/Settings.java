package com.example.hangman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.hangman.ui.login.LoginActivity;

public class Settings extends AppCompatActivity {

    Button easyButton;
    Button hardButton;
    Button logoutButton;
    Button menuButton;

    public SharedPreferences sharedPreferences;
    public Context applicationContext = MainActivity.getContextOfApplication();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        easyButton = (Button) findViewById(R.id.easyButton);
        hardButton = (Button) findViewById(R.id.hardButton);
        logoutButton = (Button) findViewById(R.id.logoutButton);
        menuButton = (Button) findViewById(R.id.backToMenu);
    }

    public void setEasy(View view){
        sharedPreferences = applicationContext.getSharedPreferences("difficulty", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("difficulty", "easy");
        editor.apply();

        Log.v("Difficulty Change", "easy");
    }

    public void setHard(View view){
        sharedPreferences = applicationContext.getSharedPreferences("difficulty", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("difficulty", "hard");
        editor.apply();

        Log.v("Difficulty Change", "hard");

    }

    public void logoutBtn(View view){
        sharedPreferences = getSharedPreferences("username", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", "Not Logged In!");
        editor.apply();

        Log.v("ResetUser", sharedPreferences.getString("username","ERROR"));

        sharedPreferences = getSharedPreferences("uid", Context.MODE_PRIVATE);
        editor.putString("uid", "None");
        editor.apply();

        Log.v("ResetUser", sharedPreferences.getString("uid","ERROR"));
    }

    public void toMenu(View view) { startActivity(new Intent(this, MainActivity.class)); }
}