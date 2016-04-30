package com.gabyquiles.eventy;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * {@link EventAdapter} exposes a list of upcoming events
 * from a {@link android.database.Cursor} to a {@link RecyclerView}
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.VH> {
    private Context mContext;
    private View mEmptyView;

    public EventAdapter(Context context, View emptyView) {
        mContext = context;
        mEmptyView = emptyView;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        if(parent instanceof RecyclerView) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.event_list_item, parent, false);
            view.setFocusable(true);
            return new VH(view);
        } else {
            throw new RuntimeException("Not bound to RecyclerView");
        }
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        //TODO: Implement the retrieval of cursor and fill fields
        holder.mTitle.setText("Titulo");
        holder.mDateTime.setText("Fecha");
    }

    @Override
    public int getItemCount() {
        //TODO: Implement the count of how many items from cursor
        return 2;
    }

    class VH extends RecyclerView.ViewHolder {
        public final TextView mTitle;
        public final TextView mDateTime;

        public VH(View view) {
            super(view);
            mTitle = (TextView) view.findViewById(R.id.event_title_textview);
            mDateTime = (TextView) view.findViewById(R.id.event_datetime_textview);
        }
    }


}
