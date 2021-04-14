package com.example.gesturetesting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LongPress extends AppCompatActivity {
    public void toLongPressDemo(View view) { startActivity(new Intent(this, LongPressDemo.class)); }

    public void toMenu(View view) { startActivity(new Intent(this, MainActivity.class)); }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_long_press);
    }
}