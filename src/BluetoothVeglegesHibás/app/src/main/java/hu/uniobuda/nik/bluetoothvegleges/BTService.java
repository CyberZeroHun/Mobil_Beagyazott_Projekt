package hu.uniobuda.nik.bluetoothvegleges;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.FragmentActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BTService{

    private int allapot;

    //A szinkronizálás védi az adatokat a szálak között.
    public synchronized int getAllapot() {
        return allapot;
    }

    public synchronized void setAllapot(int allapot) {
        this.allapot = allapot;
        //A setterben az állapot változása esetén értesítjük a fő szálat.
        h.obtainMessage(GlobalisKonstans.MSG_ALLAPOT_VALTOZAS, allapot, -1).sendToTarget();
    }

    private final BluetoothAdapter ba;
    private final Handler h;

    /*
    A konstruktorban beállítjuk az adaptert és a Handlert.
    Valamint az állapotot nincs állapotra állítjuk kezdetben, amikor létrehozzuk a szolgálltatást.
    */
    public BTService(Context activity, Handler hand) {
        this.ba = BluetoothAdapter.getDefaultAdapter();
        allapot=GlobalisKonstans.BT_NINCS_ALLAPOT;
        this.h = hand;
    }

    SzerverSzal szsz;
    KliensSzal ksz;
    CsatlakozasSzal cssz;

    //region CsatlakozasSzal
    /*
    Itt kezeljük le a bemeneti és kimeneti stream-et.
    */
    private class CsatlakozasSzal extends Thread{
        private BluetoothSocket bs;
        private InputStream is;
        private OutputStream os;

        public CsatlakozasSzal(BluetoothSocket bs){
            this.bs=bs;

            //valamit mindenféleképpen illik beállítani
            InputStream tmp_is = null;
            OutputStream tmp_os = null;

            try{
                tmp_is = bs.getInputStream();
                tmp_os = bs.getOutputStream();
            } catch (IOException e){
                e.printStackTrace();
            }

            //vagy null vagy a tényleges csatornák
            this.is = tmp_is;
            this.os = tmp_os;
        }

        /*
        Folyamatosan próbálunk olvasni a bemenetről.
        */
        @Override
        public void run() {
            //super.run();

            //Maximum 1024-hosszú üzi, ha hosszabb, akkor két részletben
            //TODO: Ezt majd az üzeneteknek megfelelően lejebb kell venni.
            byte[] buf = new byte[1024];
            int hossz;

            //végtelen ciklusban figyeljük hogy jön-e üzenet
            while(true) {
                try {
                    hossz = is.read(buf);
                    //az üzit egy Handlerrel elküldjük a GUI-nak
                    //ha erre nincs szükség, akkor a Handlerre sem
                    //üzenet ID ,arg ,felesleges arg, mit obj
                    h.obtainMessage(GlobalisKonstans.MSG_OLVASAS, hossz, -1, buf).sendToTarget();
                } catch (IOException e) {
                    //a kapcsolat megszakadásáról üzenetet küldünk az Activitynek
                    Message msg = h.obtainMessage(GlobalisKonstans.MSG_TOAST);
                    Bundle bundle = new Bundle();
                    bundle.putString(GlobalisKonstans.TOAST, String.valueOf(R.string.hiba_megszakadt));
                    msg.setData(bundle);
                    h.sendMessage(msg);

                    // Elindítjuk a szolgáltatást, hogy újraindítsuk a figyelést
                    BTService.this.Elinditas();
                    //ne folytassa, ugorjon a következő ciklusra
                    break;
                }
            }
        }
        /*
        Ezzel tudok írni a kimeneti Stream-re.
        */
        public void Iras(byte[] mit){
            try {
                os.write(mit);
                //Az írásról értesítem a Handler-t
                h.obtainMessage(GlobalisKonstans.MSG_IRAS, -1, -1, mit).sendToTarget();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /*
        Az egész kapcsolatot lezáró.
        */
        public void Lezaras() {
            try {
                bs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //endregion
    //region KliensSzal
    KliensSzal ks;
    private class KliensSzal extends Thread{
        private final BluetoothSocket bs;
        private final BluetoothDevice bd;

        /*
        Konstruktorralé beállítjuk az eszközt és
        elkészítjük hozzá a Socket-et.
        */
        public KliensSzal(BluetoothDevice bd){
            this.bd=bd;
            BluetoothSocket temp= null;
            try {
                temp= bd.createRfcommSocketToServiceRecord(UUID.fromString(GlobalisKonstans.BT_UUID));
            } catch (IOException e) {
                e.printStackTrace();
            }
            bs = temp;
        }

        /*
        A kliens szál megpróbál csatlakozni.
        Ha sikertelen akkor üzenetet küld az Activity-nek és újra
        elindítja a szolgáltatást.
        */
        @Override
        public void run() {
            //super.run();
            setName("KliensSzal");
            //Fogadásnál ne lassítja a felfedezés a fogadást, ezért leállítjuk.
            ba.cancelDiscovery();
            try {
                bs.connect();
            } catch (IOException e) {
                e.printStackTrace();
                //ha hiba van akkor próbáljuk bezárni a kapcsolatot
                try {
                    bs.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                //majd mivel úgyse tudunk vele mit kezdeni térjünk is vissza
                //de előtte kezeljük le a hibát

                //A hibáról üzenetet küldünk vissza az Activity-nek
                Message msg = h.obtainMessage(GlobalisKonstans.MSG_TOAST);
                Bundle bundle = new Bundle();
                bundle.putString(GlobalisKonstans.TOAST, String.valueOf(R.string.hiba_csatlakozas));
                msg.setData(bundle);
                h.sendMessage(msg);

                //Újra elindítjuk a szolgáltatást hogy újraindítsuk a figyelést
                BTService.this.Elinditas();

                return;
            }
            //sikeres kapcsolódás után reset-eljük a KliensSzál példányt null-al
            //mert készen vagyunk
            synchronized (BTService.this){
                ks=null;
            }
            Kapcsolodott(bs,bd);
        }

        /*
        A kliens zárásakor zárni kell a kapcsolatot.
        */
        public void Lezaras(){
            try {
                bs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //endregion
    //region SzerverSzal
    public class SzerverSzal extends Thread {
        private final BluetoothServerSocket bss;

        /*
        A konstruktor, ami az adapterhez legyártja a védett szolgáltatást.
        */
        public SzerverSzal() {
            BluetoothServerSocket temp = null;
            try {
                temp = ba.listenUsingRfcommWithServiceRecord(GlobalisKonstans.BT_NEV, UUID.fromString(GlobalisKonstans.BT_UUID));
            } catch (IOException e) {
                e.printStackTrace();
            }
            bss = temp;
        }

        /*
        A szerver csak folyamatosan figyel, amíg csak online állapotban van.
        Illetve, amíg nem csatlakozott rá valaki.
        */
        @Override
        public void run() {
            //super.run();
            setName("SzerverSzal");
            BluetoothSocket bs = null;

            while (allapot!=GlobalisKonstans.BT_CSATLAKOZTATVA){
                try {
                    bs = bss.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                    //hiba esetén ne folytassa, ugorjon a következő ciklusra
                    break;
                }

                //ha valaki csatlakozott
                if(bs!=null){
                    //így védjük meg, hogy a többi szál belekutyuljon
                    //szinkronizáljuk a dolgokat
                    synchronized (BTService.this){
                        switch (allapot){
                            case GlobalisKonstans.BT_FIGYEL:
                            case GlobalisKonstans.BT_CSATLAKOZIK:
                                Kapcsolodott(bs, bs.getRemoteDevice());//
                                break;
                            case GlobalisKonstans.BT_NINCS_ALLAPOT:
                            case GlobalisKonstans.BT_CSATLAKOZTATVA:
                                try {
                                    bss.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                    }
                }
            }
        }

        /*
        A szerver zárásakor zárni kell a kapcsolatot.
        */
        public void Lezaras(){
            try {
                bss.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //endregion

    public synchronized void Elinditas() {

        //ha a kliens fut, akkor le kell állítani és nullázni
        if(ksz!=null){
            ksz.Lezaras();
            ksz=null;
        }

        //ha van csatlakozott kapcsolat, akkor ezt is le kell indításkor állítani és lenullázni
        if(cssz!=null){
            cssz.Lezaras();
            cssz=null;
        }

        //a szervert ilyenkor figyelésre kell állítani
        setAllapot(GlobalisKonstans.BT_FIGYEL);
        //ha még nincs, akkor új példányt kell belőle készíteni és el kell indítani
        if(szsz==null){
            szsz = new SzerverSzal();
            szsz.start();
        }
    }

    public synchronized void Leallitas() {

        //ha a kliens fut, akkor le kell állítani és nullázni
        if(ksz!=null){
            ksz.Lezaras();
            ksz=null;
        }

        //ha van csatlakozott kapcsolat, akkor ezt is le kell indításkor állítani és lenullázni
        if(cssz!=null){
            cssz.Lezaras();
            cssz=null;
        }

        //ha van szerver, akkor itt ezt is le kell állítani és lenullázni
        if(szsz!=null){
            szsz.Lezaras();
            szsz=null;
        }
        //majd visszaállítani nincs állapotra a szervert
        setAllapot(GlobalisKonstans.BT_NINCS_ALLAPOT);
    }

    public void Iras(byte[] mit) {
        //csinálunk egy szinkronizált átmeneti másolatot
        CsatlakozasSzal tmp;
        synchronized (this){
            //ha nincs csatlakoztatva, akkor nem írunk semmit
            if(allapot != GlobalisKonstans.BT_CSATLAKOZTATVA) {
                return;
            }
            tmp = cssz;
        }
        tmp.Iras(mit);
    }

    public synchronized void Kapcsolodas(BluetoothDevice bd){

        //ha éppen csatlakozik és a kliens nem üres, akkor ki kell üríteni
        if(allapot== GlobalisKonstans.BT_CSATLAKOZIK) {
            if(ksz!=null){
                ksz.Lezaras();
                ksz=null;
            }
        }

        //a csatlakozásokat mindenféleképpen üríteni kell, ha van
        if(cssz!=null){
            cssz.Lezaras();
            cssz=null;
        }

        //új kliens elindítása
        ksz = new KliensSzal(bd);
        ksz.start();

        //állapot átállítása csatlakozikra
        setAllapot(GlobalisKonstans.BT_CSATLAKOZIK);
    }

    public synchronized void Kapcsolodott(BluetoothSocket bs, BluetoothDevice bd){

        //minden szálat lenullázni ha nem nulla
        if(ksz!=null){
            ksz.Lezaras();
            ksz=null;
        }
        if(cssz!=null){
            cssz.Lezaras();
            cssz=null;
        }
        if(szsz!=null){
            szsz.Lezaras();
            szsz=null;
        }

        //elindítani egy új csatlakozást az I/O kezelésére
        cssz=new CsatlakozasSzal(bs);
        cssz.start();

        //üzenetet küldeni a Handlernek az új eszköz nevével
        Message msg = h.obtainMessage(GlobalisKonstans.MSG_ESZKOZNEV);
        Bundle bundle = new Bundle();
        bundle.putString(GlobalisKonstans.ESZKOZ_NEV, bd.getName());
        msg.setData(bundle);
        h.sendMessage(msg);

        //állapot átállítás csatlakozvára
        setAllapot(GlobalisKonstans.BT_CSATLAKOZTATVA);

    }

}
