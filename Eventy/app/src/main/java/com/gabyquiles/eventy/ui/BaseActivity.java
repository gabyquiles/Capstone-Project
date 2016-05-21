package com.gabyquiles.eventy.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.gabyquiles.eventy.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Sets up commonalities between app activities
 *
 * @author gabrielquiles-perez
 */
public class BaseActivity extends AppCompatActivity {
    private final String LOG_TAG = BaseActivity.class.getSimpleName();

    protected FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        showLoginForm();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //Logout from the app
        if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            showLoginForm();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void showLoginForm() {
        if(!isSignedIn()) {
            Intent signinIntent = new Intent(this, SignInActivity.class);
            startActivity(signinIntent);
            finish();
        }
    }

    public boolean isSignedIn() {
        return (mAuth.getCurrentUser() != null);
    }
}
