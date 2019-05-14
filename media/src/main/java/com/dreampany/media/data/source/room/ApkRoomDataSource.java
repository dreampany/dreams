package com.dreampany.media.data.source.room;

import com.dreampany.media.data.misc.ApkMapper;
import com.dreampany.media.data.model.Apk;
import com.dreampany.media.data.source.api.MediaDataSource;

import java.util.List;

import javax.inject.Singleton;

import io.reactivex.Maybe;


/**
 * Created by Hawladar Roman on 7/16/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */

@Singleton
public class ApkRoomDataSource implements MediaDataSource<Apk> {

    private final ApkMapper mapper;
    private final ApkDao dao;

    public ApkRoomDataSource(ApkMapper mapper,
                             ApkDao dao) {
        this.mapper = mapper;
        this.dao = dao;
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
        return dao.getCount();
    }

    @Override
    public Maybe<Integer> getCountRx() {
        return dao.getCountRx();
    }

    @Override
    public boolean isExists(Apk apk) {
        if (mapper.isExists(apk)) {
            return true;
        }
        return dao.getCount(apk.getId()) > 0;
    }

    @Override
    public Maybe<Boolean> isExistsRx(Apk apk) {
        return Maybe.fromCallable(() -> isExists(apk));
    }

    @Override
    public long putItem(Apk apk) {
        return dao.insertOrReplace(apk);
    }

    @Override
    public Maybe<Long> putItemRx(Apk apk) {
        return Maybe.fromCallable(() -> putItem(apk));
    }

    @Override
    synchronized public List<Long> putItems(List<Apk> apks) {
        return dao.insertOrReplace(apks);
    }

    @Override
    public Maybe<List<Long>> putItemsRx(List<Apk> apks) {
        return Maybe.fromCallable(() -> putItems(apks));
    }

    @Override
    public int delete(Apk apk) {
        return 0;
    }

    @Override
    public Maybe<Integer> deleteRx(Apk apk) {
        return null;
    }

    @Override
    public List<Long> delete(List<Apk> apks) {
        return null;
    }

    @Override
    public Maybe<List<Long>> deleteRx(List<Apk> apks) {
        return null;
    }

    @Override
    public Apk getItem(long id) {
        return dao.getItem(id);
    }

    @Override
    public Maybe<Apk> getItemRx(long id) {
        return dao.getItemRx(id);
    }

    @Override
    public List<Apk> getItems() {
        return dao.getItems();
    }

    @Override
    public Maybe<List<Apk>> getItemsRx() {
        return dao.getItemsRx();
    }

    @Override
    public List<Apk> getItems(int limit) {
        return dao.getItems(limit);
    }

    @Override
    public Maybe<List<Apk>> getItemsRx(int limit) {
        return dao.getItemsRx(limit);
    }
}
