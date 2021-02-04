package com.example.clicker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    Button myButton;
    TextView myText, timerText, bestText;
    SharedPreferences sharedPreferences;
    int clicks, best = 0;
    int time = 10;

    ScheduledExecutorService service;
    ScheduledFuture timer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getPreferences(MODE_PRIVATE);

        myButton = (Button)findViewById(R.id.button);
        myText = (TextView)findViewById(R.id.textView);
        timerText = (TextView)findViewById(R.id.timerText);
        bestText = (TextView)findViewById(R.id.highscoreText);

        service = Executors.newScheduledThreadPool(1);

        best = sharedPreferences.getInt("best", 0);
        bestText.setText("Highscore: " + best);
    }

    public void onClick(View view){
        if (timer == null){
            if(clicks > best){
                best = clicks;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("best", best);
                editor.apply();
                runOnUiThread(()->bestText.setText("Highscore: " + best));
            }
            clicks = 0;
            time = 10;
            startTimer();
        }
        clicks++;
        myText.setText("Clicks: " + clicks);
    }

    public void startTimer(){
        timer = service.scheduleAtFixedRate(() -> {
            try {
                runOnUiThread(()->timerText.setText("Time Remaining: " + time));
                time--;
                if (time <= 0) {
                    if (timer != null) {
                        timer.cancel(true);
                        timer = null;
                    }

                    runOnUiThread(()->{
                       myButton.setEnabled(false);
                    });

                    service.schedule(()->runOnUiThread(()-> {
                        myButton.setEnabled(true);
                    }), 3, TimeUnit.SECONDS);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }
}