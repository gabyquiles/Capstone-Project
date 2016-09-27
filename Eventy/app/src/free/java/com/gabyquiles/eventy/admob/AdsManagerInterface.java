package com.gabyquiles.eventy.admob;

import android.content.Context;

/**
 * Description
 *
 * @author gabrielquiles-perez
 */

public interface AdsManagerInterface {
    void showInterstitial(AdHost adHost);
    void requestNewInterstitial();
}
