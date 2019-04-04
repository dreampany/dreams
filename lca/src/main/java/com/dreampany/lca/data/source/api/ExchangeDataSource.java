package com.dreampany.lca.data.source.api;

import com.dreampany.frame.data.source.api.DataSource;
import com.dreampany.lca.data.model.Exchange;

import java.util.List;

import io.reactivex.Maybe;

/**
 * Created by Hawladar Roman on 6/26/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public interface ExchangeDataSource extends DataSource<Exchange> {

    List<Exchange> getItems(String symbol, int limit);

    Maybe<List<Exchange>> getItemsRx(String symbol, int limit);
}
