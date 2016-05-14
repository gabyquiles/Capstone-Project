package com.gabyquiles.eventy.model;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Test that an event is being parced and retrieved correctly
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class GuestTestCase {
    private final String LOG_TAG = GuestTestCase.class.getSimpleName();
    protected static final String NAME = "Testing Tester";
    protected static final String EMAIL = "testing@example.com";

    @Test
    public void checkEquality() {
        Guest guest1 = new Guest(NAME, EMAIL);
        Guest guest2 = new Guest(NAME, EMAIL);

        assertThat(guest1, is(guest2));
    }
}
