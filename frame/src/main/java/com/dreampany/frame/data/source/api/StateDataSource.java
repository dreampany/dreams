package com.dreampany.frame.data.source.api;

import com.dreampany.frame.data.model.State;

import java.util.List;

import io.reactivex.Maybe;

/**
 * Created by Hawladar Roman on 7/18/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public interface StateDataSource extends DataSource<State> {

    int getCount(long id, String type, String subtype);

    int getCount(String type, String subtype, String state);

    Maybe<Integer> getCountRx(String type, String subtype, String state);

    int getCount(long id, String type, String subtype, String state);

    Maybe<Integer> getCountRx(long id, String type, String subtype, String state);

    State getItem(long id, String type, String subtype, String state);

    Maybe<State> getItemRx(long id, String type, String subtype, String state);

    Maybe<State> getItemOrderByRx(String type, String subtype, String state, long from, long to);

    List<State> getItems(String type, String subtype);

    List<State> getItems(long id, String type, String subtype);

    State getItemOrderBy(String type, String subtype);

    Maybe<State> getItemNotStateOrderByRx(String type, String subtype, String state);

    Maybe<State> getItemOrderByRx(String type, String subtype);

    Maybe<List<State>> getItemsRx(String type, String subtype);

    List<State> getItemsOrderBy(String type, String subtype, long from, long to);

    Maybe<List<State>> getItemsOrderByRx(String type, String subtype, long from, long to);

    List<State> getItems(String type, String subtype, String state);

    Maybe<List<State>> getItemsRx(String type, String subtype, String state);

    List<State> getItemsOrderBy(String type, String subtype);

    Maybe<List<State>> getItemsOrderByRx(String type, String subtype);

    List<State> getItemsOrderBy(String type, String subtype, String state);

    Maybe<List<State>> getItemsOrderByRx(String type, String subtype, String state);

    List<State> getItemsOrderBy(String type, String subtype, int limit);

    Maybe<List<State>> getItemsOrderByRx(String type, String subtype, int limit);

    List<State> getItemsOrderBy(String type, String subtype, String state, int limit);

    Maybe<List<State>> getItemsOrderByRx(String type, String subtype, String state, int limit);

    List<State> getItems(String type, String subtype, int limit);

    Maybe<List<State>> getItemsRx(String type, String subtype, int limit);
}
