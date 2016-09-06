package com.gabyquiles.eventy.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.gabyquiles.eventy.EventyApplication;
import com.gabyquiles.eventy.R;
import com.google.firebase.analytics.FirebaseAnalytics;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements EventListFragment.Callback {
    private final String LOG_TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.toolbar) Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showLoginForm();

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }
//        FirebaseAnalytics firebaseAnalytics = ((EventyApplication) getApplication()).getAnalytics();
//        Bundle bundle = new Bundle();
//        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, LOG_TAG);
//        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Event List");
//
//        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    @Override
    public void showEventDetails(Uri uri) {
        //TODO: Set for tablets
        //TODO: Manage errors, no Net (events may be out of sync), firebase permissions
        Intent eventIntent = new Intent(this, EventDetailsActivity.class);

        eventIntent.setData(uri);
        ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this);
        ActivityCompat.startActivity(this, eventIntent, activityOptions.toBundle());
    }
}
