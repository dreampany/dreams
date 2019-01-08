package com.dreampany.frame.data.source;

import com.dreampany.frame.data.model.Store;

import java.util.List;

import io.reactivex.Maybe;

/**
 * Created by Hawladar Roman on 7/18/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public interface StoreDataSource extends DataSource<Store> {

    List<String> getItemsOf(String type, String subtype, String state);

    Maybe<List<String>> getItemsOfRx(String type, String subtype, String state);

    Maybe<List<String>> getItemsOfRx(String type, String subtype, String state,  int limit);

    List<Store> getItems(String type, String subtype, String state);

    Maybe<List<Store>> getItemsRx(String type, String subtype, String state);
}
