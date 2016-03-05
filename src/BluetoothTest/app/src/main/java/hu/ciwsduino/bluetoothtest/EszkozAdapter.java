package hu.ciwsduino.bluetoothtest;

import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thecy on 2016. 03. 05..
 */
public class EszkozAdapter extends BaseAdapter {

    List<Eszkoz> eszkozok;

    public EszkozAdapter(){
        this.eszkozok= new ArrayList<Eszkoz>();
    }

    public EszkozAdapter(List<Eszkoz> eszkozok) {
        this.eszkozok = eszkozok;
    }

    @Override
    public int getCount() {
        return eszkozok.size();
    }

    @Override
    public Object getItem(int position) {
        return eszkozok.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class AtmenetiTarolo{
        AppCompatTextView nev, mac;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Eszkoz akt = eszkozok.get(position);
        AtmenetiTarolo temp;

        if(convertView==null){ //ha még nem volt újrahasznosítás
            //a listaelem kinézetét csak egyszer kell betölteni a legelején
            //ha még nem volt convertView
            convertView=View.inflate(parent.getContext(),R.layout.eszkoz_listaelem,null);
            //az átmeneti elemet is csak egyszer kell példányosítani
            temp=new AtmenetiTarolo();
            //alapértékeit a convert view-ból vesszük, amiben már
            //a be inflate-elt (betöltött) xml-ünk van
            //tehát az xml név, és cím TextView mezője
            temp.nev= (AppCompatTextView) convertView.findViewById(R.id.nev);
            temp.mac= (AppCompatTextView) convertView.findViewById(R.id.cim);
            //innentől kezdve a temp átmeneti elem les a convertView
            //referencia beállítása a convertView-ba
            convertView.setTag(temp);
        } else {
            //referencia visszatöltése a convertView-ból
            temp= (AtmenetiTarolo) convertView.getTag();
        }

        //az átmeneti tároló vegye fel az aktuális elem értékeit
        //értékek beállítása a holderen keresztül
        temp.nev.setText(akt.getNev());
        temp.mac.setText(akt.getMac_cim());

        return convertView;
    }

    public void ListaTorles(){
        eszkozok.clear();
    }
}
