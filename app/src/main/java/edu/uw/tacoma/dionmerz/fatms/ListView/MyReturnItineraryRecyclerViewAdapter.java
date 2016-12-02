package edu.uw.tacoma.dionmerz.fatms.ListView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.uw.tacoma.dionmerz.fatms.R;
import edu.uw.tacoma.dionmerz.fatms.ListView.ReturnItineraryFragment.OnListFragmentInteractionListener;
import edu.uw.tacoma.dionmerz.fatms.flight.Itinerary;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Itinerary} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class MyReturnItineraryRecyclerViewAdapter extends RecyclerView.Adapter<MyReturnItineraryRecyclerViewAdapter.ViewHolder> {

    private final List<Itinerary> mItineraryList;
    private final OnListFragmentInteractionListener mListener;

    public MyReturnItineraryRecyclerViewAdapter(List<Itinerary> items, OnListFragmentInteractionListener listener) {
        mItineraryList = items;
        mListener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_itinerary, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        int stops = mItineraryList.get(position).getmFlights().size();
        StringBuilder sb = new StringBuilder();
        StringBuilder trip = new StringBuilder();
        sb.append(stops);
        String arrivalAP = "";

        holder.mItinerary = mItineraryList.get(position);
        trip.append(mItineraryList.get(position).getmDepartureAPName() + "    to    ");
        holder.mdepartureTime.setText("Departure Time: " +mItineraryList.get(position).getmDepartureTime());
        holder.mCost.setText("Price: $" + mItineraryList.get(position).getmPriceString());
        holder.mNumberOfStops.setText("Layovers: " + sb.toString());

        if (stops == 0) {
            trip.append(mItineraryList.get(position).getmArivalAPName());
            holder.mArrivalTime.setText("Arrival Time: " + mItineraryList.get(position).getmArrivalTime());
        } else if (stops == 1) {
            Itinerary ity = mItineraryList.get(position).getmFlights().get(0);
            trip.append(ity.getmArivalAPName());
            holder.mArrivalTime.setText("Arrival Time: " + ity.getmArrivalTime());
        } else {
            Itinerary ity = mItineraryList.get(position).getmFlights().get(1);
            trip.append(ity.getmArivalAPName());
            holder.mArrivalTime.setText("Arrival Time: " + ity.getmArrivalTime());
        }
        holder.mTrip.setText(trip.toString());


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItinerary, true);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItineraryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTrip;
        public final TextView mdepartureTime;
        //public final TextView mdepartureDate;
        public final TextView mArrivalTime;
        public final TextView mNumberOfStops;
        public final TextView mCost;

        public Itinerary mItinerary;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTrip = (TextView) view.findViewById(R.id.depart_airport_list);
            mdepartureTime = (TextView) view.findViewById(R.id.depart_time_list);
           // mdepartureDate = (TextView) view.findViewById(R.id.depart_date_list);
            mArrivalTime = (TextView) view.findViewById(R.id.arrival_time_list);
            mNumberOfStops = (TextView) view.findViewById(R.id.number_stops);
            mCost = (TextView) view.findViewById(R.id.cost_list);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mdepartureTime.getText() + "'";
        }
    }
}
