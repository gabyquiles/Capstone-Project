package com.gabyquiles.eventy.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gabyquiles.eventy.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements EventListFragment.Callback, SignInFragment.LoginInterface {
    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_main);
//        showLoginForm();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!isSignedIn()) {
            showLoginForm();
        } else {
            signedIn();
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

    @Override
    public void signedIn() {
        EventListFragment eventListFragment = new EventListFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.first_container, eventListFragment)
                .commit();
    }

    private void showLoginForm() {
        SignInFragment signInFragment = new SignInFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.first_container, signInFragment)
                .commit();
    }

    public boolean isSignedIn() {
//        return (mAuth.getCurrentUser() != null);
        //TODO: Setup login
        return true;
    }
}
