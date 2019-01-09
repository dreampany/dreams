package com.dreampany.frame.data.source.room;

import com.dreampany.frame.data.misc.FlagMapper;
import com.dreampany.frame.data.model.Flag;
import com.dreampany.frame.data.source.api.FlagDataSource;
import com.dreampany.frame.data.source.local.FlagDao;

import java.util.List;

import javax.inject.Singleton;

import io.reactivex.Maybe;

/**
 * Created by Hawladar Roman on 7/18/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Singleton
public class FlagRoomDataSource implements FlagDataSource {

    private final FlagMapper mapper;
    private final FlagDao dao;

    public FlagRoomDataSource(FlagMapper mapper,
                              FlagDao dao) {
        this.mapper = mapper;
        this.dao = dao;
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
    public boolean toggle(Flag flag) {
        if (isExists(flag)) {
            dao.delete(flag);
            return false;
        } else {
            dao.insertOrReplace(flag);
            return true;
        }
    }

    @Override
    public Maybe<Boolean> toggleRx(Flag flag) {
        return isExistsRx(flag).doOnSuccess(
                flagged -> {
                    if (flagged) {
                        dao.delete(flag);
                    } else {
                        dao.insertOrReplace(flag);
                    }
                });
    }

    @Override
    public Flag getItem(long id, String type, String subtype) {
        Flag flag = mapper.getItem(id, type, subtype);
        if (flag == null) {
            flag = dao.getItem(id, type, subtype);
        }
        if (flag == null) {
            flag = new Flag(id, type, subtype);
        }
        mapper.putItem(flag);
        return flag;
    }

    @Override
    public Maybe<Flag> getItemRx(long id, String type, String subtype) {
        if (mapper.isExists(id, type, subtype)) {
            return Maybe.fromCallable(() -> mapper.getItem(id, type, subtype));
        }
        return dao.getItemRx(id, type, subtype)
                .doOnSuccess(mapper::putItem);
    }

    @Override
    public List<Flag> getItems(String type, String subtype) {
        return dao.getItems(type, subtype);
    }

    @Override
    public Maybe<List<Flag>> getItemsRx(String type, String subtype) {
        return dao.getItemsRx(type, subtype);
    }

    @Override
    public List<Flag> getItems(String type, String subtype, int limit) {
        return dao.getItems(type, subtype, limit);
    }

    @Override
    public Maybe<List<Flag>> getItemsRx(String type, String subtype, int limit) {
        return dao.getItemsRx(type, subtype, limit);
    }

    @Override
    public boolean isExists(Flag flag) {
        return dao.getCount(flag.getId(), flag.getType(), flag.getSubtype()) > 0;
    }

    @Override
    public Maybe<Boolean> isExistsRx(Flag flag) {
        return Maybe.fromCallable(() -> isExists(flag));
    }

    @Override
    public long putItem(Flag flag) {
        return dao.insertOrReplace(flag);
    }

    @Override
    public Maybe<Long> putItemRx(Flag flag) {
        return Maybe.fromCallable(() -> putItem(flag));
    }

    @Override
    public List<Long> putItems(List<Flag> flags) {
        return dao.insertOrReplace(flags);
    }

    @Override
    public Maybe<List<Long>> putItemsRx(List<Flag> flags) {
        return Maybe.fromCallable(() -> putItems(flags));
    }

    @Override
    public Flag getItem(long id) {
        if (mapper.isExists(id)) {
            return mapper.getItem(id);
        }
        Flag flag = dao.getItem(id);
        mapper.putItem(flag);
        return flag;
    }

    @Override
    public Maybe<Flag> getItemRx(long id) {
        return Maybe.fromCallable(() -> getItem(id));
    }

    @Override
    public List<Flag> getItems() {
        return dao.getItems();
    }

    @Override
    public Maybe<List<Flag>> getItemsRx() {
        return dao.getItemsRx();
    }

    @Override
    public List<Flag> getItems(int limit) {
        return dao.getItems(limit);
    }

    @Override
    public Maybe<List<Flag>> getItemsRx(int limit) {
        return dao.getItemsRx(limit);
    }
}
