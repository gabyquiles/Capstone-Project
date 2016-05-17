package com.gabyquiles.eventy.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.ui.auth.core.AuthProviderType;
import com.firebase.ui.auth.core.FirebaseLoginBaseActivity;
import com.firebase.ui.auth.core.FirebaseLoginError;
import com.gabyquiles.eventy.R;
import com.gabyquiles.eventy.Utility;

public class MainActivity extends FirebaseLoginBaseActivity implements EventListFragment.Callback {
    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private Firebase mFirebaseRef;
    private String mFirebaseUrl;
    private boolean mLoginFormShowed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set Base url for Firebase
        mFirebaseUrl = Utility.getFirebaseBaseUrl(this, null);
        mFirebaseRef = new Firebase(mFirebaseUrl);

        setContentView(R.layout.activity_main);
        fillContents();
    }

    @Override
    public void showEventDetails(Uri uri) {
        //TODO: Set for tablets
        //TODO: Manage errors, no Net (events may be out of sync), firebase permissions

        if(uri == null) {
            uri = Uri.parse(mFirebaseUrl);
        }

        Log.v(LOG_TAG, "Show Event Details function");
        Intent eventIntent = new Intent(this, EventDetailsActivity.class);
        eventIntent.setData(uri);
        ActivityCompat.startActivity(this, eventIntent, null);
    }


    @Override
    protected void onStart() {
        super.onStart();
        setEnabledAuthProvider(AuthProviderType.GOOGLE);

        if(mFirebaseRef.getAuth() == null) {
            showFirebaseLoginPrompt();
            mLoginFormShowed = true;
        }
    }

    @Override
    protected Firebase getFirebaseRef() {
        return mFirebaseRef;
    }

    @Override
    protected void onFirebaseLoginProviderError(FirebaseLoginError firebaseLoginError) {

    }

    @Override
    protected void onFirebaseLoginUserError(FirebaseLoginError firebaseLoginError) {

    }

    @Override
    protected void onFirebaseLoggedIn(AuthData authData) {
        if(mLoginFormShowed) {
            dismissFirebaseLoginPrompt();
            mLoginFormShowed = false;
        }
        //Once logged in, generate the url with user id
        mFirebaseUrl = Utility.getFirebaseBaseUrl(this, authData);
        mFirebaseRef = new Firebase(mFirebaseUrl);
        fillContents();
    }

    private void fillContents() {
        if(getAuth() != null) {
            Bundle bundle = new Bundle();
            Uri firebaseUri = Uri.parse(Utility.getFirebaseBaseUrl(this, getAuth()));
            bundle.putParcelable(EventListFragment.FIREBASE_URI, firebaseUri);
            // set Fragmentclass Arguments
            EventListFragment eventListFragment = new EventListFragment();
            eventListFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.first_container, eventListFragment)
                    .commit();
        }
    }
}
