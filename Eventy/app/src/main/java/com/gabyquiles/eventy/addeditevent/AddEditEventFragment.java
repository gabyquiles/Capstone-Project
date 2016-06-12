package com.gabyquiles.eventy.addeditevent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.gabyquiles.eventy.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Description
 *
 * @author gabrielquiles-perez
 */
public class AddEditEventFragment extends Fragment implements AddEditEventContract.View{
    private final String LOG_TAG = AddEditEventFragment.class.getSimpleName();

    public static final String ARGUMENT_EDIT_EVENT_ID = "EDIT_EVENT_ID";

    private AddEditEventContract.Presenter mPresenter;


    //    Views
    @BindView(R.id.event_title)
    EditText mTitle;

    @BindView(R.id.event_date)
    EditText mDate;

    @BindView(R.id.event_time)
    EditText mTime;

    @BindView(R.id.event_address)
    EditText mAddress;

    @BindView(R.id.new_thing)
    EditText mNewThing;

    @BindView(R.id.guest_list)
    RecyclerView mGuestList;

    @BindView(R.id.things_list)
    RecyclerView mThingsList;

    @BindView(R.id.send_invites_button)
    AppCompatImageButton mInvitesBtn;

    public static AddEditEventFragment newInstance() {
        return new AddEditEventFragment();
    }

    public AddEditEventFragment() {

    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(@NonNull AddEditEventContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        TODO: Bind save button??
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        mManager = new EventManager(getContext());

//        mEvent = new Event();
//        Bundle arguments = getArguments();
//        if(arguments != null && arguments.getParcelable(EVENT_URI) != null) {
//            mUri = arguments.getParcelable(EVENT_URI);
//        }

        View rootView = inflater.inflate(R.layout.fragment_event_details, container, false);
        ButterKnife.bind(this, rootView);

//        TODO: Restate?
//        mGuestList.setLayoutManager(new LinearLayoutManager(getActivity()));
//        mGuestAdapter = new GuestsAdapter(getActivity(), mEvent.getGuestList(), null);
//        mGuestList.setAdapter(mGuestAdapter);
//
//        mThingsList.setLayoutManager(new LinearLayoutManager(getActivity()));
//        mThingsAdapter = new ThingsAdapter(getActivity(), mEvent.getThingList(), null);
//        mThingsList.setAdapter(mThingsAdapter);


//        TODO: move to presenter??
//        mInterstitialAd = new InterstitialAd(getActivity());
//        mInterstitialAd.setAdUnitId(getString(R.string.admob_unit_id));
//
//        mInterstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                requestNewInterstitial();
//            }
//        });

//        requestNewInterstitial();

//        // Obtain the FirebaseAnalytics instance.
//        mFirebaseAnalytics = ((EventyApplication) getActivity().getApplication()).getAnalytics();
//        logEvent("Event Details");

        return rootView;
    }

    @Override
    public void showEventsList() {
        getActivity().finish();
    }

    @Override
    public void setTitle(String title) {
        mTitle.setText(title);
    }

    @Override
    public void setPlaceName(String placeName) {
        mAddress.setText(placeName);
    }
}
