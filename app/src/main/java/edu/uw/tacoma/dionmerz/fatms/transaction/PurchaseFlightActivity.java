package edu.uw.tacoma.dionmerz.fatms.transaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import edu.uw.tacoma.dionmerz.fatms.EditActivity;
import edu.uw.tacoma.dionmerz.fatms.FinishedActivity;
import edu.uw.tacoma.dionmerz.fatms.LoginActivity;
import edu.uw.tacoma.dionmerz.fatms.R;
import edu.uw.tacoma.dionmerz.fatms.history.HistoryActivity;

public class PurchaseFlightActivity extends AppCompatActivity {

    private static final String TRIPID_URL = "https://students.washington.edu/jwolf059/FATMS.php?cmd=tripid&email=";
    private static final String USER_INFO = "https://students.washington.edu/jwolf059/FATMS.php?cmd=userinfo&email=";
    private static final String INSERT_URL = "https://students.washington.edu/jwolf059/FATMS.php?cmd=purchase";
    private String mTripIDURL;
    private String mUserInfoURL;
    private String mEmail;
    private SharedPreferences mSharedPreferences;
    private String mFirstName;
    private String mLastName;
    private String mAddress;
    private String mCity;
    private String mState;
    private String mPhone;
    private String mTripID;
    private EditText mPassportID;
    private EditText mPassportCountry;
    private Boolean mIsRoundTrip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_flight);

        mSharedPreferences =
                getSharedPreferences(getString(R.string.LOGIN_PREFS)
                        , Context.MODE_PRIVATE);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        mEmail = mSharedPreferences.getString(getString(R.string.current_user),"");
        mIsRoundTrip = getIntent().getBooleanExtra("isRoundTrip", false);

        System.out.println("Email: " + mEmail);


        TextView mFirstNameTV = (TextView) findViewById(R.id.first_name);
        TextView mLastNameTV = (TextView) findViewById(R.id.last_name);
        TextView mAddressTV = (TextView) findViewById(R.id.street_add);
        TextView mCityTV = (TextView) findViewById(R.id.conf_city);
        TextView mStateTV = (TextView) findViewById(R.id.conf_state);
        TextView mPhoneTV = (TextView) findViewById(R.id.conf_zip);
        TextView mTripIDTV = (TextView) findViewById(R.id.trip_id);


        mPassportCountry = (EditText) findViewById(R.id.pass_country);
        mPassportID = (EditText) findViewById(R.id.passport_num);

        InfoTask it = new InfoTask();
        mUserInfoURL = USER_INFO + mEmail;
        mTripIDURL = TRIPID_URL + mEmail;

        try {
            String result = it.execute(mUserInfoURL, mTripIDURL).get();
        } catch (Exception e) {
            Log.e("PurchaseFlight Wait", e.getMessage());
        }

        mFirstNameTV.setText(mFirstName);
        mLastNameTV.setText(mLastName);
        mAddressTV.setText(mAddress);
        mCityTV.setText(mCity);
        mStateTV.setText(mState);
        mPhoneTV.setText(mPhone);
        mTripIDTV.setText(mTripID);


        Button bt = (Button) findViewById(R.id.purchase_trip);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mPassportID.getText().toString().isEmpty() || mPassportCountry.getText().toString().isEmpty()
                        || mPassportID.getText().toString().equals("")  || mPassportCountry.getText().toString().equals("")) {
                    Toast.makeText(getApplication(), "Passport Number & Issuing Country is required", Toast.LENGTH_LONG).show();

                } else {

                    HashMap<Integer, ArrayList<String>> mSelection = (HashMap<Integer, ArrayList<String>>) getIntent().getSerializableExtra("selections");
                    StringBuilder sb = new StringBuilder();
                    Set<Integer> keys = mSelection.keySet();
                    for (Integer i : keys) {
                        int flightID = i;
                        ArrayList<String> list = mSelection.get(i);
                        String seat = list.get(0);
                        String meal = list.get(1);

                        String url = buildInsertURL(seat, flightID, meal);
                        InfoTask insert = new InfoTask();
                        try {
                            String result = insert.execute(url).get();

                        } catch (Exception e) {
                            Log.e("OnClick, Purchase", e.getMessage());
                        }
                    }


                    Intent i = new Intent(getApplication(), FinishedActivity.class);
                    i.putExtra("email", mEmail);
                    i.putExtra("tripid", mTripID);
                    startActivity(i);
                    finish();
                }

            }
        });

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
                edit.putString(getString(R.string.current_user), "");
                edit.apply();

                Intent i = new Intent(this, LoginActivity.class);

                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(i);

                finish();
                break;

            case R.id.edit_account_menu:

                Intent edit_intent = new Intent(this, EditActivity.class);
                startActivity(edit_intent);


                break;

            case R.id.history_menu:
                Intent histIntent = new Intent(getApplicationContext(), HistoryActivity.class);
                startActivity(histIntent);
                break;


            default:
                return super.onOptionsItemSelected(item);
        }

        return false;
    }




    public String buildInsertURL(String theSeat, int theFlightID, String theMeal) {
        StringBuilder sb = new StringBuilder();
        sb.append(INSERT_URL);

        sb.append("&email=");
        sb.append(mEmail);

        sb.append("&trip_id=");
        sb.append(mTripID);

        sb.append("&flight_id=");
        sb.append(theFlightID);

        sb.append("&seat=");
        sb.append(theSeat);

        sb.append("&meal=");
        sb.append(theMeal);

        sb.append("&pass=");
        sb.append(mPassportID.getText());

        sb.append("&country=");
        sb.append(mPassportCountry.getText());

        sb.append("&round=");

        if (mIsRoundTrip) {
            sb.append("Y");
        } else {
            sb.append("N");
        }

        Log.w("URL", sb.toString());
        return sb.toString();
    }



    public class InfoTask extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                response = "";
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

                    try {

                        if (url.equals(mUserInfoURL)) {
                            JSONArray objArray = new JSONArray(response);
                            JSONObject obj = objArray.getJSONObject(0);
                            mFirstName = obj.getString("fname");
                            mLastName = obj.getString("lname");
                            mAddress = obj.getString("street_address");
                            mCity = obj.getString("city");
                            mState = obj.getString("state");
                            mPhone = obj.getString("phone_num");

                        } else if (url.equals(mTripIDURL)){
                            JSONObject obj = new JSONObject(response);
                            mTripID = obj.getString("trip_id");
                        }

                    } catch (JSONException e) {
                        Log.e("PurchaseFlight", e.getMessage());
                    }

                    Log.d("inBackGround: ", response);
                }
            }
                return response;


        }
    }
}
