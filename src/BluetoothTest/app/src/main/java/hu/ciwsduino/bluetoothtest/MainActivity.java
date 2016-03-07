package hu.ciwsduino.bluetoothtest;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

   public static final int REQUEST_ENABLE_BT = 1;

    final EszkozAdapter bt_eszkozok_sajatadapter=new EszkozAdapter();
    final EszkozAdapter bt_eszkozok_sajatadapter2=new EszkozAdapter();
    private ListView lista;
    private Button gomb3;
    private final BroadcastReceiver figy = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //megnézzük mi az akciót, amit az adattal lehet tenni
            String akcio = intent.getAction();
            //ha az akció az, hogy egy bt eszköz találtunk akkor
            if(BluetoothDevice.ACTION_FOUND.equals(akcio)){
                //kinyerjük belőle az eszközt, mármint az intent-ből
                BluetoothDevice eszk=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                bt_eszkozok_sajatadapter2.eszkozok.add(new Eszkoz(eszk.getName(), eszk.getAddress()));
                ((BaseAdapter) lista.getAdapter()).notifyDataSetChanged();
            } else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(akcio)){
                if(bt_eszkozok_sajatadapter2.getCount()==0){
                    bt_eszkozok_sajatadapter2.eszkozok.add(new Eszkoz("Nincs felderíthető eszköz!", ""));
                }
                ((BaseAdapter) lista.getAdapter()).notifyDataSetChanged();
                gomb3.setText(getString(R.string.gomb3_be));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final BluetoothAdapter bt_adapter = BluetoothAdapter.getDefaultAdapter();

        Button gomb1 = (Button) findViewById(R.id.gomb1);
        gomb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bt_adapter==null){
                    //nincs bluetooth a telefonban
                } else if(!bt_adapter.isEnabled()){
                    //nincs engedélyezve
                    Intent bt_eng = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(bt_eng,REQUEST_ENABLE_BT);
                }
            }
        });

        Button gomb2 = (Button) findViewById(R.id.gomb2);
        lista = (ListView) findViewById(R.id.bt_eszk_lista);
        gomb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt_eszkozok_sajatadapter.ListaTorles();
                Set<BluetoothDevice> parositottak = bt_adapter.getBondedDevices();
                if (parositottak.size() > 0) {
                    for (BluetoothDevice eszk : parositottak) {
                        bt_eszkozok_sajatadapter.eszkozok.add(new Eszkoz(eszk.getName(), eszk.getAddress()));
                    }
                } else {
                    bt_eszkozok_sajatadapter.eszkozok.add(new Eszkoz("Nincs párosított eszköz!", ""));
                }
                lista.setAdapter(bt_eszkozok_sajatadapter);
            }
        });

        //regisztrálás az BroadCastReceiver-re, hogy találtunk eszközt
        IntentFilter szuro = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(figy, szuro); //le is kell regisztrálni az onDestroy-ban
        //regisztrálás az BroadCastReceiver-re, hogy vége a felderítésnek
        szuro = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(figy, szuro); //le is kell regisztrálni az onDestroy-ban (a kettő együtt leiratkozódik)

        gomb3= (Button) findViewById(R.id.gomb3);
        gomb3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gomb3.getText()==getString(R.string.gomb3_be)) {
                    bt_eszkozok_sajatadapter2.ListaTorles();
                    lista.setAdapter(bt_eszkozok_sajatadapter2);
                    if (bt_adapter.isDiscovering()) {
                        bt_adapter.cancelDiscovery();
                    }
                    bt_adapter.startDiscovery();
                    gomb3.setText(getString(R.string.gomb3_ki));
                } else {
                    if (bt_adapter != null) {
                        bt_adapter.cancelDiscovery();
                    }
                    gomb3.setText(getString(R.string.gomb3_be));
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(figy);
    }
}
