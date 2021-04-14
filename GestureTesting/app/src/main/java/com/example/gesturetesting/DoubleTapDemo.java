package com.example.gesturetesting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.view.GestureDetectorCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class DoubleTapDemo extends AppCompatActivity {

    private GestureDetectorCompat mGestureDetector;
    boolean zoomedIn = false;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_double_tap_demo);
        img = (ImageView) findViewById(R.id.androidImage);

        mGestureDetector = new GestureDetectorCompat(this, new DoubleTapDemo.GestureListener());
    }

    public void toMenu(View view) { startActivity(new Intent(this, MainActivity.class)); }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            // Toast.makeText(DoubleTapDemo.this, "Double Tap Confirmed, Zooming.", Toast.LENGTH_SHORT).show();
            if (!zoomedIn){
                img.setImageResource(R.drawable.zoomed_in_logo);
            } else {
                img.setImageResource(R.drawable.androidman);
            }

            zoomedIn = !zoomedIn;
            return super.onDoubleTap(e);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int xCoord = (int)event.getX();
        int yCoord = (int)event.getY();

        String toastMessage = "Tap Location: (" + String.valueOf(xCoord) + ", " + String.valueOf(yCoord) + ")";
        Toast.makeText(DoubleTapDemo.this, toastMessage, Toast.LENGTH_SHORT).show();
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}