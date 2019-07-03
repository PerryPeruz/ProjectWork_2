package thunderbytes.com.formulanews.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import thunderbytes.com.formulanews.Models.Race;
import thunderbytes.com.formulanews.Models.Time;
import thunderbytes.com.formulanews.R;


public class DetailFragmentDate extends Fragment {

    private static DetailFragmentDate detailFragmentDate;
    private static final String ITEM_RACE = "race";


    public static DetailFragmentDate newInstance(Race race) {

        if (detailFragmentDate == null){
            detailFragmentDate = new DetailFragmentDate();
        }
        Bundle args = new Bundle();
        args.putSerializable(ITEM_RACE, race);
        detailFragmentDate.setArguments(args);
        return detailFragmentDate;
    }

    private Race vRace;
    private TextView mDateFP1;
    private TextView mDateFP2;
    private TextView mDateFP3;
    private TextView mDateQualifing;
    private TextView mDateRace;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View vView = inflater.inflate(R.layout.fragment_clock_layout, container, false);

        mDateFP1 = vView.findViewById(R.id.txt_FP1);
        mDateFP2 = vView.findViewById(R.id.txt_FP2);
        mDateFP3 = vView.findViewById(R.id.txt_FP3);
        mDateQualifing = vView.findViewById(R.id.txt_Qualifing);
        mDateRace = vView.findViewById(R.id.txt_Race);


        Bundle vBundle =  getArguments();

        if (vBundle != null) {
            vRace = (Race) vBundle.getSerializable(ITEM_RACE);

            mDateFP1.setText(android.text.format.DateFormat.format("dd.MM.yyyy", calculateDate(-2)));
            mDateFP2.setText(android.text.format.DateFormat.format("dd.MM.yyyy", calculateDate(-2)));
            mDateFP3.setText(android.text.format.DateFormat.format("dd.MM.yyyy", calculateDate(-1)));
            mDateQualifing.setText(android.text.format.DateFormat.format("dd.MM.yyyy", calculateDate(-1)) + ", --:--");
            mDateRace.setText(android.text.format.DateFormat.format("dd.MM.yyyy", vRace.getDate()) + ", " + calculateTime(vRace.getTime()));
        }

        return vView;
    }

    public Date calculateDate(int amount){
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTime(vRace.getDate());
        mCalendar.add(Calendar.DATE, amount);

        return mCalendar.getTime();
    }


    
    public String calculateTime(String timeRace) {
        SimpleDateFormat vInputDateFormat = new SimpleDateFormat("HH:mm:ss'Z'");
        vInputDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String formattedDate = "";

        try {
            Date date = vInputDateFormat.parse(timeRace);
            vInputDateFormat.setTimeZone(TimeZone.getDefault());
            SimpleDateFormat vDateFormat = new SimpleDateFormat("HH:mm");

            if(TimeZone.getDefault().inDaylightTime(calculateDate(0))){

                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.HOUR_OF_DAY, 1);

                formattedDate = vDateFormat.format(cal.getTime());
            }else{

                formattedDate = vDateFormat.format(date);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formattedDate;
    }


}
