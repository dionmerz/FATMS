package edu.uw.tacoma.dionmerz.fatms.transaction;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import edu.uw.tacoma.dionmerz.fatms.R;
import edu.uw.tacoma.dionmerz.fatms.flight.Itinerary;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmationDetailsFragment extends Fragment implements AdapterView.OnItemSelectedListener{
    private Itinerary mItinerary;
    private Boolean mIsRoundTrip;
    private ArrayList<String> mMealsList;
    private ArrayList<String> mSeatsFlight1;
    private ArrayList<String> mSeatsFlight2;
    private ArrayList<String> mSeatsFlight3;
    private HashMap<Integer, ArrayList<String>> mSelections;

    public ConfirmationDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = null;
        mItinerary = (Itinerary) getArguments().getSerializable("leg");
        mSelections = new HashMap<>();

        mMealsList = new ArrayList<>();
        mMealsList.add("Vegetarian");
        mMealsList.add("Non-vegetarian");
        mMealsList.add("None");

        if(getArguments().getBoolean("isRoundTrip")) {
            mIsRoundTrip = true;
            mSelections = new HashMap<Integer, ArrayList<String>>();
        } else {
            mIsRoundTrip = false;
            if(getArguments().getSerializable("selection") != null) {
                mSelections = (HashMap<Integer, ArrayList<String>>)getArguments().getSerializable("selection");
            }
        }

        if(mItinerary.getmFlights().size() == 0) {
            v = inflater.inflate(R.layout.fragment_confirmation_details, container, false);
            mSeatsFlight1 = (ArrayList<String>) getArguments().get("flight1_seats");
            buildNonStopUI(v);
        } else if(mItinerary.getmFlights().size() == 1) {
            v = inflater.inflate(R.layout.fragment_confirmation_details_one, container, false);
            mSeatsFlight1 = (ArrayList<String>) getArguments().get("flight1_seats");
            mSeatsFlight2 = (ArrayList<String>) getArguments().get("flight2_seats");
            buildOneStopUI(v);
        } else if(mItinerary.getmFlights().size() == 2) {
            v = inflater.inflate(R.layout.fragment_confirmation_details_two, container, false);
            mSeatsFlight1 = (ArrayList<String>) getArguments().get("flight1_seats");
            mSeatsFlight2 = (ArrayList<String>) getArguments().get("flight2_seats");
            mSeatsFlight3 = (ArrayList<String>) getArguments().get("flight3_seats");
            buildTwoStopUI(v);
        }

        Button bt = (Button) v.findViewById(R.id.continue_button);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mIsRoundTrip) {
                    ((ConfirmationActivity) getActivity()).purchaseFlight(mSelections);
                } else {
                    ((ConfirmationActivity) getActivity()).returnFlightConfirmation(mSelections);
                }
            }
        });

        return v;
    }

    public void buildNonStopUI(View theView) {
        ArrayList<String> selectionList1 = new ArrayList<String>();
        selectionList1.add(null);
        selectionList1.add(null);

        mSelections.put(mItinerary.getmFlightNumber(), selectionList1);


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

        Spinner mSeats = (Spinner) theView.findViewById(R.id.seat_spinner_leg1);
        mSeats.setOnItemSelectedListener(this);
        System.out.println("Here is a seat list: " + mSeatsFlight1);

        ArrayAdapter<String> seatAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, mSeatsFlight1);
        seatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSeats.setAdapter(seatAdapter);
        mSeats.setSelection(0);

        // Build the pub list spinner
        Spinner mMeals = (Spinner) theView.findViewById(R.id.meal_spinner_leg1);
        mMeals.setOnItemSelectedListener(this);


        ArrayAdapter<String> mealAdpater = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, mMealsList);
        mealAdpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMeals.setAdapter(mealAdpater);
        mMeals.setSelection(0);
    }

    public void buildOneStopUI(View theView) {
        ArrayList<String> selectionList1 = new ArrayList<String>();
        selectionList1.add(null);
        selectionList1.add(null);

        mSelections.put(mItinerary.getmFlightNumber(), selectionList1);

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


        Spinner mSeats = (Spinner) theView.findViewById(R.id.seat_spinner_leg1);
        mSeats.setOnItemSelectedListener(this);



        ArrayAdapter<String> seatAdpater = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, mSeatsFlight1);
        seatAdpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSeats.setAdapter(seatAdpater);
        mSeats.setSelection(0);


        Spinner mMeals = (Spinner) theView.findViewById(R.id.meal_spinner_leg1);
        mMeals.setOnItemSelectedListener(this);


        ArrayAdapter<String> mealAdpater = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, mMealsList);
        mealAdpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMeals.setAdapter(mealAdpater);
        mMeals.setSelection(0);


        Itinerary secondFlight = mItinerary.getmFlights().get(0);
        ArrayList<String> selectionList2 = new ArrayList<String>();
        selectionList2.add(null);
        selectionList2.add(null);
        mSelections.put(secondFlight.getmFlightNumber(), selectionList2);


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

        ArrayAdapter<String> seatAdpater2 = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, mSeatsFlight2);
        seatAdpater2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSeats2.setAdapter(seatAdpater2);
        mSeats2.setSelection(0);

        // Build the pub list spinner
        Spinner mMeals2 = (Spinner) theView.findViewById(R.id.meal_spinner_leg2);
        mMeals2.setOnItemSelectedListener(this);


        mMeals2.setAdapter(mealAdpater);
        mMeals2.setSelection(0);

    }

    public void buildTwoStopUI(View theView) {
        ArrayList<String> selectionList1 = new ArrayList<String>();
        selectionList1.add(null);
        selectionList1.add(null);

        mSelections.put(mItinerary.getmFlightNumber(), selectionList1);


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


        Spinner mSeats = (Spinner) theView.findViewById(R.id.seat_spinner_leg1);
        mSeats.setOnItemSelectedListener(this);


        ArrayAdapter<String> seatAdpater = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, mSeatsFlight1);
        seatAdpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSeats.setAdapter(seatAdpater);
        mSeats.setSelection(0);

        // Build the pub list spinner
        Spinner mMeals = (Spinner) theView.findViewById(R.id.meal_spinner_leg1);
        mMeals.setOnItemSelectedListener(this);


        ArrayAdapter<String> mealAdpater = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, mMealsList);
        mealAdpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMeals.setAdapter(mealAdpater);
        mMeals.setSelection(0);

        Itinerary secondFlight = mItinerary.getmFlights().get(0);
        ArrayList<String> selectionList2 = new ArrayList<String>();
        selectionList2.add(null);
        selectionList2.add(null);
        mSelections.put(secondFlight.getmFlightNumber(), selectionList2);

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

        ArrayAdapter<String> seatAdpater2 = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, mSeatsFlight2);
        seatAdpater2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSeats2.setAdapter(seatAdpater2);
        mSeats2.setSelection(0);

        // Build the pub list spinner
        Spinner mMeals2 = (Spinner) theView.findViewById(R.id.meal_spinner_leg2);
        mMeals2.setOnItemSelectedListener(this);


        mMeals2.setAdapter(mealAdpater);
        mMeals2.setSelection(0);

        Itinerary thirdFlight = mItinerary.getmFlights().get(1);
        ArrayList<String> selectionList3 = new ArrayList<String>();
        selectionList3.add(null);
        selectionList3.add(null);
        mSelections.put(thirdFlight.getmFlightNumber(), selectionList3);

        TextView departureAP3 = (TextView) theView.findViewById(R.id.departure_airport_leg3);
        departureAP3.setText(thirdFlight.getmDepartureAPName());
        TextView departureTime3 = (TextView) theView.findViewById(R.id.departure_time_leg3);
        departureTime3.setText(thirdFlight.getmDepartureTime());
        TextView arrivalAP3 = (TextView) theView.findViewById(R.id.arrival_airport_leg3);
        arrivalAP3.setText(thirdFlight.getmArivalAPName());
        TextView arrivalTime3 = (TextView) theView.findViewById(R.id.arrival_time_leg3);
        arrivalTime3.setText(thirdFlight.getmArrivalTime());

        Spinner mSeats3 = (Spinner) theView.findViewById(R.id.seat_spinner_leg3);
        mSeats3.setOnItemSelectedListener(this);

        ArrayAdapter<String> seatAdpater3 = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, mSeatsFlight3);
        seatAdpater3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSeats3.setAdapter(seatAdpater3);
        mSeats3.setSelection(0);

        // Build the pub list spinner
        Spinner mMeals3 = (Spinner) theView.findViewById(R.id.meal_spinner_leg3);
        mMeals3.setOnItemSelectedListener(this);
        mMeals3.setAdapter(mealAdpater);
        mMeals3.setSelection(0);

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if(adapterView.getId() == R.id.seat_spinner_leg1) {
            ArrayList<String> list = mSelections.get(mItinerary.getmFlightNumber());
            list.add(0, mSeatsFlight1.get(i+1));
//            Log.i("Seat Flight1: ", mSeatsFlight1.get(i));


        } else if(adapterView.getId() == R.id.seat_spinner_leg2) {
            ArrayList<String> list = mSelections.get(mItinerary.getmFlights().get(0).getmFlightNumber());
            list.add(0, mSeatsFlight2.get(i+1));
            Log.i("Seat Flight2: ", mSeatsFlight2.get(i));



        } else if(adapterView.getId() == R.id.seat_spinner_leg3) {
            ArrayList<String> list = mSelections.get(mItinerary.getmFlights().get(1).getmFlightNumber());
            list.add(0, mSeatsFlight3.get(i+1));
            Log.i("Seat Flight2: ", mSeatsFlight2.get(i));



        } else if(adapterView.getId() == R.id.meal_spinner_leg1) {
            ArrayList<String> list = mSelections.get(mItinerary.getmFlightNumber());
            list.add(1, mMealsList.get(i));
            Log.i("Meal Flight1: ", mMealsList.get(i));

        } else if(adapterView.getId() == R.id.meal_spinner_leg2) {
            ArrayList<String> list = mSelections.get(mItinerary.getmFlights().get(0).getmFlightNumber());
            list.add(1, mMealsList.get(i));
            Log.i("Meal Flight2: ", mMealsList.get(i));

        } else if(adapterView.getId() == R.id.meal_spinner_leg3) {
            ArrayList<String> list = mSelections.get(mItinerary.getmFlights().get(1).getmFlightNumber());
            list.add(1, mMealsList.get(i));
            Log.i("Meal Flight2: ", mMealsList.get(i));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
