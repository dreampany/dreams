package com.dreampany.frame.data.source.repository;

import com.dreampany.frame.data.model.State;
import com.dreampany.frame.data.source.StateDataSource;
import com.dreampany.frame.misc.Room;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import hugo.weaving.DebugLog;
import io.reactivex.Maybe;

/**
 * Created by Hawladar Roman on 7/19/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Singleton
public class StateRepository extends Repository<Long, State> implements StateDataSource {

    private final StateDataSource room;

    @DebugLog
    @Inject
    StateRepository(RxMapper rx,
                    ResponseMapper rm,
                    @Room StateDataSource room) {
        super(rx, rm);
        this.room = room;
    }

    @Override
    public int getCount(long id, String type, String subtype) {
        return room.getCount(id, type, subtype);
    }

    @Override
    public boolean isEmpty() {
        return room.isEmpty();
    }

    @Override
    public Maybe<Boolean> isEmptyRx() {
        return Maybe.fromCallable(this::isEmpty);
    }

    @Override
    public int getCount(String type, String subtype, String state) {
        return room.getCount(type, subtype, state);
    }

    @Override
    public int getCount(String type, String subtype, String state, String substate) {
        return room.getCount(type, subtype, state, substate);
    }

    @Override
    public int getCount(long id, String type, String subtype, String state) {
        return room.getCount(id, type, subtype, state);
    }

    @Override
    public int getCount(long id, String type, String subtype, String state, String substate) {
        return room.getCount(id, type, subtype, state, substate);
    }

    @Override
    public Maybe<Integer> getCountRx(String type, String subtype, String state) {
        return room.getCountRx(type, subtype, state);
    }

    @Override
    public Maybe<Integer> getCountRx(long id, String type, String subtype, String state) {
        return room.getCountRx(id, type, subtype, state);
    }

    @Override
    public State getItem(long id, String type, String subtype, String state) {
        return room.getItem(id, type, subtype, state);
    }

    @Override
    public Maybe<State> getItemRx(long id, String type, String subtype, String state) {
        return room.getItemRx(id, type, subtype, state);
    }

    @Override
    public Maybe<State> getItemOrderByRx(String type, String subtype, String state, long from, long to) {
        return room.getItemOrderByRx(type, subtype, state, from, to);
    }

    @Override
    public int getCount() {
        return room.getCount();
    }

    @Override
    public Maybe<Integer> getCountRx() {
        return room.getCountRx();
    }

    @Override
    public List<State> getItems(String type, String subtype) {
        return room.getItems(type, subtype);
    }

    @Override
    public List<State> getItems(long id, String type, String subtype) {
        return room.getItems(id, type, subtype);
    }

    @Override
    public State getItemOrderBy(String type, String subtype) {
        return null;
    }

    @Override
    public Maybe<State> getItemNotStateOrderByRx(String type, String subtype, String state) {
        return room.getItemNotStateOrderByRx(type, subtype, state);
    }

    @Override
    public Maybe<State> getItemOrderByRx(String type, String subtype) {
        return room.getItemOrderByRx(type, subtype);
    }

    @Override
    public Maybe<List<State>> getItemsRx(String type, String subtype) {
        return room.getItemsRx(type, subtype);
    }

    @Override
    public List<State> getItemsOrderBy(String type, String subtype, long from, long to) {
        return room.getItemsOrderBy(type, subtype, from, to);
    }

    @Override
    public Maybe<List<State>> getItemsOrderByRx(String type, String subtype, long from, long to) {
        return room.getItemsOrderByRx(type, subtype, from, to);
    }

    @Override
    public List<State> getItems(String type, String subtype, String state) {
        return null;
    }

    @Override
    public Maybe<List<State>> getItemsRx(String type, String subtype, String state) {
        return room.getItemsRx(type, subtype, state);
    }

    @Override
    public List<State> getItemsOrderBy(String type, String subtype) {
        return room.getItemsOrderBy(type, subtype);
    }

    @Override
    public Maybe<List<State>> getItemsOrderByRx(String type, String subtype) {
        return room.getItemsOrderByRx(type, subtype);
    }

    @Override
    public List<State> getItemsOrderBy(String type, String subtype, String state) {
        return room.getItemsOrderBy(type, subtype, state);
    }

    @Override
    public Maybe<List<State>> getItemsOrderByRx(String type, String subtype, String state) {
        return room.getItemsOrderByRx(type, subtype, state);
    }

    @Override
    public List<State> getItemsOrderBy(String type, String subtype, int limit) {
        return null;
    }

    @Override
    public Maybe<List<State>> getItemsOrderByRx(String type, String subtype, int limit) {
        return room.getItemsOrderByRx(type, subtype, limit);
    }

    @Override
    public List<State> getItemsOrderBy(String type, String subtype, String state, int limit) {
        return room.getItemsOrderBy(type, subtype, state, limit);
    }

    @Override
    public Maybe<List<State>> getItemsOrderByRx(String type, String subtype, String state, int limit) {
        return room.getItemsOrderByRx(type, subtype, state, limit);
    }

    @Override
    public List<State> getItems(String type, String subtype, int limit) {
        return room.getItems(type, subtype, limit);
    }

    @Override
    public Maybe<List<State>> getItemsRx(String type, String subtype, int limit) {
        return room.getItemsRx(type, subtype, limit);
    }

    @Override
    public boolean isExists(State state) {
        return room.isExists(state);
    }

    @Override
    public Maybe<Boolean> isExistsRx(State state) {
        return room.isExistsRx(state);
    }

    @Override
    public long putItem(State state) {
        return room.putItem(state);
    }

    @Override
    public Maybe<Long> putItemRx(State state) {
        return room.putItemRx(state);
    }

    @Override
    public List<Long> putItems(List<State> states) {
        return room.putItems(states);
    }

    @Override
    public Maybe<List<Long>> putItemsRx(List<State> states) {
        return room.putItemsRx(states);
    }

    @Override
    public State getItem(long id) {
        return room.getItem(id);
    }

    @Override
    public Maybe<State> getItemRx(long id) {
        return room.getItemRx(id);
    }

    @Override
    public List<State> getItems() {
        return room.getItems();
    }

    @Override
    public Maybe<List<State>> getItemsRx() {
        return room.getItemsRx();
    }

    @Override
    public List<State> getItems(int limit) {
        return room.getItems(limit);
    }

    @Override
    public Maybe<List<State>> getItemsRx(int limit) {
        return room.getItemsRx(limit);
    }
}
