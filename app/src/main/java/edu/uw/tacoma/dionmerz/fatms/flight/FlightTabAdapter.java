package edu.uw.tacoma.dionmerz.fatms.flight;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Andrew Merz on 11/26/2016.
 */

public class FlightTabAdapter extends FragmentPagerAdapter {


    int mNumOfTabs;

    public FlightTabAdapter(FragmentManager manager, int theTotalTabs) {
        super(manager);
        this.mNumOfTabs = theTotalTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                OneWayTabFragment oneWayTab = new OneWayTabFragment();
                return oneWayTab;
            case 1:
                RoundTripTabFragment roundTripTab = new RoundTripTabFragment();
                return roundTripTab;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
