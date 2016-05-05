package hu.ciwsduino.bluetoothtest;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by thecy on 2016. 05. 01..
 */
public class BlueToothKapcsolat extends Thread{
    private Handler h;
    private BluetoothSocket csatorna;
    private InputStream is;
    private OutputStream os;

    public BlueToothKapcsolat(BluetoothSocket csatorna, Handler h) {
        this.csatorna = csatorna;
        this.h = h;

        //valamit mindenféleképpen illik beállítani
        InputStream tmp_is = null;
        OutputStream tmp_os = null;

        try{
            tmp_is = csatorna.getInputStream();
            tmp_os = csatorna.getOutputStream();
            Log.d("ASD","jo");
        } catch (IOException e){
            e.printStackTrace();
            Log.d("ASD","rossz");
        }

        //vagy null vagy a tényleges csatornák
        this.is = tmp_is;
        this.os = tmp_os;
    }

    //Az olvasás. Nem rakom külön metódusba, mert itt ez csak ezzel foglalkozik.
    //És plusz egy felesleges függvényhívás lenne
    @Override
    public void run() {
        Log.d("ASD","FUTAFUCK");
        //super.run();
        //maximum 1024-hosszú üzi, ha hosszabb, akkor két részletben
        //EZT MAJD AZ ÜZENETEKNEK MEGFELELŐEN LEJEBB VESSZÜK
        byte[] buf = new byte[1024];
        int hossz;

        //végtelen ciklusban figyeljük hogy jön-e üzenet
        while(true){
            try {
                hossz=is.read(buf);
                //az üzit egy Handlerrel elküldjük a GUI-nak
                //ha erre nincs szükség, akkor a Handlerre sem
                //üzenet ID ,arg ,felesleges arg, mit obj
                h.obtainMessage(GlobalisKonstansok.ERKEZETT,hossz,-1,buf).sendToTarget();
            } catch (IOException e) {
                e.printStackTrace();
                //ne folytassa, ugorjon a következő ciklusra
                break;
            }
        }
    }

    //ezzel írunk byte-ot a kapcsolatra
    public void Ir(byte[] mit){
        try {
            os.write(mit);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //ezzel írunk stringet a kapcsolatra
    public void Ir(String mit){
        try {
            os.write(mit.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //ezzel lőjük le a visszaadott bluetooth kapcsolatot
    public void Lezaz(){
        try {
            csatorna.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
