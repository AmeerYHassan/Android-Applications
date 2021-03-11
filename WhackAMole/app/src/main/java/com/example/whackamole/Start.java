package com.example.whackamole;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import androidx.gridlayout.widget.GridLayout;
import androidx.lifecycle.ViewModelProvider;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.Random;
import java.util.function.Consumer;

import com.example.whackamole.db.entity.ScoreEntity;
import com.example.whackamole.viewmodel.ScoreViewModel;
import com.example.whackamole.viewmodel.ScoreViewModelFactory;

public class Start extends AppCompatActivity {
    TextView scoreText, timeRemaining;
    GridLayout grid;
    final static int totalButtons = 9;
    final static int columns = 3;
    int roundLength = 60;
    int score = 0;
    int time = 0;
    final String TAG = "WhackAMole";
    final int MARGIN = 5;
    ExecutorService executor = Executors.newFixedThreadPool(10);
    ScheduledExecutorService service;
    ScheduledFuture timer = null;
    Random rand = new Random();

    private ScoreViewModel scoreViewModel;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Instantiate the views we will use and the service as well.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        grid = findViewById(R.id.grid);
        scoreText = findViewById(R.id.scoreText);
        timeRemaining = findViewById(R.id.timeRemaining);
        service = Executors.newScheduledThreadPool(1);

        SharedPreferences sharedPreferences = getSharedPreferences("roundLength", MODE_PRIVATE);
        roundLength = sharedPreferences.getInt("roundLength",60);

        // Make sure if the timer is null to reset all of the values, start the game.
        // Build the grid and update the UI.
        buildGrid();
        updateUI();

