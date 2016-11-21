package edu.uw.tacoma.dionmerz.fatms;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URLEncoder;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private final static String REGISTER_URL
            = "http://cssgate.insttech.washington.edu/~dionmerz/fatms.php?cmd=register";

    private RegisterListener mListener;
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


        View v = inflater.inflate(R.layout.fragment_register, container, false);

        mFirstName = (EditText) v.findViewById(R.id.edit_text_fname);
        mLastName = (EditText) v.findViewById(R.id.edit_text_lname);
        mPhoneNumber = (EditText) v.findViewById(R.id.edit_text_phone);
        mAddress = (EditText) v.findViewById(R.id.edit_text_address);
        mCity = (EditText) v.findViewById(R.id.edit_text_city);
        mState = (EditText) v.findViewById(R.id.edit_text_state);
        mZip = (EditText) v.findViewById(R.id.edit_text_zip);


        // Inflate the layout for this fragment
        return v;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RegisterListener) {
            mListener = (RegisterListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement RegisterListener");
        }
    }



    private String buildRegisterURL(View v) {

        StringBuilder sb = new StringBuilder(REGISTER_URL);

        try {

            String firstName = mFirstName.getText().toString();
            sb.append("fname=");
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

    public interface RegisterListener {
        public void register(String url);
    }

}
