package com.dreampany.lca.data.source.remote;

import com.dreampany.lca.api.iwl.model.ApiIco;
import com.dreampany.lca.api.iwl.model.FinishedIcoResponse;
import com.dreampany.lca.api.iwl.model.LiveIcoResponse;
import com.dreampany.lca.api.iwl.model.UpcomingIcoResponse;
import com.dreampany.lca.data.enums.IcoStatus;
import com.dreampany.lca.data.misc.IcoMapper;
import com.dreampany.lca.data.model.Ico;
import com.dreampany.lca.data.source.api.IcoDataSource;
import com.dreampany.lca.misc.IcoWatchList;
import com.dreampany.network.manager.NetworkManager;

import java.util.List;

import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.functions.Function;

/**
 * Created by Hawladar Roman on 6/22/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Singleton
public class IcoRemoteDataSource implements IcoDataSource {

    private final NetworkManager network;
    private final IcoMapper mapper;
    private final IcoService service;

    public IcoRemoteDataSource(NetworkManager network,
                               IcoMapper mapper,
                               @IcoWatchList IcoService service) {
        this.network = network;
        this.mapper = mapper;
        this.service = service;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Maybe<Boolean> isEmptyRx() {
        return Maybe.fromCallable(this::isEmpty);
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
    public boolean isExists(Ico ico) {
        return false;
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
        return null;
    }

    @Override
    public Maybe<List<Long>> putItemsRx(List<Ico> icos) {
        return null;
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

    }

    @Override
    public Maybe<List<Ico>> getLiveItemsRx(int limit) {
        return convert(service.getLiveItemsRx().map(LiveIcoResponse::getIcos), IcoStatus.LIVE);
    }

    @Override
    public Maybe<List<Ico>> getUpcomingItemsRx(int limit) {
        return convert(service.getUpcomingItemsRx().map(UpcomingIcoResponse::getIcos), IcoStatus.UPCOMING);
    }

    @Override
    public Maybe<List<Ico>> getFinishedItemsRx(int limit) {
        return convert(service.getFinishedItemsRx().map(FinishedIcoResponse::getIcos), IcoStatus.FINISHED);
    }

    private Maybe<List<Ico>> convert(Maybe<List<ApiIco>> maybe, IcoStatus status) {
        return maybe.flatMap((Function<List<ApiIco>, MaybeSource<List<Ico>>>) apiIcos -> Flowable
                .fromIterable(apiIcos)
                .map(apiICO -> mapper.toIco(apiICO, status)).toList().toMaybe());
    }

/*    @Override
    public Completable putItem(Ico item) {
        return null;
    }

    @Override
    public Completable putItems(List<Ico> items) {
        return null;
    }

    @Override
    public Maybe<List<Ico>> getLiveItems(int limit) {
        return convert(service.getLiveItems().map(LiveIcoResponse::getIcos), IcoStatus.LIVE);
    }

    @Override
    public Maybe<List<Ico>> getUpcomingItems(int limit) {
        return convert(service.getUpcomingItems().map(UpcomingIcoResponse::getIcos), IcoStatus.UPCOMING);
    }

    @Override
    public Maybe<List<Ico>> getFinishedItems(int limit) {
        return convert(service.getFinishedItems().map(FinishedIcoResponse::getIcos), IcoStatus.FINISHED);

    }

    */
}
