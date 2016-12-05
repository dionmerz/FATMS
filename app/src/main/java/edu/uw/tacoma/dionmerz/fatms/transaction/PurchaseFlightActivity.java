package edu.uw.tacoma.dionmerz.fatms.transaction;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import edu.uw.tacoma.dionmerz.fatms.R;

public class PurchaseFlightActivity extends AppCompatActivity {

    private static final String TRIPID_URL = "https://students.washington.edu/jwolf059/FATMS.php?cmd=tripid&email=";
    private static final String USER_INFO = "https://students.washington.edu/jwolf059/FATMS.php?cmd=userinfo&email=";
    private String mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_flight);

        TextView tv = (TextView) findViewById(R.id.show_details);
        InfoTask it = new InfoTask();
        String first = USER_INFO + mEmail;
        String second = TRIPID_URL + mEmail;
        it.execute(first, second);


        




        HashMap<Integer, ArrayList<String>> mSelection = (HashMap<Integer, ArrayList<String>>) getIntent().getSerializableExtra("selections");
        StringBuilder sb = new StringBuilder();
        Set<Integer> keys = mSelection.keySet();
        for (Integer i : keys) {
            sb.append("Flight #"+ i + " Seat Selection: ");
            ArrayList<String> list = mSelection.get(i);
            sb.append(list.get(0) + " Meal Option: ");
            sb.append(list.get(1) + "/n");
        }
        tv.setText(sb.toString());
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

                    Log.d("inBackGround: ", response);
                }
            }
                return response;


        }
    }
}
