package com.gabyquiles.eventy.GoogleApiClient;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.gabyquiles.eventy.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Description
 *
 * @author gabrielquiles-perez
 */
public class ApiClientManager {
    private final String LOG_TAG = ApiClientManager.class.getSimpleName();

    private FragmentActivity mActivity;
    private GoogleApiClient.OnConnectionFailedListener mConnectionFailedListener;
    private GoogleApiClient mGoogleApiClient;

    public ApiClientManager(FragmentActivity activity, GoogleApiClient.OnConnectionFailedListener connectionFailedListener) {
        mActivity = activity;
        mConnectionFailedListener = connectionFailedListener;
    }

    public void initiate() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(mActivity.getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            // Build a GoogleApiClient with access to the Google Sign-In API and the
// options specified by gso.
            mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
                    .enableAutoManage(mActivity /* FragmentActivity */, mConnectionFailedListener /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
    }

    public Intent getSignInIntent() {
        return Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
    }

    public void destroy() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.stopAutoManage(mActivity);
            mGoogleApiClient.disconnect();
        }
    }

    public void getSignInResult(Intent data, ConnectionListener listener) {
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            listener.onSuccess(acct);
        } else {
            listener.onFailure();
        }
    }

    public interface ConnectionListener{
        void onSuccess(GoogleSignInAccount account);
        void onFailure();
    }
}
