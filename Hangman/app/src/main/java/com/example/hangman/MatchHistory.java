package com.example.hangman;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;

import java.util.ArrayList;

public class MatchHistory extends AppCompatActivity {

    ListView historyList;
    ArrayList<String> playerList = new java.util.ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_history);
        historyList = (ListView) findViewById(R.id.historyList);

        try {
            playerList = ScoreRequests.getScores("history", false);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.v("History", playerList.toString());
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, playerList);
        historyList.setAdapter(arrayAdapter);
    }
}