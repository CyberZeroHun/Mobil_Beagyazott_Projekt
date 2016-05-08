package hu.uniobuda.nik.ciwsduino;

/**
 * Created by thecy on 2016. 05. 06..
 */
public final class GlobalisKonstansok {
    public final static String GITHUB_LINK = "https://github.com/CyberZeroHun/Mobil_Beagyazott_Projekt/";
    public final static String AA_EMAIL = "ambrus.attila@stud.uni-obuda.hu";
    public final static String ZC_EMAIL = "forisz.zisis@gmail.com";
    public final static String AKTIVITASOK_LINK = "http://www.ciwsduino.webtelek.hu/aktivitas.php";
    public final static int INTERNET_ENGEDELY = 100;

    //SharedPreferences konstansok
    public final static String SP_TAV="SP_TAV";
    public final static String SP_SZOG="SP_SZOG";
    public final static String SP_TAVOSZT="SP_TAVOSZT";
    public final static String SP_SZOGOSZT="SP_SZOGOSZT";
    public final static String SP_VIB="SP_VIB";


    public static String ipAddress = null;

    private GlobalisKonstansok() {
        throw new UnsupportedOperationException("Cannot instantiate utility class");
    }
}
