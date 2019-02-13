package com.dreampany.lca.data.source.remote;

import com.dreampany.lca.api.cmc.model.*;
import com.dreampany.lca.data.enums.CoinSource;
import com.dreampany.lca.data.misc.CoinMapper;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.model.Currency;
import com.dreampany.lca.data.source.api.CoinDataSource;
import com.dreampany.lca.misc.CoinMarketCap;
import com.dreampany.lca.misc.Constants;
import com.dreampany.network.NetworkManager;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.functions.Function;

import javax.inject.Singleton;
import java.util.Collection;
import java.util.List;

/**
 * Created by Hawladar Roman on 29/5/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Singleton
public class CoinRemoteDataSource implements CoinDataSource {

    private final NetworkManager network;
    private final CoinMapper mapper;
    private final CmcService service;

    public CoinRemoteDataSource(NetworkManager network,
                                CoinMapper mapper,
                                @CoinMarketCap CmcService service) {
        this.network = network;
        this.mapper = mapper;
        this.service = service;
    }

    @Override
    public boolean isEmpty() {
        return getCount() == 0;
    }

    @Override
    public Maybe<Boolean> isEmptyRx() {
        return Maybe.fromCallable(this::isEmpty);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Maybe<Integer> getCountRx() {
        return null;
    }

    @Override
    public boolean isExists(Coin coin) {
        return false;
    }

    @Override
    public Maybe<Boolean> isExistsRx(Coin coin) {
        return null;
    }

    @Override
    public long putItem(Coin coin) {
        return 0;
    }

    @Override
    public Maybe<Long> putItemRx(Coin coin) {
        return null;
    }

    @Override
    public List<Long> putItems(List<Coin> coins) {
        return null;
    }

    @Override
    public Maybe<List<Long>> putItemsRx(List<Coin> coins) {
        return null;
    }

    @Override
    public int delete(Coin coin) {
        return 0;
    }

    @Override
    public Maybe<Integer> deleteRx(Coin coin) {
        return null;
    }

    @Override
    public List<Long> delete(List<Coin> coins) {
        return null;
    }

    @Override
    public Maybe<List<Long>> deleteRx(List<Coin> coins) {
        return null;
    }

    @Override
    public Coin getItem(long id) {
/*        try {
            Response<CMCCoinResponse> response = service.getCoin(id, Constants.Structure.ARRAY).execute();
            if (response.isSuccessful()) {
                return getCoins(Objects.requireNonNull(response.body()));
            }
        } catch (IOException | RuntimeException e) {
            Timber.e(e);
        }*/
        return null;
    }

    @Override
    public Maybe<Coin> getItemRx(long id) {
        return null;
    }

    @Override
    public List<Coin> getItems() {
        return null;
    }

    @Override
    public Maybe<List<Coin>> getItemsRx() {
        return Maybe.empty();
/*        return service
                .getCoinsRx(Constants.Structure.ARRAY)
                .flatMap((Function<CMCCoinsResponse, MaybeSource<List<Coin>>>) this::getItemsRx);*/
    }

    @Override
    public List<Coin> getItems(int limit) {
        return null;
    }

    @Override
    public Maybe<List<Coin>> getItemsRx(int limit) {
        return null;
    }


/*    @Override
    public Maybe<List<Coin>> getItemsRx(CoinSource source) {
        return Maybe.fromCallable(new Callable<List<Coin>>() {
            @Override
            public List<Coin> call() throws Exception {
                Response<CmcListingResponseV1> response = service.getListing(Constants.Key.CMC_PRO, Constants.Limit.COIN_DEFAULT_INDEX, Constants.Limit.COIN).execute();

                CmcListingResponseV1 result = response.body();
                return new ArrayList<>();
            }
        });
        return getListingRx(source, Constants.Limit.COIN_DEFAULT_INDEX, Constants.Limit.COIN_PAGE);
    }*/

