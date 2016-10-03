package com.gabyquiles.eventy.events;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.gabyquiles.eventy.EventyApplication;
import com.gabyquiles.eventy.R;
import com.gabyquiles.eventy.util.ActivityUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Main activity. Will show a list of events.
 *
 * @author gabrielquiles-perez
 */
public class EventsActivity extends AppCompatActivity {
    private final String LOG_TAG = EventsActivity.class.getSimpleName();

    @Inject EventsPresenter mPresenter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.events_activity);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }

        EventsFragment eventsFragment =
                (EventsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (eventsFragment == null) {
            // Create the fragment
            eventsFragment = EventsFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), eventsFragment, R.id.contentFrame);
        }

        // Create the presenter
        EventyApplication.get(this).getAppComponent()
                .plus(new EventsPresenterModule(eventsFragment)).inject(this);
    }
}
