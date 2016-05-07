package com.gabyquiles.eventy.model;

import android.os.Parcel;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


/**
 * Test that an event is being parced and retrieved correctly
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class EventTestCase {
    protected static final String TITLE = "This is the tile of the event";
    protected static final String PLACE = "Venue to hold the event";

    private Event eventUnderTest;

    @Before
    public void setUp() {
        eventUnderTest = getEvent();
    }

    @Test
    public void writeToParcel() {
        Parcel dest = Parcel.obtain();
        eventUnderTest.writeToParcel(dest, 0);
        dest.setDataPosition(0);
        Event unparcelled = Event.CREATOR.createFromParcel(dest);

        //TODO: is is not working for full event
        assertThat(eventUnderTest.getDate(), is(unparcelled.getDate()));
        assertThat(eventUnderTest.getTitle(), is(unparcelled.getTitle()));
        assertThat(eventUnderTest.getPlace(), is(unparcelled.getPlace()));
    }

    private Event getEvent() {
        Event event = new Event();
        event.setTitle(TITLE);
        event.setPlace(PLACE);

        return event;
    }

}
