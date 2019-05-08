package com.dreampany.lca.data.source.api;

import com.dreampany.frame.data.source.api.DataSource;
import com.dreampany.lca.data.enums.CoinSource;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.enums.Currency;

import io.reactivex.Maybe;

import java.util.List;

/**
 * Created by Hawladar Roman on 29/5/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public interface CoinDataSource extends DataSource<Coin> {

    List<Coin> getItems(CoinSource source, Currency currency, int index, int limit);

    Maybe<List<Coin>> getItemsRx(CoinSource source, Currency currency, int index, int limit);

    Coin getItem(CoinSource source, Currency currency, long coinId);

    Maybe<Coin> getItemRx(CoinSource source, Currency currency, long coinId);

    List<Coin> getItems(CoinSource source, Currency currency, List<Long> coinIds);

    Maybe<List<Coin>> getItemsRx(CoinSource source, Currency currency, List<Long> coinIds);
}
