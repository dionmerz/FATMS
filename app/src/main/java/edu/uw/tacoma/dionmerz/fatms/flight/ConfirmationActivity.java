package edu.uw.tacoma.dionmerz.fatms.flight;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import edu.uw.tacoma.dionmerz.fatms.R;

public class ConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        String status = "IsRoundTrip";
        if (getIntent().getSerializableExtra("return") == null) {
            status = "IsOneWay";
        }



        TextView tV = (TextView) findViewById(R.id.confirm);
        tV.setText(status);
    }
}
