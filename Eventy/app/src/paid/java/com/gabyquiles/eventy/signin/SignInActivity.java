package com.gabyquiles.eventy.signin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import com.gabyquiles.eventy.R;
import com.gabyquiles.eventy.ui.MainActivity;
import com.gabyquiles.eventy.ui.SignUpActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Login user into Eventy
 *
 * @author gabrielquiles-perez
 */
public class SignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        FirebaseAuth.AuthStateListener, OnCompleteListener<AuthResult> {
    private final String LOG_TAG = SignInActivity.class.getSimpleName();
    private final int RC_GOOGLE_SIGN_IN = 1;
    private final int RC_SIGN_UP = 2;

    @BindView(R.id.email_textview) EditText mEmailEdit;
    @BindView(R.id.password_textview) EditText mPasswordEdit;

    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;

    private String mEmail;
    private String mPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth != null && mAuth.getCurrentUser() != null) {
            startMainActivity();
        } else {
            // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            // Build a GoogleApiClient with access to the Google Sign-In API and the
// options specified by gso.
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.stopAutoManage(this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            // User is signed in
            Log.d(LOG_TAG, "onAuthStateChanged:signed_in:" + user.getUid());
        } else {
            // User is signed out
            Log.d(LOG_TAG, "onAuthStateChanged:signed_out");
        }
    }

    @OnClick(R.id.login_btn)
    public void emailLogin() {
        if(validated() && mEmail != null && mPassword != null) {
            mAuth.signInWithEmailAndPassword(mEmail, mPassword).addOnCompleteListener(this, this);
        }
    }

    @OnClick(R.id.google_sign_in_button)
    public void signIn() {
        Intent googleSignInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(googleSignInIntent, RC_GOOGLE_SIGN_IN);
    }

    @OnClick(R.id.signup_link)
    public void signUp() {
        Intent signUpIntent = new Intent(this, SignUpActivity.class);
        startActivity(signUpIntent);
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        // If sign in fails, display a message to the user. If sign in succeeds
        // the auth state listener will be notified and logic to handle the
        // signed in user can be handled in the listener.
        if (!task.isSuccessful()) {
            authenticationFailed(task);
        } else {
            startMainActivity();
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            firebaseAuthWithGoogle(acct);
        } else {
            // Google Sign In failed, update UI appropriately
            // ...
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, this);
    }

    private void startMainActivity() {
        Intent listingIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(listingIntent);
        finish();
    }

    private void authenticationFailed(Task<AuthResult> task) {
        Log.w(LOG_TAG, "signInWithCredential", task.getException());
        Toast.makeText(getApplicationContext(), getString(R.string.authentication_error),
                Toast.LENGTH_SHORT).show();
    }

    private boolean validated() {
        boolean result = true;
        mEmail = mEmailEdit.getText().toString();
        mPassword  = mPasswordEdit.getText().toString();
        if(mEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
            mEmailEdit.setError(getString(R.string.invalid_email_error));
            result = false;
        }

        if(mPassword.isEmpty()) {
            mPasswordEdit.setError(getString(R.string.empty_password));
            result = false;
        }
        return result;
    }
}
