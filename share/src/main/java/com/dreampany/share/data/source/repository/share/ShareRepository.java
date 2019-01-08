package com.dreampany.share.data.source.repository.share;

import com.dreampany.frame.data.source.repository.Repository;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.media.data.model.Media;
import com.dreampany.media.data.source.api.MediaDataSource;
import com.dreampany.share.data.source.ShareDataSource;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Maybe;

/**
 * Created by Hawladar Roman on 8/14/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class ShareRepository<T extends Media> extends Repository<String, T> implements ShareDataSource<T> {

    protected final ShareDataSource<T> local;

    ShareRepository(RxMapper rx,
                    ResponseMapper rm,
                    ShareDataSource<T> local) {
        super(rx, rm);
        this.local = local;
    }

    @Override
    public ShareDataSource<T> getThis() {
        return null;
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
    public boolean isShared(T t) {
        return local.isShared(t);
    }

    @Override
    public Maybe<Boolean> isSharedRx(T t) {
        return local.isSharedRx(t);
    }

    @Override
    public long putShare(T t) {
        return local.putShare(t);
    }

    @Override
    public Maybe<Long> putShareRx(T t) {
        return local.putShareRx(t);
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
        return local.putItemsRx(ts);
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

    @Override
    public List<T> getSharedItems(MediaDataSource<T> source) {
        return null;
    }

    @Override
    public Maybe<List<T>> getSharedItemsRx(MediaDataSource<T> source) {
        return local.getSharedItemsRx(source);
    }
}
