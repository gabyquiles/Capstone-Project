package com.gabyquiles.eventy.firebase.authentication;

import com.google.firebase.auth.FirebaseUser;

/**
 * Describes functions to interact with FirebaseAuth
 *
 * @author gabrielquiles-perez
 */

public interface AuthenticationManagerInterface {
    boolean isSignedIn();
    FirebaseUser getUser();
}
