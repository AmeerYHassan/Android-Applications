package com.example.hangman;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.gridlayout.widget.GridLayout;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hangman.ui.login.LoginActivity;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class GameStart extends AppCompatActivity {
    String currGameWord;
    String currWorkingString;
    AlertDialog.Builder builder;
    HashMap<Character, ArrayList<Integer>> indexMap = new HashMap();

    List<String> letterList = Arrays.asList(
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
            "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
    );

    final static int largeFileLines = 734;
    final static int smallFileLines = 3510;
    final static int columns = 8;
    final static int totalButtons = 26;
    final int MARGIN = 5;
    int guessesLeft = 6;
    int finalScore = 0;
    Boolean gameContinuing = true;

    TextView hangmanView;
    GridLayout grid;
    ImageView hangmanImage;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_start);

        hangmanView = (TextView) findViewById(R.id.hangmanWordView);
        grid = (GridLayout) findViewById(R.id.gridLayout);
        hangmanImage = (ImageView) findViewById(R.id.hangmanImage);
        builder = new AlertDialog.Builder(this);

        buildGrid();
        startGame();
    }

    void buildGrid(){

        for(int i = 0; i < totalButtons; i++){
            String currLetter = letterList.get(i);
            ContextThemeWrapper newContext = new ContextThemeWrapper(
                    this,
                    R.style.Theme_AppCompat
            );

            Button btnTag = new Button(newContext);
            btnTag.setId(i);
            btnTag.setText(currLetter);
            btnTag.setClickable(true);
            btnTag.setWidth(20);
            btnTag.setHeight(20);
            btnTag.setOnClickListener(this::fillInBlanks);
            btnTag.setBackgroundColor(Color.BLUE);

            GridLayout.LayoutParams param = new GridLayout.LayoutParams();
            param.height = 130;
            param.width = 130;

            param.rightMargin = MARGIN;
            param.topMargin = MARGIN;
            param.leftMargin = MARGIN;
            param.rowSpec = GridLayout.spec(i/columns);
            param.columnSpec = GridLayout.spec(i%columns);
            btnTag.setLayoutParams (param);

            //add button to the layout
            grid.addView(btnTag);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startGame(){
        guessesLeft = 6;
        hangmanImage.setImageResource(R.drawable.hangman6);
        currGameWord = randomString();
        buildGrid();
        stringSetup();
        hangmanView.setText(currWorkingString);
    }

    public void stringSetup(){
        String blankWord = "";

        for (int i = 0; i < currGameWord.length(); i++)
            blankWord = blankWord + "-";

        currWorkingString = blankWord;
        indexMap = new HashMap();

        for(int i = 0; i < currGameWord.length(); i++){
            ArrayList<Integer> indices = new ArrayList();
            Character currChar = currGameWord.charAt(i);

            if(indexMap.containsKey(currChar))
                indices.addAll(indexMap.get(currChar));

            indices.add(i);
            indexMap.put(currChar, indices);
        }

        Log.v("Hangman", String.valueOf(indexMap));
    }

    public void fillInBlanks(View view){
        char currGuess = letterList.get(view.getId()).toLowerCase().charAt(0);

        if (indexMap.containsKey(currGuess)){
            ArrayList<Integer> indices = indexMap.get(currGuess);
            char[] workingArray = currWorkingString.toCharArray();

            for (int i = 0; i<indices.size(); i++){
                workingArray[indices.get(i)] = currGuess;
            }
            currWorkingString = String.valueOf(workingArray);
            hangmanView.setText(currWorkingString);
        } else {
            guessesLeft -= 1;
            updateHangmanImage();
        }

        checkGame();
        view.setClickable(false);
        view.setBackgroundColor(Color.GRAY);
    }

    public void checkGame(){
        if (guessesLeft == 0) {
            finalScore = 1;
            ScoreRequests.sendScore(finalScore);
            builder.setMessage("You have lost with a score of 1, the word was " + currGameWord)
                    .setCancelable(false)
                    .setPositiveButton("Back to Menu", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(GameStart.this, MainActivity.class));
                            finish();
                        }
                    })
                    .setNegativeButton("Play Again", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startGame();
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.setTitle("You lost :(");
            alert.show();
        }
        else if (guessesLeft > 0 && currWorkingString.indexOf('-') == -1){
            finalScore = currGameWord.length();
            ScoreRequests.sendScore(finalScore);
            Log.v("WINNER", String.valueOf(finalScore));
            builder.setMessage("You have won with a score of " + String.valueOf(finalScore) + ", saving your score.")
                    .setCancelable(false)
                    .setPositiveButton("Back to Menu", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("Play Again", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startGame();
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.setTitle("You won!");
            alert.show();
        }
    }

    public void updateHangmanImage(){
        switch(guessesLeft) {
            case 5:
                hangmanImage.setImageResource(R.drawable.hangman5);
                break;
            case 4:
                hangmanImage.setImageResource(R.drawable.hangman4);
                break;
            case 3:
                hangmanImage.setImageResource(R.drawable.hangman3);
                break;
            case 2:
                hangmanImage.setImageResource(R.drawable.hangman2);
                break;
            case 1:
                hangmanImage.setImageResource(R.drawable.hangman1);
                break;
            case 0:
                hangmanImage.setImageResource(R.drawable.hangman0);
                break;
            default:
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String randomString() {
        Random rand = new Random();
        String randomWord = "";
        String text = "";
        String difficulty = "";

        SharedPreferences sharedPreferences = getSharedPreferences("difficulty", MODE_PRIVATE);
        difficulty = sharedPreferences.getString("difficulty","easy");
        Log.v("Difficulty Reading", difficulty);

        if (difficulty.equalsIgnoreCase("easy")){
            Log.v("What", "Inside easy");
            try {
                InputStream is = getAssets().open("small_words.txt");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();

                text = new String(buffer);
                String str[] = text.split("\n");
                randomWord = str[rand.nextInt(largeFileLines)];
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                InputStream is = getAssets().open("large_words.txt");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();

                text = new String(buffer);
                String str[] = text.split("\n");
                randomWord = str[rand.nextInt(largeFileLines)];
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Log.v("Hangman Word", randomWord);
        return randomWord;
    }
}