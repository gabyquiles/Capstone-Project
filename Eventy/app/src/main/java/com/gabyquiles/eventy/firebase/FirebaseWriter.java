package com.gabyquiles.eventy.firebase;

import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;
import com.gabyquiles.eventy.model.Event;

/**
 * Manages the writes to Firebase, insertion and updates of events.
 *
 * @author gabrielquiles-perez
 */
public class FirebaseWriter extends FirebaseManager {
    private final String LOG_TAG = FirebaseWriter.class.getSimpleName();

    protected Firebase mFirebaseClient;

    public FirebaseWriter(String firebase_url) {
        if(firebase_url == null) {
            firebase_url = FirebaseManager.EVENTY_BASE_URL + FirebaseManager.EVENTS_PATH;
        }
        mFirebaseClient = new Firebase(firebase_url);
    }

    public void addValueEventListener(ValueEventListener listener) {
        mFirebaseClient.addValueEventListener(listener);
    }

    public void removeEventListener(ValueEventListener listener) {
        mFirebaseClient.removeEventListener(listener);
    }

    public Event save(Event event){
        String key = event.getKey();
        mFirebaseClient = mFirebaseClient.getRoot().child(FirebaseManager.EVENTS_PATH);
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
