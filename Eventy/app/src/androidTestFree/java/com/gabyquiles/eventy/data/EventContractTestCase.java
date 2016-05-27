package com.gabyquiles.eventy.data;


import android.net.Uri;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Test;
import org.junit.runner.RunWith;

import static com.gabyquiles.eventy.data.EventContract.normalizeDate;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Test that the EventContract is creating the correct Uris
 *
 * @author gabrielquiles-perez
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class EventContractTestCase {
    private final String LOG_TAG = EventContractTestCase.class.getSimpleName();

    private static final long TEST_EVENT_ID = 1;
    private static final long TEST_GUEST_ID = 2;
    private static final long TEST_THING_ID = 3;
    private static final long TEST_EVENT_DATE = 1419033600L;

    @Test
    public void buildEventUri() {
        //content://com.gabyquiles.eventy.free/events/1
        Uri eventUri = EventContract.EventEntry.buildEventUri(TEST_EVENT_ID);

        assertThat("Error: Null Uri returned.", eventUri, is(notNullValue()));
        assertThat("Error: Event ID not properly appended to the end of the Uri",
                eventUri.getLastPathSegment(), is(Long.valueOf(TEST_EVENT_ID).toString()));
        assertThat("Error: Event Uri doesn' match our expected result",
                eventUri.toString(), is("content://com.gabyquiles.eventy.free/events/1"));
    }

    @Test
    public void buildEventEventUriForDate() {
        //content://com.gabyquiles.eventy.free/events?date=123
        Uri eventUri = EventContract.EventEntry.buildEventWithDateUri(TEST_EVENT_DATE);

        assertThat("Error: Null Uri returned.", eventUri, is(notNullValue()));
        assertThat("Error: Event Date is not a parameter of the Uri",
                eventUri.getQueryParameterNames(), containsInAnyOrder(EventContract.EventEntry.COLUMN_DATE));
        assertThat("Error: Event Date is not set correctly as a parameter of the Uri",
                eventUri.getQueryParameter(EventContract.EventEntry.COLUMN_DATE), is(Long.valueOf(normalizeDate(TEST_EVENT_DATE)).toString()));
    }

    @Test
    public void buildGuestUri() {
        //content://com.gabyquiles.eventy.free/guests/1
        Uri guestUri = EventContract.GuestEntry.buildGuestUri(TEST_GUEST_ID);

        assertThat("Error: Null Uri returned.", guestUri, is(notNullValue()));
        assertThat("Error: Guest ID not properly appended to the end of the Uri",
                guestUri.getLastPathSegment(), is(Long.valueOf(TEST_GUEST_ID).toString()));
        assertThat("Error: Event Uri doesn' match our expected result",
                guestUri.toString(), is("content://com.gabyquiles.eventy.free/guests/2"));
    }

    @Test
    public void buildGuestUriForEvent() {
        //content://com.gabyquiles.eventy.free/guests?event=1
        Uri guestsUri = EventContract.GuestEntry.buildEventGuestsUri(TEST_EVENT_ID);

        assertThat("Error: Null Uri returned.", guestsUri, is(notNullValue()));
        assertThat("Error: Event ID is not a parameter of the Uri",
                guestsUri.getQueryParameterNames(), containsInAnyOrder(EventContract.GuestEntry.COLUMN_EVENT_KEY));
        assertThat("Error: Event ID is not set correctly as a parameter of the Uri",
                guestsUri.getQueryParameter(EventContract.GuestEntry.COLUMN_EVENT_KEY), is(Long.valueOf(TEST_EVENT_ID).toString()));
    }

    @Test
    public void buildThingsUri() {
        //content://com.gabyquiles.eventy.free/things/1
        Uri thingUri = EventContract.ThingEntry.buildThingUri(TEST_THING_ID);

        assertThat("Error: Null Uri returned.", thingUri, is(notNullValue()));
        assertThat("Error: Thing ID not properly appended to the end of the Uri",
                thingUri.getLastPathSegment(), is(Long.valueOf(TEST_THING_ID).toString()));
        assertThat("Error: Thing Uri doesn' match our expected result",
                thingUri.toString(), is("content://com.gabyquiles.eventy.free/things/3"));
    }

    @Test
    public void buildThingsUriForEvent() {
        //content://com.gabyquiles.eventy.free/things?event=1
        Uri thingsUri = EventContract.ThingEntry.buildEventThingsUri(TEST_EVENT_ID);

        assertThat("Error: Null Uri returned.", thingsUri, is(notNullValue()));
        assertThat("Error: Event ID is not a parameter of the Uri",
                thingsUri.getQueryParameterNames(), containsInAnyOrder(EventContract.GuestEntry.COLUMN_EVENT_KEY));
        assertThat("Error: Event ID is not set correctly as a parameter of the Uri",
                thingsUri.getQueryParameter(EventContract.GuestEntry.COLUMN_EVENT_KEY), is(Long.valueOf(TEST_EVENT_ID).toString()));
    }

}