        if(timer == null){
            score = 0;
            time = roundLength;
            startGame();
        }
    }

    // Button to take you back to the menu.
    public void backToMenu(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // If this button is pressed, start the game again.
    public void playAgain(View view){
        startGame();
    }

    void startGame(){
        // Make the navigation/restart buttons invisible.
        findViewById(R.id.backToMenuBtn).setVisibility(View.GONE);
        findViewById(R.id.playAgainBtn).setVisibility(View.GONE);
        // Start a timer based on round length.
        timer = service.scheduleAtFixedRate(() -> {
            try{
                // Update the UI, decrease time, start the next mole.
                runOnUiThread(() -> {
                    timeRemaining.setText("Time remaining: " + time + " seconds");
                    time--;
                    nextMole(0);
                    if (rand.nextInt(3) == 2)
                        nextMole(1);
                    if (rand.nextInt(6) == 5)
                        nextMole(2);
                });

                // When the time is up, make the buttons visible again, put the score in a final variable,
                // reset all of the other variables, prompt the user for their name, and then save
                // the score to the rooms database.
                if(time <= 0){
                    final int nScore = score;
                    if (timer != null) {
                        timer.cancel(true);
                        timer = null;
                        score = 0;
                        time = roundLength;
                    }
                    runOnUiThread(() -> {
                        findViewById(R.id.backToMenuBtn).setVisibility(View.VISIBLE);
                        findViewById(R.id.playAgainBtn).setVisibility(View.VISIBLE);
                        promptUser("Time's Up!", "You got a score of " + nScore + "! " +
                                "Type in your name to record your score", true, (x) -> {
                            if(x != null) {
                                Log.v(TAG, x.toString() + ": " + nScore);
                                saveScore(x.toString(), nScore);
                            }
                        });
                    });
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    // Simple method to make sure the score is up to date on the UI.
    void updateUI(){
        scoreText.setText("Score: " + score);
    }

    // Build a simple 3x3 grid of Image Buttons. Each button is identical in attributes. The only
    // thing that changes is when they are allowed to be clicked and the mole image that pops up.
    void buildGrid(){
        for(int i = 0; i < totalButtons; i++){
            ContextThemeWrapper newContext = new ContextThemeWrapper(
                    this,
                    R.style.Theme_AppCompat
            );

            ImageButton btnTag = new ImageButton(newContext);
            btnTag.setScaleType(ImageView.ScaleType.FIT_CENTER);
            btnTag.setId(i);
            btnTag.setClickable(false);
            btnTag.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);

            GridLayout.LayoutParams param =new GridLayout.LayoutParams();
            param.height = 250;
            param.width = 250;

            param.rightMargin = MARGIN;
            param.topMargin = MARGIN;
            param.setGravity(Gravity.CENTER);
            param.columnSpec = GridLayout.spec(i/columns);
            param.rowSpec = GridLayout.spec(i%columns);
            btnTag.setLayoutParams (param);

            //add button to the layout
            grid.addView(btnTag);
        }
    }

    // Method for when the user successfully clicks on a mole. Return the button to the original state
    // and increment the score as well as update the UI.
    void successfulClick(View view){
        ImageButton clickedButton = (ImageButton) view;
        clickedButton.setImageResource(0);
        clickedButton.setClickable(false);
        score += 1;
        updateUI();
    }

    void successfulIronClick(View view){
        ImageButton clickedButton = (ImageButton) view;
        clickedButton.setImageResource(0);
        clickedButton.setClickable(false);
        score += 2;
        updateUI();
    }

    void successfulGoldClick(View view){
        ImageButton clickedButton = (ImageButton) view;
        clickedButton.setImageResource(0);
        clickedButton.setClickable(false);
        score += 3;
        updateUI();
    }

    // Generate the next mole. The mole type tells the function what kind of mole to generate.
    // Iron moles have a 1 in 3 chance to spawn, and a gold mole has 1 in 6 chance to spawn.
    // Regular moles give one point, iron moles give two, gold moles give three.
    void nextMole(int moleType){
        Log.v(TAG, "nextMole()");
        updateUI();
        executor.submit(()->{
            int randomButton = rand.nextInt(9);
            runOnUiThread(()->{
                Log.v(TAG, "Selecting " + randomButton);
                ImageButton currButton = findViewById(randomButton);
                currButton.setClickable(true);
                switch(moleType) {
                    case 0:
                        currButton.setImageResource(R.drawable.mole);
                        currButton.setOnClickListener(this::successfulClick);
                        break;
                    case 1:
                        currButton.setImageResource(R.drawable.iron_mole);
                        currButton.setOnClickListener(this::successfulIronClick);
                        break;
                    case 2:
                        currButton.setImageResource(R.drawable.gold_mole);
                        currButton.setOnClickListener(this::successfulGoldClick);
                        break;
                    default:
                        Log.v(TAG, "Case failed, shouldn't be here.");
                }
            });
            try {
                TimeUnit.MILLISECONDS.sleep(rand.nextInt(3000-250) + 250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runOnUiThread(()->{
                Log.v(TAG, "Deselecting " + randomButton);
                ((ImageButton)findViewById(randomButton)).setImageResource(0);
                ((ImageButton)findViewById(randomButton)).setClickable(false);
                ((ImageButton)findViewById(randomButton)).setOnClickListener(null);
            });
        });
    }

    // Creates an alert dialog to prompt the user for their name and automatically submits the score.
    void promptUser(String title, String message, boolean expectsInput, Consumer callback) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(title);
        alert.setMessage(message);
        EditText input = null;
        if (expectsInput) {
            // Set an EditText view to get user input
            input = new EditText(this);
            alert.setView(input);
        }

        EditText finalInput = input;
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = "-";
                if (expectsInput) {
                    value = finalInput.getText().toString();
                    Log.v(TAG, "WE got it!!! " + value);
                    // Do something with value!
                }
                if (callback != null) {
                    callback.accept(value);
                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
                Log.v(TAG, "They want out");
                if (callback != null) {
                    callback.accept(null);
                }
            }
        });

        alert.show();
    }

    // Method to save the score to the database given the name and the score.
    void saveScore(String name, int score){
        if(scoreViewModel == null){
            scoreViewModel = new ViewModelProvider(this, new ScoreViewModelFactory(getApplication())).get(ScoreViewModel.class);
        }
        ScoreEntity user = new ScoreEntity(name, score);
        scoreViewModel.insert(user);
    }
}
