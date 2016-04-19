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


    //folyósó median hengerrel
    private int tavolsagok[] = {
            110,126,125,126,107,107,107,107,107,105,107,105,105,105,103,104,103,103,98,98,98,98,162,97,97,97,97,97,97,98,98,98,97,99,98,52,135,193,273,197,275,214,267,267,267,266,265,265,256,265,114,265,265,55,264,257,264,93,75,76,75,75,74,74,74,74,74,74,74,73,48,73,73,74,74,73,73,73,73,74,73,73,74,74,74,74,74,49,48,47,47,48,48,47,47,46,40,45,38,45,37,45,37,44,37,41,39,41,41,40,40,40,40,40,40,40,41,41,41,41,47,47,47,47,47,41,41,41,41,40,40,40,40,40,41,40,40,42,42,41,45,45,45,45,45,45,46,46,47,46,47,47,47,47,48,48,49,50,74,74,74,74,73,73,73,73,73,73,73,74,73,49,73,74,74,74,74,74,74,74,48,75,74,74,75,75,76,195,265,265,265,264,265,265,265,265,190,195,207,266,207,267,195,195,137,274,136,102,133,133,134,252,98,275,98,276,97,171,162,99,97,107,98,102,98,159,161,105,97,103,107,104,105,105,127,108,108,105,165,54,108,126
    };

    /*
    //folyósó median henger nélkül
    private int tavolsagok[] = {
            107,106,105,104,103,96,98,97,97,97,96,95,96,96,96,96,96,96,96,96,97,97,163,98,97,97,98,98,100,103,135,100,136,135,136,138,197,265,265,266,200,265,134,265,77,76,75,75,75,74,74,75,74,65,74,74,74,74,74,74,66,73,74,74,74,74,74,52,73,74,73,74,49,48,40,48,48,47,47,47,46,46,46,39,46,46,46,47,46,46,40,46,38,47,37,46,37,47,37,40,38,40,37,40,40,40,40,40,47,47,37,47,47,47,48,47,48,48,48,48,50,49,49,49,49,48,48,48,47,47,47,47,47,47,47,40,40,40,40,39,40,42,41,41,41,40,47,47,47,47,46,46,46,46,46,47,46,46,46,46,46,47,47,47,47,47,47,48,48,48,48,48,50,74,74,73,73,73,73,74,74,74,74,73,73,74,74,74,74,74,74,74,75,74,74,75,64,75,75,77,77,265,85,195,197,265,265,195,137,266,137,137,266,135,134,138,171,171,170,269,99,53,97,96,61,96,96,96,97,96,95,96,96,165,96,97,96,96,106,96,127,103
    };
    */
    /*
    //szoba median henger nélkül
    private int tavolsagok[] = {
            164,111,112,111,111,111,111,110,110,111,110,112,110,110,112,111,112,112,113,219,220,185,206,220,215,220,220,134,206,207,218,219,219,203,204,219,203,203,203,203,202,130,132,131,131,132,165,132,201,133,132,132,131,132,133,61,124,131,123,125,123,123,135,123,123,123,123,125,124,124,123,123,124,193,123,277,123,124,273,124,149,149,124,125,135,125,52,128,134,151,56,135,135,135,135,222,149,64,114,128,229,135,152,53,108,109,108,84,106,106,108,107,107,106,157,53,108,106,41,108,107,107,107,109,106,108,107,106,107,106,110,106,106,108,109,109,107,107,108,106,107,107,109,110,108,109,108,113,111,129,127,127,135,137,136,135,137,135,126,128,125,124,124,126,124,124,123,123,124,125,124,123,123,123,123,125,123,122,123,123,124,123,124,123,123,124,124,123,134,124,132,132,134,203,133,130,132,132,131,203,204,202,203,203,202,202,202,203,204,203,204,196,183,181,181,178,183,183,195,184,164,220,216,112,220,221,221,110,223,222,221,109,111,264,108,112,111,272,110,117,268,253
    };
    */
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
