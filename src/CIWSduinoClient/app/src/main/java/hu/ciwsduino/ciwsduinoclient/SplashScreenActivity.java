package hu.ciwsduino.ciwsduinoclient;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreenActivity extends AppCompatActivity {

    private static int SPLASH_CHANGE_TIME = 3000; //3" után vált

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //egy timer, ami az idő leteltével átvált a Main Activity-re
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //simán egy intent-el megnyitjuk a Main-t
                //ide már nem jövünk vissza, tehát nem várunk eredményt
                Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(i);
                //hogy a vissza gombbal se lehessen vissza jönni, be is zárjuk ezt a SplashScreen-t
                //a finish meghívja: pause, stop és destroy-t, tehát teljesen bezárja az activity-t
                finish();
            }
        },SPLASH_CHANGE_TIME);
    }
}
