package com.gabyquiles.eventy.firebase.database;

import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Describes functions to interact with FirebaseDatabase
 *
 * @author gabrielquiles-perez
 */

public interface DatabaseManagerInterface {

    Query getEventsList() throws Exception;
    void getEvent(String key, ValueEventListener listener);
}
