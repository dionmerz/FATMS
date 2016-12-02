package edu.uw.tacoma.dionmerz.fatms.flight;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import edu.uw.tacoma.dionmerz.fatms.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RoundTripTabFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private final static String URL
            = "https://students.washington.edu/jwolf059/FATMS.php?cmd=airports";
    private Spinner mDepartureSpinner;
    private Spinner mArrivalSpinner;
    private ArrayList<String> mAirportList;
    private View mView;
    private String mDepartureAP;
    private String mArrivalAP;
    private String mDate;
    private String mReturnDate;
    private static final String FORMAT = "yyyy/MM/dd";
    private Calendar myCalendar = Calendar.getInstance();

    private final SimpleDateFormat myDateFormater = new SimpleDateFormat(FORMAT, Locale.US);

    public RoundTripTabFragment() {
        mAirportList = new ArrayList<>();
        mAirportList.add("Select Airport");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_round_trip_tab, container, false);


        try {
            RoundTripTabFragment.AirportTaskOutgoing task = new RoundTripTabFragment.AirportTaskOutgoing();
            String result = task.execute(URL).get();
        } catch (Exception e) {
            Log.e("OneWayTab: ", e.getMessage());
        }

        // Build the Airport list spinner
        mDepartureSpinner = (Spinner) mView.findViewById(R.id.departure_spinner_round);
        mDepartureSpinner.setOnItemSelectedListener(this);


        ArrayAdapter<String> airports = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, mAirportList);
        airports.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDepartureSpinner.setAdapter(airports);
        mDepartureSpinner.setSelection(0);

        // Build the pub list spinner
        mArrivalSpinner = (Spinner) mView.findViewById(R.id.arrival_spinner_round);
        mArrivalSpinner.setOnItemSelectedListener(this);


        //ArrayAdapter<String> airports = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, mAirportList);
        airports.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mArrivalSpinner.setAdapter(airports);
        mArrivalSpinner.setSelection(0);


        final TextView departDateText = (TextView) mView.findViewById(R.id.textView_depart_date_round_trip);


        final DatePickerDialog.OnDateSetListener dateDialog = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                departDateText.setText(myDateFormater.format(myCalendar.getTime()));


            }

        };

        Button departDateButton = (Button) mView.findViewById(R.id.button_departure_date_round_trip);

        departDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), R.style.DateDialogTheme,
                        dateDialog, myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));



                datePickerDialog.show();


            }
        });

        final TextView returnDateText = (TextView) mView.findViewById(R.id.textView_return_date_round_trip);

        final DatePickerDialog.OnDateSetListener returnDateDialog = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                returnDateText.setText(myDateFormater.format(myCalendar.getTime()));


            }

        };


        Button returnDateButton = (Button) mView.findViewById(R.id.button_return_date_round_trip);

        returnDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), R.style.DateDialogTheme,
                        returnDateDialog, myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));



                datePickerDialog.show();


            }
        });

        Button bt = (Button) mView.findViewById(R.id.button_search_round_trip);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDate = (String) departDateText.getText();
                mReturnDate = (String) returnDateText.getText();
                Intent i = new Intent(getActivity().getApplication(), FlightResultActivity.class);
                i.putExtra("depart", mDepartureAP);
                i.putExtra("arrive", mArrivalAP);
                i.putExtra("date", mDate);
                i.putExtra("return", mReturnDate);


                startActivity(i);

            }
        });

        return mView;
    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView.getId() == R.id.arrival_spinner_round) {
            mArrivalAP = mAirportList.get(i);
        } else if(adapterView.getId() == R.id.departure_spinner_round) {
            mDepartureAP = mAirportList.get(i);
        }

        System.out.println(mArrivalAP);
        System.out.println(mDepartureAP);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public class AirportTaskOutgoing extends AsyncTask<String, Void, String> {


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
                    JSONObject name = jsonArray.getJSONObject(i);
                    mAirportList.add(name.getString("name"));

                }


            } catch (JSONException e) {
                Toast.makeText(getActivity().getApplicationContext(), "Something wrong with the data" +
                        e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

    }

}
