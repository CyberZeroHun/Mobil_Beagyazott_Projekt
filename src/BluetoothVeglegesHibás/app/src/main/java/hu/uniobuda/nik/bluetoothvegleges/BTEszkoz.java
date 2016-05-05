package hu.uniobuda.nik.bluetoothvegleges;

import java.util.ArrayList;

/**
 * Created by thecy on 2016. 05. 05..
 */
public class BTEszkoz {
    /*
    Egy eszköz objektum, amely a nevét és mac címét tárolja.
    */

    private String nev;
    private String mac_cim;

    public BTEszkoz(String nev, String mac_cim) {
        this.nev = nev;
        this.mac_cim = mac_cim;
    }

    public String getNev() {
        return nev;
    }

    public void setNev(String nev) {
        this.nev = nev;
    }

    public String getMac_cim() {
        return mac_cim;
    }

    public void setMac_cim(String mac_cim) {
        this.mac_cim = mac_cim;
    }
}
