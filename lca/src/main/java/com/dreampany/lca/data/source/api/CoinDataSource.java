package com.dreampany.lca.data.source.api;

import com.dreampany.frame.data.source.DataSource;
import com.dreampany.lca.data.enums.CoinSource;
import com.dreampany.lca.data.model.Coin;

import java.util.List;

import io.reactivex.Maybe;

/**
 * Created by Hawladar Roman on 29/5/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public interface CoinDataSource extends DataSource<Coin> {


    List<Coin> getListing(CoinSource source);

    Maybe<List<Coin>> getListingRx(CoinSource source);

    List<Coin> getListing(CoinSource source, int limit);

    Maybe<List<Coin>> getListingRx(CoinSource source, int limit);

    List<Coin> getItems(CoinSource source, int start);

    Maybe<List<Coin>> getItemsRx(CoinSource source, int start);

    List<Coin> getItems(CoinSource source, int start, int limit);

    Maybe<List<Coin>> getItemsRx(CoinSource source, int start, int limit);

    Coin getItemByCoinId(long coinId);

    Maybe<Coin> getItemByCoinIdRx(long coinId);

    Maybe<List<Coin>> getItemsByCoinIdsRx(List<Long> coinIds);

    Coin getItemBySymbol(String symbol);

    Maybe<Coin> getItemBySymbolRx(String symbol);

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
