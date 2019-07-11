package thunderbytes.com.formulanews.Activities;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.material.tabs.TabLayout;

import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import thunderbytes.com.formulanews.Broadcast.InternetReceiver;
import thunderbytes.com.formulanews.Broadcast.NotificationPublisher;
import thunderbytes.com.formulanews.Adapter.AdapterDetailFragmentPager;
import thunderbytes.com.formulanews.Fragments.TimerFragment;
import thunderbytes.com.formulanews.MainActivity;
import thunderbytes.com.formulanews.Managers.NotificationManager;
import thunderbytes.com.formulanews.Models.Race;
import thunderbytes.com.formulanews.Observable.InternetObservable;
import thunderbytes.com.formulanews.R;

public class DetailRace extends AppCompatActivity implements Observer, TimerFragment.ITimerFragment {

    //costanti condivise nella main acitivity per passare dati
    public static final String ITEM_RACE = "item race";
    public static final String RACE_NUMBER = "race number";

    private static final String SHARED_PREF_NOTIFICATION = "notification";

    //variabili di classe
    private String notificationSavingkey;
    private Race vRace;
    private int vRaceNumber;
    private boolean notify = false;
    private BroadcastReceiver InternetReceiver = null;
    private Toolbar toolbar;
    private   AdapterDetailFragmentPager adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //- SEZIONE: INIZIALIZZAZIONE PAGINA
        setContentView(R.layout.detail_paralax_layout);
        //TextView mTitle = findViewById(R.id.title_detailActivity);
        ImageView mCircuit = findViewById(R.id.htab_header);
        ImageButton mNotification = findViewById(R.id.buttonNotification);
        toolbar = findViewById(R.id.htab_toolbar);

        InternetReceiver = new InternetReceiver();
        broadcastIntent();
        InternetObservable.getInstance().addObserver(this);

        //- SEZIONE RECUPERO DATI
        Bundle vBundle =  getIntent().getExtras();
        if (vBundle != null){
            vRace = (Race) vBundle.getSerializable(ITEM_RACE);
            vRaceNumber = vBundle.getInt(RACE_NUMBER);

            //mTitle.setText(vRace.getRaceName());
            toolbar.setTitle(vRace.getRaceName());
            int id = getResources().getIdentifier("thunderbytes.com.formulanews:drawable/" + vRace.getCircuit().getLocation().getCountry().toString().toLowerCase(), null, null);
            mCircuit.setImageResource(id);
        }

        //- SEZIONE INIZIALIZZAZIONE VIE PAGE CON I SCROLL TAB (AdapterDetailFragmentPager se ne occupa della gestione)
        ViewPager viewPager = (ViewPager) findViewById(R.id.htab_viewpager);
        adapter = new AdapterDetailFragmentPager(getSupportFragmentManager(), vRaceNumber,vRace);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.htab_tabs);
        tabLayout.setupWithViewPager(viewPager);




        //- SEZIONE GESTIONE NOTIFICHE
        notificationSavingkey = vRace.getRaceName();
        Log.d("TAG","nome corsa"+vRace.getRaceName());
        Log.d("TAG","id corsa"+vRace.getId());
        Date date = new Date();

        if(vRace.getDate().getTime() > date.getTime() || vRace.getId() == 0) {

            mNotification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!vRace.isNotify()) {
                        vRace.setNotify(true);
                        enableNotification();
                        mNotification.setImageResource(R.drawable.bell_select);
                    } else {
                        vRace.setNotify(false);
                        disableNotification();
                        mNotification.setImageResource(R.drawable.bell);
                    }

                }
            });


            vRace.setNotify(getNotification());

            if (vRace.isNotify()) {
                mNotification.setImageResource(R.drawable.bell_select);
            }

        }
        else
        {
            mNotification.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(InternetReceiver);
    }


    private void enableNotification(){
        NotificationManager.setReminder(this, NotificationPublisher.class,DetailRace.class,vRace);
        Toast.makeText(this, "Notifica Attivata", Toast.LENGTH_SHORT).show();
    }

    private void disableNotification(){
        NotificationManager.cancelReminder(this,NotificationPublisher.class);
        Toast.makeText(this, "Notifica Disattivata", Toast.LENGTH_SHORT).show();
    }


    public void onBackPressed() {
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if(vRace.isNotify())
        {
            saveNotification();
        }
        else {
            removeNotification();
        }

        if (isTaskRoot()) {
           Intent vIntent = new Intent(this, MainActivity.class);   // write your code to switch between fragments.
            startActivity(vIntent);
        } else {
            super.onBackPressed();
        }

        adapter.stopTimer();

    }


    private boolean getNotification() {
        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NOTIFICATION, MODE_PRIVATE);
        boolean notify = sp.getBoolean(notificationSavingkey, false);

        return notify;
    }

    private void saveNotification() {

        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NOTIFICATION, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(notificationSavingkey, true);
        editor.apply();

    }

    private void removeNotification()
    {
        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NOTIFICATION, MODE_PRIVATE);
        sp.edit().remove(notificationSavingkey).commit();
    }


    public void broadcastIntent() {
        registerReceiver(InternetReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }


    @Override
    public void update(Observable o, Object arg) {
        Toast.makeText(DetailRace.this, "" + arg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startTimer() {
        adapter.startTimer();
    }
}

