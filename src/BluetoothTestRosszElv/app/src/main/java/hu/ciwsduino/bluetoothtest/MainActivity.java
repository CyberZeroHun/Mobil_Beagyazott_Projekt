package hu.ciwsduino.bluetoothtest;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends Activity {

   public static final int REQUEST_ENABLE_BT = 1;
    public static final int REQUEST_ENABLE_FELFEDEZHETO = 2;

    final EszkozAdapter bt_eszkozok_sajatadapter=new EszkozAdapter();
    final EszkozAdapter bt_eszkozok_sajatadapter2=new EszkozAdapter();
    private ListView lista;
    private Button gomb3,gomb4;
    private AppCompatTextView bejovo;
    private BluetoothDevice bt_ciwsduino;

    private BlueToothKliens k;

    //szervernél az összeset ebben tároljuk, kliensnél csak egy kapcsolat van benne a szerver felé
    private ArrayList<BlueToothKapcsolat> kapcsolatok;

    private final BroadcastReceiver figy = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //megnézzük mi az akciót, amit az adattal lehet tenni
            String akcio = intent.getAction();
            //ha az akció az, hogy egy bt eszköz találtunk akkor
            if(BluetoothDevice.ACTION_FOUND.equals(akcio)){
                //kinyerjük belőle az eszközt, mármint az intent-ből
                BluetoothDevice eszk=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (eszk.getBondState() != BluetoothDevice.BOND_BONDED) { //csak akkor jelezze ki, ha nincs már párosítva
                    bt_eszkozok_sajatadapter2.eszkozok.add(new Eszkoz(eszk.getName(), eszk.getAddress()));
                    ((BaseAdapter) lista.getAdapter()).notifyDataSetChanged();
                }
            } else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(akcio)){
                if(bt_eszkozok_sajatadapter2.getCount()==0){
                    bt_eszkozok_sajatadapter2.eszkozok.add(new Eszkoz("Nincs felderíthető eszköz!", ""));
                }
                ((BaseAdapter) lista.getAdapter()).notifyDataSetChanged();
                gomb3.setText(getString(R.string.gomb3_be));
            } else if(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED.equals(akcio)){
                int elozo = intent.getIntExtra(BluetoothAdapter.EXTRA_PREVIOUS_SCAN_MODE,BluetoothAdapter.ERROR);
                int most = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE,BluetoothAdapter.ERROR);
                if(most!=BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){ //így hibákra is visszavált, de így a jó
                    //ha most változott és már nem felfedezhetőre
                    //akkor visszaállíthatom a gomb feliratát, hogy ismét megnyomhassák
                    gomb4.setText(getString(R.string.gomb4_be));
                }
            }
        }
    };

    private Handler figyelo = new Handler() {
        @Override
        public void handleMessage(Message uzenet) {
            switch(uzenet.what){
                case GlobalisKonstansok.ERKEZETT:
                    //arg1: üzenet hossza, a bufferben
                    //arg2: lényegtelen, -1-el jelölve
                    // obj a byte[]buffer
                    int hossz= uzenet.arg1;
                    byte[] be = (byte[]) uzenet.obj;
                    bejovo = (AppCompatTextView) findViewById(R.id.bejovo);
                    bejovo.setText("Új üzenet jött!\r\n");
                    bejovo.append(be.toString());
                    //bejovo.invalidate();
                    break;
                case GlobalisKonstansok.VALAMI:
                    break;
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
                        //!!!!! el kell tároljuk, hogyha aztán hozzá akarunk férni
                        Log.d("ASD", eszk.getName());
                        //if(eszk.getName().toString().equals("CIWSduino")){
                        if(eszk.getName().toString().equals("HC-05")){
                            Log.d("ASD","Talált CIWS-t.");
                            bt_ciwsduino = eszk;
                        } else {
                            Log.d("ASD","Nem talált CIWS-t.");
                        }
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
                if (gomb3.getText() == getString(R.string.gomb3_be)) {
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

        //regisztrálás az BroadCastReceiver-re, hogy változott az eszközünk felderíthetősége
        szuro = new IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        this.registerReceiver(figy, szuro); //le is kell regisztrálni az onDestroy-ban (a három együtt leiratkozódik)
        gomb4= (Button) findViewById(R.id.gomb4);
        final Intent felfedezheto = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE); //kihoztam, hogy ne hozzon létre mindig új példányt
        gomb4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gomb4.getText()==getString(R.string.gomb4_be)) {
                    felfedezheto.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,300);
                    startActivityForResult(felfedezheto,REQUEST_ENABLE_FELFEDEZHETO);
                    gomb4.setText(getString(R.string.gomb4_ki));
                } else {
                    //a felfedezhetőséget csak úgy kapcsolhatjuk ki, ha 1mp-re állítjuk vissza és az gyorsan lejár
                    //ekkor már nem kérdezi meg a felhasználót, mert egyszer már megkérdeztük és elfogadta
                    felfedezheto.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 1);
                    startActivityForResult(felfedezheto, REQUEST_ENABLE_FELFEDEZHETO);
                    //gomb4.setText(getString(R.string.gomb4_be)); //ez magától állítja ha lejárt
                }
            }
        });

        Button gomb5= (Button) findViewById(R.id.gomb5);
        gomb5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // csatlakozunk a CIWSduino-hoz, ha volt a párosított listában
                if (bt_ciwsduino != null) {
                    k = new BlueToothKliens(bt_ciwsduino, figyelo);
                    bt_adapter.cancelDiscovery();
                    k.run();
                }
            }
        });

        Button gomb6= (Button) findViewById(R.id.gomb6);
        gomb6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                k.btk.Ir("ASDQWE - egy uj uzenet.\r\n");
                //k.btk.run();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_ENABLE_FELFEDEZHETO) {
            if (resultCode==RESULT_CANCELED) {
                //a felhasználó nem engedélyezte a felfedezhetőséget
                gomb4.setText(getString(R.string.gomb4_be));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(figy);
    }
}
