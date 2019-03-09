package com.dreampany.lca.data.source.room;

import com.dreampany.lca.data.misc.CoinAlertMapper;
import com.dreampany.lca.data.model.CoinAlert;
import com.dreampany.lca.data.source.api.CoinAlertDataSource;
import com.dreampany.lca.data.source.dao.CoinAlertDao;
import io.reactivex.Maybe;

import javax.inject.Singleton;
import java.util.List;

/**
 * Created by Roman-372 on 2/20/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
public class CoinAlertRoomDataSource implements CoinAlertDataSource {

    private final CoinAlertMapper mapper;
    private final CoinAlertDao dao;

    public CoinAlertRoomDataSource(CoinAlertMapper mapper,
                                   CoinAlertDao dao) {
        this.mapper = mapper;
        this.dao = dao;
    }

    @Override
    public void clear() {

    }

    @Override
    public CoinAlert getItem(String symbol) {
        return dao.getItem(symbol);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Maybe<Boolean> isEmptyRx() {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Maybe<Integer> getCountRx() {
        return null;
    }

    @Override
    public boolean isExists(CoinAlert coinAlert) {
        return false;
    }

    @Override
    public Maybe<Boolean> isExistsRx(CoinAlert coinAlert) {
        return null;
    }

    @Override
    public long putItem(CoinAlert coinAlert) {
        return dao.insertOrReplace(coinAlert);
    }

    @Override
    public Maybe<Long> putItemRx(CoinAlert coinAlert) {
        return null;
    }

    @Override
    public List<Long> putItems(List<CoinAlert> coinAlerts) {
        return null;
    }

    @Override
    public Maybe<List<Long>> putItemsRx(List<CoinAlert> coinAlerts) {
        return null;
    }

    @Override
    public int delete(CoinAlert coinAlert) {
        return 0;
    }

    @Override
    public Maybe<Integer> deleteRx(CoinAlert coinAlert) {
        return null;
    }

    @Override
    public List<Long> delete(List<CoinAlert> coinAlerts) {
        return null;
    }

    @Override
    public Maybe<List<Long>> deleteRx(List<CoinAlert> coinAlerts) {
        return null;
    }

    @Override
    public CoinAlert getItem(long id) {
        return null;
    }

    @Override
    public Maybe<CoinAlert> getItemRx(long id) {
        return null;
    }

    @Override
    public List<CoinAlert> getItems() {
        return null;
    }

    @Override
    public Maybe<List<CoinAlert>> getItemsRx() {
        return dao.getItemsRx();
    }

    @Override
    public List<CoinAlert> getItems(int limit) {
        return null;
    }

    @Override
    public Maybe<List<CoinAlert>> getItemsRx(int limit) {
        return null;
    }
}
