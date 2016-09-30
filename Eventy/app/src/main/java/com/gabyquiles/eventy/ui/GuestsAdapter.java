package com.gabyquiles.eventy.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gabyquiles.eventy.R;
import com.gabyquiles.eventy.model.BaseGuest;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link GuestsAdapter} exposes a list of {@link BaseGuest} that are invited to an event
 *
 * @author gabrielquiles-perez
 */
public class GuestsAdapter extends RecyclerView.Adapter<GuestsAdapter.GuestHolder> {
    private final String LOG_TAG = GuestsAdapter.class.getSimpleName();


    private View mEmptyView;
    private List mList;
    private Context mContext;


    public GuestsAdapter(Context context, List<BaseGuest> list, View emptyView) {
        mContext = context;
        mList = list;
        mEmptyView = emptyView;
    }

    @Override
    public GuestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(parent instanceof RecyclerView) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.guest_list_item, parent, false);
            view.setFocusable(true);
            return new GuestHolder(view);
        } else {
            throw new RuntimeException("Not bound to RecyclerViewSelection");
        }
    }

    @Override
    public void onBindViewHolder(GuestHolder holder, int position) {
        BaseGuest guest = (BaseGuest) mList.get(position);
        holder.mGuestName.setText(guest.getName());
    }

    @Override
    public int getItemCount() {
        int count = mList.size();
        if (mEmptyView != null) {
            mEmptyView.setVisibility(count == 0 ? View.VISIBLE : View.GONE);
        }
        return count;
    }

    public void updateList(List<BaseGuest> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public static class GuestHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.guest_name) TextView mGuestName;

        public GuestHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
