package hu.uniobuda.nik.bluetoothvegleges;

import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by thecy on 2016. 05. 05..
 */
public class BTEszkozAdapter extends BaseAdapter {

    //Az eszközöket tartalmazó lista
    private ArrayList<BTEszkoz> eszkozok;

    /*
    Két konstruktor
    */
    public BTEszkozAdapter() {
        this.eszkozok = new ArrayList<BTEszkoz>();
    }
    public BTEszkozAdapter(ArrayList<BTEszkoz> eszkozok) {
        this.eszkozok = eszkozok;
    }

    //a mérete
    @Override
    public int getCount() {
        return eszkozok.size();
    }

    //az adott elem
    @Override
    public Object getItem(int position) {
        return eszkozok.get(position);
    }

    //id-hez id :D
    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
    Egy átmeneti tároló az adatok átadásához.
    Az eszközök nevét és MAC címét tárolja.
    */
    class AtmenetiTarolo{
        AppCompatTextView nev, mac;
    }

    /*
    Visszaadja az aktuális elem kinézetét egy View-ban
    */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return null;
        BTEszkoz akt = eszkozok.get(position);
        AtmenetiTarolo temp;

        if(convertView==null){ //ha még nem volt újrahasznosítás
            //a listaelem kinézetét csak egyszer kell betölteni a legelején
            //ha még nem volt convertView
            convertView = View.inflate(parent.getContext(),R.layout.eszkoz_lista_elem,null);
            //az átmeneti elemet is csak egyszer kell példányosítani
            temp = new AtmenetiTarolo();
            //alapértékeit a convert view-ból vesszük, amiben már
            //a be inflate-elt (betöltött) xml-ünk van
            //tehát az xml név, és cím TextView mezője
            temp.nev = (AppCompatTextView) convertView.findViewById(R.id.nev);
            temp.mac = (AppCompatTextView) convertView.findViewById(R.id.cim);
            //innentől kezdve a temp átmeneti elem les a convertView
            //referencia beállítása a convertView-ba
            convertView.setTag(temp);
        } else {
            //referencia visszatöltése a convertView-ból
            temp = (AtmenetiTarolo) convertView.getTag();
        }

        //az átmeneti tároló vegye fel az aktuális elem értékeit
        //értékek beállítása a holderen keresztül
        temp.nev.setText(akt.getNev());
        temp.mac.setText(akt.getMac_cim());

        return convertView;
    }
    /*
    Ezzel tudunk új eszközt felvenni a listába.
    */
    public void Hozzaadas(BTEszkoz e){
        eszkozok.add(e);
    }

    //Ezzel kitörölhető a teljes lista
    public void ListaTorles(){
        eszkozok.clear();
    }
}
