package com.gabyquiles.eventy.signin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.gabyquiles.eventy.EventyApplication;
import com.gabyquiles.eventy.R;
import com.gabyquiles.eventy.events.EventsActivity;
import com.gabyquiles.eventy.ui.SignUpActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Login user into Eventy
 *
 * @author gabrielquiles-perez
 */
public class SignInActivity extends AppCompatActivity implements SignInContract.View {
    private final String LOG_TAG = SignInActivity.class.getSimpleName();
    static final int RC_GOOGLE_SIGN_IN = 1;
    private final int RC_SIGN_UP = 2;

    @BindView(R.id.email_textview) EditText mEmailEdit;
    @BindView(R.id.password_textview) EditText mPasswordEdit;

    @Inject SignInPresenter mPresenter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);

        // Create the presenter
        EventyApplication.get(this).getAppComponent()
                .plus(new SignInPresenterModule(this)).inject(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mPresenter.result(requestCode, resultCode, data);
    }

    @OnClick(R.id.login_btn)
    public void emailLogin() {
        mPresenter.emailSignIn(mEmailEdit.getText().toString(), mPasswordEdit.getText().toString());
    }

    @OnClick(R.id.google_sign_in_button)
    public void signIn() {
        mPresenter.signIn();
    }

    @Override
    public void startGoogleSignInActivity(Intent signInIntent) {
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
    }

    @OnClick(R.id.signup_link)
    public void signUp() {
        Intent signUpIntent = new Intent(this, SignUpActivity.class);
        startActivity(signUpIntent);
    }

    public void authenticationFailed() {
//        Log.w(LOG_TAG, "signInWithCredential", task.getException());
        Toast.makeText(getApplicationContext(), getString(R.string.authentication_error),
                Toast.LENGTH_SHORT).show();
    }

    public void networkProblems() {
//        Log.w(LOG_TAG, "signInWithCredential", task.getException());
        Toast.makeText(getApplicationContext(), getString(R.string.network_error),
                Toast.LENGTH_SHORT).show();
    }


    @Override
    public void showInvalidEmailMessage() {
        mEmailEdit.setError(getString(R.string.invalid_email_error));
    }

    @Override
    public void showInvalidPasswordMessage() {
        mPasswordEdit.setError(getString(R.string.empty_password));
    }

    @Override
    public void startEventsActivity() {
        Intent listingIntent = new Intent(this, EventsActivity.class);
        startActivity(listingIntent);
        finish();
    }

    @Override
    public void setPresenter(SignInContract.Presenter presenter) {

    }
}
