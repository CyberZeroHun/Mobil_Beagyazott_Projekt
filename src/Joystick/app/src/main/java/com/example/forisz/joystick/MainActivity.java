package com.example.forisz.joystick;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Joystick joystick;
    TextView joyOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        joystick = (Joystick)findViewById(R.id.joystick);
        joyOut = (TextView)findViewById(R.id.joy);
        joyOut.setText(String.format("Joystick position x: %f, y:%f", 0f, 0f));

        joystick.setJoystickEventListener(new Joystick.JoystickEventListener() {
            @Override
            public void onPositionChange(float x, float y) {
                joyOut.setText(String.format("Joystick position x: %f, y:%f", x, y));
                Log.w("MainActivity", String.format("Joystick position x: %f, y:%f", x, y));
            }

            @Override
            public void onJoystickReleased() {
                joyOut.setText(String.format("Joystick position x: %f, y:%f", 0f, 0f));
                Log.w("MainActivity", String.format("onJoystickReleased"));
            }

            @Override
            public void onJoystickTouched() {
                joyOut.setText(String.format("Joystick position x: %f, y:%f", 0f, 0f));
                Log.w("MainActivity", String.format("onJoystickTouched"));
            }
        });

    }
}
