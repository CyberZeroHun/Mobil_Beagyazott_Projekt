package hu.ciwsduino.bluetoothtest;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by thecy on 2016. 05. 01..
 */
public class BlueToothKliens extends Thread {
    BluetoothSocket bs;
    BluetoothDevice bd;
    Handler h;
    BlueToothKapcsolat btk;

    //uuid az alkalmazáshoz
    //alapbeállításon a modulnál a Service UUID: 0xFFE0 (ezt majd át kell állítani)
    //a jkarakterisztika pedig Characteristic: 0xFFE1
    //private final String BT_UUID = "0000ffe0-0000-1000-8000-00805f9b34fb";
    private final String BT_UUID = "00001101-0000-1000-8000-00805f9b34fb";

    //konstruktor
    public BlueToothKliens(BluetoothDevice bd, Handler h) {
        this.bd = bd;
        this.h = h;
        //átmeneti mert ha nem sikerül, akkor null-t illik visszaadnunk
        BluetoothSocket tmp_bs = null;
        try {
            tmp_bs = bd.createRfcommSocketToServiceRecord(UUID.fromString(BT_UUID));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.bs = tmp_bs;
        Log.d("ASD",bs.toString());
    }

    @Override
    public void run() {
        //super.run();
        //itt az eszköz bluetooth felfedező folyamatát az adapteren keresztül le kell állítani ha megy
        //mert nagyon lassítja a transzfert: mBluetoothAdapter.cancelDiscovery();
        try {
            bs.connect();
            Log.d("ASD","SIKER");
            //ha sikerült létrehozni a kapcsolatot, akkor menedzselnünk kell
            btk = new BlueToothKapcsolat(bs, h);
        } catch (IOException e) {
            Log.d("ASD","SIKERTELEN");
            e.printStackTrace();
            //ha hiba van akkor próbáljuk bezárni a kapcsolatot
            try {
                bs.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            //majd mivel úgyse tudunk vele mit kezdeni térjünk is vissza
            return;
        }
    }

    public void Lezaras(){
        try {
            bs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
