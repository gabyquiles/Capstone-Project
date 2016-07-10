package com.gabyquiles.eventy.ui;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.gabyquiles.eventy.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Verifies that the Event Details Activity accept the creation of an event
 * TODO: Check that it works to edit an event
 * TODO: Check the rotation of screen
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class EventDetailsActivityTest {

    @Rule
    public ActivityTestRule<EventDetailsActivity> mActivityRule =
            new ActivityTestRule<>(EventDetailsActivity.class);

    @Test
    public void checkActivityLoaded(){
        onView(withId(R.id.save_button)).check(matches(withContentDescription("Save Event Details")));
//        onView(withId(R.id.event_title)).check(matches(withHint("Title")));
//        onView(withId(R.id.event_date)).check(matches(withHint("Date")));
//        onView(withId(R.id.event_time)).check(matches(withHint("Time")));
//        onView(withId(R.id.event_address)).check(matches(withHint("Address")));
//        TODO: Content Description Add Guest
//        TODO: Content Description Send Invitations
//        TODO: Content Description Add Thing Text
//        TODO: Content Description Add Thing Button
    }

}
