package com.gabyquiles.eventy.ui;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.gabyquiles.eventy.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

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
    public ActivityTestRule<MainActivity> mMainActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

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
    }

//     TODO: check that "no events message" if no events
//    TODO: Take into account tablets?
}
