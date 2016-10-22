package com.gabyquiles.eventy.firebase.authentication;

import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Manages all interaction with Firebase Auth
 *
 * @author gabrielquiles-perez
 */
public class AuthenticationManager implements AuthenticationManagerInterface, FirebaseAuth.AuthStateListener{
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

    public void signOut() {
        mAuth.signOut();
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

    }

    @Override
    public void emailSignIn(String email, String password, OnCompleteListener listener) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(listener);
    }

    @Override
    public void signIn(GoogleSignInAccount account, OnCompleteListener listener) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(listener);
    }
}
