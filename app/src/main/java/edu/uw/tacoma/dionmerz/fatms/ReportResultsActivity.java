package edu.uw.tacoma.dionmerz.fatms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ReportResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_results);



        String title_extra = getIntent().getStringExtra("title");
        String people = getIntent().getStringExtra("report");

        TextView list = (TextView) findViewById(R.id.textView_reports);
        TextView title = (TextView) findViewById(R.id.textView_title_report);

        title.setText(title_extra);
        list.setText(people);

    }
}
