package edu.uw.tacoma.dionmerz.fatms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,
        RegisterFragment.RegisterListener {

    private final static String LOGIN_URL
            = "http://cssgate.insttech.washington.edu/~dionmerz/fatms.php?cmd=login";


    private SharedPreferences mSharedPreferences;
    private RegisterFragment mRegisterFragment;
    private Button mLoginButton;
    private Button mRegisterButton;
    private EditText mUserEmailText;
    private EditText mPwdText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mSharedPreferences =
                getSharedPreferences(getString(R.string.LOGIN_PREFS)
                        , Context.MODE_PRIVATE);


        mUserEmailText = (EditText) findViewById(R.id.email_text);
        mPwdText = (EditText) findViewById(R.id.pass_text);
        mRegisterButton = (Button) findViewById(R.id.register_button);
        mLoginButton = (Button) findViewById(R.id.login_button);
        mRegisterButton.setOnClickListener(this);


        Button signInButton = (Button) findViewById(R.id.login_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                // login(userId, pwd);
            }

        });

    }

    public void login(String userId, String pwd) {

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            try {
                //Check if the login and password are valid
                //new LoginTask().execute(url);


                //Intent i = new Intent(this, CourseActivity.class);
                //startActivity(i);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(this, "No network connection available. Cannot authenticate user",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        mSharedPreferences.edit()
                .putBoolean(getString(R.string.LOGGEDIN), true)
                .commit();


    }


    private String buildLoginURL(View v) {

        StringBuilder sb = new StringBuilder(LOGIN_URL);

        try {

            String userID = mUserEmailText.getText().toString();
            sb.append("id=");
            sb.append(userID);


            String userPwd = mPwdText.getText().toString();
            sb.append("&pwd=");
            sb.append(userPwd);

        } catch (Exception e) {
            Toast.makeText(v.getContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
        return sb.toString();
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.register_button:

                String userId = mUserEmailText.getText().toString();
                String pwd = mPwdText.getText().toString();
                if (TextUtils.isEmpty(userId)) {
                    Toast.makeText(v.getContext(), "Enter userid"
                            , Toast.LENGTH_SHORT)
                            .show();
                    mUserEmailText.requestFocus();
                    return;
                }
                if (!userId.contains("@")) {
                    Toast.makeText(v.getContext(), "Enter a valid email address"
                            , Toast.LENGTH_SHORT)
                            .show();
                    mUserEmailText.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(v.getContext(), "Enter password"
                            , Toast.LENGTH_SHORT)
                            .show();
                    mPwdText.requestFocus();
                    return;
                }
                if (pwd.length() < 6) {
                    Toast.makeText(v.getContext()
                            , "Enter password of at least 6 characters"
                            , Toast.LENGTH_SHORT)
                            .show();
                    mPwdText.requestFocus();
                    return;
                }

                mRegisterFragment = new RegisterFragment();
                Bundle args = new Bundle();
                args.putString("email", userId.toLowerCase());
                args.putString("pwd", pwd);
                mRegisterFragment.setArguments(args);
                FragmentTransaction transaction = getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.activity_login, mRegisterFragment)
                        .addToBackStack(null);

                // Commit the transaction
                transaction.commit();


                break;


        }
    }

    @Override
    public void register(String url) {
        RegisterTask task = new RegisterTask();
        task.execute(new String[]{url.toString()});

// Takes you back to the previous fragment by popping the current fragment out.
        getSupportFragmentManager().popBackStackImmediate();
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
                    response = "Unable to add course, Reason: "
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
                    Toast.makeText(getApplicationContext(), "User successfully registered!"
                            , Toast.LENGTH_LONG)
                            .show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to add: "
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

