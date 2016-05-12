package com.gabyquiles.eventy.firebase;

import android.net.Uri;

import com.firebase.client.ChildEventListener;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

/**
 * Serve as the base class for Firebase interactions.
 *
 * @author gabrielquiles-perez
 */
public abstract class FirebaseManager {
    private final String LOG_TAG = FirebaseManager.class.getSimpleName();

    public static final String EVENTY_BASE_URL = "https://eventy.firebaseio.com";
    public static final String EVENTS_PATH = "/events/";
    public static final Uri EVENTS_URI = Uri.parse(EVENTY_BASE_URL + EVENTS_PATH);

    protected Query mFirebaseClient;

    public void addValueEventListener(ValueEventListener listener) {
        mFirebaseClient.addValueEventListener(listener);
    }

    public void removeEventListener(ValueEventListener listener) {
        mFirebaseClient.removeEventListener(listener);
    }

    public void addChildEventListener(ChildEventListener listener) {
        mFirebaseClient.addChildEventListener(listener);
    }

    public void removeEventListener(ChildEventListener listener) {
        mFirebaseClient.removeEventListener(listener);
    }
}
