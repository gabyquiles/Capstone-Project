package com.gabyquiles.eventy.signin;

import android.content.Intent;

import com.gabyquiles.eventy.BasePresenter;
import com.gabyquiles.eventy.BaseView;

/**
 * Description
 *
 * @author gabrielquiles-perez
 */

public interface SignInContract {

    interface View extends BaseView<SignInContract.Presenter> {
        void showInvalidEmailMessage();
        void showInvalidPasswordMessage();
        void startGoogleSignInActivity(Intent signInIntent);
        void startEventsActivity();
        void authenticationFailed();
        void networkProblems();
    }

    interface Presenter extends BasePresenter {
        void emailSignIn(String email, String password);
        void result(int requestCode, int resultCode, Intent data);
        void signIn();
    }
}
