package com.dreampany.media.data.source.repository;

import com.dreampany.frame.data.source.repository.Repository;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.media.data.model.Media;
import com.dreampany.media.data.source.api.MediaDataSource;

import java.util.List;

import io.reactivex.Maybe;


/**
 * Created by Hawladar Roman on 7/19/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class MediaRepository<T extends Media> extends Repository<String, T> implements MediaDataSource<T> {

    protected final MediaDataSource<T> memory;
    protected final MediaDataSource<T> room;

    MediaRepository(RxMapper rx,
                    ResponseMapper rm,
                    MediaDataSource<T> memory,
                    MediaDataSource<T> room) {
        super(rx, rm);
        this.memory = memory;
        this.room = room;
    }

    @Override
    public MediaDataSource<T> getThis() {
        return this;
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
        return room.getCountRx();
    }

    @Override
    public boolean isExists(T t) {
        return room.isExists(t);
    }

    @Override
    public Maybe<Boolean> isExistsRx(T t) {
        return room.isExistsRx(t);
    }

    @Override
    public long putItem(T t) {
        return room.putItem(t);
    }

    @Override
    public Maybe<Long> putItemRx(T t) {
        return room.putItemRx(t);
    }

    @Override
    public List<Long> putItems(List<T> ts) {
        return room.putItems(ts);
    }

    @Override
    public Maybe<List<Long>> putItemsRx(List<T> ts) {
        return room.putItemsRx(ts);
    }

    @Override
    public T getItem(long id) {
        return room.getItem(id);
    }

    @Override
    public Maybe<T> getItemRx(long id) {
        return room.getItemRx(id);
    }

    @Override
    public List<T> getItems() {
        return room.getItems();
    }

    @Override
    public Maybe<List<T>> getItemsRx() {
        return room.getItemsRx();
    }

    @Override
    public List<T> getItems(int limit) {
        return room.getItems();
    }

    @Override
    public Maybe<List<T>> getItemsRx(int limit) {
        return room.getItemsRx(limit);
    }

    //fresh api
    public Maybe<List<T>> getItemsRx(boolean fresh) {
        Maybe<List<T>> memory = saveRoomOfItems(this.memory.getItemsRx());
        Maybe<List<T>> room = this.room.getItemsRx();
        return fresh ? concatFirstRx(memory, room) : concatFirstRx(room, memory);
    }

    //private api
    private Maybe<List<T>> saveRoomOfItems(Maybe<List<T>> source) {
        return source
                .filter(items -> !(DataUtil.isEmpty(items)))
                .doOnSuccess(words -> {
                    rx.compute(putItemsRx(words)).subscribe();
                });
    }
}
