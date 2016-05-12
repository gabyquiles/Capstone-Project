package com.gabyquiles.eventy.firebase;

import com.firebase.client.Firebase;

import java.util.Calendar;

/**
 * Manage the reads of multiple events
 *
 * @author gabrielquiles-perez
 */
public class FirebaseReader extends FirebaseManager {
    private final String LOG_TAG = FirebaseReader.class.getSimpleName();

    public FirebaseReader(String firebase_url) {
        if(firebase_url == null) {
            firebase_url = FirebaseManager.EVENTY_BASE_URL + FirebaseManager.EVENTS_PATH;
        }
        // Order the listing by date and filter out old events
        long today = Calendar.getInstance().getTimeInMillis();
        mFirebaseClient = new Firebase(firebase_url).orderByChild("date").startAt(today);
    }
}
