package hu.ciwsduino.ciwsduinoclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    //átkerültünk ide a SplashScreen-ből
    //ha app-ot váltunk, majd visszajövünk ide, akkor nincs gond, ez jön be
    //de ha itt nyomunk egy visszát, akkor lekicsinyíti az appot, majd ha
    //így jövünk vissza bele, akkor újból a SplashScreen töltődik-be

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
