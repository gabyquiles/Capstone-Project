package com.gabyquiles.eventy.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.gabyquiles.eventy.R;
import com.google.firebase.auth.FirebaseAuth;

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
    }

    @Override
    public void showEventDetails(Uri uri) {
        //TODO: Set for tablets
        //TODO: Manage errors, no Net (events may be out of sync), firebase permissions
        Intent eventIntent = new Intent(this, EventDetailsActivity.class);
        eventIntent.setData(uri);
        ActivityCompat.startActivity(this, eventIntent, null);
    }
}
