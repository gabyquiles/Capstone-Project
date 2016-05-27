package com.gabyquiles.eventy.ui;

import android.content.Context;
import android.net.Uri;
import android.view.View;

import com.gabyquiles.eventy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Calendar;

/**
 * Description
 *
 * @author gabrielquiles-perez
 */
public class EventListAdapterFactory extends RecyclerViewAdapterFactory implements RecyclerViewAdapterFactory.AdapterOnClickHandler{
    private final String LOG_TAG = EventListAdapterFactory.class.getSimpleName();

    private DatabaseReference mFirebase;
    private Context mContext;

    @Override
    EventAdapter getAdapter(Context context, View emptyView) {
        mContext = context;
        Query ref = getFirebaseReference();
        return new EventAdapter(mContext, ref, emptyView, this);
    }

    private Query getFirebaseReference() {
        long today = Calendar.getInstance().getTimeInMillis();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser mUser = auth.getCurrentUser();

        mFirebase = FirebaseDatabase.getInstance().getReference().child(mContext.getString(R.string.firebase_users_path));
        mFirebase = mFirebase.child(mUser.getUid()).child("events");
        return mFirebase.orderByChild("date").startAt(today);
    }

    @Override
    public void onClick(String key) {
        Uri eventUri = Uri.parse(mFirebase.toString()).buildUpon().appendPath(key).build();
        ((EventListFragment.Callback) mContext).showEventDetails(eventUri);
    }

    @Override
    public void delete(String key) {
        Uri eventUri = Uri.parse(mFirebase.toString()).buildUpon().appendPath(key).build();
        FirebaseDatabase.getInstance().getReferenceFromUrl(eventUri.toString()).removeValue();
    }
}
