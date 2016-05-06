package com.gabyquiles.eventy.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gabyquiles.eventy.R;

public class EventDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        // If this activity is newly created
        if(savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(EventDetailsActivityFragment.EVENT_URI, getIntent().getData());

            EventDetailsActivityFragment fragment = new EventDetailsActivityFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().add(R.id.details_container, fragment)
                    .commit();

        }
    }

}
