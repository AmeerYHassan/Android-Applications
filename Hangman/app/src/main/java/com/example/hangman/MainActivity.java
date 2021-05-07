package com.example.hangman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hangman.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {
    public static Context hangmanContext;
    String currUser = "";
    Button startGame;
    Button personalButton;
    TextView currUserTextView;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hangmanContext = getApplicationContext();
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        currUserTextView = (TextView) findViewById(R.id.currUserText);
        startGame = (Button) findViewById(R.id.startGame);
        startGame.setEnabled(false); // Change back to false

        personalButton = (Button) findViewById(R.id.personalBest);
        personalButton.setEnabled(false);


        sharedPreferences = getSharedPreferences("username", Context.MODE_PRIVATE);
        currUser = sharedPreferences.getString("username","Not Logged In!");
        currUserTextView.setText(currUser);

        if (!currUser.equalsIgnoreCase("Not Logged In!")){
            startGame.setEnabled(true);
            personalButton.setEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getSharedPreferences("username", MODE_PRIVATE);
        currUser = sharedPreferences.getString("username","Not Logged In!");
        currUserTextView.setText(currUser);

        if (!(currUser.equalsIgnoreCase("Not Logged In!"))){
            startGame.setEnabled(true);
            personalButton.setEnabled(true);
        }
    }

    public static Context getContextOfApplication(){ return hangmanContext; }
    public void toLogin(View view) { startActivity(new Intent(this, LoginActivity.class)); }
    public void toTop(View view) { startActivity(new Intent(this, TopScores.class)); }
    public void toHistory(View view) { startActivity(new Intent(this, MatchHistory.class)); }
    public void toPersonal(View view) { startActivity(new Intent(this, PersonalScores.class)); }
    public void toSettings(View view) { startActivity(new Intent(this, Settings.class)); }
    public void toGame(View view) { startActivity(new Intent(this, GameStart.class)); }
}