package edu.uw.tacoma.dionmerz.fatms.flight;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.uw.tacoma.dionmerz.fatms.LoginActivity;
import edu.uw.tacoma.dionmerz.fatms.R;
import edu.uw.tacoma.dionmerz.fatms.RegisterFragment;

public class FlightSearchActivity extends AppCompatActivity {

    private final static String UPDATE_URL
            = "https://students.washington.edu/jwolf059/FATMS.php?cmd=update";

    SharedPreferences mSharedPreferences;
    private EditText mPhoneNumber;
    private EditText mAddress;
    private EditText mCity;
    private EditText mState;
    private EditText mZip;
    private Dialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_search);

        mSharedPreferences =
                getSharedPreferences(getString(R.string.LOGIN_PREFS)
                        , Context.MODE_PRIVATE);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setText("One Way Flight"));
        tabLayout.addTab(tabLayout.newTab().setText("Round Trip Flight"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final FlightTabAdapter flightAdapter = new FlightTabAdapter(getSupportFragmentManager(),
                tabLayout.getTabCount());

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);




        viewPager.setAdapter(flightAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

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

                mDialog = new Dialog(this);
                mDialog.setContentView(R.layout.dialog_edit_account);
                mDialog.setTitle("Update Account");

                Button updateButton = (Button) mDialog.findViewById(R.id.update_account_button);
                Button cancelButton = (Button) mDialog.findViewById(R.id.cancel_update_button);

                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        mPhoneNumber = (EditText) mDialog.findViewById(R.id.edit_text_phone);
                        mAddress = (EditText) mDialog.findViewById(R.id.edit_text_address);
                        mCity = (EditText) mDialog.findViewById(R.id.edit_text_city);
                        mState = (EditText) mDialog.findViewById(R.id.edit_text_state);
                        mZip = (EditText) mDialog.findViewById(R.id.edit_text_zip);




                        String url = buildRegisterURL(view);
                        update(url);
                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDialog.dismiss();
                    }
                });


                mDialog.show();


                break;


            default:
                return super.onOptionsItemSelected(item);
        }

        return false;
    }


    private String buildRegisterURL(View v) {

        StringBuilder sb = new StringBuilder(UPDATE_URL);

        try {
            String email = mSharedPreferences.getString(getString(R.string.current_user),"");
            sb.append("&email=");
            sb.append(cleanSpace(email));

            String phoneNum = mPhoneNumber.getText().toString();
            sb.append("&phone=");
            sb.append(cleanSpace(phoneNum));

            String address = mAddress.getText().toString();
            sb.append("&address=");
            sb.append(cleanSpace(address));

            String city = mCity.getText().toString();
            sb.append("&city=");
            sb.append(cleanSpace(city));

            String state = mState.getText().toString();
            sb.append("&state=");
            sb.append(cleanSpace(state));

            String zip = mZip.getText().toString();
            sb.append("&zip=");
            sb.append(zip);

        } catch (Exception e) {
            Toast.makeText(v.getContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }

        return sb.toString();
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

    public void update(String url) {
        UpdateTask task = new UpdateTask();
        task.execute(new String[]{url.toString()});
    }




    private class UpdateTask extends AsyncTask<String, Void, String> {


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
                    response = "Unable to update, Reason: "
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

            Log.i("OnPostExecute: ", result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                if (status.equals("success")) {


                    Toast.makeText(getApplicationContext(), "Update Successful", Toast.LENGTH_LONG).show();

                    mDialog.dismiss();



                } else {
                    Toast.makeText(getApplicationContext(), "Failed to update: "
                                    + jsonObject.get("error")
                            , Toast.LENGTH_LONG)
                            .show();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Something wrong with the data" +
                        e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

}
