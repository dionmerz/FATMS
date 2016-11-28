package edu.uw.tacoma.dionmerz.fatms.flight;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by JeremyWolf on 11/28/16.
 */

public class Itinerary {
    private double mPrice;
    private int mFlightNumber;
    private String mTailNum;
    private ArrayList<Itinerary> mFlights;
    private String mDepartureAPName;
    private String mArivalAPName;
    private String mDate;
    private String mDepartureTime;
    private String mArrivalTime;



    public Itinerary(double thePrice, int theFlightNumber, String theTailNum, String theDepartureAP, String theArrivalAP,
                     String theDate, String theDepartureTime, String theArrivalTime) {
        mPrice = thePrice;
        mFlightNumber = theFlightNumber;
        mTailNum = theTailNum;
        mFlights = new ArrayList<>();
        mDepartureAPName = theDepartureAP;
        mArivalAPName = theArrivalAP;
        mDate = theDate;
        mDepartureAPName = theDepartureTime;
        mArrivalTime = theArrivalTime;


    }

    public void caculateCurrentPrice(Double thePrice) {
        mPrice += thePrice;
    }

    public void addFlight(Itinerary theFlight) {
        mFlights.add(theFlight);
    }

    public static Itinerary flightJSONParse(JSONObject theResult) {

        Itinerary firstFlight = null;

        try {
            Double price = theResult.getDouble("cur_price");
            int flightID = theResult.getInt("flight_id");
            String tailNumber = theResult.getString("tail_num");
            String destName = theResult.getString("dest 1");
            String startDate = theResult.getString("flight_start_Date");
            String departName = theResult.getString("depart 1");
            String startTime = theResult.getString("flight_start_time");
            String endTime = theResult.getString("flight_end_time");


            firstFlight = new Itinerary(price, flightID, tailNumber, departName, destName, startDate, startTime, endTime);


            if (theResult.has("depart 2") && !theResult.has("depart 3")) {
                Double price2 = theResult.getDouble("price 2");
                int flightID2 = theResult.getInt("flight_id 2");
                String tailNumber2 = theResult.getString("tail_num2");
                String startDate2 = theResult.getString("start_Date 2");
                String departName2 = theResult.getString("depart 2");
                String startTime2 = theResult.getString("flight_start_time2");
                String endTime2 = theResult.getString("flight_end_time 2");
                String destName2 = theResult.getString("dest 2");

                Itinerary secondFlight = new Itinerary(price2, flightID2, tailNumber2, departName2, destName2, startDate2, startTime2, endTime2);
                firstFlight.caculateCurrentPrice(price2);
                firstFlight.addFlight(secondFlight);

            } else {
                Double price3 = theResult.getDouble("price 3");
                int flightID3 = theResult.getInt("flight_id 3");
                String tailNumber3 = theResult.getString("tail_num3");
                String startDate3 = theResult.getString("start_Date 3");
                String departName3 = theResult.getString("depart 3");
                String startTime3 = theResult.getString("flight_start_time 3");
                String endTime3 = theResult.getString("flight_end_time 3");
                String destName3 = theResult.getString("dest 3");

                Itinerary thirdFlight = new Itinerary(price3, flightID3, tailNumber3, departName3, destName3, startDate3, startTime3, endTime3);
                firstFlight.caculateCurrentPrice(price3);
                firstFlight.addFlight(thirdFlight);

            }
        } catch (JSONException e) {
            Log.e("Itinerary: ", e.getMessage());
        }
        return firstFlight;
    }

    public double getmPrice() {
        return mPrice;
    }

    public int getmFlightNumber() {
        return mFlightNumber;
    }

    public String getmTailNum() {
        return mTailNum;
    }

    public ArrayList<Itinerary> getmFlights() {
        return mFlights;
    }

    public String getmDepartureAPName() {
        return mDepartureAPName;
    }

    public String getmArivalAPName() {
        return mArivalAPName;
    }

    public String getmDate() {
        return mDate;
    }

    public String getmDepartureTime() {
        return mDepartureTime;
    }

    public String getmArrivalTime() {
        return mArrivalTime;
    }
}


