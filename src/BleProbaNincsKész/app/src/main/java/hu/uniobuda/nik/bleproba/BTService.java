package hu.uniobuda.nik.bleproba;

import android.app.Service;
import android.bluetooth.BluetoothGatt;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

public class BTService extends Service {

    private BluetoothGatt BTGAtt;

    public BTService() {
    }

    public class HelyiBinder extends Binder{
        BTService getService() {
            return BTService.this;
        }
    }

    private final IBinder b = new HelyiBinder();

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return b;
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    public void close() {
        if (BTGAtt == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            BTGAtt.close();
        }
        BTGAtt = null;
    }

    // After using a given device, you should make sure that BluetoothGatt.close() is called
    // such that resources are cleaned up properly.  In this particular example, close() is
    // invoked when the UI is disconnected from the Service.
    @Override
    public boolean onUnbind(Intent intent) {
        close();
        return super.onUnbind(intent);
    }
}
