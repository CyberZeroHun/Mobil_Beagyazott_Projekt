package hu.uniobuda.nik.ciwsduino;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.net.InetAddress;

import hu.uniobuda.nik.joystick.Joystick;
import hu.uniobuda.nik.joystick.JoystickEventListener;

import static hu.uniobuda.nik.ciwsduino.GlobalisKonstansok.*;

/**
 * Created by thecy on 2016. 04. 25..
 */
public class ManualisVezerlesFragment extends Fragment implements Handler.Callback {
    private static final String DISCOVER_SERVER_MESSAGE = "DISCOVER_SERVER_MESSAGE";
    private static final int UDP_PORT = 41234;
    private static final String TAG = ManualisVezerlesFragment.class.getSimpleName();
    private UdpSocketThread udpThread;
    private Handler messageHandler;
    private Button goToFullscreenButton;
    private Joystick joystick1;
    private TextView angleTextView, serverTextview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //beinflate-elem a fragment-be az xml tartalmát
        View view = inflater.inflate(R.layout.fragment_manualis_vezerles, container, false);
        goToFullscreenButton = (Button)view.findViewById(R.id.go_to_fullscreen);
        angleTextView = (TextView)view.findViewById(R.id.angleTextView);
        serverTextview = (TextView)view.findViewById(R.id.serverTextView);
        if (ipAddress == null) {
            goToFullscreenButton.setEnabled(false);
        }

        joystick1 = (Joystick)view.findViewById(R.id.joystick_1);

        joystick1.setJoystickEventListener(new JoystickEventListener() {
            @Override
            public void onPositionChange(float x, float y, float deg) {
                angleTextView.setText(R.string.fegyver_szog + Float.toString(deg));
            }

            @Override
            public void onJoystickReleased() {

            }

            @Override
            public void onJoystickTouched() {

            }
        });
// To dismiss the dialog



        goToFullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FullscreenActivity.class);
                getActivity().startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG, "onStart");
        if (ipAddress == null) {
            messageHandler = new IncomingMessageHandler(this);
            if (udpThread == null) {
                try {
                    udpThread = new UdpSocketThread(messageHandler, getBroadcastAddress());
                    udpThread.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();

        //udpThread.interrupt();
        //udpThread = null;

        Log.v(TAG, "onStop");
    }

    private InetAddress getBroadcastAddress() throws IOException {
        WifiManager wifi = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcp = wifi.getDhcpInfo();
        if (dhcp == null) {
            Log.d(TAG, "Could not get dhcp info");
            return null;
        }

        int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++) {
            quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
        }
        return InetAddress.getByAddress(quads);
    }

    @Override
    public boolean handleMessage(Message message) {
        // Only execute if the server is not found yet.
        if (ipAddress == null) {
            Log.v(TAG, message.obj == null ? "null" : (String)message.obj);
            if (message.obj == null) {
                // There was an error locating the server
            } else {
                // The server ip was found.
                ipAddress = (String)message.obj;
                //startVideoStreaming();
                goToFullscreenButton.setEnabled(true);
            }
        }
        return false;
    }

    private static class IncomingMessageHandler extends Handler {
        public IncomingMessageHandler(Callback callback) {
            super(callback);
        }
    }
}