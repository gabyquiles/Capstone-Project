package com.gabyquiles.eventy.model;

import android.os.Parcel;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Test that an event is being parced and retrieved correctly
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class EventTestCase {
    protected static final String TITLE = "This is the tile of the event";
    protected static final String PLACE = "Venue to hold the event";

    private FreeEvent eventUnderTest;

    @Before
    public void setUp() {
        eventUnderTest = getEvent();
    }

    @Test
    public void writeToParcel() {
        Parcel dest = Parcel.obtain();
        eventUnderTest.writeToParcel(dest, 0);
        dest.setDataPosition(0);
        FreeEvent unparcelled = FreeEvent.CREATOR.createFromParcel(dest);

        assertThat(eventUnderTest, is(unparcelled));
    }

    private FreeEvent getEvent() {
        FreeEvent event = new FreeEvent();
        event.setTitle(TITLE);
        event.setPlaceName(PLACE);

        return event;
    }

}
