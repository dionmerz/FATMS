package edu.uw.tacoma.dionmerz.fatms.transaction;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import edu.uw.tacoma.dionmerz.fatms.R;
import edu.uw.tacoma.dionmerz.fatms.flight.Itinerary;

public class ConfirmationActivity extends AppCompatActivity {

    private ArrayList<Itinerary> mFlights;
    private final static String URL
            = "https://students.washington.edu/jwolf059/FATMS.php?cmd=seats";
    private ArrayList<String> mSeatsFlight1;
    private ArrayList<String> mSeatsFlight2;
    private ArrayList<String> mSeatsFlight3;
    private int mFlightOneFlightNumber;
    private int mFlightTwoFlightNumber;
    private int mFlightThreeFlightNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);


        // Is a round trip flight.
        if (getIntent().getSerializableExtra("return") != null) {

            Itinerary outgoing = (Itinerary) getIntent().getSerializableExtra("outgoing");
            instatiatieFlightData(outgoing);
            ConfirmationDetailsFragment confirmDetails = new ConfirmationDetailsFragment();
            Bundle args = new Bundle();
            args.putSerializable("leg", outgoing);
            args.putBoolean("isRoundTrip", true);
            args.putSerializable("flight1_seats", mSeatsFlight1);
            args.putSerializable("flight2_seats", mSeatsFlight2);
            args.putSerializable("flight3_seats", mSeatsFlight3);
            confirmDetails.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.activity_confirmation, confirmDetails);

            transaction.commit();

            //Is a one way flight.
        } else {
            Itinerary outgoing = (Itinerary) getIntent().getSerializableExtra("outgoing");

            instatiatieFlightData(outgoing);
            ConfirmationDetailsFragment confirmDetails = new ConfirmationDetailsFragment();
            Bundle args = new Bundle();
            args.putSerializable("leg", outgoing);
            args.putBoolean("isRoundTrip", false);
            args.putSerializable("flight1_seats", mSeatsFlight1);
            args.putSerializable("flight2_seats", mSeatsFlight2);
            args.putSerializable("flight3_seats", mSeatsFlight3);
            confirmDetails.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.activity_confirmation, confirmDetails);

            transaction.commit();

        }
    }


    public void returnFlightConfirmation(HashMap<Integer, ArrayList<String>> theSelections) {

        Itinerary returning = (Itinerary) getIntent().getSerializableExtra("return");
        instatiatieFlightData(returning);
        ConfirmationDetailsFragment confirmDetails = new ConfirmationDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable("leg", returning);
        args.putBoolean("isRoundTrip", false);
        args.putBoolean("roundTrip", true);
        args.putSerializable("selection", theSelections);
        args.putSerializable("flight1_seats", mSeatsFlight1);
        args.putSerializable("flight2_seats", mSeatsFlight2);
        args.putSerializable("flight3_seats", mSeatsFlight3);
        confirmDetails.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.activity_confirmation, confirmDetails);

        transaction.commit();
    }


    public void purchaseFlight(HashMap<Integer, ArrayList<String>> theSelections) {
        Intent i = new Intent(getApplicationContext(), PurchaseFlightActivity.class);
        i.putExtra("selections", theSelections);

        if (getIntent().getSerializableExtra("return") != null) {
            i.putExtra("isRoundTrip", true);
        }
        startActivity(i);
    }

    public void instatiatieFlightData(Itinerary theItinerary) {

        try {
            mFlightOneFlightNumber = theItinerary.getmFlightNumber();
            SeatTask task = new SeatTask();
            String result = task.execute(buildSeatUrl(mFlightOneFlightNumber)).get();


            if (theItinerary.getmFlights().size() == 1) {
                mFlightTwoFlightNumber = theItinerary.getmFlights().get(0).getmFlightNumber();
                SeatTask task2 = new SeatTask();
                result = task2.execute(buildSeatUrl(mFlightTwoFlightNumber)).get();


            } else if (theItinerary.getmFlights().size() == 2) {
                mFlightTwoFlightNumber = theItinerary.getmFlights().get(0).getmFlightNumber();
                SeatTask task2 = new SeatTask();
                result = task2.execute(buildSeatUrl(mFlightTwoFlightNumber)).get();
                mFlightThreeFlightNumber = theItinerary.getmFlights().get(1).getmFlightNumber();
                SeatTask task3 = new SeatTask();
                result = task3.execute(buildSeatUrl(mFlightThreeFlightNumber)).get();
            }
        } catch (Exception e) {
            Log.e("Seat: ", e.getMessage());
        }
    }

    public String buildSeatUrl(int theFlightID) {

        StringBuilder sb = new StringBuilder(URL);

        sb.append("&flight_id=");
        sb.append(theFlightID);

        Log.i("URLConfim: ", sb.toString());

        return sb.toString();
    }

    public class SeatTask extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    Log.i("You have an error ", "Waytogo");
                    response = "Unable to login, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();


                    // Something wrong with the network or the URL.
                    try {

                        Log.i("PostExecuteConfirm: ", response);
                        JSONObject jsonObject = new JSONObject(response);
                        int flightNumber = jsonObject.getInt("result");

                        if (flightNumber == mFlightOneFlightNumber) {
                            mSeatsFlight1 = new ArrayList<>();

                            JSONArray array = jsonObject.getJSONArray("seat_array");
                            for (int i = 1; i <= array.length(); i++) {
                                if (!array.getString(i - 1).equals("1")) {
                                    mSeatsFlight1.add(String.valueOf(i));
                                }
                            }
                            Log.i("List1", " Built");
                        } else if (flightNumber == mFlightTwoFlightNumber) {
                            mSeatsFlight2 = new ArrayList<>();


                            JSONArray array = jsonObject.getJSONArray("seat_array");
                            for (int i = 1; i <= array.length(); i++) {
                                if (array.getString(i - 1) != "1") {
                                    mSeatsFlight2.add(String.valueOf(i));
                                }
                            }
                            Log.i("List2", " Built");

                        } else if (flightNumber == mFlightThreeFlightNumber) {
                            mSeatsFlight3 = new ArrayList<>();


                            JSONArray array = jsonObject.getJSONArray("seat_array");
                            for (int i = 1; i <= array.length(); i++) {
                                if (array.getString(i - 1) != "1") {
                                    mSeatsFlight3.add(String.valueOf(i));
                                }
                            }
                            Log.i("List3", " Built");

                        }
                    } catch (Exception e) {

                    }
                }
                Log.d("inBackGround: ", response);
            }
            return response;

        }
    }
}
