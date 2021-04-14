package com.example.gesturetesting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private GestureDetectorCompat mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGestureDetector = new GestureDetectorCompat(this, new GestureListener());
    }

    public void toDoubleTap(View view) { startActivity(new Intent(this, DoubleTap.class)); }

    public void toFling(View view) { startActivity(new Intent(this, Fling.class)); }

    public void toLongPress(View view) { startActivity(new Intent(this, LongPress.class)); }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Toast.makeText(MainActivity.this, "Fling", Toast.LENGTH_LONG).show();
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Toast.makeText(MainActivity.this, "Double Tap", Toast.LENGTH_LONG).show();
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Toast.makeText(MainActivity.this, "Single Tap", Toast.LENGTH_LONG).show();
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Toast.makeText(MainActivity.this, "Long Press", Toast.LENGTH_LONG).show();
            super.onLongPress(e);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}