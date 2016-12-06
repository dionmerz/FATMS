package edu.uw.tacoma.dionmerz.fatms;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class FinishedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished);

        TextView tripID = (TextView) findViewById(R.id.trip_id_purchase);
        tripID.setText(getIntent().getStringExtra("tripid"));
    }
}
