package com.gabyquiles.eventy.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import com.gabyquiles.eventy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Create a new account using email and password
 *
 * @author gabrielquiles-perez
 */
public class SignUpActivity extends AppCompatActivity {
    private final String LOG_TAG = SignUpActivity.class.getSimpleName();
    @BindView(R.id.email_textview) EditText mEmailEdit;
    @BindView(R.id.password_textview) EditText mPasswordEdit;
    @BindView(R.id.confirm_password_textview) EditText mConfirmPasswordEdit;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private String mEmail;
    private String mPassword;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
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
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @OnClick(R.id.create_account_btn)
    public void createAccount() {
        if(validated()) {
            mAuth.createUserWithEmailAndPassword(mEmail, mPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this, getString(R.string.cannot_create_account),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Intent mainActivityIntent = new Intent(SignUpActivity.this, MainActivity.class);
                                startActivity(mainActivityIntent);
                                finish();
                            }
                        }
                    });
        }
    }

    @OnClick(R.id.login_link)
    public void login() {
//        Go back to login screen.
        finish();
    }

    private boolean validated() {
        boolean result = true;
        mEmail = mEmailEdit.getText().toString();
        mPassword  = mPasswordEdit.getText().toString();
        String confirmation = mConfirmPasswordEdit.getText().toString();
        if(mEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
            mEmailEdit.setError(getString(R.string.invalid_email_error));
            result = false;
        }

        if(mPassword.isEmpty() || mPassword.length() < 5) {
            mPasswordEdit.setError(getString(R.string.invalid_password));
            result = false;
        } else if(!mPassword.equals(confirmation)) {
            mPasswordEdit.setError(getString(R.string.password_mismatch_error));
            result = false;
        }
        return result;
    }
}
