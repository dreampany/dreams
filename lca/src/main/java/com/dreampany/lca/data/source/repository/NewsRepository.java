package com.dreampany.lca.data.source.repository;

import com.dreampany.frame.data.source.repository.Repository;
import com.dreampany.frame.misc.Remote;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.Room;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.frame.util.TimeUtil;
import com.dreampany.lca.data.model.News;
import com.dreampany.lca.data.source.api.NewsDataSource;
import com.dreampany.lca.data.source.pref.Pref;
import com.dreampany.lca.misc.Constants;
import com.dreampany.network.manager.NetworkManager;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;


import io.reactivex.Maybe;
import io.reactivex.internal.functions.Functions;

/**
 * Created by Hawladar Roman on 6/22/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Singleton
public class NewsRepository extends Repository<Long, News> implements NewsDataSource {

    private final NetworkManager network;
    private final Pref pref;
    private final NewsDataSource room;
    private final NewsDataSource remote;


    @Inject
    NewsRepository(RxMapper rx,
                   ResponseMapper rm,
                   NetworkManager network,
                   Pref pref,
                   @Room NewsDataSource room,
                   @Remote NewsDataSource remote) {
        super(rx, rm);
        this.network = network;
        this.pref = pref;
        this.room = room;
        this.remote = remote;
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
        return room.getCount();
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
        return room.putItem(news);
    }

    @Override
    public Maybe<Long> putItemRx(News news) {
        return room.putItemRx(news);
    }

    @Override
    public List<Long> putItems(List<News> news) {
        return room.putItems(news);
    }

    @Override
    public Maybe<List<Long>> putItemsRx(List<News> news) {
        return room.putItemsRx(news);
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
        return null;
    }

    @Override
    public Maybe<List<News>> getItemsRx(int limit) {
        Maybe<List<News>> remote = getRemoteItemsIfRx(limit);
        Maybe<List<News>> roomAny = room.getItemsRx(limit);
        return concatFirstRx(remote, roomAny);
    }

    private Maybe<List<News>> getRemoteItemsIfRx(int limit) {
        Maybe<List<News>> maybe = isNewsExpired()
                ? remote.getItemsRx(limit)
                : Maybe.empty();
        return maybe.filter(news -> !DataUtil.isEmpty(news))
                .doOnSuccess(news -> {
                    rx.compute(putItemsRx(news)).subscribe(Functions.emptyConsumer(), Functions.emptyConsumer());
                    pref.commitNewsTime();
                });
    }

    private boolean isNewsExpired() {
        long lastTime = pref.getNewsTime();
        return TimeUtil.isExpired(lastTime, Constants.Delay.INSTANCE.getNews());
    }
}
