package edu.uw.tacoma.dionmerz.fatms.transaction;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import edu.uw.tacoma.dionmerz.fatms.R;
import edu.uw.tacoma.dionmerz.fatms.flight.Itinerary;

public class ConfirmationActivity extends AppCompatActivity  {
    private ArrayList<Itinerary> mFlights;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        // Is a round trip flight.
        if(getIntent().getSerializableExtra("return") != null) {
            Itinerary outgoing = (Itinerary) getIntent().getSerializableExtra("outgoing");
            Itinerary returning = (Itinerary) getIntent().getSerializableExtra("return");


            //Is a one way flight.
        } else {
            Itinerary outgoing = (Itinerary) getIntent().getSerializableExtra("outgoing");

            ConfirmationDetailsFragment confirmDetails = new ConfirmationDetailsFragment();
            Bundle args = new Bundle();
            args.putSerializable("leg", outgoing);
            confirmDetails.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.activity_confirmation, confirmDetails);

            transaction.commit();

        }
    }
}
