package com.gabyquiles.eventy.firebase.database;

import android.content.Context;

import com.gabyquiles.eventy.R;
import com.gabyquiles.eventy.firebase.authentication.AuthenticationManager;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Calendar;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Manages all Firebase Database interaction
 *
 * @author gabrielquiles-perez
 */
public class DatabaseManager implements DatabaseManagerInterface {
    private final String LOG_TAG = DatabaseManager.class.getSimpleName();

    private Context mContext;
    private AuthenticationManager mAuthManager;
    private FirebaseDatabase mDatabase;

    @Inject
    @Singleton
    public DatabaseManager(Context context, AuthenticationManager authManager) {
        mContext = context;
        mAuthManager = authManager;
        mDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    public Query getEventsList() {
        long today = Calendar.getInstance().getTimeInMillis();

        FirebaseUser user = mAuthManager.getUser();

        if (user != null) {
            DatabaseReference dbRef = mDatabase.getReference().child(mContext.getString(R.string.firebase_users_path));
            dbRef = dbRef.child(user.getUid()).child("events");
//            TODO: uncomment the startAt to show only the future events
            return dbRef.orderByChild("date");//.startAt(today);
        } else {
//            TODO: throw not logged exception
        }
//        TODO: This can be removed once the exception has been added
        return null;
    }

    @Override
    public Query getEvent() {
        return null;
    }
}
