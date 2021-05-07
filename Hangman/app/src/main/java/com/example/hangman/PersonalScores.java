package com.example.hangman;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;

import java.util.ArrayList;

public class PersonalScores extends AppCompatActivity {

    ListView personalList;
    ArrayList<String> playerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_scores);
        personalList = (ListView) findViewById(R.id.personalListView);

        try {
            playerList = ScoreRequests.getScores("top", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.v("TopScores", playerList.toString());
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, playerList);
        personalList.setAdapter(arrayAdapter);
    }
}