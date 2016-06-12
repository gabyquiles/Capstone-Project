package com.gabyquiles.eventy.addeditevent;

import com.gabyquiles.eventy.BasePresenter;
import com.gabyquiles.eventy.BaseView;

/**
 * Description
 *
 * @author gabrielquiles-perez
 */
public interface AddEditEventContract {
    interface View extends BaseView<Presenter> {
        void showEventsList();

        void setTitle(String title);
        void setPlaceName(String placeName);

    }

    interface Presenter extends BasePresenter {
//        TODO: Expand
        void saveEvent();

        void populateEvent();
    }
}
