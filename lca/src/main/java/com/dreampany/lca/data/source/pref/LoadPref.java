package com.dreampany.lca.data.source.pref;

import android.content.Context;

import com.dreampany.frame.data.source.pref.BasePref;
import com.dreampany.frame.util.TimeUtil;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Hawladar Roman on 10/2/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Singleton
public class LoadPref extends BasePref {

    private static final String DEFAULT_FAVORITE_COMMITTED = "default_favorite_committed";
    private static final String COIN_INDEX_TIME = "coin_index_time";

    @Inject
    LoadPref(Context context) {
        super(context);
    }

    synchronized public void commitDefaultFavorite() {
        setPrivately(DEFAULT_FAVORITE_COMMITTED, true);
    }

    synchronized public boolean isDefaultFavoriteCommitted() {
        return getPrivately(DEFAULT_FAVORITE_COMMITTED, false);
    }

    synchronized public void setCoinIndexTime(int coinIndex) {
        setPrivately(COIN_INDEX_TIME + coinIndex, TimeUtil.currentTime());
    }

    synchronized public long getCoinIndexTime(int coinIndex) {
        return getPrivately(COIN_INDEX_TIME + coinIndex, 0L);
    }
}
