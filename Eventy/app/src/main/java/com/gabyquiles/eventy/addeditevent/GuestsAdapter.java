package com.gabyquiles.eventy.addeditevent;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gabyquiles.eventy.R;
import com.gabyquiles.eventy.model.BaseGuest;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description
 *
 * @author gabrielquiles-perez
 */
public class GuestsAdapter extends RecyclerView.Adapter<GuestsAdapter.GuestHolder> implements
        EventDetailsLists {
    private final String LOG_TAG = GuestsAdapter.class.getSimpleName();

    private Context mContext;
    protected List mList;

    public GuestsAdapter(Context context) {
        mContext = context;
        if (mList == null) {
            mList = new ArrayList();
        }
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
        if (mList != null) {
            return mList.size();
        } else {
            return 0;
        }
    }

    @Override
    public void updateList(List list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public List getList() {
        return mList;
    }

    @Override
    public void addToList(Object guest) {
        if (mList != null) {
            mList.add(guest);
        }
        notifyDataSetChanged();
    }

    public static class GuestHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.guest_name)
        TextView mGuestName;

        public GuestHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
