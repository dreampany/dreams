package com.dreampany.lca.data.source.api;

import com.dreampany.frame.data.source.api.DataSource;
import com.dreampany.lca.data.enums.CoinSource;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.model.Currency;

import io.reactivex.Maybe;

import java.util.List;

/**
 * Created by Hawladar Roman on 29/5/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public interface CoinDataSource extends DataSource<Coin> {

    Coin getItem(CoinSource source, Currency currency, long coinId);

    List<Coin> getItems(CoinSource source, Currency currency, long index, long limit, long lastUpdated);

    Maybe<List<Coin>> getItemsRx(CoinSource source, Currency currency, long index, long limit, long lastUpdated);

    Coin getItem(CoinSource source, Currency currency, long coinId, long lastUpdated);

    Maybe<Coin> getItemRx(CoinSource source, Currency currency, long coinId, long lastUpdated);

    List<Coin> getItems(CoinSource source, Currency currency, List<Long> coinIds, long lastUpdated);

    Maybe<List<Coin>> getItemsRx(CoinSource source, Currency currency, List<Long> coinIds, long lastUpdated);
}
