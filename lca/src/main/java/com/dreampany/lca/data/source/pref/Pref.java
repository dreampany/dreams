package com.dreampany.lca.data.source.pref;

import android.content.Context;

import com.dreampany.frame.data.source.pref.FramePref;
import com.dreampany.frame.util.TimeUtil;
import com.dreampany.lca.data.enums.IcoStatus;
import com.dreampany.lca.data.model.Currency;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Hawladar Roman on 3/7/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Singleton
public class Pref extends FramePref {

    private static final String DEFAULT_FAVORITE_COMMITTED = "default_favorite_committed";
    private static final String COIN_LISTING_TIME = "coin_listing_time";
    private static final String ICO_TIME = "ico_time";
    private static final String NEWS_TIME = "news_time";
    private static final String COIN_UPDATE_TIME = "coin_update_time";
    private static final String CURRENCY = "currency";
    private static final String CURRENCY_GRAPH = "currency_graph";

    @Inject
    Pref(Context context) {
        super(context);
    }

    synchronized public void commitDefaultFavorite() {
        setPrivately(DEFAULT_FAVORITE_COMMITTED, true);
    }

    synchronized public boolean isDefaultFavoriteCommitted() {
        return getPrivately(DEFAULT_FAVORITE_COMMITTED, false);
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

    synchronized public long getCoinUpdateTime(String coinSymbol, String currency) {
        return getPrivately(COIN_UPDATE_TIME + coinSymbol + currency, 0L);
    }

    synchronized public void commitCoinUpdateTime(String coinSymbol, String currency) {
        setPrivately(COIN_UPDATE_TIME + coinSymbol + currency, TimeUtil.currentTime());
    }

    synchronized public void commitCoinListingTime(int coinIndex, String currency) {
        setPrivately(COIN_LISTING_TIME + coinIndex + currency, TimeUtil.currentTime());
    }

    synchronized public long getCoinListingTime(int coinIndex, String currency) {
        return getPrivately(COIN_LISTING_TIME + coinIndex + currency, 0L);
    }

    synchronized public void setCurrency(Currency currency) {
        setPrivately(CURRENCY, currency);
    }

    synchronized public Currency getCurrency(Currency currency) {
        return getPrivately(CURRENCY, Currency.class, currency);
    }

    synchronized public void setGraphCurrency(Currency currency) {
        setPrivately(CURRENCY_GRAPH, currency);
    }

    synchronized public Currency getGraphCurrency(Currency currency) {
        return getPrivately(CURRENCY_GRAPH, Currency.class, currency);
    }

}
