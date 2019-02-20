package com.dreampany.lca.data.source.repository;

import com.dreampany.frame.data.source.repository.Repository;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.Room;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.lca.data.model.CoinAlarm;
import com.dreampany.lca.data.source.api.CoinAlarmDataSource;
import com.dreampany.lca.data.source.pref.Pref;
import com.dreampany.network.NetworkManager;
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
public class CoinAlarmRepository extends Repository<Long, CoinAlarm> implements CoinAlarmDataSource {

    private final CoinAlarmDataSource room;

    @Inject
    CoinAlarmRepository(RxMapper rx,
                   ResponseMapper rm,
                   NetworkManager network,
                   Pref pref,
                   @Room CoinAlarmDataSource room) {
        super(rx, rm);
        this.room = room;
    }

    @Override
    public void clear() {

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
    public boolean isExists(CoinAlarm coinAlarm) {
        return false;
    }

    @Override
    public Maybe<Boolean> isExistsRx(CoinAlarm coinAlarm) {
        return null;
    }

    @Override
    public long putItem(CoinAlarm coinAlarm) {
        return 0;
    }

    @Override
    public Maybe<Long> putItemRx(CoinAlarm coinAlarm) {
        return null;
    }

    @Override
    public List<Long> putItems(List<CoinAlarm> coinAlarms) {
        return null;
    }

    @Override
    public Maybe<List<Long>> putItemsRx(List<CoinAlarm> coinAlarms) {
        return null;
    }

    @Override
    public int delete(CoinAlarm coinAlarm) {
        return 0;
    }

    @Override
    public Maybe<Integer> deleteRx(CoinAlarm coinAlarm) {
        return null;
    }

    @Override
    public List<Long> delete(List<CoinAlarm> coinAlarms) {
        return null;
    }

    @Override
    public Maybe<List<Long>> deleteRx(List<CoinAlarm> coinAlarms) {
        return null;
    }

    @Override
    public CoinAlarm getItem(long id) {
        return null;
    }

    @Override
    public Maybe<CoinAlarm> getItemRx(long id) {
        return null;
    }

    @Override
    public List<CoinAlarm> getItems() {
        return null;
    }

    @Override
    public Maybe<List<CoinAlarm>> getItemsRx() {
        return null;
    }

    @Override
    public List<CoinAlarm> getItems(int limit) {
        return null;
    }

    @Override
    public Maybe<List<CoinAlarm>> getItemsRx(int limit) {
        return null;
    }
}
