package hu.uniobuda.nik.bleproba;

import android.bluetooth.BluetoothDevice;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by thecy on 2016. 05. 04..
 */

public class EszkozAdapter extends BaseAdapter{

    private ArrayList<BluetoothDevice> eszkozok;
    private LayoutInflater inf;

    public EszkozAdapter(LayoutInflater inf) {
        super();
        this.eszkozok = new ArrayList<BluetoothDevice>();
        this.inf = inf; //SzkennelesActivity.this.getLayoutInflater();
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

    static class ViewHolder {
        AppCompatTextView deviceName;
        AppCompatTextView deviceAddress;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = inf.inflate(R.layout.eszkoz_listaelem, null);
            viewHolder = new ViewHolder();
            viewHolder.deviceAddress = (AppCompatTextView) convertView.findViewById(R.id.eszkozcim);
            viewHolder.deviceName = (AppCompatTextView) convertView.findViewById(R.id.eszkoznev);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        BluetoothDevice device = eszkozok.get(position);
        final String deviceName = device.getName();
        if (deviceName != null && deviceName.length() > 0) {
            viewHolder.deviceName.setText(deviceName);
        } else {
            viewHolder.deviceName.setText(R.string.ismeretlen);
        }
        viewHolder.deviceAddress.setText(device.getAddress());

        return convertView;
    }

    public void UjEszkoz(BluetoothDevice eszk){
        if(!eszkozok.contains(eszk)){
            eszkozok.add(eszk);
        }
    }

    public BluetoothDevice OlvasEszkoz(int position){
        return eszkozok.get(position);
    }

    public void Urites(){
        eszkozok.clear();
    }
}
