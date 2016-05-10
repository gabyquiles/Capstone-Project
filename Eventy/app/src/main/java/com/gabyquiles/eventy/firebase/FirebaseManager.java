package com.gabyquiles.eventy.firebase;

import android.net.Uri;

import com.firebase.client.ChildEventListener;
import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;
import com.gabyquiles.eventy.model.Event;

/**
 * Serve as the manager for Firebase. A way to abstract Firebase interactions
 *
 * @author gabrielquiles-perez
 */
public class FirebaseManager {
    private final String LOG_TAG = FirebaseManager.class.getSimpleName();

    public static final String EVENTY_BASE_URL = "https://eventy.firebaseio.com";
    public static final String EVENTS_PATH = "/events/";
    public static final Uri EVENTS_URI = Uri.parse(EVENTY_BASE_URL + EVENTS_PATH);

    private Firebase mFirebaseClient;

    public FirebaseManager(String firebase_url) {
        if(firebase_url == null) {
            firebase_url = EVENTY_BASE_URL + EVENTS_PATH;
        }
        mFirebaseClient = new Firebase(firebase_url);
    }

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

    public Event save(Event event){
        String key = event.getKey();
        mFirebaseClient = mFirebaseClient.getRoot().child(EVENTS_PATH);
        if(key == null) {
            mFirebaseClient = mFirebaseClient.push();
            mFirebaseClient.setValue(event);
            event.setKey(mFirebaseClient.getKey());
        } else {
            mFirebaseClient = mFirebaseClient.child(key);
            mFirebaseClient.setValue(event);
        }
        return event;
    }
}
