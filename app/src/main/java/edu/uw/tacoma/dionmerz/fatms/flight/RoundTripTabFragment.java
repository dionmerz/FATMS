package edu.uw.tacoma.dionmerz.fatms.flight;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tacoma.dionmerz.fatms.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RoundTripTabFragment extends Fragment {


    public RoundTripTabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_round_trip_tab, container, false);
    }

}
