package hu.uniobuda.nik.ciwsduino;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by thecy on 2016. 05. 07.
 */
public class AktivitasAdapter extends BaseAdapter {

    public List<Aktivitas> aktivitasok;

    //üreset létrehozó konstruktor
    public AktivitasAdapter(){
        aktivitasok= new ArrayList<Aktivitas>();
    }

    //már feltöltöttet létrehozó konstruktor
    public AktivitasAdapter(List<Aktivitas> eszkozok) {
        this.aktivitasok = eszkozok;
    }

    @Override
    public int getCount() {
        return aktivitasok.size();
    }

    @Override
    public Object getItem(int position) {
        return aktivitasok.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        TextView ido;
        TextView szog;
        TextView tav;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.aktivitaslista_elem, null);

            holder = new ViewHolder();
            holder.ido = (TextView)convertView.findViewById(R.id.ido);
            holder.szog = (TextView)convertView.findViewById(R.id.szog);
            holder.tav = (TextView)convertView.findViewById(R.id.tav);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        Aktivitas aktivitas = aktivitasok.get(position);
        holder.tav.setText(Integer.toString(aktivitas.getTav()));
        holder.szog.setText(Integer.toString(aktivitas.getSzog()));
        holder.ido.setText(aktivitas.getIdo());

        return convertView;


        /*

        Aktivitas akt = aktivitasok.get(position);
        AtmenetiTarolo temp;

        if(convertView==null){ //ha még nem volt újrahasznosítás
            //a listaelem kinézetét csak egyszer kell betölteni a legelején
            //ha még nem volt convertView
            convertView=View.inflate(parent.getContext(),R.layout.aktivitaslista_elem,null);
            //az átmeneti elemet is csak egyszer kell példányosítani
            temp =new AtmenetiTarolo();
            //alapértékeit a convert view-ból vesszük, amiben már
            //a be inflate-elt (betöltött) xml-ünk van
            //tehát az xml név, és cím TextView mezője
            temp.ido= (AppCompatTextView) convertView.findViewById(R.id.ido);
            temp.szog= (AppCompatTextView) convertView.findViewById(R.id.szog);
            temp.tav= (AppCompatTextView) convertView.findViewById(R.id.tav);
            //innentől kezdve a temp átmeneti elem les a convertView
            //referencia beállítása a convertView-ba
            convertView.setTag(temp);
        } else {
            //referencia visszatöltése a convertView-ból
            temp= (AtmenetiTarolo) convertView.getTag();
        }

        //az átmeneti tároló vegye fel az aktuális elem értékeit
        //értékek beállítása a holderen keresztül
        temp.ido.setText(akt.getIdo());
        temp.szog.setText(akt.getSzog());
        temp.tav.setText(akt.getTav());
        */

     //   return convertView;
    }



    public void Hozzaadas( Aktivitas a){
        aktivitasok.add(a);
    }

    public void ListaTorles(){
        aktivitasok.clear();
    }
}
