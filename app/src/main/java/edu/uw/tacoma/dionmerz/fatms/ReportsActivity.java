package edu.uw.tacoma.dionmerz.fatms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.uw.tacoma.dionmerz.fatms.flight.FlightSearchActivity;

public class ReportsActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String REPORT_URL
            = "https://students.washington.edu/jwolf059/FATMS.php?cmd=";

    private SharedPreferences mSharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        mSharedPreferences =
                getSharedPreferences(getString(R.string.LOGIN_PREFS)
                        , Context.MODE_PRIVATE);


        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar_reports);
        setSupportActionBar(myToolbar);

        myToolbar.dismissPopupMenus();


        Button report1_button = (Button) findViewById(R.id.button_report_nonUS);
        Button report2_button = (Button) findViewById(R.id.button_report_popular);

        report1_button.setOnClickListener(this);
        report2_button.setOnClickListener(this);

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case R.id.logout_menu:

                SharedPreferences.Editor edit = mSharedPreferences.edit();

                edit.putBoolean(getString(R.string.LOGGEDIN), false);
                edit.apply();

                Intent i = new Intent(this, LoginActivity.class);

                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(i);

                finish();
                break;


            default:
                return super.onOptionsItemSelected(item);
        }

        return false;
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        StringBuilder sb = new StringBuilder(REPORT_URL);

        switch (id) {
            case R.id.button_report_nonUS:


                sb.append("onewaynotcit");
                report(sb.toString());

                break;

            case R.id.button_report_popular:

                sb.append("popflight");
                report(sb.toString());
                break;

            case R.id.button_add_flight:

                break;


        }


    }


    public void report(String url) {

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            try {

                new ReportsActivity.ReportTask().execute(url);


            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(this, "No network connection available. Cannot authenticate user",
                    Toast.LENGTH_SHORT).show();
            return;
        }

    }




    private class ReportTask extends AsyncTask<String, Void, String> {


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
                JSONObject jsonObject = new JSONObject(result);


                try {
                    if (jsonObject.getString("popflight") != null) {


                        JSONArray flightArray = jsonObject.getJSONArray("popflight");


                        for(int i = 0; i < flightArray.length(); i++) {
                            JSONObject person = flightArray.getJSONObject(i);
                            sbReport.append("Flight ID: ");
                            sbReport.append(person.get("flight_id"));
                            sbReport.append(" \n");

                            sbReport.append("Passenger Count: ");
                            sbReport.append(person.get("Passengers"));
                            sbReport.append(" \n\n");


                        }


                        intent.putExtra("title", "Popular Flights Report");
                    }
                }
                catch (Exception e) {
                    if (jsonObject.getString(("onewaynotcit")) != null) {


                       JSONArray peopleArray = jsonObject.getJSONArray("onewaynotcit");

                        for(int i = 0; i < peopleArray.length(); i++) {
                            JSONObject person = peopleArray.getJSONObject(i);
                            sbReport.append("Name: ");
                            sbReport.append(person.get("name"));
                            sbReport.append(" \n");

                            sbReport.append("Passport ID: ");
                            sbReport.append(person.get("passport_id"));
                            sbReport.append(" \n");

                            sbReport.append("Country: ");
                            sbReport.append(person.get("country"));
                            sbReport.append(" \n");

                            sbReport.append("Flight ID: ");
                            sbReport.append(person.get("flight_id"));
                            sbReport.append(" \n\n");

                        }




                        intent.putExtra("title", "Homeland Security Department\nTravel Report");

                    }
                }


                intent.putExtra("report", sbReport.toString());

                startActivity(intent);

//                for(int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject name = jsonArray.getJSONObject(i);
//                    mAirportList.add(name.getString("name"));
//
//                }


            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Something wrong with the data" +
                        e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
