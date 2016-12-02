package edu.uw.tacoma.dionmerz.fatms.transaction;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import edu.uw.tacoma.dionmerz.fatms.R;
import edu.uw.tacoma.dionmerz.fatms.flight.Itinerary;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmationDetailsFragment extends Fragment implements AdapterView.OnItemSelectedListener{
    private Itinerary mItinerary;

    public ConfirmationDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = null;
        mItinerary = (Itinerary) getArguments().getSerializable("leg");

        if(mItinerary.getmFlights().size() == 0) {
            v = inflater.inflate(R.layout.fragment_confirmation_details, container, false);
            buildNonStopUI(v);
        } else if(mItinerary.getmFlights().size() == 1) {
            v = inflater.inflate(R.layout.fragment_confirmation_details_one, container, false);
            buildOneStopUI(v);
        } else if(mItinerary.getmFlights().size() == 2) {
            v = inflater.inflate(R.layout.fragment_confirmation_details_two, container, false);
            buildTwoStopUI(v);
        }

        return v;
    }

    public void buildNonStopUI(View theView) {
        TextView departureAP = (TextView) theView.findViewById(R.id.departure_airport);
        departureAP.setText(mItinerary.getmDepartureAPName());
        TextView departureTime = (TextView) theView.findViewById(R.id.departure_time);
        departureTime.setText(mItinerary.getmDepartureTime());
        TextView arrivalAP = (TextView) theView.findViewById(R.id.arrival_airport);
        arrivalAP.setText(mItinerary.getmArivalAPName());
        TextView arrivalTime = (TextView) theView.findViewById(R.id.arrival_time);
        arrivalTime.setText(mItinerary.getmArrivalTime());
        TextView departureDate = (TextView) theView.findViewById(R.id.departure_date);
        departureDate.setText(mItinerary.getmDate());

        ArrayList<String> meals = new ArrayList<>();
        meals.add("Vegetarian Option");
        meals.add("Ham Sandwich");
        meals.add("Steak & Potatoes");
        meals.add("None");

        ArrayList<String> seats = new ArrayList<>();
        for (int i = 1; i <= 200; i++) {
            seats.add(String.valueOf(i));
        }

        Spinner mSeats = (Spinner) theView.findViewById(R.id.seat_spinner_leg1);
        mSeats.setOnItemSelectedListener(this);


        ArrayAdapter<String> airports = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, seats);
        airports.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSeats.setAdapter(airports);
        mSeats.setSelection(0);

        // Build the pub list spinner
        Spinner mMeals = (Spinner) theView.findViewById(R.id.meal_spinner_leg1);
        mMeals.setOnItemSelectedListener(this);


        ArrayAdapter<String> airports2 = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, meals);
        airports2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMeals.setAdapter(airports2);
        mMeals.setSelection(0);



    }

    public void buildOneStopUI(View theView) {
        TextView departureAP = (TextView) theView.findViewById(R.id.departure_airport_leg1);
        departureAP.setText(mItinerary.getmDepartureAPName());
        TextView departureTime = (TextView) theView.findViewById(R.id.departure_time_leg1);
        departureTime.setText(mItinerary.getmDepartureTime());
        TextView arrivalAP = (TextView) theView.findViewById(R.id.arrival_airport_leg1);
        arrivalAP.setText(mItinerary.getmArivalAPName());
        TextView arrivalTime = (TextView) theView.findViewById(R.id.arrival_time_leg1);
        arrivalTime.setText(mItinerary.getmArrivalTime());
        TextView departureDate = (TextView) theView.findViewById(R.id.departure_date_leg1);
        departureDate.setText(mItinerary.getmDate());

        ArrayList<String> meals = new ArrayList<>();
        meals.add("Vegetarian Option");
        meals.add("Ham Sandwich");
        meals.add("Steak & Potatoes");
        meals.add("None");

        ArrayList<String> seats = new ArrayList<>();
        for (int i = 1; i <= 200; i++) {
            seats.add(String.valueOf(i));
        }

        Spinner mSeats = (Spinner) theView.findViewById(R.id.seat_spinner_leg1);
        mSeats.setOnItemSelectedListener(this);


        ArrayAdapter<String> seatAdpater = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, seats);
        seatAdpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSeats.setAdapter(seatAdpater);
        mSeats.setSelection(0);

        // Build the pub list spinner
        Spinner mMeals = (Spinner) theView.findViewById(R.id.meal_spinner_leg1);
        mMeals.setOnItemSelectedListener(this);


        ArrayAdapter<String> mealAdpater = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, meals);
        mealAdpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMeals.setAdapter(mealAdpater);
        mMeals.setSelection(0);


        Itinerary secondFlight = mItinerary.getmFlights().get(0);
        TextView departureAP2 = (TextView) theView.findViewById(R.id.departure_airport_leg2);
        departureAP2.setText(secondFlight.getmDepartureAPName());
        TextView departureTime2 = (TextView) theView.findViewById(R.id.departure_time_leg2);
        departureTime2.setText(secondFlight.getmDepartureTime());
        TextView arrivalAP2 = (TextView) theView.findViewById(R.id.arrival_airport_leg2);
        arrivalAP2.setText(secondFlight.getmArivalAPName());
        TextView arrivalTime2= (TextView) theView.findViewById(R.id.arrival_time_leg2);
        arrivalTime2.setText(secondFlight.getmArrivalTime());

        Spinner mSeats2 = (Spinner) theView.findViewById(R.id.seat_spinner_leg2);
        mSeats2.setOnItemSelectedListener(this);


        mSeats2.setAdapter(seatAdpater);
        mSeats2.setSelection(0);

        // Build the pub list spinner
        Spinner mMeals2 = (Spinner) theView.findViewById(R.id.meal_spinner_leg2);
        mMeals2.setOnItemSelectedListener(this);


        mMeals2.setAdapter(mealAdpater);
        mMeals2.setSelection(0);

    }

    public void buildTwoStopUI(View theView) {
        TextView departureAP = (TextView) theView.findViewById(R.id.departure_airport_leg1);
        departureAP.setText(mItinerary.getmDepartureAPName());
        TextView departureTime = (TextView) theView.findViewById(R.id.departure_time_leg1);
        departureTime.setText(mItinerary.getmDepartureTime());
        TextView arrivalAP = (TextView) theView.findViewById(R.id.arrival_airport_leg1);
        arrivalAP.setText(mItinerary.getmArivalAPName());
        TextView arrivalTime = (TextView) theView.findViewById(R.id.arrival_time_leg1);
        arrivalTime.setText(mItinerary.getmArrivalTime());
        TextView departureDate = (TextView) theView.findViewById(R.id.departure_date_leg1);
        departureDate.setText(mItinerary.getmDate());

        ArrayList<String> meals = new ArrayList<>();
        meals.add("Vegetarian Option");
        meals.add("Ham Sandwich");
        meals.add("Steak & Potatoes");
        meals.add("None");

        ArrayList<String> seats = new ArrayList<>();
        for (int i = 1; i <= 200; i++) {
            seats.add(String.valueOf(i));
        }

        Spinner mSeats = (Spinner) theView.findViewById(R.id.seat_spinner_leg1);
        mSeats.setOnItemSelectedListener(this);


        ArrayAdapter<String> seatAdpater = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, seats);
        seatAdpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSeats.setAdapter(seatAdpater);
        mSeats.setSelection(0);

        // Build the pub list spinner
        Spinner mMeals = (Spinner) theView.findViewById(R.id.meal_spinner_leg1);
        mMeals.setOnItemSelectedListener(this);


        ArrayAdapter<String> mealAdpater = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, meals);
        mealAdpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMeals.setAdapter(mealAdpater);
        mMeals.setSelection(0);

        Itinerary secondFlight = mItinerary.getmFlights().get(0);
        TextView departureAP2 = (TextView) theView.findViewById(R.id.departure_airport_leg2);
        departureAP2.setText(secondFlight.getmDepartureAPName());
        TextView departureTime2 = (TextView) theView.findViewById(R.id.departure_time_leg2);
        departureTime2.setText(secondFlight.getmDepartureTime());
        TextView arrivalAP2 = (TextView) theView.findViewById(R.id.arrival_airport_leg2);
        arrivalAP2.setText(secondFlight.getmArivalAPName());
        TextView arrivalTime2= (TextView) theView.findViewById(R.id.arrival_time_leg2);
        arrivalTime2.setText(secondFlight.getmArrivalTime());

        Spinner mSeats2 = (Spinner) theView.findViewById(R.id.seat_spinner_leg2);
        mSeats2.setOnItemSelectedListener(this);


        mSeats2.setAdapter(seatAdpater);
        mSeats2.setSelection(0);

        // Build the pub list spinner
        Spinner mMeals2 = (Spinner) theView.findViewById(R.id.meal_spinner_leg2);
        mMeals2.setOnItemSelectedListener(this);


        mMeals2.setAdapter(mealAdpater);
        mMeals2.setSelection(0);

        Itinerary thirdFlight = mItinerary.getmFlights().get(1);
        TextView departureAP3 = (TextView) theView.findViewById(R.id.departure_airport_leg3);
        departureAP3.setText(thirdFlight.getmDepartureAPName());
        TextView departureTime3 = (TextView) theView.findViewById(R.id.departure_time_leg3);
        departureTime3.setText(thirdFlight.getmDepartureTime());
        TextView arrivalAP3 = (TextView) theView.findViewById(R.id.arrival_airport_leg3);
        arrivalAP3.setText(thirdFlight.getmArivalAPName());
        TextView arrivalTime3 = (TextView) theView.findViewById(R.id.arrival_time_leg3);
        arrivalTime3.setText(thirdFlight.getmArrivalTime());

        Spinner mSeats3 = (Spinner) theView.findViewById(R.id.seat_spinner_leg2);
        mSeats3.setOnItemSelectedListener(this);


        mSeats3.setAdapter(seatAdpater);
        mSeats3.setSelection(0);

        // Build the pub list spinner
        Spinner mMeals3 = (Spinner) theView.findViewById(R.id.meal_spinner_leg2);
        mMeals3.setOnItemSelectedListener(this);


        mMeals3.setAdapter(mealAdpater);
        mMeals3.setSelection(0);

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
