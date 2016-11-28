package edu.uw.tacoma.dionmerz.fatms.flight;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    private ArrayList<Itinerary> mItineraryList;

    public FlightResultActivity() {
        mItineraryList = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_result);
        SearchTask task = new SearchTask();
        String urlNonStop = buildSeachUrl("nonstop");
        String urlOneStop = buildSeachUrl("onestop");
        String urlTwoStop = buildSeachUrl("twostop");
        task.execute(urlNonStop);
        //task.execute(urlOneStop);
        //task.execute(urlTwoStop);


    }

    private String buildSeachUrl(String theType) {

        StringBuilder sb = new StringBuilder(URL);


        try {
            sb.append(theType);

            String departAP = getIntent().getStringExtra("depart_ap");
            sb.append("&start=");
            sb.append(departAP);


            String destinAP = getIntent().getStringExtra("destin_ap");
            sb.append("&end=");
            sb.append(destinAP);

            String date = getIntent().getStringExtra("start_date");
            sb.append("&date=");
            sb.append(destinAP);

        } catch (Exception e) {
            Toast.makeText(this, "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
        return sb.toString();
    }

    private class SearchTask extends AsyncTask<String, Void, String> {


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
            // Something wrong with the network or the URL.
            try {
                JSONArray jsonArray = new JSONArray(result);
                for(int i = 0; i < result.length(); i++) {
                    Itinerary ity = Itinerary.flightJSONParse(jsonArray.getJSONObject(i));
                    mItineraryList.add(ity);
                }


            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Something wrong with the data" +
                        e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

}
