package com.gabyquiles.eventy.addeditevent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.gabyquiles.eventy.EventyApplication;
import com.gabyquiles.eventy.R;
import com.gabyquiles.eventy.events.EventsActivity;
import com.gabyquiles.eventy.util.ActivityUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//TODO: Create EventsPresenter in Paid Version
public class AddEditEventActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar mToolbar;

    @Inject AddEditEventPresenter mPresenter;

    private AddEditEventFragment mAddEditEventFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        mAddEditEventFragment =
                (AddEditEventFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        String eventId = null;
        if (mAddEditEventFragment == null) {
            mAddEditEventFragment = AddEditEventFragment.newInstance();
            if(getIntent().hasExtra(AddEditEventFragment.ARGUMENT_EDIT_EVENT_ID)) {
                eventId = getIntent().getStringExtra(
                        AddEditEventFragment.ARGUMENT_EDIT_EVENT_ID);
                Bundle bundle = new Bundle();
                bundle.putString(AddEditEventFragment.ARGUMENT_EDIT_EVENT_ID, eventId);
                mAddEditEventFragment.setArguments(bundle);
            }

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                mAddEditEventFragment, R.id.contentFrame);
        }

        // Create the presenter
        EventyApplication.get(this).getAppComponent()
                .plus(new AddEditEventPresenterModule(mAddEditEventFragment, eventId)).inject(this);
    }

    @OnClick(R.id.save_button)
    public void save() {
        Intent mainIntent = new Intent(this, EventsActivity.class);
        startActivity(mainIntent);
        mAddEditEventFragment.saveEvent();
        finish();
    }
}
