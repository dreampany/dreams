package com.dreampany.lca.data.source.api;

import com.dreampany.frame.data.source.api.DataSource;
import com.dreampany.lca.data.model.CoinAlert;

/**
 * Created by Hawladar Roman on 29/5/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public interface CoinAlertDataSource extends DataSource<CoinAlert> {

    void clear();

    boolean isExists(String symbol);

    CoinAlert getItem(String symbol);
}
