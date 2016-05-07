package hu.uniobuda.nik.ciwsduino;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.ListViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import static hu.uniobuda.nik.ciwsduino.GlobalisKonstansok.*;

/**
 * Created by thecy on 2016. 04. 25..
 */
public class AktivitasokFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //beinflate-elem a fragment-be az xml tartalmát
        return inflater.inflate(R.layout.fragment_aktivitasok, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //megkeressük a listát
        ListViewCompat lv = (ListViewCompat) view.findViewById(R.id.aktivitaslista);
        //összekötjük a listát az adapterével
        AktivitasAdapter aa = new AktivitasAdapter();
        lv.setAdapter(aa);

        //beolvassuk az URL-ről a JSON-t
        String jsonFromhtml="";
        InputStream is = null;
        try {
            URL u = new URL(AKTIVITASOK_LINK);
            URLConnection uc = u.openConnection();
            is = uc.getInputStream();
            //beolvassuk a http kérésre adott választ
            int hossz=0;
            byte[] buff = new byte[1024];
            //1024 byte-onként olvassuk amíg van mit
            while((hossz= is.read(buff))!= -1){
                jsonFromhtml=jsonFromhtml.concat(buff.toString());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //a végén próbáljuk lezárni a fájlt, ha le lehet
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //feldolgozzuk a JSON-t Aktivitásokra és feltöltjük vele az adaptert
        try{
            //kinyerjük a gyökér elemet
            JSONObject jo = new JSONObject(jsonFromhtml);
            //abból kinyerjük az első szinten az összes aktivitás objektumot
            JSONArray ja = jo.getJSONArray("aktivitas");

            String ido;
            int szog, tav;
            //nincs FOREACH a JSON object-re, ezért FOR-t kell használni
            for (int i=0; i< ja.length();i++) {
                JSONObject akt = ja.getJSONObject(i);
                //minden objektumnak kinyerjük a tulajdonságait
                ido=akt.optString("ido").toString();
                szog=Integer.parseInt(akt.optString("szog").toString());
                tav=Integer.parseInt(akt.optString("tav").toString());
                aa.Hozzaadas(new AktivitasObject(ido, szog, tav));
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }
}