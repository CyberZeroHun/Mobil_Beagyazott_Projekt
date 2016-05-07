package hu.uniobuda.nik.ciwsduino;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.regex.Pattern;

import static hu.uniobuda.nik.ciwsduino.GlobalisKonstansok.*;

/**
 * Created by thecy on 2016. 04. 25..
 */
public class NevjegyFragment extends Fragment {

    AppCompatTextView tv;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //beinflate-elem a fragment-be az xml tartalmát
        return inflater.inflate(R.layout.fragment_nevjegy, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv = (AppCompatTextView) getView().findViewById(R.id.nevjegysz);
        //egybeformázom a névjegy szövegét
        Resources res = getResources();
        String szoveg = String.format(
                    res.getString(R.string.nevjegy_szoveg),
                    res.getString(R.string.app_name),
                    GITHUB_LINK,
                    res.getString(R.string.dev),
                    res.getString(R.string.nev),
                    res.getString(R.string.email),
                    res.getString(R.string.aa),
                    AA_EMAIL,
                    res.getString(R.string.zc),
                    ZC_EMAIL
        );
        tv.setText(szoveg);
        //TODO: több szót összekapcsolni reguláris kifejezéssel (a szó csak egyszer szerepeljen, és
        //TODO: csak az egyik
        //TODO: megtalálni hogy lehet ugyanezzel a módszerrel színezni egy szövegrészt a textView-ban
        //TODO: bepozícionálni a nézetet
        Linkify.addLinks(tv,Pattern.compile(AA_EMAIL),"mailto:"+AA_EMAIL);
    }
}
