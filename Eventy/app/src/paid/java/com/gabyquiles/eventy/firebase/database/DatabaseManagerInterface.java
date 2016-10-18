package com.gabyquiles.eventy.firebase.database;

import com.google.firebase.database.Query;

/**
 * Describes functions to interact with FirebaseDatabase
 *
 * @author gabrielquiles-perez
 */

public interface DatabaseManagerInterface {

    Query getEventsList();
    Query getEvent();
}
