package thunderbytes.com.formulanews.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import thunderbytes.com.formulanews.R;

public class PilotsRanking extends Fragment {

    public PilotsRanking() {
        //Costruttore vuoto
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main, null);
    }
}