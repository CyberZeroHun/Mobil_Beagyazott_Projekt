package hu.uniobuda.nik.bluetoothvegleges;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Created by thecy on 2016. 05. 04..
 */
public class MainActivity extends AppCompatActivity {

    private int mindkettoEngedelyezve;
    private BluetoothAdapter bta;

    //Ez a metódus kezeli le az engedélykérésekből érkező eredményeket
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        /*
        Marshmallow miatt le kell kezelnünk az engedélyeket.
        Ez a módszer csak akkor jó, ha egyszerre egy engedélyt és egy eredményt dolgozunk fel.
        */
        switch(requestCode){
            case GlobalisKonstans.BT_ENGEDELY:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //ha engedélyezték a bluetootht permission-t
                    mindkettoEngedelyezve++;
                }
                break;
            case GlobalisKonstans.BT_ADMIN_ENGEDELY:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //ha engedélyezték a bluetooth admin permission-t
                    mindkettoEngedelyezve++;
                }
                break;
        }
    }

    //Megkérlyük a szükséges engedélyeket, leellenőrizzük, hogy van-e bluetooth
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //region engedelykero
        mindkettoEngedelyezve = 0;
        /*
        Marshmallow miatt a felhasználót meg kell kérdeznünk a permission-ökről
        nem elég a manifest-ben felvennünk őket.
        */

        int permissionCheck = ContextCompat.checkSelfPermission(
                MainActivity.this,
                Manifest.permission.BLUETOOTH
        );
        int permissionCheck2 = ContextCompat.checkSelfPermission(
                MainActivity.this,
                Manifest.permission.BLUETOOTH_ADMIN
        );
        if (permissionCheck == PackageManager.PERMISSION_DENIED){
            //ha nincs, akkor kérnünk kell
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.BLUETOOTH
            },GlobalisKonstans.BT_ENGEDELY);
        } else  if (permissionCheck2 == PackageManager.PERMISSION_DENIED) {
            //ha nincs, akkor kérnünk kell
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.BLUETOOTH_ADMIN
            },GlobalisKonstans.BT_ADMIN_ENGEDELY);
        } else {
            //ebben az ágban mindkettőhöz van már engedélyünk, így ténykedhetünk
            mindkettoEngedelyezve=2;
        }
        //endregion

        //region erdemes-e folytatni? van bluetooth? vannak engedelyek?
        bta = BluetoothAdapter.getDefaultAdapter();
        if(bta==null || mindkettoEngedelyezve!=2){
            /*
            Nincs is bluetooth az eszközünkben.
            Jelezzük ezt a felhasználónak egy üzenetben
            és ne is folytassuk tovább.
            Ha nem engedélyezték mindkettő bluetooth engedélyt, akkor
            szintén nincs mit tennünk.
            */
            Toast.makeText(this,R.string.nincs_bluetooth,Toast.LENGTH_LONG).show();
            //this.finish();
            /*
            TODO: Egy olyan fragmentet tölts be, amely kiírja, hogy a szolgáltatás nem elérhető
            TODO: Illetve vetérelni se tudjuk a fegyverzetet, ne jelenjen meg a joystick.
            TODO: A beállításokat sem tudjuk neki elküldeni, szürküljön ki.
            TODO: Igazából ilyenkor csak kamerakép van a wifiről maximum.
            */
            if(savedInstanceState==null){
                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction()
                        .replace(R.id.fragment_tarolo,new UzenetFragment())
                        .commit();
            }
        } else
        {
            if(savedInstanceState==null){
                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction()
                        .replace(R.id.fragment_tarolo,new BTFragment())
                        .commit();
            }
            //TODO: Ha minden OK, akkor az ULTRAHANG fragmentet és a többi funkció is betölthető.
        }
        //endregion

    }

    /*
    Ezzel készítek el egy menüt arra az esetre, ha a bluetooth-ot mégis bekapcsolnák.

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.alap_menu,menu);
        return true;
    }


    Ez figyeli, hogy mit választunk ki a menüből.

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.menu_bt_bekapcs:
                if(!bta.isEnabled()) {
                    /*
                    Ha nincs bekapcsolva a bluetooth, amikor rákattintunk a menüre, akkor
                    rá kell vennünk, hogy elindítsa a felhasználó.

                    Intent inte = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(inte, GlobalisKonstans.BT_BEKAPCSOLAS_KERES);
                }
                break;
        }
        return true;
    }*/
}
