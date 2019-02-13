package com.dreampany.lca.data.source.repository;

import com.dreampany.frame.data.source.repository.Repository;
import com.dreampany.frame.misc.Room;
import com.dreampany.frame.misc.Remote;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.lca.data.model.Graph;
import com.dreampany.lca.data.source.api.GraphDataSource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Maybe;

/**
 * Created by Hawladar Roman on 6/26/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Singleton
public class GraphRepository extends Repository<Long, Graph> implements GraphDataSource {

    private final GraphDataSource local;
    private final GraphDataSource remote;

    @Inject
    GraphRepository(RxMapper rx,
                    ResponseMapper rm,
                    @Room GraphDataSource local,
                    @Remote GraphDataSource remote) {
        super(rx, rm);
        this.local = local;
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
        return local.getCount();
    }

    @Override
    public Maybe<Integer> getCountRx() {
        return null;
    }

    @Override
    public boolean isExists(Graph graph) {
        return local.isExists(graph);
    }

    @Override
    public Maybe<Boolean> isExistsRx(Graph graph) {
        return null;
    }

    @Override
    public long putItem(Graph graph) {
        return local.putItem(graph);
    }

    @Override
    public Maybe<Long> putItemRx(Graph graph) {
        return local.putItemRx(graph);
    }

    @Override
    public List<Long> putItems(List<Graph> graphs) {
        return null;
    }

    @Override
    public Maybe<List<Long>> putItemsRx(List<Graph> graphs) {
        return local.putItemsRx(graphs);
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
    public List<Long> delete(List<Graph> graphs) {
        return null;
    }

    @Override
    public Maybe<List<Long>> deleteRx(List<Graph> graphs) {
        return null;
    }

    @Override
    public Graph getItem(long id) {
        return null;
    }

    @Override
    public Maybe<Graph> getItemRx(long id) {
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
        return getWithSave(remote.getItemRx(slug, startTime, endTime));
    }

    private Maybe<Graph> getWithSave(Maybe<Graph> maybe) {
        return maybe
                .doOnSuccess(graph -> rx.compute(putItemRx(graph)).subscribe());
    }
}
