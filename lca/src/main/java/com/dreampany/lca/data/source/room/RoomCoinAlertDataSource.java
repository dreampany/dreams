/*
package com.dreampany.lca.data.source.room;

import com.dreampany.lca.data.misc.CoinAlertMapper;
import com.dreampany.lca.data.model.CoinAlert;
import com.dreampany.lca.data.source.api.CoinAlertDataSource;
import com.dreampany.lca.data.source.dao.CoinAlertDao;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Singleton;

import io.reactivex.Maybe;

*/
/**
 * Created by Roman-372 on 2/20/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 *//*

@Singleton
public class RoomCoinAlertDataSource implements CoinAlertDataSource {

    private final CoinAlertMapper mapper;
    private final CoinAlertDao dao;

    public RoomCoinAlertDataSource(CoinAlertMapper mapper,
                                   CoinAlertDao dao) {
        this.mapper = mapper;
        this.dao = dao;
    }

    @Override
    public boolean isExists(String id) {
        return dao.getCount(id) > 0;
    }


    @Override
    public int getCount() {
        return dao.getCount();
    }

    @Override
    public boolean isExists(CoinAlert coinAlert) {
        return  dao.getCount(coinAlert.getId()) > 0;
    }

    @Override
    public Maybe<Boolean> isExistsRx(CoinAlert coinAlert) {
        return Maybe.fromCallable(() -> isExists(coinAlert));
    }

    @Override
    public long putItem(CoinAlert coinAlert) {
        return dao.insertOrReplace(coinAlert);
    }


    @Override
    public int delete(CoinAlert coinAlert) {
        return dao.delete(coinAlert);
    }

    @Override
    public CoinAlert getItem(String id) {
        return dao.getItem(id);
    }

    @Override
    public List<CoinAlert> getItems() {
        List<CoinAlert> alerts = dao.getItems();
        return alerts;
    }

    @Override
    public Maybe<List<CoinAlert>> getItemsRx() {
        return Maybe.fromCallable(new Callable<List<CoinAlert>>() {
            @Override
            public List<CoinAlert> call() throws Exception {
                return getItems();
            }
        });
    }

}
*/
