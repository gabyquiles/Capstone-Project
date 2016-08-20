package com.gabyquiles.eventy.ui;

import android.content.Intent;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
import android.view.View;

import com.gabyquiles.eventy.R;
import com.gabyquiles.eventy.events.EventsActivity;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.any;

/**
 * Test the list of events Screen
 *
 * @author gabrielquiles-perez
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class EventListScreenTestCase {
    private final String LOG_TAG = EventListScreenTestCase.class.getSimpleName();

    @Rule
    public ActivityTestRule<EventsActivity> mMainActivityTestRule = new ActivityTestRule<>(EventsActivity.class);

//    @Before
//    public void sleepBetweenTests() {
//        long millis = 1000;
////        final long startTime = System.currentTimeMillis();
////        final long endTime = startTime + millis;
////        do {
////
////        }while (System.currentTimeMillis() < endTime);
//
//        if(mMainActivityTestRule.getActivity().isFinishing()) {
//            mMainActivityTestRule.getActivity().finish();
//        }
//    }

    @After
    public void desps() {
//        while(!mMainActivityTestRule.getActivity().isFinishing()) {
            Log.v(LOG_TAG, "After");
//        }
    }

    /**
     * Check that main activity loaded correctly
     */
    @Test
    public void load() {
        onView(withId(R.id.add_event_fab)).check(matches(isDisplayed()));
    }

    /**
     * Create a new event by clicking the FAB
     */
    @Test
    public void createNewEvent() {
        onView(withId(R.id.add_event_fab))
                .perform(click());
        onView(withId(R.id.details_pane))
                .check(matches(isDisplayed()));
        Log.v(LOG_TAG, "Create Event finished");
    }

    /**
     * Opens Edit Event
     */
    @Test
    public void editEvent() {
        onView(withId(R.id.event_list)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, recyclerClick()));
        onView(withId(R.id.details_pane))
                .check(matches(isDisplayed()));
        Log.v(LOG_TAG, "Edit Event finished");
    }

//     TODO: check that "no events message" if no events
//    TODO: Delete Event
//    TODO: Take into account tablets?

    public static ViewAction recyclerClick() {
        return new ViewAction() {

            @Override
            public Matcher<View> getConstraints() {
                return any(View.class);
            }

            @Override
            public String getDescription() {
                return "performing click() on recycler view item";
            }

            @Override
            public void perform(UiController uiController, View view) {
                view.performClick();
            }
        };
    }
}
