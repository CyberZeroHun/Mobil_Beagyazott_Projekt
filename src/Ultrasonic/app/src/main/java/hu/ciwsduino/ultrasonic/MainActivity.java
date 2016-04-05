package hu.ciwsduino.ultrasonic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private UltrasonicView uv;
    private Timer idozito;
    private TimerTask feladat;
    private int testSzog;
    private int irany;
    private float testTav;
    private int elteltIdo=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //beazonosítjuk
        uv= (UltrasonicView) findViewById(R.id.ultrahang);

        testSzog=0;
        irany=1;

        idozito = new Timer();
        feladat = new TimerTask() {
            @Override
            public void run() {
                Random r =new Random();
                //testTav=r.nextFloat()*500;
                //testTav=100f;
                if(elteltIdo<400) {
                    if (testSzog >= 0 && testSzog <= 30) {
                        testTav = 340;
                    } else if (testSzog >= 31 && testSzog <= 80) {
                        testTav = 210;
                    } else if (testSzog >= 81 && testSzog <= 120) {
                        testTav = 480;
                    }
                } else {
                    if (testSzog >= 0 && testSzog <= 30) {
                        testTav = 340;
                    } else if (testSzog >= 31 && testSzog <= 40) {
                        testTav = 210;
                    } else if (testSzog >= 41 && testSzog <= 60) {
                        testTav = 300;
                    } else if (testSzog >= 61 && testSzog <= 120) {
                        testTav = 480;
                    }
                }
                uv.UjErtek(testSzog,testTav, irany);
                testSzog+=irany;
                if(testSzog==119 || testSzog==0) irany=-irany;
                elteltIdo++;
            }
        };
        idozito.schedule(feladat,0,50);
    }
}
