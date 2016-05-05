package hu.uniobuda.nik.bleproba;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private boolean szkennelesAlatt=false;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);

        if(!szkennelesAlatt){
            menu.findItem(R.id.menu_leallitas).setVisible(false);
            menu.findItem(R.id.menu_szkenneles).setVisible(true);
            menu.findItem(R.id.menu_frissites).setActionView(null);
        } else {
            menu.findItem(R.id.menu_leallitas).setVisible(true);
            menu.findItem(R.id.menu_szkenneles).setVisible(false);
            menu.findItem(R.id.menu_frissites).setActionView(R.layout.actionbar_folyamatjelzo);
        }

        return super.onCreateOptionsMenu(menu);
        //return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_szkenneles:
                szkennelesAlatt = true;
                invalidateOptionsMenu();
                return true;
                //break;
            case R.id.menu_leallitas:
                szkennelesAlatt = false;
                invalidateOptionsMenu();
                return true;
                //break;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
