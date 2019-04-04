package com.dreampany.frame.data.source.api;

import java.util.List;

import io.reactivex.Maybe;

/**
 * Created by Hawladar Roman on 4/8/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public interface DataSource<T> {

    boolean isEmpty();

    Maybe<Boolean> isEmptyRx();

    long getCount();

    Maybe<Long> getCountRx();

    boolean isExists(T t);

    Maybe<Boolean> isExistsRx(T t);

    long putItem(T t);

    Maybe<Long> putItemRx(T t);

    List<Long> putItems(List<T> ts);

    Maybe<List<Long>> putItemsRx(List<T> ts);

    int delete(T t);

    Maybe<Integer> deleteRx(T t);

    List<Long> delete(List<T> ts);

    Maybe<List<Long>> deleteRx(List<T> ts);

    T getItem(long id);

    Maybe<T> getItemRx(long id);

    List<T> getItems();

    Maybe<List<T>> getItemsRx();

    List<T> getItems(long limit);

    Maybe<List<T>> getItemsRx(long limit);
}
