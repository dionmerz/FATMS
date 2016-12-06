package edu.uw.tacoma.dionmerz.fatms;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class EditActivity extends AppCompatActivity {

    private final static String UPDATE_URL
            = "https://students.washington.edu/jwolf059/FATMS.php?cmd=update";

    SharedPreferences mSharedPreferences;

    private EditText mPhoneNumber;
    private EditText mAddress;
    private EditText mCity;
    private EditText mState;
    private EditText mZip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        mSharedPreferences =
                getSharedPreferences(getString(R.string.LOGIN_PREFS)
                        , Context.MODE_PRIVATE);


        TextView current_email = (TextView) findViewById(R.id.update_current_email);

        String email = mSharedPreferences.getString(getString(R.string.current_user),"");
        current_email.setText("User: " + email);
        Button updateButton = (Button) findViewById(R.id.update_account_button);
        Button cancelButton = (Button) findViewById(R.id.cancel_update_button);


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mPhoneNumber = (EditText) findViewById(R.id.edit_text_phone);
                mAddress = (EditText) findViewById(R.id.edit_text_address);
                mCity = (EditText) findViewById(R.id.edit_text_city);
                mState = (EditText) findViewById(R.id.edit_text_state);
                mZip = (EditText) findViewById(R.id.edit_text_zip);

                String url = buildUpdateURL(view);
                update(url);

                Toast.makeText(getApplicationContext(), "Update Successful", Toast.LENGTH_LONG).show();

                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }



    private String buildUpdateURL(View v) {

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

    }
}
