package com.gabyquiles.eventy.signin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Patterns;

import com.gabyquiles.eventy.GoogleApiClient.ApiClientManager;
import com.gabyquiles.eventy.analytics.AnalyticsManager;
import com.gabyquiles.eventy.firebase.authentication.AuthenticationManager;
import com.gabyquiles.eventy.firebase.authentication.AuthenticationManagerInterface;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;

import javax.inject.Inject;

/**
 * Description
 *
 * @author gabrielquiles-perez
 */
public class SignInPresenter implements SignInContract.Presenter {
    private final String LOG_TAG = SignInPresenter.class.getSimpleName();

    private SignInContract.View mView;
    private AuthenticationManagerInterface mAuth;
    private ApiClientManager mApiManager;
    private OnCompleteListener mLoginListener;


    @Inject
    public SignInPresenter(@NonNull SignInContract.View signInView,
                           @NonNull AnalyticsManager analytics, @NonNull AuthenticationManager auth) {
        mView = signInView;
        mAuth = auth;
        mApiManager = new ApiClientManager((FragmentActivity) mView, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//                TODO: Manage this
            }
        });
        mLoginListener = new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful()) {
                    try{
                        throw task.getException();
                    } catch(FirebaseNetworkException failedNetwork) {
                        mView.networkProblems();
                    } catch (Exception e) {
                        mView.authenticationFailed();
                    }

                } else {
                    mView.startEventsActivity();
                }
            }
        };
    }

    @Override
    public void start() {
        if (mAuth.isSignedIn()) {
            mView.startEventsActivity();
        } else {
            mApiManager.initiate();
        }
    }

    public void destroy() {
        mApiManager.destroy();
    }

    public void emailSignIn(String email, String password) {
        if(validate(email, password)) {
            mAuth.emailSignIn(email, password, mLoginListener);
        }

    }

    @Override
    public void result(int requestCode, int resultCode, Intent data) {
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == SignInActivity.RC_GOOGLE_SIGN_IN) {
            mApiManager.getSignInResult(data, new ApiClientManager.ConnectionListener() {
                @Override
                public void onSuccess(GoogleSignInAccount account) {
                    mAuth.signIn(account, mLoginListener);
                }

                @Override
                public void onFailure() {

                }
            });
        }
    }

    @Override
    public void signIn() {
        mView.startGoogleSignInActivity(mApiManager.getSignInIntent());
    }

    private boolean validate(String email, String password) {
        boolean result = true;
        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mView.showInvalidEmailMessage();
            result = false;
        }

        if(password.isEmpty()) {
            mView.showInvalidPasswordMessage();
            result = false;
        }
        return result;
    }
}
