package com.dreampany.lca.data.source.repository;

import com.dreampany.framework.data.source.repository.Repository;
import com.dreampany.framework.injector.annote.Remote;
import com.dreampany.framework.misc.ResponseMapper;
import com.dreampany.framework.injector.annote.Room;
import com.dreampany.framework.misc.RxMapper;
import com.dreampany.framework.util.TimeUtil;
import com.dreampany.lca.data.enums.Currency;
import com.dreampany.lca.data.model.Graph;
import com.dreampany.lca.data.source.api.GraphDataSource;
import com.dreampany.lca.data.source.pref.Pref;
import com.dreampany.lca.misc.Constants;
import com.dreampany.network.manager.NetworkManager;
import io.reactivex.Maybe;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Created by Hawladar Roman on 6/26/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Singleton
public class GraphRepository extends Repository<Long, Graph> implements GraphDataSource {

    private final NetworkManager network;
    private final Pref pref;
    private final GraphDataSource room;
    private final GraphDataSource remote;

    @Inject
    GraphRepository(RxMapper rx,
                    ResponseMapper rm,
                    NetworkManager network,
                    Pref pref,
                    @Room GraphDataSource room,
                    @Remote GraphDataSource remote) {
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
    public boolean isExists(Graph graph) {
        return room.isExists(graph);
    }

    @Override
    public Maybe<Boolean> isExistsRx(Graph graph) {
        return null;
    }

    @Override
    public long putItem(Graph graph) {
        return room.putItem(graph);
    }

    @Override
    public Maybe<Long> putItemRx(Graph graph) {
        return room.putItemRx(graph);
    }

    @Override
    public List<Long> putItems(List<? extends Graph> graphs) {
        return null;
    }

    @Override
    public Maybe<List<Long>> putItemsRx(List<? extends Graph> graphs) {
        return room.putItemsRx(graphs);
    }

    @Override
    public int delete(Graph graph) {
        return 0;
    }

    @Override
    public Maybe<Integer> deleteRx(Graph graph) {
        return null;
    }

    @Override
    public List<Long> delete(List<? extends Graph> graphs) {
        return null;
    }

    @Override
    public Maybe<List<Long>> deleteRx(List<? extends Graph> graphs) {
        return null;
    }

    @Override
    public Graph getItem(String id) {
        return null;
    }

    @Override
    public Maybe<Graph> getItemRx(String id) {
        return null;
    }

    @Override
    public List<Graph> getItems() {
        return null;
    }

    @Override
    public Maybe<List<Graph>> getItemsRx() {
        return null;
    }

    @Override
    public List<Graph> getItems(int limit) {
        return null;
    }

    @Override
    public Maybe<List<Graph>> getItemsRx(int limit) {
        return null;
    }

    @Override
    public Graph getItem(String slug, long startTime, long endTime) {
        return null;
    }

    @Override
    public Maybe<Graph> getItemRx(String slug, long startTime, long endTime) {
        return contactSingleSuccess(
                remote.getItemRx(slug, startTime, endTime),
                graph -> rx.compute(putItemRx(graph)).subscribe()
        );
    }

    private boolean isGraphExpired(String symbol, Currency currency) {
        long time = pref.getGraphTime(symbol, currency.name());
        return TimeUtil.isExpired(time, Constants.Time.INSTANCE.getGraph());
    }
}
