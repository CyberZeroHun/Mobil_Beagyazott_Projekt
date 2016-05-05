package hu.uniobuda.nik.bluetoothvegleges;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

/**
 * Created by thecy on 2016. 05. 04..
 */
public class BTFragment extends Fragment {

    private BluetoothAdapter bta;
    private BTService btserv;

    /*
    A fragment elkészítésekor leellenőrizzük, hogy be van-e kapcsolva a BT.
    */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        TODO: Ez elvileg nem fog megjelenni, ha nincs Bluetooth eszköz,
        TODO: De a biztonság kedvéért itt is leellenőrzöm.
        TODO: Ha nincs, akkor befejezi a fragmentet
         */
        bta=BluetoothAdapter.getDefaultAdapter();
        if(bta==null){
            Toast.makeText(this.getActivity(),R.string.nincs_bluetooth,Toast.LENGTH_LONG).show();
            this.getActivity().finish();
        }
    }

    /*
    Be inflateljük a fragment tartalmát XML-ből, itt készül el a felépítésének a hierarchiája.
    */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_bt, container, false);
    }


    /*
    Amikor ténylegesen is elkészült a hierarchia, akkor feldolgozzuk benne azokat az elemeket,
    amelyeket használni fogunk.
    */
    // TODO: Itt még én nem tudom, hogy mit szeretnék használni, majd eldöntöm.
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //super.onViewCreated(view, savedInstanceState);
        kv = (ListViewCompat) view.findViewById(R.id.eszkoz_lista);
    }

    //region Handler
    private String csatlakoztatottEszkozNeve = null;
    private ArrayAdapter<String> kaa;
    /*
    A szálak között a biztonságos üzenetküldést Handlerrel oldhatjuk meg.
    */
    //TODO: ezt még meg kell valósítani
    private final Handler hand = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            FragmentActivity fa = getActivity();
            switch(msg.what){
                //ha a szálak állapotváltozásról küldtek üzenetet
                case GlobalisKonstans.MSG_ALLAPOT_VALTOZAS:
                    switch (msg.arg1){
                        //TODO: itt kiírhatnánk állapotváltozást az ActionBar-ra, vagy bárhová
                        case GlobalisKonstans.BT_CSATLAKOZTATVA:
                            //egyenlőre az üzenetváltásokat Adapterben tároljuk
                            //új csatlakozásnál törölni kell
                            kaa.clear();
                            //kiíródik
                            break;
                        case GlobalisKonstans.BT_CSATLAKOZIK:
                            break;
                        case GlobalisKonstans.BT_NINCS_ALLAPOT:
                        case GlobalisKonstans.BT_FIGYEL:
                            break;
                    }
                    break;
                //ha a szálak üzenet elküldéséről küldtek üzenetet
                case GlobalisKonstans.MSG_IRAS:
                    //Egyenlőre hozzáadjuk az adapterhez az eszköz nevét és az üzenetét
                    //TODO: később itt kell lekezelni, hogy a kiküldött üzenettel mit kezdjünk
                    byte[] wbuf = (byte[]) msg.obj;
                    kaa.add(String.valueOf(R.string.en) + new String(wbuf));
                    break;
                //ha a szálak üzenet fogadásáról küldtek üzenetet
                case GlobalisKonstans.MSG_OLVASAS:
                    //Egyenlőre hozzáadjuk az adapterhez az eszköz nevét és az üzenetét
                    //TODO: később itt kell lekezelni, hogy a beérkezett üzenettel mit kezdjünk
                    byte[] rbuf = (byte[]) msg.obj;
                    kaa.add(csatlakoztatottEszkozNeve+" : "+new String(rbuf,0,msg.arg1));
                    break;
                //ha a szálak eszköz nevetküldtek el csatlakozáskor
                case GlobalisKonstans.MSG_ESZKOZNEV:
                    //külön ki is mentjük az eszköz nevét
                    csatlakoztatottEszkozNeve=msg.getData().getString(GlobalisKonstans.ESZKOZ_NEV);
                    //ha van aktivity, amire ki lehet rajzolni, akkor kiiratjuk a csatlakozás tényét
                    if(fa!=null){
                        Toast.makeText(fa,String.valueOf(R.string.sikeres_csatlakozas)+" "+csatlakoztatottEszkozNeve,Toast.LENGTH_LONG).show();
                    }
                    break;
                //ha a szálak egy toast üzenetet küldtek el
                case GlobalisKonstans.MSG_TOAST:
                    //ha van aktivity, amire ki lehet rajzolni, akkor a msg-ben található üzenetet kiiratjuk
                    if(fa!=null){
                        Toast.makeText(fa, msg.getData().getString(GlobalisKonstans.TOAST), Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    };
    //endregion

    private ListViewCompat kv;
    /*
    Itt indítjuk el az egész kommunikációt.
    */
    //TODO: ez még nem biztos, hogy teljes
    private void SzolgaltatasInditasa(){
        //TODO:Jelenleg adapterbe írjuk a csatlakozásokat, üzenetküldéseket és fogadásokat
        //kaa = new ArrayAdapter<String>(getActivity(),R.layout.uzenet_lista_elem);
        //kv.setAdapter(kaa);
        //TODO: itt még a gombok eseménykezelőit beállíthatjuk, hogy

        //elindítjuk a szolgálltatást
        //btserv = new BTService(getActivity(), hand);

    }

    /*
    Amikor Intent-el indítunk egy kérést a felhasználó számára, hogy kapcsolja be a BT-t
    akkor a válasz ide fog megérkezni.
    */
    //TODO: itt még több állapot is lehet
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case GlobalisKonstans.BT_BEKAPCSOLAS_KERES:
                if(resultCode== Activity.RESULT_OK){
                    //Ha leOKézta a BT-t bekapcsolását akkor indíthatjuk a szolgálltatást.
                    SzolgaltatasInditasa();
                    /*
                    //ha okot nyomtak eltüntetem a menüből a BlueTooth bekapcsolása opciót
                    MenuItem elem = (MenuItem) getActivity().findViewById(R.id.menu_bt_bekapcs);
                    elem.setVisible(false);
                    getActivity().invalidateOptionsMenu();
                    */
                } else {
                    //Ha nem OK-t nyomott, akkor be kell fejezni a fragmentet ismét.
                    Toast.makeText(this.getActivity(),R.string.nem_kapcsolta_be,Toast.LENGTH_LONG).show();
                    //this.getActivity().finish();
                    /*
                    TODO: Egy olyan fragmentet tölts be, amely kiírja, hogy a szolgáltatás nem elérhető
                    TODO: Illetve vetérelni se tudjuk a fegyverzetet, ne jelenjen meg a joystick.
                    TODO: A beállításokat sem tudjuk neki elküldeni, szürküljön ki.
                    TODO: Igazából ilyenkor csak kamerakép van a wifiről maximum.
                    */
                    FragmentManager fm = getFragmentManager();
                    fm.beginTransaction()
                        .replace(R.id.fragment_tarolo,new UzenetFragment())
                        .commit();
                }
                break;
            case GlobalisKonstans.BT_CSATLAKOZAS_KERES:
                //ha csatlakozáskérés érkezik, akkor kinyerjük a a kérésből az eszköz címét
                //ami alapján meghatározzuk az eszközt és a szolgáltatásunk kapcsolódik rá
                if (requestCode==Activity.RESULT_OK){
                    String cim=data.getExtras().getString(GlobalisKonstans.EXTRA_ESZKOZ_CIM);
                    BluetoothDevice eszk = bta.getRemoteDevice(cim);
                    btserv.Kapcsolodas(eszk);
                }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if(!bta.isEnabled()) {
            /*
            Ha nincs bekapcsolva a bluetooth, amikor a fragment láthatóvá válik, akkor
            rá kell vennünk, hogy elindítsa a felhasználó.
            Ez akkor is megjelenik, ha átlép egy másik app-ba és közben tiltja le, majd visszajön ide.
             */
            //TODO: viszont ha közben tiltja le, akkor azt még nem érzékeli
            Intent inte = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(inte, GlobalisKonstans.BT_BEKAPCSOLAS_KERES);
        } else if(btserv == null){
            //Egyébként ha még nem indítottuk el a szolgáltatást, akkor most megtehetjük.
            SzolgaltatasInditasa();
        }
        /*
        Minden més esetben be van kapcsolva már a BT és
        a Service is el van indítva.
        */
    }

    /*
    Amikor a fragment látható és interaktív használatra is kész,
    mert vagy most készült el, vagy leállított állapotból folytatódik,
    akkor ha már volt elindítva szolgáltatás és most nincs állapota,
    akkor most ténylegesen is el kell indítani.
    */
    //TODO: itt még több állapot is lehet
    @Override
    public void onResume() {
        super.onResume();
        if(btserv!=null && btserv.getAllapot() == GlobalisKonstans.BT_NINCS_ALLAPOT){
            btserv.Elinditas();
        }
    }

    /*
        A fragment végleges megtisztításakor ha volt elindított szolgálltatás
        akkor azt le kell állítanunk.
    */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(btserv!=null){
            btserv.Leallitas();
        }
    }

    /*
    Ezzel tudunk üzenetet küldeni a bluetooth modulnak.
    */
    private void UzenetKuldes(String uzenet){
        /*
        Amennyiben még nem csatlakoztunk eszközre, nem tudunk üzenetet küldeni neki
        így térjünk is vissza, ne csináljunk semmit.
        */
        if(btserv.getAllapot() != GlobalisKonstans.BT_CSATLAKOZTATVA){
            Toast.makeText(getActivity(),R.string.nem_kapcsolta_be,Toast.LENGTH_LONG).show();
            return;
        }

        /*
        Egyébként ha csatlakozva vagyunk és nem üres az üzenetünk, akkor küldjük el.
         */
        if(!uzenet.isEmpty()){
            byte[] mit = uzenet.getBytes();
            btserv.Iras(mit);
            Toast.makeText(getActivity(),R.string.uzenet_elkuldve+" "+mit,Toast.LENGTH_LONG).show();
        }
    }


    private void EszkozFelderites(){
        //Ha éppen felderítenénk, akkor azt fejezzük be.
        if (bta.isDiscovering()) {
            bta.cancelDiscovery();
        }
        bta.startDiscovery();
    }

    //region BroadCastReceiver
    private BTEszkozAdapter btea = new BTEszkozAdapter();
    /*
    Ez a BroadCast Receiver, amellyel elkapjuk a kinyert elérhető és
    párosított BlueTooth eszközöket és hozzáadjuk az Adapterünkhöz.
    */
    private final BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String akcio = intent.getAction();
            //az akció, hogy új eszközt találtunk
            if(BluetoothDevice.ACTION_FOUND.equals(akcio)) {
                BluetoothDevice eszk = intent.getParcelableExtra((BluetoothDevice.EXTRA_DEVICE));
                //ha nincs még párosítva, akkor hozzáadjuk
                if(eszk.getBondState()!=BluetoothDevice.BOND_BONDED) {
                    btea.Hozzaadas(new BTEszkoz(eszk.getName(),eszk.getAddress()) );
                }
            } else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(akcio)) {
                //az akció, hogy vége a felderítésnek
                //és nem találtunk egy elemet sem, akkor felveszünk egy üres elemet
                if(btea.isEmpty()){
                    btea.Hozzaadas(new BTEszkoz(String.valueOf(R.string.nincs_felderitheto), ""));
                }
            }
        }
    };
    //endregion

    // region Menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.bt_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.secure_connect_scan: {
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(getActivity(), EszkozListaFragment.class);
                startActivityForResult(serverIntent, GlobalisKonstans.BT_CSATLAKOZAS_KERES);
                return true;
            }
            case R.id.discoverable: {
                //ez a menüelem kérést nyújt, hogy felfedezhetővé váljunk mások előtt
                //ha mi töltjük be a szerver szerepét
                //TODO: most nincs erre szükség, mert kliensek vagyunk
                if (bta.getScanMode() !=
                        BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
                            Intent inte = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                            inte.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                            startActivity(inte);
                }
                return true;
            }
        }
        return false;
    }
    //endregion

}
