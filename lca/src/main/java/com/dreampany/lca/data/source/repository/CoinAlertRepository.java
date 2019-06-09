package com.dreampany.lca.data.source.repository;

import com.dreampany.frame.data.source.repository.Repository;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.Room;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.lca.data.model.CoinAlert;
import com.dreampany.lca.data.source.api.CoinAlertDataSource;
import com.dreampany.lca.data.source.pref.Pref;
import com.dreampany.network.manager.NetworkManager;
import io.reactivex.Maybe;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Created by Roman-372 on 2/20/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
public class CoinAlertRepository extends Repository<Long, CoinAlert> implements CoinAlertDataSource {

    private final CoinAlertDataSource room;

    @Inject
    CoinAlertRepository(RxMapper rx,
                        ResponseMapper rm,
                        NetworkManager network,
                        Pref pref,
                        @Room CoinAlertDataSource room) {
        super(rx, rm);
        this.room = room;
    }

    @Override
    public boolean isExists(String id) {
        return room.isExists(id);
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
        return room.getCount();
    }

    @Override
    public Maybe<Integer> getCountRx() {
        return null;
    }

    @Override
    public boolean isExists(CoinAlert coinAlert) {
        return room.isExists(coinAlert);
    }

    @Override
    public Maybe<Boolean> isExistsRx(CoinAlert coinAlert) {
        return null;
    }

    @Override
    public long putItem(CoinAlert coinAlert) {
        return room.putItem(coinAlert);
    }

    @Override
    public Maybe<Long> putItemRx(CoinAlert coinAlert) {
        return null;
    }

    @Override
    public List<Long> putItems(List<CoinAlert> coinAlert) {
        return null;
    }

    @Override
    public Maybe<List<Long>> putItemsRx(List<CoinAlert> coinAlerts) {
        return null;
    }

    @Override
    public int delete(CoinAlert coinAlert) {
        return room.delete(coinAlert);
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
    public CoinAlert getItem(String id) {
        return room.getItem(id);
    }

    @Override
    public Maybe<CoinAlert> getItemRx(String id) {
        return null;
    }

    @Override
    public List<CoinAlert> getItems() {
        return null;
    }

    @Override
    public Maybe<List<CoinAlert>> getItemsRx() {
        return room.getItemsRx();
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
