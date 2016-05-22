package com.gabyquiles.eventy.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gabyquiles.eventy.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link ThingsAdapter} exposes a list of things to bring to an event
 *
 * @author gabrielquiles-perez
 */
public class ThingsAdapter extends RecyclerView.Adapter<ThingsAdapter.ThingsHolder> {
    private final String LOG_TAG = GuestsAdapter.class.getSimpleName();

    private View mEmptyView;
    private List mList;
    private Context mContext;


    public ThingsAdapter(Context context, List<String> list, View emptyView) {
        mContext = context;
        mList = list;
        mEmptyView = emptyView;
    }

    @Override
    public ThingsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(parent instanceof RecyclerView) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.string_list_item, parent, false);
            view.setFocusable(true);
            return new ThingsHolder(view);
        } else {
            throw new RuntimeException("Not bound to RecyclerViewSelection");
        }
    }

    @Override
    public void onBindViewHolder(ThingsHolder holder, int position) {
        String string = (String) mList.get(position);
        holder.mTextItem.setText(string);
    }

    @Override
    public int getItemCount() {
        int count = mList.size();
        if (mEmptyView != null) {
            mEmptyView.setVisibility(count == 0 ? View.VISIBLE : View.GONE);
        }
        return count;
    }

    public void updateList(List<String> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public static class ThingsHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_item) TextView mTextItem;
        public ThingsHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }
}
