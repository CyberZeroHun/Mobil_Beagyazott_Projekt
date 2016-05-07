package hu.uniobuda.nik.ciwsduino;

/**
 * Created by thecy on 2016. 05. 07..
 */
/*
Saját osztály, amelyben az aktivitás objektumokat fogjuk eltárolni.
*/
public class AktivitasObject {

    //változók
    private String ido;
    private int szog;
    private int tav;

    //a konstrulktor
    public AktivitasObject(String ido, int szog, int tav) {
        this.ido = ido;
        this.szog = szog;
        this.tav = tav;
    }

    //getterek és setterek
    public String getIdo() {
        return ido;
    }

    public void setIdo(String ido) {
        this.ido = ido;
    }

    public int getSzog() {
        return szog;
    }

    public void setSzog(int szog) {
        this.szog = szog;
    }

    public int getTav() {
        return tav;
    }

    public void setTav(int tav) {
        this.tav = tav;
    }

}
