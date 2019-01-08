package com.dreampany.frame.data.source.pref;

import android.content.Context;

import com.dreampany.frame.util.TimeUtil;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Hawladar Roman on 8/29/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Singleton
public class AdPref extends BasePref {

    private static final String INTERSTITIAL_TIME = "interstitial_time";
    private static final String REWARDED_TIME = "rewarded_time";

    @Inject
    AdPref(Context context) {
        super(context);
    }

    public void setInterstitialTime(long time) {
        setPrivately(INTERSTITIAL_TIME, time);
    }

    public void setRewardedTime(long time) {
        setPrivately(REWARDED_TIME, time);
    }

    public boolean isInterstitialTimeExpired(long expireTime) {
        updateIfMissing(INTERSTITIAL_TIME, TimeUtil.currentTime());
        long time = getPrivately(INTERSTITIAL_TIME, 0L);
        return TimeUtil.isExpired(time, expireTime);
    }

    public boolean isRewardedTimeExpired(long expireTime) {
        updateIfMissing(REWARDED_TIME, TimeUtil.currentTime());
        long time = getPrivately(REWARDED_TIME, 0L);
        return TimeUtil.isExpired(time, expireTime);
    }

    private void updateIfMissing(String key, long value) {
        if (!isPrivateAvailable(key)) {
            setPrivately(key, value);
        }
    }
}
