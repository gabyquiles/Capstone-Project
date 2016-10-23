package com.gabyquiles.eventy.firebase.database;

import android.content.Context;

import com.gabyquiles.eventy.R;
import com.gabyquiles.eventy.firebase.authentication.AuthenticationManager;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
    public Query getEventsList() throws Exception {
        long today = Calendar.getInstance().getTimeInMillis();

        DatabaseReference dbRef = userEventsReference();

        if (dbRef != null) {
//            TODO: uncomment the startAt to show only the future events
            return dbRef.orderByChild("date");//.startAt(today);
        } else {
//            TODO: throw not logged exception
            throw new Exception("Weba");
        }
    }

    @Override
    public void getEvent(String key, ValueEventListener listener) {
        DatabaseReference dbRef = userEventsReference();
        dbRef.child(key).addValueEventListener(listener);
    }

    private DatabaseReference userEventsReference() {
        FirebaseUser user = mAuthManager.getUser();
        if(user != null) {
            DatabaseReference dbRef = mDatabase.getReference().child(mContext.getString(R.string.firebase_users_path));
            dbRef = dbRef.child(user.getUid()).child("events");
            return dbRef;
        }
        return null;
    }
}
