package com.dreampany.lca.data.source.repository;

import com.annimon.stream.Stream;
import com.dreampany.frame.data.misc.StateMapper;
import com.dreampany.frame.data.model.State;
import com.dreampany.frame.data.source.repository.StateRepository;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.frame.util.TimeUtil;
import com.dreampany.lca.data.enums.CoinSource;
import com.dreampany.lca.data.enums.ItemState;
import com.dreampany.lca.data.enums.ItemSubtype;
import com.dreampany.lca.data.enums.ItemType;
import com.dreampany.lca.data.misc.CoinAlertMapper;
import com.dreampany.lca.data.misc.CoinMapper;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.model.CoinAlert;
import com.dreampany.lca.data.model.Currency;
import com.dreampany.lca.data.source.pref.Pref;
import com.google.common.collect.Maps;
import io.reactivex.Maybe;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Roman-372 on 2/12/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

@Singleton
public class ApiRepository {

    private final RxMapper rx;
    private final ResponseMapper rm;
    private final Pref pref;
    private final CoinMapper coinMapper;
    private final StateMapper stateMapper;
    private final CoinAlertMapper alertMapper;
    private final CoinRepository coinRepo;
    private final StateRepository stateRepo;
    private final CoinAlertRepository alertRepo;
    private final Map<Coin, Boolean> favorites;
    private final Map<Coin, Boolean> alerts;

    @Inject
    ApiRepository(RxMapper rx,
                  ResponseMapper rm,
                  Pref pref,
                  CoinMapper coinMapper,
                  StateMapper stateMapper,
                  CoinAlertMapper alertMapper,
                  CoinRepository coinRepo,
                  StateRepository stateRepo,
                  CoinAlertRepository alertRepo) {
        this.rx = rx;
        this.rm = rm;
        this.pref = pref;
        this.coinMapper = coinMapper;
        this.stateMapper = stateMapper;
        this.alertMapper = alertMapper;
        this.coinRepo = coinRepo;
        this.stateRepo = stateRepo;
        this.alertRepo = alertRepo;
        favorites = Maps.newConcurrentMap();
        alerts = Maps.newConcurrentMap();
    }

    public boolean hasState(Coin coin, ItemSubtype subtype, ItemState state) {
        boolean stated = stateRepo.getCount(coin.getId(), ItemType.COIN.name(), subtype.name(), state.name()) > 0;
        return stated;
    }

    public long putState(Coin coin, ItemSubtype subtype, ItemState state) {
        State s = new State(coin.getId(), ItemType.COIN.name(), subtype.name(), state.name());
        s.setTime(TimeUtil.currentTime());
        long result = stateRepo.putItem(s);
        return result;
    }

    public int removeState(Coin coin, ItemSubtype subtype, ItemState state) {
        State s = new State(coin.getId(), ItemType.COIN.name(), subtype.name(), state.name());
        s.setTime(TimeUtil.currentTime());
        int result = stateRepo.delete(s);
        return result;
    }

    public long putFavorite(Coin coin) {
        long result = putState(coin, ItemSubtype.DEFAULT, ItemState.FAVORITE);
        return result;
    }

    public int removeFavorite(Coin coin) {
        int result = removeState(coin, ItemSubtype.DEFAULT, ItemState.FAVORITE);
        return result;
    }


    public boolean isFavorite(Coin coin) {
        if (!favorites.containsKey(coin)) {
            boolean flagged = hasState(coin, ItemSubtype.DEFAULT, ItemState.FAVORITE);
            favorites.put(coin, flagged);
        }
        return favorites.get(coin);
    }

    public boolean hasAlert(Coin coin) {
        if (!alerts.containsKey(coin)) {
            boolean alert = alertRepo.isExists(coin.getSymbol());
            alerts.put(coin, alert);
        }
        return alerts.get(coin);
    }

    public boolean toggleFavorite(Coin coin) {
        boolean flagged = hasState(coin, ItemSubtype.DEFAULT, ItemState.FAVORITE);
        if (flagged) {
            removeFavorite(coin);
            favorites.put(coin, false);
        } else {
            putFavorite(coin);
            favorites.put(coin, true);
        }
        return favorites.get(coin);
    }

    public List<Coin> getItemsIf(CoinSource source, String[] symbols, Currency currency) {
        return coinRepo.getItemsRx(source, symbols, currency).blockingGet();
    }

    public Coin getItemIf(CoinSource source, String symbol, Currency currency) {
        return coinRepo.getItemRx(source, symbol, currency).blockingGet();
    }

    public Maybe<Coin> getItemIfRx(CoinSource source, String symbol, long lastUpdated, Currency currency) {
        return coinRepo.getItemRx(source, symbol, lastUpdated, currency);
    }

    public Maybe<List<Coin>> getItemsIfRx(CoinSource source, int index, int limit, long lastUpdated, Currency currency) {
        return coinRepo.getItemsRx(source, index, limit, lastUpdated, currency);
    }

    public List<Coin> getFavorites(CoinSource source, Currency currency) {
        List<State> states = stateRepo.getItems(ItemType.COIN.name(), ItemSubtype.DEFAULT.name(), ItemState.FAVORITE.name());
        return getItemsIf(states, source, currency);
    }

    public CoinAlert getCoinAlert(String symbol) {
        return alertRepo.getItem(symbol);
    }

    public Maybe<List<CoinAlert>> getCoinAlertsRx() {
        return alertRepo.getItemsRx();
    }

    public long putItem(Coin coin, CoinAlert coinAlert) {
        long result = alertRepo.putItem(coinAlert);
        if (result != -1) {
            alerts.put(coin, true);
        }
        return result;
    }

    public int delete(Coin coin, CoinAlert coinAlert) {
        int result =  alertRepo.delete(coinAlert);
        if (result != -1) {
            alerts.put(coin, false);
        }
        return result;
    }

    public int getCoinCount() {
        return coinRepo.getCount();
    }

    private List<Coin> getItemsIf(List<State> states, CoinSource source, Currency currency) {
        if (DataUtil.isEmpty(states)) {
            return null;
        }
        List<Coin> result = new ArrayList<>(states.size());
        Stream.of(states).forEach(state -> result.add(coinMapper.toItem(state, source, currency, coinRepo)));
        return result;
    }
}