/*    @Override
    public Maybe<List<Coin>> getListingRx(CoinSource source, int start, int limit) {
        return service
                .getListingRx(Constants.Key.CMC_PRO, start, limit)
                .flatMap((Function<CmcListingResponse, MaybeSource<List<Coin>>>) this::getItemsRx);
    }*/

    @Override
    public void clear() {

    }

    @Override
    public Coin getItem(CoinSource source, String symbol, Currency currency) {
        return null;
    }

    @Override
    public Maybe<Coin> getItemRx(CoinSource source, String symbol, Currency currency) {
        return service
                .getQuotesRx(Constants.Key.CMC_PRO, symbol, currency.name())
                .flatMap((Function<CmcQuotesResponse, MaybeSource<Coin>>) this::getItemRx);
    }

    /**
     * @param source
     * @param index      >= 0
     * @param limit
     * @param currency
     * @return
     */
    @Override
    public List<Coin> getItems(CoinSource source, int index, int limit, Currency currency) {
        return null;
    }

    @Override
    public Maybe<List<Coin>> getItemsRx(CoinSource source, int index, int limit, Currency currency) {
        int start = index + 1;
        return service
                .getListingRx(Constants.Key.CMC_PRO, start, limit, currency.name())
                .flatMap((Function<CmcListingResponse, MaybeSource<List<Coin>>>) this::getItemsRx);
    }

    @Override
    public List<Coin> getItems(CoinSource source, String[] symbols, Currency currency) {
        return null;
    }

    @Override
    public Maybe<List<Coin>> getItemsRx(CoinSource source, String[] symbols, Currency currency) {
        String symbol = mapper.joinString(symbols, Constants.Sep.SEP_COMMA);
        return service
                .getQuotesRx(Constants.Key.CMC_PRO, symbol, currency.name())
                .flatMap((Function<CmcQuotesResponse, MaybeSource<List<Coin>>>) this::getItemsRx);
    }

    private Coin getItem(CmcCoinResponse response) {
        Coin result = null;
/*        if (!response.hasError()) {
            result = mapper.toItem(response.getCoins(), true);
        }*/
        return result;
    }

/*    private List<Coin> getCoins(CmcCoinListingResponse response) {
        if (response != null && !response.hasError()) {
            Collection<CmcCoin> items = response.getData();
            if (!DataUtil.isEmpty(items)) {
                List<Coin> result = new ArrayList<>(items.size());
                Stream.of(items).forEach(item -> result.add(mapper.toItem(item, false)));
                return result;
            }
        }
        return null;
    }*/

    private List<Coin> getItems(CmcCoinsResponse response) {
/*        if (!response.hasError()) {
            List<CmcCoin> items = response.getData();
            if (!DataUtil.isEmpty(items)) {
                List<Coin> result = new ArrayList<>(items.size());
                Stream.of(items).forEach(item -> result.add(mapper.toItem(item, true)));
                return result;
            }
        }*/
        return null;
    }

    private Maybe<Coin> getItemRx(CmcCoinResponse response) {
/*        if (!response.hasError()) {
            CmcCoin item = response.getCoins();
            return Maybe.just(item).map(in -> mapper.toItem(in, true));
        }*/
        return null;
    }

    private Maybe<Coin> getItemRx(CmcQuotesResponse response) {
        if (response.hasError() || !response.hasData()) {
            return null;
        }
        CmcCoin item = response.getFirst();
        return Maybe.just(item).map(in -> mapper.toItem(in, true));
    }

    private Maybe<List<Coin>> getItemsRx(CmcListingResponse response) {
        if (response.hasError()) {
            return null;
        }
        Collection<CmcCoin> items = response.getData();
        return Flowable.fromIterable(items)
                .map(in -> mapper.toItem(in, true))
                .toSortedList((left, right) -> left.getRank() - right.getRank())
                .toMaybe();
    }

    private Maybe<List<Coin>> getItemsRx(CmcQuotesResponse response) {
        if (response.hasError() || !response.hasData()) {
            return null;
        }
        Collection<CmcCoin> items = response.getData().values();
        return Flowable.fromIterable(items)
                .map(in -> mapper.toItem(in, true))
                .toSortedList((left, right) -> left.getRank() - right.getRank())
                .toMaybe();
    }

    private Maybe<List<Coin>> getItemsRx(CmcCoinsResponse response) {
/*        if (!response.hasError()) {
            Collection<CmcCoin> items = response.getData();
            return Flowable.fromIterable(items)
                    .map(in -> mapper.toItem(in, true))
                    .toList()
                    .toMaybe();
        }*/
        return null;
    }
}
