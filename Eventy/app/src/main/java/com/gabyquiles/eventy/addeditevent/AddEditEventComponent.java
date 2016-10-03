package com.gabyquiles.eventy.addeditevent;

import com.gabyquiles.eventy.util.ActivityScope;

import dagger.Subcomponent;

/**
 * Description
 *
 * @author gabrielquiles-perez
 */
@ActivityScope
@Subcomponent(
        modules = {
                AddEditEventPresenterModule.class
        }
)
public interface AddEditEventComponent {

    AddEditEventActivity inject(AddEditEventActivity activity);
}