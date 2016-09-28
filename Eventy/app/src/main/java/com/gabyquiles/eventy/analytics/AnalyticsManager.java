package com.gabyquiles.eventy.analytics;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Description
 *
 * @author gabrielquiles-perez
 */
public class AnalyticsManager implements AnalyticsManagerInterface {
    private final String LOG_TAG = AnalyticsManager.class.getSimpleName();
    private FirebaseAnalytics mAnalytics;

    @Inject
    @Singleton
    public AnalyticsManager(FirebaseAnalytics firebaseAnalytics) {
        mAnalytics = firebaseAnalytics;
    }

    @Override
    public void logEvent(String eventDescription) {

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, LOG_TAG);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, eventDescription);

        mAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }
}
