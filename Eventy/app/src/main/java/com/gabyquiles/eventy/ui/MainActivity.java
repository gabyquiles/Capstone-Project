package com.gabyquiles.eventy.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.gabyquiles.eventy.R;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements EventListFragment.Callback {
    private final String LOG_TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.toolbar) Toolbar mToolbar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        showLoginForm();


        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        showLoginForm();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //Logout from the app
        if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            showLoginForm();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showEventDetails(Uri uri) {
        //TODO: Set for tablets
        //TODO: Manage errors, no Net (events may be out of sync), firebase permissions
        Intent eventIntent = new Intent(this, EventDetailsActivity.class);
        eventIntent.setData(uri);
        ActivityCompat.startActivity(this, eventIntent, null);
    }

    private void showLoginForm() {
        if(!isSignedIn()) {
            Intent signinIntent = new Intent(this, SignInActivity.class);
            startActivity(signinIntent);
            finish();
        }
    }

    public boolean isSignedIn() {
        return (mAuth.getCurrentUser() != null);
    }
}
