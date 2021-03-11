package com.example.whackamole;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void toStart(View view) { startActivity(new Intent(this, Start.class)); }

    public void toHistory(View view) { startActivity(new Intent(this, History.class)); }

    public void toScores(View view) { startActivity(new Intent(this, Scores.class)); }

    public void toSettings(View view) { startActivity(new Intent(this, Settings.class)); }

    public void quitApp(View view){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
}