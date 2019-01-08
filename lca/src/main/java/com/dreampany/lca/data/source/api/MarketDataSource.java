package com.dreampany.lca.data.source.api;

import com.dreampany.frame.data.source.DataSource;
import com.dreampany.lca.data.model.Market;

import java.util.List;

import io.reactivex.Maybe;

/**
 * Created by Hawladar Roman on 6/26/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public interface MarketDataSource extends DataSource<Market> {

    List<Market> getItems(String fromSymbol, String toSymbol, int limit);

    Maybe<List<Market>> getItemsRx(String fromSymbol, String toSymbol, int limit);
}
