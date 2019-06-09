package com.dreampany.lca.data.source.repository;

import com.dreampany.frame.data.source.repository.Repository;
import com.dreampany.frame.misc.Remote;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.Room;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.frame.util.TimeUtil;
import com.dreampany.lca.data.enums.IcoStatus;
import com.dreampany.lca.data.model.Ico;
import com.dreampany.lca.data.source.api.IcoDataSource;
import com.dreampany.lca.data.source.pref.Pref;
import com.dreampany.lca.misc.Constants;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.dreampany.network.manager.NetworkManager;
import hugo.weaving.DebugLog;
import io.reactivex.Maybe;

/**
 * Created by Hawladar Roman on 6/22/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Singleton
public class IcoRepository extends Repository<Long, Ico> implements IcoDataSource {

    private final NetworkManager network;
    private final Pref pref;
    private final IcoDataSource room;
    private final IcoDataSource remote;

    @DebugLog
    @Inject
    IcoRepository(RxMapper rx,
                  ResponseMapper rm,
                  NetworkManager network,
                  Pref pref,
                  @Room IcoDataSource room,
                  @Remote IcoDataSource remote) {
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
    public boolean isExists(Ico ico) {
        return room.isExists(ico);
    }

    @Override
    public Maybe<Boolean> isExistsRx(Ico ico) {
        return null;
    }

    @Override
    public long putItem(Ico ico) {
        return 0;
    }

    @Override
    public Maybe<Long> putItemRx(Ico ico) {
        return null;
    }

    @Override
    public List<Long> putItems(List<Ico> icos) {
        return room.putItems(icos);
    }

    @Override
    public Maybe<List<Long>> putItemsRx(List<Ico> icos) {
        return room.putItemsRx(icos);
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
    public Ico getItem(String id) {
        return null;
    }

    @Override
    public Maybe<Ico> getItemRx(String id) {
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

/*    @Override
    public void clear(IcoStatus status) {
        room.clear(status);
        remote.clear(status);
    }*/

    @Override
    public List<Ico> getLiveItems(int limit) {
        return null;
    }

    @Override
    public Maybe<List<Ico>> getLiveItemsRx(int limit) {
        Maybe<List<Ico>> remoteIf = getRemoteItemsIfRx(IcoStatus.LIVE, limit);
        Maybe<List<Ico>> roomAny = getRoomItemsIfRx(this.room.getLiveItemsRx(limit));
        return concatFirstRx(remoteIf, roomAny);
    }

    @Override
    public List<Ico> getUpcomingItems(int limit) {
        return null;
    }

    @Override
    public Maybe<List<Ico>> getUpcomingItemsRx(int limit) {
        Maybe<List<Ico>> remoteIf = getRemoteItemsIfRx(IcoStatus.UPCOMING, limit);
        Maybe<List<Ico>> roomAny = getRoomItemsIfRx(this.room.getUpcomingItemsRx(limit));
        return concatFirstRx(remoteIf, roomAny);
    }

    @Override
    public List<Ico> getFinishedItems(int limit) {
        return null;
    }

    @Override
    public Maybe<List<Ico>> getFinishedItemsRx(int limit) {
        Maybe<List<Ico>> remoteIf = getRemoteItemsIfRx(IcoStatus.FINISHED, limit);
        Maybe<List<Ico>> roomAny = getRoomItemsIfRx(this.room.getFinishedItemsRx(limit));
        return concatFirstRx(remoteIf, roomAny);
    }

    private Maybe<List<Ico>> getRoomItemsIfRx(Maybe<List<Ico>> source) {
        return Maybe.fromCallable(() -> {
            if (!isEmpty()) {
                return source.blockingGet();
            }
            return null;
        });
    }

    private Maybe<List<Ico>> getRemoteItemsIfRx(IcoStatus status, int limit) {
        Maybe<List<Ico>> remoteAny = null;
        switch (status) {
            case LIVE:
                remoteAny = remote.getLiveItemsRx(limit);
                break;
            case UPCOMING:
                remoteAny = remote.getUpcomingItemsRx(limit);
                break;
            case FINISHED:
                remoteAny = remote.getFinishedItemsRx(limit);
                break;
        }
        Maybe<List<Ico>> maybe = isIcoExpired(status)
                ? remoteAny
                : Maybe.empty();
        return maybe.filter(items -> !DataUtil.isEmpty(items))
                .doOnSuccess(items -> {
                    rx.compute(putItemsRx(items)).subscribe();
                    pref.commitIcoTime(status);
                });
    }

    private boolean isIcoExpired(IcoStatus status) {
        long lastTime = pref.getIcoTime(status);
        return TimeUtil.isExpired(lastTime, Constants.Delay.INSTANCE.getIco());
    }
}
