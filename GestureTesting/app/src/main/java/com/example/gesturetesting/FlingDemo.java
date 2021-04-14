package com.example.gesturetesting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class FlingDemo extends AppCompatActivity {

    private GestureDetectorCompat mGestureDetector;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fling_demo);

        img = (ImageView) findViewById(R.id.flingImage);
        mGestureDetector = new GestureDetectorCompat(this, new FlingDemo.GestureListener());
    }

    public void toMenu(View view) { startActivity(new Intent(this, MainActivity.class)); }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            String toastText = "Fling Velocities (X, Y): ("+String.valueOf(velocityX)+", "+String.valueOf(velocityY)+")";
            Toast.makeText(FlingDemo.this, "Fling X, Y: " + toastText, Toast.LENGTH_SHORT).show();

            img.setImageResource(0);
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}