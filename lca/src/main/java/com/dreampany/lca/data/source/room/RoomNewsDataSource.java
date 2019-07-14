/*
package com.dreampany.lca.data.source.room;

import com.dreampany.frame.misc.exception.EmptyException;
import com.dreampany.lca.data.misc.NewsMapper;
import com.dreampany.lca.data.model.News;
import com.dreampany.lca.data.source.api.NewsDataSource;
import com.dreampany.lca.data.source.dao.NewsDao;

import java.util.List;

import javax.inject.Singleton;

import io.reactivex.Maybe;

*/
/**
 * Created by Hawladar Roman on 7/5/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 *//*


@Singleton
public class RoomNewsDataSource implements NewsDataSource {

    private final NewsMapper mapper;
    private final NewsDao dao;

    public RoomNewsDataSource(NewsMapper mapper,
                              NewsDao dao) {
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
    public boolean isExists(News news) {
        return false;
    }

    @Override
    public Maybe<Boolean> isExistsRx(News news) {
        return null;
    }

    @Override
    public long putItem(News news) {
        return dao.insertOrReplace(news);
    }

    @Override
    public Maybe<Long> putItemRx(News news) {
        return Maybe.create(emitter -> {
            long result = putItem(news);
            if (emitter.isDisposed()) {
                return;
            }
            if (result == -1L) {
                emitter.onError(new EmptyException());
            } else {
                emitter.onSuccess(result);
            }
        });
    }

    @Override
    public List<Long> putItems(List<News> news) {
        return dao.insertOrReplace(news);
    }

    @Override
    public Maybe<List<Long>> putItemsRx(List<News> news) {
        return Maybe.fromCallable(() -> putItems(news));
    }

    @Override
    public int delete(News news) {
        return 0;
    }

    @Override
    public Maybe<Integer> deleteRx(News news) {
        return null;
    }

    @Override
    public List<Long> delete(List<News> news) {
        return null;
    }

    @Override
    public Maybe<List<Long>> deleteRx(List<News> news) {
        return null;
    }

    @Override
    public News getItem(String id) {
        return null;
    }

    @Override
    public Maybe<News> getItemRx(String id) {
        return null;
    }

    @Override
    public List<News> getItems() {
        return null;
    }

    @Override
    public Maybe<List<News>> getItemsRx() {
        return null;
    }

    @Override
    public List<News> getItems(int limit) {
        return dao.getItems(limit);
    }

    @Override
    public Maybe<List<News>> getItemsRx(int limit) {
        return dao.getItemsRx(limit);
    }
}
*/
