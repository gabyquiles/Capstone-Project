package com.gabyquiles.eventy.events;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.gabyquiles.eventy.EventyApplication;
import com.gabyquiles.eventy.R;
import com.gabyquiles.eventy.data.DataModule;
import com.gabyquiles.eventy.model.Event;
import com.gabyquiles.eventy.util.ActivityUtils;
import com.google.firebase.analytics.FirebaseAnalytics;

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
//      TODO: Move to presenter
//        showLoginForm();

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
        DaggerEventsComponent.builder()
                .dataModule(new DataModule(this, getSupportLoaderManager()))
                .eventsPresenterModule(new EventsPresenterModule(eventsFragment)).build()
                .inject(this);
//        mPresenter.setLoaderManager(getSupportLoaderManager());

//        TODO: This should be here????
        FirebaseAnalytics firebaseAnalytics = ((EventyApplication) getApplication()).getAnalytics();
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, LOG_TAG);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Event List");

        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }
}
