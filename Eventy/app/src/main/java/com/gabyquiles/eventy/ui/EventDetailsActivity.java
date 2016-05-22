package com.gabyquiles.eventy.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.gabyquiles.eventy.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventDetailsActivity extends BaseActivity {
    @BindView(R.id.toolbar) Toolbar mToolbar;
    EventDetailsFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        ButterKnife.bind(this);

        // If this activity is newly created
        if(savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(EventDetailsFragment.EVENT_URI, getIntent().getData());

            mFragment = new EventDetailsFragment();
            mFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().add(R.id.details_container, mFragment)
                    .commit();
        }

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    @OnClick(R.id.save_button)
    public void save() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
        mFragment.save();
        finish();
    }
}
