package com.dreampany.frame.data.source.room;

import com.dreampany.frame.data.misc.StateMapper;
import com.dreampany.frame.data.model.State;
import com.dreampany.frame.data.source.api.StateDataSource;
import com.dreampany.frame.data.source.dao.StateDao;

import java.util.List;

import javax.inject.Singleton;

import io.reactivex.Maybe;

/**
 * Created by Hawladar Roman on 7/18/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Singleton
public class StateRoomDataSource implements StateDataSource {

    private final StateMapper mapper;
    private final StateDao dao;

    public StateRoomDataSource(StateMapper mapper,
                               StateDao dao) {
        this.mapper = mapper;
        this.dao = dao;
    }

    @Override
    public int getCountById(String id, String type, String subtype) {
        return dao.getCountById(id, type, subtype);
    }

    @Override
    public Maybe<Integer> getCountByIdRx(String id, String type, String subtype) {
        return null;
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
    public int getCount(String type, String subtype, String state) {
        return dao.getCount(type, subtype, state);
    }

    @Override
    public Maybe<Integer> getCountRx(String type, String subtype, String state) {
        return dao.getCountRx(type, subtype, state);
    }

    @Override
    public int getCountById(String id, String type, String subtype, String state) {
        return dao.getCount(id, type, subtype, state);
    }

    @Override
    public Maybe<Integer> getCountRx(String id, String type, String subtype, String state) {
        return dao.getCountRx(id, type, subtype, state);
    }

    @Override
    public State getItem(String id, String type, String subtype, String state) {
        State item = mapper.getItem(id, type, subtype, state);
        if (item == null) {
            item = dao.getItem(id, type, subtype, state);
        }
        if (item == null) {
            item = new State(id, type, subtype, state);
        }
        mapper.putItem(item);
        return item;
    }

    @Override
    public Maybe<State> getItemRx(String id, String type, String subtype, String state) {
        if (mapper.isExists(id, type, subtype, state)) {
            return Maybe.fromCallable(() -> mapper.getItem(id, type, subtype, state));
        }
        return dao.getItemRx(id, type, subtype, state)
                .doOnSuccess(mapper::putItem);
    }

    @Override
    public Maybe<State> getItemOrderByRx(String type, String subtype, String state, long from, long to) {
        return dao.getItemOrderByRx(type, subtype, state, from, to);
    }

    @Override
    public List<State> getItems(String type, String subtype) {
        return dao.getItems(type, subtype);
    }


    @Override
    public State getItemOrderBy(String type, String subtype) {
        return null;
    }

    @Override
    public Maybe<State> getItemNotStateOrderByRx(String type, String subtype, String state) {
        return dao.getItemNotStateOrderByRx(type, subtype, state);
    }

    @Override
    public Maybe<State> getItemOrderByRx(String type, String subtype) {
        return null;
    }

    @Override
    public Maybe<List<State>> getItemsRx(String type, String subtype) {
        return dao.getItemsRx(type, subtype);
    }

    @Override
    public List<State> getItemsOrderBy(String type, String subtype, long from, long to) {
        return dao.getItemsOrderBy(type, subtype, from, to);
    }

    @Override
    public Maybe<List<State>> getItemsOrderByRx(String type, String subtype, long from, long to) {
        return dao.getItemsOrderByRx(type, subtype, from, to);
    }

    @Override
    public State getItem(String type, String subtype, String state) {
        return dao.getItem(type, subtype, state);
    }

    @Override
    public Maybe<State> getItemRx(String type, String subtype, String state) {
        return dao.getItemRx(type, subtype, state);
    }

    @Override
    public List<State> getItems(String type, String subtype, String state) {
        return dao.getItemsWithoutId(type, subtype, state);
    }

    @Override
    public Maybe<List<State>> getItemsRx(String type, String subtype, String state) {
        return dao.getItemsRx(type, subtype, state);
    }

    @Override
    public List<State> getItemsOrderBy(String type, String subtype) {
        return null;
    }

    @Override
    public Maybe<List<State>> getItemsOrderByRx(String type, String subtype) {
        return dao.getItemsOrderByRx(type, subtype);
    }

    @Override
    public List<State> getItemsOrderBy(String type, String subtype, String state) {
        return dao.getItemsOrderBy(type, subtype, state);
    }

    @Override
    public Maybe<List<State>> getItemsOrderByRx(String type, String subtype, String state) {
        return dao.getItemsOrderByRx(type, subtype, state);
    }

    @Override
    public List<State> getItemsOrderBy(String type, String subtype, int limit) {
        return null;
    }

    @Override
    public Maybe<List<State>> getItemsOrderByRx(String type, String subtype, int limit) {
        return dao.getItemsOrderByRx(type, subtype, limit);
    }

    @Override
    public List<State> getItemsOrderBy(String type, String subtype, String state, int limit) {
        return dao.getItemsOrderBy(type, subtype, state, limit);
    }

    @Override
    public Maybe<List<State>> getItemsOrderByRx(String type, String subtype, String state, int limit) {
        return dao.getItemsOrderByRx(type, subtype, state, limit);
    }

    @Override
    public List<State> getItems(String type, String subtype, int limit) {
        return dao.getItems(type, subtype, limit);
    }

    @Override
    public Maybe<List<State>> getItemsRx(String type, String subtype, int limit) {
        return dao.getItemsRx(type, subtype, limit);
    }

    @Override
    public boolean isExists(State state) {
        return dao.getCount(state.getId(), state.getType(), state.getSubtype(), state.getState()) > 0;
    }

    @Override
    public Maybe<Boolean> isExistsRx(State state) {
        return Maybe.fromCallable(() -> isExists(state));
    }

    @Override
    public long putItem(State state) {
        return dao.insertOrReplace(state);
    }

    @Override
    public Maybe<Long> putItemRx(State state) {
        return Maybe.fromCallable(() -> putItem(state));
    }

    @Override
    public List<Long> putItems(List<State> states) {
        return dao.insertOrReplace(states);
    }

    @Override
    public Maybe<List<Long>> putItemsRx(List<State> states) {
        return Maybe.fromCallable(() -> putItems(states));
    }

    @Override
    public int delete(State state) {
        return dao.delete(state);
    }

    @Override
    public Maybe<Integer> deleteRx(State state) {
        return null;
    }

    @Override
    public List<Long> delete(List<State> states) {
        return null;
    }

    @Override
    public Maybe<List<Long>> deleteRx(List<State> states) {
        return null;
    }

    @Override
    public State getItem(String id) {
        return null;
    }

    @Override
    public Maybe<State> getItemRx(String id) {
        return null;
    }

    @Override
    public List<State> getItems() {
        return dao.getItems();
    }

    @Override
    public Maybe<List<State>> getItemsRx() {
        return dao.getItemsRx();
    }

    @Override
    public List<State> getItems(int limit) {
        return dao.getItems(limit);
    }

    @Override
    public Maybe<List<State>> getItemsRx(int limit) {
        return dao.getItemsRx(limit);
    }
}
