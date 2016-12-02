package edu.uw.tacoma.dionmerz.fatms.flight;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import edu.uw.tacoma.dionmerz.fatms.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RoundTripTabFragment extends Fragment {


    private static final String FORMAT = "MM/dd/yy";
    private Calendar myCalendar = Calendar.getInstance();


    private final SimpleDateFormat myDateFormater = new SimpleDateFormat(FORMAT, Locale.US);

    public RoundTripTabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_round_trip_tab, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        final TextView departDateText = (TextView) view.findViewById(R.id.textView_depart_date_round_trip);


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

        Button departDateButton = (Button) view.findViewById(R.id.button_departure_date_round_trip);

        departDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), R.style.DateDialogTheme,
                        dateDialog, myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));



                datePickerDialog.show();


            }
        });

        final TextView returnDateText = (TextView) view.findViewById(R.id.textView_return_date_round_trip);

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


        Button returnDateButton = (Button) view.findViewById(R.id.button_return_date_round_trip);

        returnDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), R.style.DateDialogTheme,
                        returnDateDialog, myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));



                datePickerDialog.show();


            }
        });

        // Inflate the layout for this fragment
        return view;
    }

}
