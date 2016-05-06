package com.example.forisz.joystickexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.forisz.joystick.Joystick;
import com.example.forisz.joystick.JoystickEventListener;

public class MainActivity extends AppCompatActivity {

    private static final String outString = "x: %f, y: %f, d: %f";

    private Joystick joystick1, joystick2, joystick3;
    private TextView tw;
    private Button button1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tw = (TextView)findViewById(R.id.test);
        button1 = (Button)findViewById(R.id.button1);
        joystick1 = (Joystick)findViewById(R.id.joystick1);
        joystick2 = (Joystick)findViewById(R.id.joystick2);
        joystick3 = (Joystick)findViewById(R.id.joystick3);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joystick1.setResetPositionAfterRelease(!joystick1.isResetPositionAfterRelease());
                joystick1.resetPositionToCenter();
                joystick2.resetPositionToCenter();
            }
        });


        joystick1.setJoystickEventListener(new JoystickEventListener() {
            @Override
            public void onPositionChange(float x, float y, float deg) {
                tw.setText(String.format(outString, x, y, deg));
            }

            @Override
            public void onJoystickReleased() {
                tw.setText(String.format(outString, 0f, 0f, 0f));
                //              joystick2.resetPositionToCenter();
            }

            @Override
            public void onJoystickTouched() {
                tw.setText(String.format(outString, 0f, 0f, 0f));
                //               joystick2.resetPositionToCenter();
            }
        });

        joystick2.setJoystickEventListener(new JoystickEventListener() {
            @Override
            public void onPositionChange(float x, float y, float deg) {
     //           joystick1.setPosition(x, y);
            }

            @Override
            public void onJoystickReleased() {
        //        joystick1.setPosition(0f, 1f);
            }

            @Override
            public void onJoystickTouched() {

            }
        });

        joystick3.setJoystickEventListener(new JoystickEventListener() {
            @Override
            public void onPositionChange(float x, float y, float deg) {
                joystick1.setPosition(x, y);
                joystick2.setPosition(x, y);
            }

            @Override
            public void onJoystickReleased() {
                joystick1.resetPositionToCenter();
                joystick2.resetPositionToCenter();

            }

            @Override
            public void onJoystickTouched() {

            }
        });

    }
}
