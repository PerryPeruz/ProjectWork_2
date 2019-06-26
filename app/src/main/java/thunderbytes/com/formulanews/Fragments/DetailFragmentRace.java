package thunderbytes.com.formulanews.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import thunderbytes.com.formulanews.Adapter.AdapterResult;
import thunderbytes.com.formulanews.Managers.SeasonManager;
import thunderbytes.com.formulanews.Models.Season;
import thunderbytes.com.formulanews.R;

public class DetailFragmentRace extends Fragment implements SeasonManager.OnSeasonFetched{


    //SEZIONE SIGLETON DI CLASSE
    private static final String RACE_POSITION = "positionDetail";
    private static DetailFragmentRace detailFragmentRace;

    public static DetailFragmentRace newInstance(int racePosition) {

        if (detailFragmentRace == null){
            detailFragmentRace = new DetailFragmentRace();
        }

        Bundle args = new Bundle();
        args.putInt(RACE_POSITION, racePosition);

        detailFragmentRace.setArguments(args);
        return detailFragmentRace;
    }


    private ListView listView;
    private ProgressBar pgsBar;
    private TextView loading;
    private TextView cell_three;
    private TextView cell_four;
    private TextView cell_five;
    private LinearLayout mSuperTable;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        View vView = inflater.inflate(R.layout.fragment_item_layout, container, false);

        listView = vView.findViewById(R.id.listViewResult);
        pgsBar = vView.findViewById(R.id.pBar2);
        loading = vView.findViewById(R.id.loading2);
        cell_three = vView.findViewById(R.id.txt_cell_three);
        cell_four = vView.findViewById(R.id.txt_cell_four);
        cell_five = vView.findViewById(R.id.txt_cell_five);
        mSuperTable = vView.findViewById(R.id.list_table);

        mSuperTable.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        pgsBar.setVisibility(View.VISIBLE);


        Bundle vBundle =  getArguments();
        if (vBundle != null){
            int mRaceNumber = vBundle.getInt(RACE_POSITION);
            new SeasonManager(2019, mRaceNumber+1, SeasonManager.RaceType.results, DetailFragmentRace.this);
        }

        return vView;
    }


    @Override
    public void onSeasonRetrievedSuccessfully(Season season) {

        try {
            pgsBar.setVisibility(View.GONE);
            loading.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            mSuperTable.setVisibility(View.VISIBLE);

            if(season.races.isEmpty()){
                Toast.makeText(getActivity(), "Non ci sono dati", Toast.LENGTH_LONG).show();
            }
            else {

                AdapterResult adapter = new AdapterResult(season.getRaces().get(0).Results);
                listView.setAdapter(adapter);

            }

        } catch (Exception e) {
            Log.e("ERROR","Error " + e.getMessage());
            Toast.makeText(getActivity(), "Si è verificato un errore", Toast.LENGTH_LONG).show();
        }
    }
}