package com.gabyquiles.eventy.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.gabyquiles.eventy.R;

public class MainActivity extends AppCompatActivity implements EventListFragment.Callback{
    private final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void showEventDetails(Uri uri) {
        //TODO: Set for tablets
        //TODO: Set for editing

        Log.v(LOG_TAG, "Show Event Details function");
        Intent eventIntent = new Intent(this, EventDetailsActivity.class);
        eventIntent.setData(uri);
        ActivityCompat.startActivity(this, eventIntent, null);
    }
}
