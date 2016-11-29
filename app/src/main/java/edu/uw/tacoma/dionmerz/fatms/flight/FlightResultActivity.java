package edu.uw.tacoma.dionmerz.fatms.flight;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import edu.uw.tacoma.dionmerz.fatms.R;

public class FlightResultActivity extends AppCompatActivity {
    private final static String URL
            = "https://students.washington.edu/jwolf059/FATMS.php?cmd=";
    private ArrayList<Itinerary> mItineraryListOutgoing;
    private ArrayList<Itinerary> mItineraryListReturning;
    private boolean isReady;

    public FlightResultActivity() {
        mItineraryListOutgoing = new ArrayList<>();
        mItineraryListReturning = new ArrayList<>();
        isReady = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_result);


        String date = getIntent().getStringExtra("date");
        String departAP = getIntent().getStringExtra("depart");
        String destinAP = getIntent().getStringExtra("arrive");

        // Done for both round trip and one way.
        SearchTaskOutgoing tasknonStop = new SearchTaskOutgoing();
        SearchTaskOutgoing taskOneStop = new SearchTaskOutgoing();
        SearchTaskOutgoing taskTwoStop = new SearchTaskOutgoing();
        String urlNonStop = buildSeachUrl("nonstop", departAP, destinAP, date);
        String urlOneStop = buildSeachUrl("onestop", departAP, destinAP, date);
        String urlTwoStop = buildSeachUrl("twostop", departAP, destinAP, date);
        tasknonStop.execute(urlNonStop);
        taskOneStop.execute(urlOneStop);
        taskTwoStop.execute(urlTwoStop);

        //Done for roundtrip flights (return flights)
        if(getIntent().getStringExtra("return") != null) {
            String returnDate = getIntent().getStringExtra("return");
            SearchTaskReturning tasknonStopReturn = new SearchTaskReturning();
            SearchTaskReturning taskOneStopReturn = new SearchTaskReturning();
            SearchTaskReturning taskTwoStopReturn = new SearchTaskReturning();
            String urlNonStopReturn = buildSeachUrl("nonstop", destinAP, departAP, returnDate);
            String urlOneStopReturn = buildSeachUrl("onestop", destinAP, departAP, returnDate);
            String urlTwoStopReturn = buildSeachUrl("twostop", destinAP, departAP, returnDate);
            tasknonStopReturn.execute(urlNonStopReturn);
            taskOneStopReturn.execute(urlOneStopReturn);
            taskTwoStopReturn.execute(urlTwoStopReturn);
        }


        //task.execute(urlOneStop);
        //task.execute(urlTwoStop);
    }

    private String buildSeachUrl(String theType, String theDeparture, String theDestination, String theDate) {

        StringBuilder sb = new StringBuilder(URL);


        try {
            sb.append(theType);

            sb.append("&start=");
            sb.append(cleanSpace(theDeparture));

            sb.append("&end=");
            sb.append(cleanSpace(theDestination));

            sb.append("&date=");
            sb.append(theDate);

        } catch (Exception e) {
            Toast.makeText(this, "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
        return sb.toString();
    }

    private class SearchTaskOutgoing extends AsyncTask<String, Void, String> {


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
                    response = "Unable to login, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            Log.d("inBackGround: ", response);
            return response;

        }


        /**
         * It checks to see if there was a problem with the URL(Network) which is when an
         * exception is caught. It tries to call the parse Method and checks to see if it was successful.
         * If not, it displays the exception.
         *
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {

            Log.d("PostExecute: ", result);
            // Something wrong with the network or the URL.
            try {
                JSONArray jsonArray = new JSONArray(result);


                for(int i = 0; i < jsonArray.length(); i++) {
                    Itinerary ity = Itinerary.flightJSONParse(jsonArray.getJSONObject(i));
                    mItineraryListOutgoing.add(ity);

                }

                for(Itinerary ity : mItineraryListOutgoing) {
                    System.out.println("");
                    System.out.println("\nFlight Outgoing");
                    System.out.println("Flight Size: " + ity.getmFlights().size());
                    System.out.println("Arrival Airport: " + ity.getmArivalAPName());
                    System.out.println("Arrival Time: " + ity.getmArrivalTime());
                    System.out.println("Departure Airport: "+ ity.getmDepartureAPName());
                    System.out.println("Departure Time: " + ity.getmDepartureTime());
                    System.out.println("Flight Number: " + ity.getmFlightNumber());
                    for (Itinerary lit : ity.getmFlights()) {
                        System.out.println("");
                        System.out.println("Flight in side");
                        System.out.println("Flight Size: " + lit.getmFlights().size());
                        System.out.println("Arrival Airport: " + lit.getmArivalAPName());
                        System.out.println("Arrival Time: " + lit.getmArrivalTime());
                        System.out.println("Departure Airport: "+ lit.getmDepartureAPName());
                        System.out.println("Departure Time: " + lit.getmDepartureTime());
                        System.out.println("Flight Number: " + lit.getmFlightNumber());
                    }

                }


            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Something wrong with the data" +
                        e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

    }

    private class SearchTaskReturning extends AsyncTask<String, Void, String> {


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
                    response = "Unable to login, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            Log.d("inBackGround: ", response);
            return response;

        }


        /**
         * It checks to see if there was a problem with the URL(Network) which is when an
         * exception is caught. It tries to call the parse Method and checks to see if it was successful.
         * If not, it displays the exception.
         *
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {

            Log.d("PostExecute: ", result);
            // Something wrong with the network or the URL.
            try {
                JSONArray jsonArray = new JSONArray(result);


                for(int i = 0; i < jsonArray.length(); i++) {
                    Itinerary ity = Itinerary.flightJSONParse(jsonArray.getJSONObject(i));
                    mItineraryListReturning.add(ity);

                }

                for(Itinerary ity : mItineraryListReturning) {
                    System.out.println("");
                    System.out.println("\nFlight returning");
                    System.out.println("Flight Size: " + ity.getmFlights().size());
                    System.out.println("Arrival Airport: " + ity.getmArivalAPName());
                    System.out.println("Arrival Time: " + ity.getmArrivalTime());
                    System.out.println("Departure Airport: "+ ity.getmDepartureAPName());
                    System.out.println("Departure Time: " + ity.getmDepartureTime());
                    System.out.println("Flight Number: " + ity.getmFlightNumber());
                    for (Itinerary lit : ity.getmFlights()) {
                        System.out.println("");
                        System.out.println("Flight in side");
                        System.out.println("Flight Size: " + lit.getmFlights().size());
                        System.out.println("Arrival Airport: " + lit.getmArivalAPName());
                        System.out.println("Arrival Time: " + lit.getmArrivalTime());
                        System.out.println("Departure Airport: "+ lit.getmDepartureAPName());
                        System.out.println("Departure Time: " + lit.getmDepartureTime());
                        System.out.println("Flight Number: " + lit.getmFlightNumber());
                    }

                }


            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Something wrong with the data" +
                        e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

    }

    private String cleanSpace(String theText) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < theText.length(); i++) {
            if (theText.charAt(i) == 32) {
                stringBuilder.append("%20");
            } else {
                stringBuilder.append(theText.charAt(i));
            }
        }
        return stringBuilder.toString();

    }

}
