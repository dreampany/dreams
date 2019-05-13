package com.dreampany.media.data.source.repository;

import com.dreampany.frame.data.source.repository.Repository;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.media.data.model.Media;
import com.dreampany.media.data.source.api.MediaDataSource;

import java.util.List;

import io.reactivex.Maybe;


/**
 * Created by Hawladar Roman on 7/19/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class MediaRepository<T extends Media> extends Repository<Long, T> implements MediaDataSource<T> {

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
    public boolean isExists(T t) {
        return false;
    }

    @Override
    public Maybe<Boolean> isExistsRx(T t) {
        return null;
    }

    @Override
    public long putItem(T t) {
        return 0;
    }

    @Override
    public Maybe<Long> putItemRx(T t) {
        return null;
    }

    @Override
    public List<Long> putItems(List<T> ts) {
        return null;
    }

    @Override
    public Maybe<List<Long>> putItemsRx(List<T> ts) {
        return null;
    }

    @Override
    public int delete(T t) {
        return 0;
    }

    @Override
    public Maybe<Integer> deleteRx(T t) {
        return null;
    }

    @Override
    public List<Long> delete(List<T> ts) {
        return null;
    }

    @Override
    public Maybe<List<Long>> deleteRx(List<T> ts) {
        return null;
    }

    @Override
    public T getItem(long id) {
        return null;
    }

    @Override
    public Maybe<T> getItemRx(long id) {
        return null;
    }

    @Override
    public List<T> getItems() {
        return null;
    }

    @Override
    public Maybe<List<T>> getItemsRx() {
        return null;
    }

    @Override
    public List<T> getItems(int limit) {
        return null;
    }

    @Override
    public Maybe<List<T>> getItemsRx(int limit) {
        return null;
    }
}
