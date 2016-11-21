package edu.uw.tacoma.dionmerz.fatms;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.net.URLEncoder;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private final static String REGISTER_URL
            = "http://cssgate.insttech.washington.edu/~dionmerz/fatms.php?cmd=register";

    private Bundle mBundle;
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mPhoneNumber;
    private EditText mAddress;
    private EditText mCity;
    private EditText mState;
    private EditText mZip;


    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        final View v = inflater.inflate(R.layout.fragment_register, container, false);

        mFirstName = (EditText) v.findViewById(R.id.edit_text_fname);
        mLastName = (EditText) v.findViewById(R.id.edit_text_lname);
        mPhoneNumber = (EditText) v.findViewById(R.id.edit_text_phone);
        mAddress = (EditText) v.findViewById(R.id.edit_text_address);
        mCity = (EditText) v.findViewById(R.id.edit_text_city);
        mState = (EditText) v.findViewById(R.id.edit_text_state);
        mZip = (EditText) v.findViewById(R.id.edit_text_zip);

        mBundle = savedInstanceState;

        Button register = (Button) v.findViewById(R.id.reg_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              String url = buildRegisterURL(view);


            }
        });


        // Inflate the layout for this fragment
        return v;

    }

    public void register(String url) {
        RegisterTask task = new RegisterTask();
        task.execute(new String[]{url.toString()});

// Takes you back to the previous fragment by popping the current fragment out.
        getActivity().getSupportFragmentManager().popBackStackImmediate();
    }




    private String buildRegisterURL(View v) {

        StringBuilder sb = new StringBuilder(REGISTER_URL);

        try {
            String email = mBundle.getString("email");
            sb.append("email=");
            sb.append(email);

            String pwd = mBundle.getString("pwd");
            sb.append("&pwd=");
            sb.append(pwd);

            String firstName = mFirstName.getText().toString();
            sb.append("&fname=");
            sb.append(firstName);

            String lastName = mLastName.getText().toString();
            sb.append("&lname=");
            sb.append(lastName);

            String phoneNum = mPhoneNumber.getText().toString();
            sb.append("&phone=");
            sb.append(lastName);

            String address = mAddress.getText().toString();
            sb.append("&address=");
            sb.append(address);

            String city = mCity.getText().toString();
            sb.append("&city=");
            sb.append(city);

            String state = mState.getText().toString();
            sb.append("&state=");
            sb.append(state);

            String zip = mZip.getText().toString();
            sb.append("&zip=");
            sb.append(zip);

        }
        catch(Exception e) {
            Toast.makeText(v.getContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
        return sb.toString();
    }






    private class RegisterTask extends AsyncTask<String, Void, String> {


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
                    response = "Unable to register, Reason: "
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
                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                if (status.equals("success")) {
                    Toast.makeText(getActivity().getApplicationContext(), "User successfully registered!"
                            , Toast.LENGTH_LONG)
                            .show();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Failed to add: "
                                    + jsonObject.get("error")
                            , Toast.LENGTH_LONG)
                            .show();
                }
            } catch (JSONException e) {
                Toast.makeText(getActivity().getApplicationContext(), "Something wrong with the data" +
                        e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

}
