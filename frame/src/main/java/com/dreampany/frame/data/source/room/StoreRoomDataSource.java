package com.dreampany.frame.data.source.room;

import com.dreampany.frame.data.misc.StoreMapper;
import com.dreampany.frame.data.model.Store;
import com.dreampany.frame.data.source.api.StoreDataSource;
import com.dreampany.frame.data.source.local.StoreDao;

import java.util.List;

import javax.inject.Singleton;

import io.reactivex.Maybe;

/**
 * Created by Hawladar Roman on 7/18/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Singleton
public class StoreRoomDataSource implements StoreDataSource {

    private final StoreMapper mapper;
    private final StoreDao dao;

    public StoreRoomDataSource(StoreMapper mapper,
                               StoreDao dao) {
        this.mapper = mapper;
        this.dao = dao;
    }

    @Override
    public List<String> getItemsOf(String type, String subtype, String state) {
        return dao.getItemsOf(type, subtype, state);
    }

    @Override
    public Maybe<List<String>> getItemsOfRx(String type, String subtype, String state) {
        return dao.getItemsOfRx(type, subtype, state);
    }

    @Override
    public Maybe<List<String>> getItemsOfRx(String type, String subtype, String state, int limit) {
        return dao.getItemsOfRx(type, subtype, state, limit);
    }

    @Override
    public List<Store> getItems(String type, String subtype, String state) {
        return null;
    }

    @Override
    public Maybe<List<Store>> getItemsRx(String type, String subtype, String state) {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return getCount() == 0;
    }

    @Override
    public Maybe<Boolean> isEmptyRx() {
        return Maybe.fromCallable(this::isEmpty);
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
    public boolean isExists(Store store) {
        return false;
    }

    @Override
    public Maybe<Boolean> isExistsRx(Store store) {
        return null;
    }

    @Override
    public long putItem(Store store) {
        return dao.insertOrReplace(store);
    }

    @Override
    public Maybe<Long> putItemRx(Store store) {
        return Maybe.fromCallable(() -> putItem(store));
    }

    @Override
    public List<Long> putItems(List<Store> stores) {
        return null;
    }

    @Override
    public Maybe<List<Long>> putItemsRx(List<Store> stores) {
        return null;
    }

    @Override
    public int delete(Store store) {
        return 0;
    }

    @Override
    public Maybe<Integer> deleteRx(Store store) {
        return null;
    }

    @Override
    public List<Long> delete(List<Store> stores) {
        return null;
    }

    @Override
    public Maybe<List<Long>> deleteRx(List<Store> stores) {
        return null;
    }

    @Override
    public Store getItem(String id) {
        return null;
    }

    @Override
    public Maybe<Store> getItemRx(String id) {
        return null;
    }

    @Override
    public List<Store> getItems() {
        return null;
    }

    @Override
    public Maybe<List<Store>> getItemsRx() {
        return null;
    }

    @Override
    public List<Store> getItems(int limit) {
        return null;
    }

    @Override
    public Maybe<List<Store>> getItemsRx(int limit) {
        return null;
    }
}
