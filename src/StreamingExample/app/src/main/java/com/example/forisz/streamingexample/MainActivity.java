package com.example.forisz.streamingexample;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.webkit.WebView;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    WebView webView;
    MediaPlayer mediaPlayer;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);

        //surfaceHolder.setFixedSize(176, 144);

        String url = "http://192.168.0.35:8090";

        surfaceHolder.setKeepScreenOn(true);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.e("SURFACE", "Surface created");
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource("http://192.168.0.35:8090");
            mediaPlayer.setDisplay(surfaceHolder);
            mediaPlayer.prepare(); // might take long! (for buffering, etc)
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
