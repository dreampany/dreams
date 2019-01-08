package com.dreampany.frame.data.source;

import com.dreampany.frame.data.model.Flag;

import java.util.List;

import io.reactivex.Maybe;

/**
 * Created by Hawladar Roman on 7/18/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public interface FlagDataSource extends DataSource<Flag> {

    boolean toggle(Flag flag);

    Maybe<Boolean> toggleRx(Flag flag);

    Flag getItem(long id, String type, String subtype);

    Maybe<Flag> getItemRx(long id, String type, String subtype);

    List<Flag> getItems(String type, String subtype);

    Maybe<List<Flag>> getItemsRx(String type, String subtype);

    List<Flag> getItems(String type, String subtype, int limit);

    Maybe<List<Flag>> getItemsRx(String type, String subtype, int limit);
}
