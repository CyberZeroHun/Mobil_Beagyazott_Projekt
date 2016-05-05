package hu.ciwsduino.bluetoothtest;

/**
 * Created by thecy on 2016. 03. 05..
 */
public class Eszkoz {
    private String nev;
    private String mac_cim;

    public Eszkoz(String nev, String mac_cim) {
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
