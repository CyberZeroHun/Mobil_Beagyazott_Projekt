package hu.uniobuda.nik.bluetoothvegleges;

/**
 * Created by thecy on 2016. 05. 04..
 */
public interface GlobalisKonstans {

    //ezek a konstansok bárhonnan elérhetőek

    //Marshmallow engedélykérések
    public static final int BT_ENGEDELY = 100;
    public static final int BT_ADMIN_ENGEDELY = 101;

    //bluetooth bekapcsolásának kérése
    public static final int BT_BEKAPCSOLAS_KERES = 1;
    public static final int BT_CSATLAKOZAS_KERES = 2;

    //adatra vonatkozó kulcs
    public static final String EXTRA_ESZKOZ_CIM = "device_address";

    //a bluetooth állapotai
    public static final int BT_NINCS_ALLAPOT = 10;
    public static final int BT_CSATLAKOZTATVA = 11;
    public static final int BT_FIGYEL = 12;
    public static final int BT_CSATLAKOZIK = 13;

    //a Handler-nek küldött üzenettel kapcsolatos dolgok
    public static final int MSG_ALLAPOT_VALTOZAS = 1000;
    public static final int MSG_OLVASAS = 1001;
    public static final int MSG_IRAS = 1002;
    public static final int MSG_ESZKOZNEV = 1003;
    public static final int MSG_TOAST = 1004;

    //a Handler.től érkező kulcsok
    public static final String ESZKOZ_NEV = "device_name";
    public static final String TOAST = "toast";

    //

    //szerver esetén a neve és azonosítója
    public final String BT_NEV = "CIWSduino";
    public final String BT_UUID = "00001101-0000-1000-8000-00805f9b34fb";
}
