package hu.uniobuda.nik.ciwsduino;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Created by Forisz on 07/05/16.
 */
public class UdpSocketThread extends Thread {
    private static final int UDP_PORT = 41234;
    private static final String DISCOVER_SERVER_MESSAGE = "DISCOVER_SERVER_MESSAGE";
    private static final String TAG = UdpSocketThread.class.getSimpleName();

    private Handler messageHandler;
    private InetAddress broadcastAddress;
    private DatagramSocket incomingSocket;

    public Handler getMessageHandler() {
        return messageHandler;
    }

    //<editor-fold desc="Constructors">
    public UdpSocketThread(String threadName) {
        super(threadName);
    }

    public UdpSocketThread(Handler messageHandler, InetAddress broadcastAddress) {
        this.messageHandler = messageHandler;
        this.broadcastAddress = broadcastAddress;
    }
    //</editor-fold>

    @Override
    public void run() {
        InetAddress ipAddress = null;
        try {
            // Send a UDP message to the current networks broadcast ip address.
            sendDiscoveryRequest();
            // Start listening for responses on the same port
            incomingSocket = new DatagramSocket(UDP_PORT);
            incomingSocket.setSoTimeout(5000);
            byte[] received_data = new byte[1024];

            DatagramPacket receivedPacket = new DatagramPacket(received_data, received_data.length);
            incomingSocket.receive(receivedPacket);
            ipAddress = receivedPacket.getAddress();

        } catch (IOException e) {
            Log.e(TAG, e.getMessage() == null ? "IOException" : e.getMessage());
            e.printStackTrace();
        }

        if (incomingSocket != null && !incomingSocket.isClosed()) {
            incomingSocket.close();
            incomingSocket = null;
        }

        Message msg = Message.obtain();
        msg.obj = ipAddress == null ? null : ipAddress.getHostAddress();
        messageHandler.sendMessage(msg);
    }


    @Override
    public void interrupt() {
        super.interrupt();

        //incomingSocket.close();
        //incomingSocket = null;
    }

    private void sendDiscoveryRequest() throws IOException {
        DatagramSocket socket = new DatagramSocket();
        socket.setBroadcast(true);
        socket.setSoTimeout(5000);

        DatagramPacket packet = new DatagramPacket(
                DISCOVER_SERVER_MESSAGE.getBytes(),
                DISCOVER_SERVER_MESSAGE.length(),
                broadcastAddress,
                UDP_PORT);

        Log.v(TAG, String.format("Broadcast packet sent to: %s. Packet: %s", broadcastAddress, packet.toString()));

        socket.send(packet);
        socket.close();
    }

}
