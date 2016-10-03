package com.gabyquiles.eventy.admob;

import android.content.Context;

import com.gabyquiles.eventy.BuildConfig;
import com.gabyquiles.eventy.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Description
 *
 * @author gabrielquiles-perez
 */
public class AdsManager implements AdsManagerInterface {
    private final String LOG_TAG = AdsManager.class.getSimpleName();
    private InterstitialAd mInterstitialAd;
    private Context mContext;
    private AdHost mAdHost;


    @Inject
    @Singleton
    public AdsManager(Context context) {
        mContext = context;
        InterstitialAd interstitialAd = new InterstitialAd(context);
        interstitialAd.setAdUnitId(mContext.getString(R.string.admob_unit_id));
        mInterstitialAd = interstitialAd;
    }

    @Override
    public void showInterstitial(AdHost host) {
        mAdHost = host;
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                mAdHost.onClosedAd();
            }
        });
        if(mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    public void requestNewInterstitial() {
        AdRequest.Builder builder = new AdRequest.Builder();
        if(BuildConfig.DEBUG) {
            builder = builder.addTestDevice("0");
        }
        AdRequest adRequest = builder.build();

        mInterstitialAd.loadAd(adRequest);
    }
}
