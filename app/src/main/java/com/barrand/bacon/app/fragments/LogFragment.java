package com.barrand.bacon.app.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.barrand.bacon.app.R;
import com.barrand.bacon.app.db.TripDatabaseHandler;
import com.barrand.bacon.app.model.Trip;

import java.util.ArrayList;

/**
 * Created by bbarr233 on 5/1/14.
 */
public class LogFragment extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static LogFragment newInstance(int sectionNumber) {
        LogFragment fragment = new LogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public LogFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.log_view, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.log_tv);
        TripDatabaseHandler db = new TripDatabaseHandler(this.getActivity());
        ArrayList<Trip> trips = db.getAllTrips();
        String log = "";
        for(Trip t:trips){
            log += t.toString()+"\n";
        }
        textView.setText(log);
        return rootView;
    }
}
