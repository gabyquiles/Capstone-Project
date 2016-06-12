package com.gabyquiles.eventy.addeditevent;

import com.gabyquiles.eventy.data.DataModule;
import com.gabyquiles.eventy.events.EventsActivity;
import com.gabyquiles.eventy.events.EventsPresenterModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Description
 *
 * @author gabrielquiles-perez
 */

@Singleton
@Component(modules = {DataModule.class, AddEditEventPresenterModule.class})
public interface AddEditEventComponent {

    void inject(AddEditEventActivity activity);
}