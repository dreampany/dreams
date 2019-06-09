package com.dreampany.lca.data.source.remote;

import com.dreampany.lca.data.misc.GraphMapper;
import com.dreampany.lca.data.model.Graph;
import com.dreampany.lca.data.source.api.GraphDataSource;
import com.dreampany.lca.misc.CoinMarketCapGraph;
import com.dreampany.network.manager.NetworkManager;

import java.util.List;

import javax.inject.Singleton;

import io.reactivex.Maybe;

/**
 * Created by Hawladar Roman on 30/5/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Singleton
public class GraphRemoteDataSource implements GraphDataSource {

    private final NetworkManager network;
    private final GraphMapper mapper;
    private final CoinMarketCapGraphService service;

    public GraphRemoteDataSource(NetworkManager network,
                                 GraphMapper mapper,
                                 @CoinMarketCapGraph CoinMarketCapGraphService service) {
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
        return null;
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
        return service
                .getGraphRx(slug, startTime, endTime)
                .map(cmcGraph -> {
                    cmcGraph.setStartTime(startTime);
                    cmcGraph.setEndTime(endTime);
                    return mapper.toGraph(cmcGraph);
                });
    }
}
