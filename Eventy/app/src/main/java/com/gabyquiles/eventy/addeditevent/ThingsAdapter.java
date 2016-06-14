package com.gabyquiles.eventy.addeditevent;

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
 * Description
 *
 * @author gabrielquiles-perez
 */
public class ThingsAdapter extends RecyclerView.Adapter<ThingsAdapter.ThingsHolder> implements
        EventDetailsLists {
    private final String LOG_TAG = ThingsAdapter.class.getSimpleName();

    private Context mContext;
    protected List mList;

    public ThingsAdapter(Context context) {
        mContext = context;
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
    public void addToList(Object thing) {
        if (mList != null) {
            mList.add(thing);
        }
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
