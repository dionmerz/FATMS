package edu.uw.tacoma.dionmerz.fatms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import edu.uw.tacoma.dionmerz.fatms.history.HistoryActivity;

public class FinishedActivity extends AppCompatActivity {

    SharedPreferences mSharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished);

        mSharedPreferences =
                getSharedPreferences(getString(R.string.LOGIN_PREFS)
                        , Context.MODE_PRIVATE);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        TextView tripID = (TextView) findViewById(R.id.trip_id_purchase);
        tripID.setText(getIntent().getStringExtra("tripid"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case R.id.logout_menu:

                SharedPreferences.Editor edit = mSharedPreferences.edit();

                edit.putBoolean(getString(R.string.LOGGEDIN), false);
                edit.putString(getString(R.string.current_user), "");
                edit.apply();

                Intent i = new Intent(this, LoginActivity.class);

                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(i);

                finish();
                break;

            case R.id.edit_account_menu:

                Intent edit_intent = new Intent(this, EditActivity.class);
                startActivity(edit_intent);


                break;

            case R.id.history_menu:
                Intent histIntent = new Intent(getApplicationContext(), HistoryActivity.class);
                startActivity(histIntent);
                break;


            default:
                return super.onOptionsItemSelected(item);
        }

        return false;
    }
}
