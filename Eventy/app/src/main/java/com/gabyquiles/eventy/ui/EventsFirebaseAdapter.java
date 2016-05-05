package com.gabyquiles.eventy.ui;

import android.support.v7.widget.RecyclerView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gabrielquiles-perez on 5/3/16.
 */
public abstract class EventsFirebaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Query mQuery;
    private List<T> mModels;
    private List<String> mKeys;
    private Class<T> mModelClass;
    private ChildEventListener mListener;

    public EventsFirebaseAdapter(Query query, Class<T> modelClass) {
        mQuery = query;
        mModelClass = modelClass;
        setup();
    }

    public void setup() {
        if(mModels == null) {
            mModels = new ArrayList<T>();
        }
        if(mKeys == null) {
            mKeys = new ArrayList<String>();
        }
        if(mListener == null) {
            mListener = mQuery.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildKey) {
                    T model = dataSnapshot.getValue(mModelClass);
                    String key = dataSnapshot.getKey();

                    if (previousChildKey == null) {
                        mModels.add(0, model);
                        mKeys.add(0, key);
                    } else {
                        int previousKeyIdx = mKeys.indexOf(previousChildKey);
                        int currentIdx = previousKeyIdx + 1;
                        if (currentIdx >= mModels.size()) {
                            mModels.add(model);
                            mKeys.add(key);
                        } else {
                            mModels.add(currentIdx, model);
                            mKeys.add(currentIdx, key);
                        }
                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    String key = dataSnapshot.getKey();
                    T model = dataSnapshot.getValue(mModelClass);
                    int position = mKeys.indexOf(key);
                    mModels.set(position, model);
                    notifyDataSetChanged();
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    String key = dataSnapshot.getKey();
                    int position = mKeys.indexOf(key);
                    mKeys.remove(position);
                    mModels.remove(position);
                    notifyDataSetChanged();
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }
    }

    public void cleanUp() {
        mQuery.removeEventListener(mListener);
        mListener = null;
        mModels.clear();
        mKeys.clear();
    }

    @Override
    public int getItemCount() {
        return mModels.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        T model = mModels.get(position);
        populateHolder(holder, model);
    }

    protected abstract void populateHolder(RecyclerView.ViewHolder viewHolder, T model);
}
