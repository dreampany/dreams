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

    void clear();

    Coin getItem(CoinSource source, String symbol, Currency currency);

    Maybe<Coin> getItemRx(CoinSource source, String symbol, Currency currency);

    List<Coin> getItems(CoinSource source, int index, int limit, Currency currency);

    Maybe<List<Coin>> getItemsRx(CoinSource source, int index, int limit, Currency currency);

    List<Coin> getItems(CoinSource source, String[] symbols, Currency currency);

    Maybe<List<Coin>> getItemsRx(CoinSource source, String[] symbols, Currency currency);

    boolean isFlagged(Coin coin);

    Maybe<Boolean> isFlaggedRx(Coin coin);

    long putFlag(Coin coin);

    Maybe<Long> putFlagRx(Coin coin);

    List<Long> putFlags(List<Coin> coins);

    Maybe<List<Long>> putFlagsRx(List<Coin> coins);

    boolean toggleFlag(Coin coin);

    Maybe<Boolean> toggleFlagRx(Coin coin);

    List<Coin> getFlags();

    Maybe<List<Coin>> getFlagsRx();

    List<Coin> getFlags(int limit);

    Maybe<List<Coin>> getFlagsRx(int limit);
}
