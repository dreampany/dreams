package com.dreampany.lca.data.source.room;

import com.dreampany.lca.data.misc.GraphMapper;
import com.dreampany.lca.data.model.Graph;
import com.dreampany.lca.data.source.api.GraphDataSource;

import java.util.List;

import javax.inject.Singleton;

import com.dreampany.lca.data.source.dao.GraphDao;
import io.reactivex.Maybe;

/**
 * Created by Hawladar Roman on 30/5/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Singleton
public class GraphRoomDataSource implements GraphDataSource {

    private final GraphMapper mapper;
    private final GraphDao dao;

    public GraphRoomDataSource(GraphMapper mapper,
                               GraphDao dao) {
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
    public boolean isExists(Graph graph) {
        return false;
    }

    @Override
    public Maybe<Boolean> isExistsRx(Graph graph) {
        return null;
    }

    @Override
    public long putItem(Graph graph) {
        return 0;
    }

    @Override
    public Maybe<Long> putItemRx(Graph graph) {
        return Maybe.fromCallable(() -> putItem(graph));
    }

    @Override
    public List<Long> putItems(List<Graph> graphs) {
        return null;
    }

    @Override
    public Maybe<List<Long>> putItemsRx(List<Graph> graphs) {
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
        return dao.getItemRx(slug, startTime, endTime);
    }
}
