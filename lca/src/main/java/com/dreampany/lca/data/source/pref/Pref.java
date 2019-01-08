package com.dreampany.lca.data.source.pref;

import android.content.Context;

import com.dreampany.frame.data.source.pref.FramePref;
import com.dreampany.frame.util.TimeUtil;
import com.dreampany.lca.data.enums.IcoStatus;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Hawladar Roman on 3/7/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Singleton
public class Pref extends FramePref {

    private static final String DEFAULT_FLAG_COMMITTED = "default_flag_committed";
    private static final String COIN_LISTING_TIME = "coin_listing_time";
    private static final String ICO_TIME = "ico_time";
    private static final String NEWS_TIME = "news_time";
    private static final String COIN_UPDATE_TIME = "coin_update_time";

    @Inject
    Pref(Context context) {
        super(context);
    }

    synchronized public void commitDefaultFlag() {
        setPrivately(DEFAULT_FLAG_COMMITTED, true);
    }

    synchronized public boolean isDefaultFlagCommitted() {
        return getPrivately(DEFAULT_FLAG_COMMITTED, false);
    }

    synchronized public void commitCoinListingTime() {
        setPrivately(COIN_LISTING_TIME, TimeUtil.currentTime());
    }

    synchronized public long getCoinListingTime() {
        return getPrivately(COIN_LISTING_TIME, 0L);
    }

    synchronized public void commitNewsTime() {
        setPrivately(NEWS_TIME, TimeUtil.currentTime());
    }

    synchronized public long getNewsTime() {
        return getPrivately(NEWS_TIME, 0L);
    }

    synchronized public void commitIcoTime(IcoStatus status) {
        setPrivately(ICO_TIME + status.name(), TimeUtil.currentTime());
    }

    synchronized public long getIcoTime(IcoStatus status) {
        return getPrivately(ICO_TIME + status.name(), 0L);
    }

    synchronized public long getCoinUpdateTime(String coinSymbol) {
        return getPrivately(COIN_UPDATE_TIME + coinSymbol, 0L);
    }

    synchronized public void commitCoinUpdateTime(String coinSymbol) {
        setPrivately(COIN_UPDATE_TIME + coinSymbol, TimeUtil.currentTime());
    }
}
