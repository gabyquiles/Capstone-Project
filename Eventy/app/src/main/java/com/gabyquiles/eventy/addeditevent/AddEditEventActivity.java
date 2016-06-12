package com.gabyquiles.eventy.addeditevent;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.gabyquiles.eventy.R;
import com.gabyquiles.eventy.data.DataModule;
import com.gabyquiles.eventy.util.ActivityUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddEditEventActivity extends AppCompatActivity {
    public static final String EXTRA_EVENT_ID = "EVENT_ID";

    @BindView(R.id.toolbar) Toolbar mToolbar;

    @Inject AddEditEventPresenter mPresenter;

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

        AddEditEventFragment addEditEventFragment =
                (AddEditEventFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        String eventId = null;
        if (addEditEventFragment == null) {
            addEditEventFragment = AddEditEventFragment.newInstance();
            if(getIntent().hasExtra(AddEditEventFragment.ARGUMENT_EDIT_EVENT_ID)) {
                eventId = getIntent().getStringExtra(
                        AddEditEventFragment.ARGUMENT_EDIT_EVENT_ID);
                Bundle bundle = new Bundle();
                bundle.putString(AddEditEventFragment.ARGUMENT_EDIT_EVENT_ID, eventId);
                addEditEventFragment.setArguments(bundle);
            }

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                addEditEventFragment, R.id.contentFrame);
        }

        // Create the presenter
        DaggerAddEditEventComponent.builder()
                .dataModule(new DataModule(this, getSupportLoaderManager()))
                .addEditEventPresenterModule(
                        new AddEditEventPresenterModule(addEditEventFragment, eventId)).build()
                .inject(this);
    }

//    @OnClick(R.id.save_button)
//    public void save() {
//        Intent mainIntent = new Intent(this, MainActivity.class);
//        startActivity(mainIntent);
//        mFragment.save();
//        finish();
//    }
}
