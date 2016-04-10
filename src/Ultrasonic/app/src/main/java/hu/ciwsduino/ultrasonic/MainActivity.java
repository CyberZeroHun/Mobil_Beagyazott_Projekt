package hu.ciwsduino.ultrasonic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.Array;
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
    /*    private int tavolsagok[] = {
            136,305,305,149,138,136,137,136,136,137,136,137,137,136,136,136,136,136,137,137,136,137,136,136,137,136,124,124,123,124,
            123,124,124,123,123,124,123,124,123,148,124,124,150,125,147,125,145,127,147,146,148,146,145,146,147,147,148,149,147,148,
            149,148,150,150,139,137,137,136,137,136,136,136,136,136,136,136,137,136,136,137,135,136,136,137,136,136,136,136,136,136,
            136,136,136,136,136,136,136,121,121,121,125,122,120,121,125,128,121,124,161,121,122,161,136,121,121,204,118,120,119,119,
            120,204,119,118,117,118,118,117,206,119,118,119,119,205,119,119,122,204,118,118,121,118,205,120,122,119,163,120,123,123,
            268,161,160,138,135,136,136,136,135,136,136,136,136,136,136,136,136,135,136,136,136,136,136,136,136,136,137,135,136,136,
            137,136,136,136,136,137,136,136,136,136,136,136,136,136,136,136,127,127,145,124,125,123,125,124,124,126,127,123,124,125,
            138,141,147,147,127,138,150,138,126,148,124,148,147,150,148,149,152,150,155,135,136,136,136,136,136,136,137,136,136,136,
            136,136
    };*/
    private int tavolsagok[] = {
            57, 146, 63, 55, 58, 66, 61, 51, 51, 52, 52, 52, 50, 53, 52, 52, 43, 53, 53, 53,
            46, 53, 54, 54, 55, 55, 56, 57, 56, 55, 59, 61, 60, 43, 101, 103, 102, 102, 102, 102,
            102, 105, 53, 102, 106, 104, 104, 103, 105, 105, 290, 107, 107, 160, 108, 106, 113, 284, 160, 107,
            106, 151, 183, 137, 137, 136, 136, 137, 104, 169, 137, 135, 70, 68, 68, 67, 58, 67, 67, 67,
            70, 58, 66, 65, 66, 66, 63, 66, 64, 65, 66, 60, 59, 61, 59, 52, 59, 57, 58, 66,
            56, 57, 57, 56, 52, 56, 56, 56, 53, 56, 55, 55, 57, 55, 55, 55, 56, 52, 56, 56,
            56, 57, 57, 57, 57, 55, 56, 55, 57, 52, 55, 55, 56, 58, 55, 56, 55, 57, 52, 56,
            56, 57, 52, 57, 57, 58, 58, 58, 57, 58, 65, 52, 59, 60, 65, 66, 53, 64, 65, 66,
            68, 59, 67, 66, 66, 68, 65, 68, 67, 67, 282, 69, 70, 282, 185, 137, 136, 136, 136, 137,
            105, 167, 136, 136, 115, 152, 114, 142, 105, 158, 106, 158, 106, 110, 108, 113, 158, 108, 107, 185,
            106, 106, 292, 102, 102, 102, 104, 104, 340, 101, 101, 101, 103, 104, 308, 102, 59, 105, 170, 58,
            324, 55, 54, 54, 50, 53, 54, 53, 50, 53, 52, 53, 50, 52, 52, 53, 51, 52, 52, 52, 52
    };

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
                /*if(elteltIdo<400) {
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
                }*/
                if(elteltIdo>=tavolsagok.length){
                    elteltIdo=0;
                }
                testTav=tavolsagok[elteltIdo];

                uv.UjErtek(testSzog,testTav, irany);
                testSzog+=irany;
                if(testSzog==119 || testSzog==0) irany=-irany;
                elteltIdo++;
            }
        };
        idozito.schedule(feladat,0,50);
    }
}
