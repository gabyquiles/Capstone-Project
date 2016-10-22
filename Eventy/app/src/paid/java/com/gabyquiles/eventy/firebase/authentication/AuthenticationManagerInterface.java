package com.gabyquiles.eventy.firebase.authentication;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseUser;

/**
 * Describes functions to interact with FirebaseAuth
 *
 * @author gabrielquiles-perez
 */

public interface AuthenticationManagerInterface {
    boolean isSignedIn();
    FirebaseUser getUser();
    void signOut();
    void emailSignIn(String email, String password, OnCompleteListener listener);
    void signIn(GoogleSignInAccount account, OnCompleteListener listener);
}
