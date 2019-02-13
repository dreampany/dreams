package com.dreampany.lca.data.source.room;

import com.dreampany.lca.data.enums.IcoStatus;
import com.dreampany.lca.data.misc.IcoMapper;
import com.dreampany.lca.data.model.Ico;
import com.dreampany.lca.data.source.api.IcoDataSource;

import java.util.List;

import javax.inject.Singleton;

import com.dreampany.lca.data.source.dao.IcoDao;
import io.reactivex.Maybe;

/**
 * Created by Hawladar Roman on 7/10/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */

@Singleton
public class IcoRoomDataSource implements IcoDataSource {

    private final IcoMapper mapper;
    private final IcoDao dao;

    public IcoRoomDataSource(IcoMapper mapper,
                             IcoDao dao) {
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
        return null;
    }

    @Override
    public boolean isExists(Ico ico) {
        return false;
    }

    @Override
    public Maybe<Boolean> isExistsRx(Ico ico) {
        return null;
    }

    @Override
    public long putItem(Ico ico) {
        return dao.insertOrReplace(ico);
    }

    @Override
    public Maybe<Long> putItemRx(Ico ico) {
        return Maybe.fromCallable(() -> putItem(ico));
    }

    @Override
    public List<Long> putItems(List<Ico> icos) {
        return dao.insertOrReplace(icos);
    }

    @Override
    public Maybe<List<Long>> putItemsRx(List<Ico> icos) {
        return Maybe.fromCallable(() -> putItems(icos));
    }

    @Override
    public int delete(Ico ico) {
        return 0;
    }

    @Override
    public Maybe<Integer> deleteRx(Ico ico) {
        return null;
    }

    @Override
    public List<Long> delete(List<Ico> icos) {
        return null;
    }

    @Override
    public Maybe<List<Long>> deleteRx(List<Ico> icos) {
        return null;
    }

    @Override
    public Ico getItem(long id) {
        return null;
    }

    @Override
    public Maybe<Ico> getItemRx(long id) {
        return null;
    }

    @Override
    public List<Ico> getItems() {
        return null;
    }

    @Override
    public Maybe<List<Ico>> getItemsRx() {
        return null;
    }

    @Override
    public List<Ico> getItems(int limit) {
        return null;
    }

    @Override
    public Maybe<List<Ico>> getItemsRx(int limit) {
        return null;
    }

    @Override
    public void clear(IcoStatus status) {
        mapper.clear(status);
    }

    @Override
    public List<Ico> getLiveItems(int limit) {
        return null;
    }

    @Override
    public Maybe<List<Ico>> getLiveItemsRx(int limit) {
        return dao.getItemsRx(IcoStatus.LIVE.name(), limit);
    }

    @Override
    public List<Ico> getUpcomingItems(int limit) {
        return null;
    }

    @Override
    public Maybe<List<Ico>> getUpcomingItemsRx(int limit) {
        return dao.getItemsRx(IcoStatus.UPCOMING.name(), limit);
    }

    @Override
    public List<Ico> getFinishedItems(int limit) {
        return null;
    }

    @Override
    public Maybe<List<Ico>> getFinishedItemsRx(int limit) {
        return dao.getItemsRx(IcoStatus.FINISHED.name(), limit);
    }

/*    @Override
    public Completable putItem(Ico item) {
        return Completable.fromAction(() -> dao.insertOrReplace(item));
    }

    @Override
    public Completable putItems(List<Ico> items) {
         return Completable.fromAction(() -> dao.insertOrReplace(items));
    }

    @Override
    public Maybe<List<Ico>> getLiveItems(int limit) {
      return dao.getItemsRx(IcoStatus.LIVE.name(), limit);
    }

    @Override
    public Maybe<List<Ico>> getUpcomingItems(int limit) {
       return dao.getItemsRx(IcoStatus.UPCOMING.name(), limit);
    }

    @Override
    public Maybe<List<Ico>> getFinishedItems(int limit) {
        return dao.getItemsRx(IcoStatus.FINISHED.name(), limit);
    }*/
}
