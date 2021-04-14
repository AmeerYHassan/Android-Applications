package com.example.gesturetesting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LongPressDemo extends AppCompatActivity {
    private GestureDetectorCompat mGestureDetector;
    ImageView img;
    TextView extraInfo;

    public void toMenu(View view) { startActivity(new Intent(this, MainActivity.class)); }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_long_press_demo);

        img = (ImageView) findViewById(R.id.flingImage);
        extraInfo = (TextView) findViewById(R.id.extraInfo);
        mGestureDetector = new GestureDetectorCompat(this, new LongPressDemo.GestureListener());
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public void onLongPress(MotionEvent e) {
            String toastText = "Fun fact: Android was initially released September 23, 2008. Almost 13 years ago!";
            Toast.makeText(LongPressDemo.this, toastText, Toast.LENGTH_LONG).show();
            extraInfo.setText(toastText);
            super.onLongPress(e);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int xCoord = (int)event.getX();
        int yCoord = (int)event.getY();

        String toastMessage = "Tap Location: (" + String.valueOf(xCoord) + ", " + String.valueOf(yCoord) + ")";

        Toast toast = Toast.makeText(LongPressDemo.this, toastMessage, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

}