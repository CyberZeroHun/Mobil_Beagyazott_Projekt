package hu.uniobuda.nik.ciwsduino;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Switch;

import static hu.uniobuda.nik.ciwsduino.GlobalisKonstansok.*;

/**
 * Created by thecy on 2016. 04. 25..
 */
public class BeallitasokFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //beinflate-elem a fragment-be az xml tartalmát
        return inflater.inflate(R.layout.fragment_beallitasok, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    SharedPreferences.Editor ed;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //eltárolom az elemeket
        NumberPicker np_tav = (NumberPicker) view.findViewById(R.id.tavvalaszto);
        NumberPicker np_szog = (NumberPicker) view.findViewById(R.id.szogvalaszto);
        NumberPicker np_tavosztas = (NumberPicker) view.findViewById(R.id.tavosztasvalaszto);
        NumberPicker np_szogosztas = (NumberPicker) view.findViewById(R.id.szogosztasvalaszto);
        Switch sw = (Switch) view.findViewById(R.id.vibracio);

        //beállítások
        SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        ed = sp.edit();

        //minimumok és maximumok beállítása
        np_tav.setMinValue(100);
        np_tav.setMaxValue(500);
        np_szog.setMinValue(30);
        np_szog.setMaxValue(180);
        np_tavosztas.setMinValue(1);
        np_tavosztas.setMaxValue(20);
        np_szogosztas.setMinValue(1);
        np_szogosztas.setMaxValue(20);

        //az értékek változásakor elmentjük a SharedPreferences-be
        np_tav.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
             @Override
             public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                 ed.putInt(SP_TAV, newVal);
                 ed.commit();
             }
         });
        np_szog.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                ed.putInt(SP_SZOG, newVal);
                ed.commit();
            }
        });
        np_tavosztas.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                ed.putInt(SP_TAVOSZT, newVal);
                ed.commit();
            }
        });
        np_szogosztas.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                ed.putInt(SP_SZOGOSZT, newVal);
                ed.commit();
            }
        });
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ed.putBoolean(SP_VIB, isChecked);
                ed.commit();
            }
        });

        //kezdeti érték SharedPRef, vagy ha nincs, akkor alapérték
        np_tav.setValue(sp.getInt(SP_TAV,500));
        np_szog.setValue(sp.getInt(SP_SZOG,120));
        np_tavosztas.setValue(sp.getInt(SP_TAVOSZT,10));
        np_szogosztas.setValue(sp.getInt(SP_SZOGOSZT,8));
        sw.setChecked(sp.getBoolean(SP_VIB,true));


    }
}
