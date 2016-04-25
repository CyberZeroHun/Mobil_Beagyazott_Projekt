package hu.uniobuda.nik.ciwsduino;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;

import java.util.ArrayList;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

public class MainActivity extends AppCompatActivity {

    //ez a két lista tárolja nekünk a felhasználandó fragmenteket
    //és a hozzájuk tartozó fülek neveit és az ikonjait
    ArrayList<Fragment> fragmentek;
    ArrayList<String> fulNevek;
    ArrayList<Drawable> ikonok;

    //SectionPager a fülekhez és ViewPager a nézetek swipeolásához
    private SectionsPagerAdapter spa; //ezt mi magunk hozzuk létra a lapozgatáshoz
    private ViewPager vp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //felvesszük a listába fülek neveit a string erőforrásokból
        fulNevek = new ArrayList<String>();
        fulNevek.add(this.getResources().getString(R.string.ultrahang));
        fulNevek.add(this.getResources().getString(R.string.manualisvezerles));
        fulNevek.add(this.getResources().getString(R.string.aktivitasok));
        fulNevek.add(this.getResources().getString(R.string.beallitasok));
        fulNevek.add(this.getResources().getString(R.string.nevjegy));

        //felvesszük a listába a fragmenteket
        fragmentek = new ArrayList<Fragment>();
        fragmentek.add(new UltrahangFragment());
        fragmentek.add(new ManualisVezerlesFragment());
        fragmentek.add(new AktivitasokFragment());
        fragmentek.add(new BeallitasokFragment());
        fragmentek.add(new NevjegyFragment());

        //felvesszük a listába az ikonjaikat
        ikonok = new ArrayList<Drawable>();
        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ikonok.add(this.getResources().getDrawable(R.drawable.ic_settings_input_antenna_white_48px, this.getTheme()));
            ikonok.add(this.getResources().getDrawable(R.drawable.ic_settings_input_antenna_white_48px, this.getTheme()));
            ikonok.add(this.getResources().getDrawable(R.drawable.ic_settings_input_antenna_white_48px, this.getTheme()));
            ikonok.add(this.getResources().getDrawable(R.drawable.ic_settings_input_antenna_white_48px, this.getTheme()));
            ikonok.add(this.getResources().getDrawable(R.drawable.ic_settings_input_antenna_white_48px, this.getTheme()));
        } else {
            ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_settings_input_antenna_white_48px);
            ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_settings_input_antenna_white_48px);
            ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_settings_input_antenna_white_48px);
            ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_settings_input_antenna_white_48px);
            ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_settings_input_antenna_white_48px);
        }*/
        ikonok.add(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_settings_input_antenna_white_48dp));
        ikonok.add(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_my_location_white_48dp));
        ikonok.add(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_filter_center_focus_white_48dp));
        ikonok.add(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_settings_white_48dp));
        ikonok.add(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_insert_emoticon_white_48dp));


        //toolbar beállítása
        Toolbar tb = (Toolbar) findViewById(R.id.eszkozsor);
        setSupportActionBar(tb);

        /*
        A fragmenteket kezelő menedzseren keresztül
        érjük el a fülek fragmentjeit a későbbiekben
         */
        spa = new SectionsPagerAdapter(getSupportFragmentManager());

        //a viewpager beállítása
        vp = (ViewPager) findViewById(R.id.lapozo);
        vp.setAdapter(spa);

        //tab layout, amely a füleket fogja tartalmazni
        TabLayout tl = (TabLayout) findViewById(R.id.fulek);
        tl.setupWithViewPager(vp);
    }

        public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentek.get(position);
        }

        @Override
        public int getCount() {
            return fragmentek.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            /*
            SpannableStringBuilder sb = new SpannableStringBuilder(" " + fulNevek.get(position));
            Drawable main_create = ikonok.get(position);
            main_create.setBounds(0, 0, main_create.getIntrinsicWidth(), main_create.getIntrinsicHeight());
            ImageSpan span = new ImageSpan(main_create, ImageSpan.ALIGN_BASELINE);
            sb.setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return sb;*/
            /*
            SpannableString ss = new SpannableString(" "+fulNevek.get(position));
            ss.setSpan(new ImageSpan(ikonok.get(position), DynamicDrawableSpan.ALIGN_BASELINE),0,1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return ss;*/
            /*
            SpannableStringBuilder ssb = new SpannableStringBuilder(" "+fulNevek.get(position));
            ssb.setSpan(new ImageSpan(ikonok.get(position), DynamicDrawableSpan.ALIGN_BASELINE),0,1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return ssb;
            */
            return fulNevek.get(position);
        }
    }
}
