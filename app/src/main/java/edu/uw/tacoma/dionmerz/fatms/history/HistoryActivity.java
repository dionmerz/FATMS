package edu.uw.tacoma.dionmerz.fatms.history;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.uw.tacoma.dionmerz.fatms.R;
import edu.uw.tacoma.dionmerz.fatms.ReportResultsActivity;
import edu.uw.tacoma.dionmerz.fatms.ReportsActivity;

public class HistoryActivity extends AppCompatActivity {

    private final static String HISTORY_URL
            = "https://students.washington.edu/jwolf059/FATMS.php?cmd=history";

    private String mEmail;
    private SharedPreferences mSharedPrefs;
    private TextView historyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mSharedPrefs =
                getSharedPreferences(getString(R.string.LOGIN_PREFS)
                        , Context.MODE_PRIVATE);

        mEmail = mSharedPrefs.getString(getString(R.string.current_user), "");

        historyText = (TextView) findViewById(R.id.textView_history);
        if (mEmail.length() > 1) {
            history(HISTORY_URL, mEmail);
        }


    }


    public void history(String url, String theEmail) {

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            try {
                StringBuilder sb = new StringBuilder(HISTORY_URL);
                sb.append("&email=");
                sb.append(theEmail);

                Log.i("URL", sb.toString());

                new HistoryTask().execute(sb.toString());


            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(this, "No network connection available. Cannot authenticate user",
                    Toast.LENGTH_SHORT).show();
            return;
        }

    }



    private class HistoryTask extends AsyncTask<String, Void, String> {


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
                    response = "Unable to get Report, Reason: "
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

            Intent intent = new Intent(getApplicationContext(), ReportResultsActivity.class);

            StringBuilder sbReport = new StringBuilder();

            Log.d("PostExecute: ", result);
            // Something wrong with the network or the URL.
            try {
                JSONArray jsonArray = new JSONArray(result);



                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject record = jsonArray.getJSONObject(i);
                            sbReport.append("Trip ID: ");
                            sbReport.append(record.get("trip_id"));
                            sbReport.append(" \n");

                            sbReport.append("Flight ID: ");
                            sbReport.append(record.get("flight_id"));
                            sbReport.append(" \n");

                            sbReport.append("Seat Number: ");
                            sbReport.append(record.get("seat_num"));
                            sbReport.append(" \n");

                            sbReport.append("Date: ");
                            sbReport.append(record.get("date"));
                            sbReport.append(" \n");

                            sbReport.append("From: ");
                            sbReport.append(record.get("city"));
                            sbReport.append(" \n");

                            sbReport.append("To: ");
                            sbReport.append(record.get("dest"));
                            sbReport.append(" \n\n");


                        }
                historyText.setText(sbReport.toString());



            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Something wrong with the data" +
                        e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}

