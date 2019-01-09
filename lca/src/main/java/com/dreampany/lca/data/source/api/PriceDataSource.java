package com.dreampany.lca.data.source.api;

import com.dreampany.frame.data.source.api.DataSource;
import com.dreampany.lca.data.model.Price;

import io.reactivex.Maybe;

/**
 * Created by Hawladar Roman on 7/23/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public interface PriceDataSource extends DataSource<Price> {

    Price getItem(String id);

    Maybe<Price> getItemRx(String id);
}
