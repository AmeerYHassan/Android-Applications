package com.example.whackamole;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.whackamole.db.entity.ScoreEntity;
import com.example.whackamole.viewmodel.ScoreViewModel;
import com.example.whackamole.viewmodel.ScoreViewModelFactory;

public class Settings extends AppCompatActivity {
    private ScoreViewModel scoreViewModel;
    int roundLength = 60;
    TextView nameText, roundLengthText;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Instantiate the views we will be using.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        nameText = findViewById(R.id.nameText);
        roundLengthText = findViewById(R.id.roundLengthText);
        sharedPreferences = getPreferences(MODE_PRIVATE);

        // Instantiate the scoreViewModel if it's null.
        if(scoreViewModel == null){
            scoreViewModel = new ViewModelProvider(this, new ScoreViewModelFactory(getApplication())).get(ScoreViewModel.class);
        }
    }

    // Delete all records on button click.
    public void deleteAllRecords(View view){
        AsyncTask.execute(() -> {
            scoreViewModel.deleteAll();
        });
    }

    // Button to take you back to the menu.
    public void backToMenu(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // Delete a specific record based off of username.
    public void deleteSpecificRecord(View view){
        // If the search text box has characters, get the text and search for a result
        // in the rooms database. If a result is found, delete the first occurrence.
        String searchTextValue = nameText.getText().toString();
        if (searchTextValue.length() > 0){
            AsyncTask.execute(() -> {
                ScoreEntity currUser = scoreViewModel.findByName(searchTextValue);
                scoreViewModel.delete(currUser);
            });
            nameText.setText("");
        }
    }

    public void getRoundLength(View view){
        String roundLengthTextValue = roundLengthText.getText().toString();
        if (roundLengthTextValue.length() > 0){
            roundLength = Integer.parseInt(roundLengthTextValue);
            roundLengthText.setText("");

            SharedPreferences sharedPref = getSharedPreferences("roundLength", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("roundLength", roundLength);
            editor.apply();

            Log.v("TESTING", String.valueOf(sharedPreferences.getInt("roundLength", 60)));
        }
        Log.v("Settings", String.valueOf(roundLength));
    }
}