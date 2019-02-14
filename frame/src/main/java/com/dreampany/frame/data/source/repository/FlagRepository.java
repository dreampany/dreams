/*
package com.dreampany.frame.data.source.repository;

import com.dreampany.frame.data.model.Flag;
import com.dreampany.frame.data.source.api.FlagDataSource;
import com.dreampany.frame.misc.Room;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import hugo.weaving.DebugLog;
import io.reactivex.Maybe;

*/
/**
 * Created by Hawladar Roman on 7/18/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 *//*

@Singleton
public class FlagRepository extends Repository<Long, Flag> implements FlagDataSource {

    private final FlagDataSource room;

    @DebugLog
    @Inject
    FlagRepository(RxMapper rx,
                   ResponseMapper rm,
                   @Room FlagDataSource room) {
        super(rx, rm);
        this.room = room;
    }

    @Override
    public boolean isEmpty() {
        return room.isEmpty();
    }

    @Override
    public Maybe<Boolean> isEmptyRx() {
        return Maybe.fromCallable(this::isEmpty);
    }

    @Override
    public int getCount() {
        return room.getCount();
    }

    @Override
    public Maybe<Integer> getCountRx() {
        return room.getCountRx();
    }

    @Override
    public boolean toggle(Flag favorite) {
        return room.toggle(favorite);
    }

    @Override
    public Maybe<Boolean> toggleRx(Flag favorite) {
        return room.toggleRx(favorite);
    }

    @Override
    public Flag getItem(long id, String type, String subtype) {
        return room.getItem(id, type, subtype);
    }

    @Override
    public Maybe<Flag> getItemRx(long id, String type, String subtype) {
        return room.getItemRx(id, type, subtype);
    }

    @Override
    public List<Flag> getItems(String type, String subtype) {
        return room.getItems(type, subtype);
    }

    @Override
    public Maybe<List<Flag>> getItemsRx(String type, String subtype) {
        return room.getItemsRx(type, subtype);
    }

    @Override
    public List<Flag> getItems(String type, String subtype, int limit) {
        return room.getItems(type, subtype, limit);
    }

    @Override
    public Maybe<List<Flag>> getItemsRx(String type, String subtype, int limit) {
        return room.getItemsRx(type, subtype, limit);
    }

    @Override
    public boolean isExists(Flag favorite) {
        return room.isExists(favorite);
    }

    @Override
    public Maybe<Boolean> isExistsRx(Flag favorite) {
        return room.isExistsRx(favorite);
    }

    @Override
    public long putItem(Flag favorite) {
        return room.putItem(favorite);
    }

    @Override
    public Maybe<Long> putItemRx(Flag favorite) {
        return room.putItemRx(favorite);
    }

    @Override
    public List<Long> putItems(List<Flag> flags) {
        return room.putItems(flags);
    }

    @Override
    public Maybe<List<Long>> putItemsRx(List<Flag> flags) {
        return room.putItemsRx(flags);
    }

    @Override
    public Flag getItem(long id) {
        return room.getItem(id);
    }

    @Override
    public Maybe<Flag> getItemRx(long id) {
        return room.getItemRx(id);
    }

    @Override
    public List<Flag> getItems() {
        return room.getItems();
    }

    @Override
    public Maybe<List<Flag>> getItemsRx() {
        return room.getItemsRx();
    }

    @Override
    public List<Flag> getItems(int limit) {
        return room.getItems(limit);
    }

    @Override
    public Maybe<List<Flag>> getItemsRx(int limit) {
        return room.getItemsRx(limit);
    }
}
*/
