package hu.uniobuda.nik.ciwsduino;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by thecy on 2016. 04. 25..
 */
public class NevjegyFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //beinflate-elem a fragment-be az xml tartalmát
        return inflater.inflate(R.layout.fragment_nevjegy, container, false);
    }
}
