package edu.uw.tacoma.dionmerz.fatms.flight;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import edu.uw.tacoma.dionmerz.fatms.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ItineraryDetails extends Fragment {
    private Itinerary mItinerary;

    public ItineraryDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = null;
        Button bt = null;
        mItinerary = (Itinerary) getArguments().getSerializable("itinerary");
        int numLayover = mItinerary.getmFlights().size();
        if (numLayover == 0) {
            v = inflater.inflate(R.layout.fragment_itinerary_details, container, false);
            bt = (Button) v.findViewById(R.id.select_flight);
            buildNonStopUI(v);
        } else if (numLayover == 1) {
            v = inflater.inflate(R.layout.fragment_itinerary_details_one, container, false);
            bt = (Button) v.findViewById(R.id.select_flight_1);
            buildOneStopUI(v);
        } else {
            v = inflater.inflate(R.layout.fragment_itinerary_details_two, container, false);
            bt = (Button) v.findViewById(R.id.select_flight_2);
            buildTwoStopUI(v);

        }

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(getArguments().getBoolean("isReturn")) {
                    ((FlightResultActivity) getActivity()).goToConfirmation(mItinerary);
                } else {
                    ((FlightResultActivity) getActivity()).returnFlightSearch(mItinerary);
                }
            }
        });

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

        Itinerary secondFlight = mItinerary.getmFlights().get(0);
        TextView departureAP2 = (TextView) theView.findViewById(R.id.departure_airport_leg2);
        departureAP2.setText(secondFlight.getmDepartureAPName());
        TextView departureTime2 = (TextView) theView.findViewById(R.id.departure_time_leg2);
        departureTime2.setText(secondFlight.getmDepartureTime());
        TextView arrivalAP2 = (TextView) theView.findViewById(R.id.arrival_airport_leg2);
        arrivalAP2.setText(secondFlight.getmArivalAPName());
        TextView arrivalTime2= (TextView) theView.findViewById(R.id.arrival_time_leg2);
        arrivalTime2.setText(secondFlight.getmArrivalTime());

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

        Itinerary secondFlight = mItinerary.getmFlights().get(0);
        TextView departureAP2 = (TextView) theView.findViewById(R.id.departure_airport_leg2);
        departureAP2.setText(secondFlight.getmDepartureAPName());
        TextView departureTime2 = (TextView) theView.findViewById(R.id.departure_time_leg2);
        departureTime2.setText(secondFlight.getmDepartureTime());
        TextView arrivalAP2 = (TextView) theView.findViewById(R.id.arrival_airport_leg2);
        arrivalAP2.setText(secondFlight.getmArivalAPName());
        TextView arrivalTime2= (TextView) theView.findViewById(R.id.arrival_time_leg2);
        arrivalTime2.setText(secondFlight.getmArrivalTime());

        Itinerary thirdFlight = mItinerary.getmFlights().get(1);
        TextView departureAP3 = (TextView) theView.findViewById(R.id.departure_airport_leg3);
        departureAP3.setText(thirdFlight.getmDepartureAPName());
        TextView departureTime3 = (TextView) theView.findViewById(R.id.departure_time_leg3);
        departureTime3.setText(thirdFlight.getmDepartureTime());
        TextView arrivalAP3 = (TextView) theView.findViewById(R.id.arrival_airport_leg3);
        arrivalAP3.setText(thirdFlight.getmArivalAPName());
        TextView arrivalTime3 = (TextView) theView.findViewById(R.id.arrival_time_leg3);
        arrivalTime3.setText(thirdFlight.getmArrivalTime());

    }




}
