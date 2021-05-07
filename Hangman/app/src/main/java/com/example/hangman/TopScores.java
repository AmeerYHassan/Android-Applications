package com.example.hangman;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;

import java.util.ArrayList;

public class TopScores extends AppCompatActivity {

    ListView activityLayout;
    ArrayList<String> playerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_scores);
        activityLayout = (ListView) findViewById(R.id.topList);

        try {
            playerList = ScoreRequests.getScores("top", false);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.v("TopScores", playerList.toString());
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, playerList);
        activityLayout.setAdapter(arrayAdapter);
    }
}