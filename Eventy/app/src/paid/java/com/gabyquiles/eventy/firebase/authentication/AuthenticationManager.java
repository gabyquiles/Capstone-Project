package com.gabyquiles.eventy.firebase.authentication;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Manages all interaction with Firebase Auth
 *
 * @author gabrielquiles-perez
 */
public class AuthenticationManager {
    private final String LOG_TAG = AuthenticationManager.class.getSimpleName();

    private FirebaseAuth mAuth;

    @Inject
    @Singleton
    public AuthenticationManager() {
        mAuth = FirebaseAuth.getInstance();
    }

    public boolean isSignedIn() {
        return (mAuth.getCurrentUser() != null);
    }

    public FirebaseUser getUser() {
        return mAuth.getCurrentUser();
    }

}
