package com.dreampany.lca.data.source.remote;

import com.dreampany.frame.misc.exception.EmptyException;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.frame.util.TimeUtil;
import com.dreampany.lca.api.cmc.model.CmcCoin;
import com.dreampany.lca.api.cmc.model.CmcListingResponse;
import com.dreampany.lca.api.cmc.model.CmcQuotesResponse;
import com.dreampany.lca.data.enums.CoinSource;
import com.dreampany.lca.data.misc.CoinMapper;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.model.Currency;
import com.dreampany.lca.data.source.api.CoinDataSource;
import com.dreampany.lca.misc.CoinMarketCap;
import com.dreampany.lca.misc.Constants;
import com.dreampany.network.manager.NetworkManager;
import com.google.common.collect.Maps;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.apache.commons.lang3.tuple.MutablePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import retrofit2.Response;
import timber.log.Timber;

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

    private final List<String> keys;
    private final CircularFifoQueue<Integer> indexQueue;
    private final Map<Integer, MutablePair<Long, Integer>> indexStatus;

    public CoinRemoteDataSource(NetworkManager network,
                                CoinMapper mapper,
                                @CoinMarketCap CmcService service) {
        this.network = network;
        this.mapper = mapper;
        this.service = service;
        keys = Collections.synchronizedList(new ArrayList<>());
        keys.add(Constants.Key.CMC_PRO_ROMAN_BJIT);
        keys.add(Constants.Key.CMC_PRO_IFTE_NET);
        keys.add(Constants.Key.CMC_PRO_DREAMPANY);

        indexQueue = new CircularFifoQueue<>(keys.size());
        indexStatus = Maps.newConcurrentMap();
        for (int index = 0; index < keys.size(); index++) {
            indexQueue.add(index);
            indexStatus.put(index, MutablePair.of(TimeUtil.currentTime(), 0));
        }
    }

    @Override
    public List<Coin> getItems(CoinSource source, Currency currency, int index, int limit) {
        if (network.isObserving() && !network.hasInternet()) {
            return null;
        }
        int start = index + 1;
        for (int loop = 0; loop < keys.size(); loop++) {
            String apiKey = getApiKey();
            try {
                Response<CmcListingResponse> response = service.getListing(apiKey, currency.name(), start, limit).execute();
                if (response.isSuccessful()) {
                    CmcListingResponse result = response.body();
                    return getItemsRx(source, result).blockingGet();
                }
            } catch (IOException | RuntimeException e) {
                Timber.e(e);
                iterateQueue();
            }
        }
        return null;
    }

    @Override
    public Maybe<List<Coin>> getItemsRx(CoinSource source, Currency currency, int index, int limit) {
        return Maybe.create(emitter -> {
            List<Coin> result = getItems(source, currency, index, limit);
            if (emitter.isDisposed()) {
                throw new IllegalStateException();
            }
            if (DataUtil.isEmpty(result)) {
                emitter.onError(new EmptyException());
            } else {
                emitter.onSuccess(result);
            }
        });
    }

    @Override
    public Coin getItem(CoinSource source, Currency currency, long coinId) {
        if (network.isObserving() && !network.hasInternet()) {
            return null;
        }
        String ids = String.valueOf(coinId);
        for (int loop = 0; loop < keys.size(); loop++) {
            String apiKey = getApiKey();
            try {
                Response<CmcQuotesResponse> response = service.getQuotesByIds(apiKey, currency.name(), ids).execute();
                if (response.isSuccessful()) {
                    CmcQuotesResponse result = response.body();
                    return getItemRx(source, result).blockingGet();
                }
            } catch (IOException | RuntimeException e) {
                Timber.e(e);
                iterateQueue();
            }
        }
        return null;
    }

    @Override
    public Maybe<Coin> getItemRx(CoinSource source, Currency currency, long coinId) {
        return Maybe.create(emitter -> {
            Coin result = getItem(source, currency, coinId);
            if (emitter.isDisposed()) {
                throw new IllegalStateException();
            }
            if (DataUtil.isEmpty(result)) {
                emitter.onError(new EmptyException());
            } else {
                emitter.onSuccess(result);
            }
        });
    }

    @Override
    public List<Coin> getItems(CoinSource source, Currency currency, List<Long> coinIds) {
        if (network.isObserving() && !network.hasInternet()) {
            return null;
        }
        String ids = mapper.joinLongToString(coinIds, Constants.Sep.COMMA);
        for (int loop = 0; loop < keys.size(); loop++) {
            String apiKey = getApiKey();
            try {
                Response<CmcQuotesResponse> response = service.getQuotesByIds(apiKey, ids, currency.name()).execute();
                if (response.isSuccessful()) {
                    CmcQuotesResponse result = response.body();
                    return getItemsRx(source, result).blockingGet();
                }
            } catch (IOException | RuntimeException e) {
                Timber.e(e);
                iterateQueue();
            }
        }
        return null;
    }

    @Override
    public Maybe<List<Coin>> getItemsRx(CoinSource source, Currency currency, List<Long> coinIds) {
        return Maybe.create(emitter -> {
            List<Coin> result = getItems(source, currency, coinIds);
            if (emitter.isDisposed()) {
                throw new IllegalStateException();
            }
            if (DataUtil.isEmpty(result)) {
                emitter.onError(new EmptyException());
            } else {
                emitter.onSuccess(result);
            }
        });
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Maybe<Boolean> isEmptyRx() {
        return null;
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
        return null;
    }

    @Override
    public List<Coin> getItems(int limit) {
        return null;
    }

    @Override
    public Maybe<List<Coin>> getItemsRx(int limit) {
        return null;
    }


    /* private api */
    private String getApiKey() {
        adjustIndexStatus();
        return keys.get(indexQueue.peek());
    }

    private void iterateQueue() {
        indexQueue.add(indexQueue.peek());
    }

    private void adjustIndexStatus() {
        int index = indexQueue.peek();
        if (TimeUtil.isExpired(indexStatus.get(index).left, Constants.Delay.INSTANCE.getCmcKey())) {
            indexStatus.get(index).setLeft(TimeUtil.currentTime());
            indexStatus.get(index).setRight(0);
        }
        if (indexStatus.get(index).right > Constants.Limit.CMC_KEY) {
            iterateQueue();
        }
    }

    /* private api*/
    private Maybe<List<Coin>> getItemsRx(CoinSource source, CmcListingResponse response) {
        if (response == null || response.hasError() || !response.hasData()) {
            return null;
        }
        Collection<CmcCoin> items = response.getData();
        return Flowable.fromIterable(items)
                .map(in -> mapper.toItem(source, in, true))
                //.toList()
                .toSortedList((left, right) -> left.getRank() - right.getRank())
                .toMaybe();
    }

    private Maybe<List<Coin>> getItemsRx(CoinSource source, CmcQuotesResponse response) {
        if (response == null || response.hasError() || !response.hasData()) {
            return null;
        }
        Collection<CmcCoin> items = response.getData().values();
        return Flowable.fromIterable(items)
                .map(in -> mapper.toItem(source, in, true))
                .toSortedList((left, right) -> left.getRank() - right.getRank())
                .toMaybe();
    }

    private Maybe<Coin> getItemRx(CoinSource source, CmcQuotesResponse response) {
        if (response.hasError() || !response.hasData()) {
            return null;
        }
        CmcCoin item = response.getFirst();
        return Maybe.just(item).map(in -> mapper.toItem(source, in, true));
    }

/*    @Override
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
        try {
            Response<CMCCoinResponse> response = service.getCoin(id, Constants.Structure.ARRAY).execute();
            if (response.isSuccessful()) {
                return getCoins(Objects.requireNonNull(response.body()));
            }
        } catch (IOException | RuntimeException e) {
            Timber.e(e);
        }
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
        return service
                .getCoinsRx(Constants.Structure.ARRAY)
                .flatMap((Function<CMCCoinsResponse, MaybeSource<List<Coin>>>) this::getItemsRx);
    }

    @Override
    public List<Coin> getItems(int limit) {
        return null;
    }

    @Override
    public Maybe<List<Coin>> getItemsRx(int limit) {
        return null;
    }


    @Override
    public Maybe<List<Coin>> getItemsRx(CoinSource source) {
        return Maybe.fromCallable(new Callable<List<Coin>>() {
            @Override
            public List<Coin> call() throws Exception {
                Response<CmcListingResponseV1> response = service.getListing(Constants.Key.CMC_PRO, Constants.Limit.COIN_START_INDEX, Constants.Limit.COIN).execute();

                CmcListingResponseV1 result = response.body();
                return new ArrayList<>();
            }
        });
        return getListingRx(source, Constants.Limit.COIN_START_INDEX, Constants.Limit.COIN_PAGE);
    }

    @Override
    public Maybe<List<Coin>> getListingRx(CoinSource source, int start, int limit) {
        return service
                .getListingRx(Constants.Key.CMC_PRO, start, limit)
                .flatMap((Function<CmcListingResponse, MaybeSource<List<Coin>>>) this::getItemsRx);
    }

    @Override
    public void clear() {

    }

    @Override
    public Coin getItem(CoinSource source, long id, Currency currency) {
        return null;
    }

    @Override
    public Coin getItem(CoinSource source, String symbol, Currency currency) {
        if (network.isObserving() && !network.hasInternet()) {
            return null;
        }
        for (int loop = 0; loop < keys.size(); loop++) {
            String apiKey = getApiKey();
            try {
                Response<CmcQuotesResponse> response = service.getQuotes(apiKey, symbol, currency.name()).execute();
                if (response.isSuccessful()) {
                    CmcQuotesResponse result = response.body();
                    return getItemRx(result).blockingGet();
                }
            } catch (IOException | RuntimeException e) {
                Timber.e(e);
                iterateQueue();
            }
        }
        return null;
    }

    @Override
    public Coin getItem(CoinSource source, String symbol, long lastUpdated, Currency currency) {
        return null;
    }

    @Override
    public Maybe<Coin> getItemRx(CoinSource source, String symbol, Currency currency) {
        return service
                .getQuotesRx(Constants.Key.CMC_PRO_ROMAN_BJIT, symbol, currency.name())
                .flatMap((Function<CmcQuotesResponse, MaybeSource<Coin>>) this::getItemRx);
        return Maybe.fromCallable(() -> getItem(source, symbol, currency));
    }

    @Override
    public Maybe<Coin> getItemRx(CoinSource source, String symbol, long lastUpdated, Currency currency) {
        return null;
    }

    *
     * @param source
     * @param index    >= 0
     * @param limit
     * @param currency
     * @return

    @Override
    public List<Coin> getItems(CoinSource source, int index, int limit, long lastUpdated, Currency currency) {
        if (network.isObserving() && !network.hasInternet()) {
            return null;
        }
        int start = index + 1;
        for (int loop = 0; loop < keys.size(); loop++) {
            String apiKey = getApiKey();
            try {
                Response<CmcListingResponse> response = service.getListing(apiKey, start, limit, currency.name()).execute();
                if (response.isSuccessful()) {
                    CmcListingResponse result = response.body();
                    return getItemsRx(result).blockingGet();
                }
            } catch (IOException | RuntimeException e) {
                Timber.e(e);
                iterateQueue();
            }
        }
        return null;
    }

    @Override
    public Maybe<List<Coin>> getItemsRx(CoinSource source, int index, int limit, long lastUpdated, Currency currency) {
        if (!network.hasInternet()) {
            return Maybe.error(new EmptyException());
        }
        int start = index + 1;
        return service
                .getListingRx(Constants.Key.CMC_PRO_ROMAN_BJIT, start, limit, currency.name())
                .flatMap((Function<CmcListingResponse, MaybeSource<List<Coin>>>) this::getItemsRx);
        return Maybe.fromCallable(() -> getItems(source, index, limit, lastUpdated, currency));
    }

    @Override
    public List<Coin> getItems(CoinSource source, List<String> symbols, Currency currency) {
        if (network.isObserving() && !network.hasInternet()) {
            return null;
        }
        String symbol = mapper.joinString(symbols, Constants.Sep.COMMA);
        for (int loop = 0; loop < keys.size(); loop++) {
            String apiKey = getApiKey();
            try {
                Response<CmcQuotesResponse> response = service.getQuotes(apiKey, symbol, currency.name()).execute();
                if (response.isSuccessful()) {
                    CmcQuotesResponse result = response.body();
                    return getItemsRx(source, result).blockingGet();
                }
            } catch (IOException | RuntimeException e) {
                Timber.e(e);
                iterateQueue();
            }
        }
        return null;
    }

    @Override
    public Maybe<List<Coin>> getItemsRx(CoinSource source, List<String> symbols, Currency currency) {
        String symbol = mapper.joinString(symbols, Constants.Sep.COMMA);
        return service
                .getQuotesRx(Constants.Key.CMC_PRO_ROMAN_BJIT, symbol, currency.name())
                .flatMap((Function<CmcQuotesResponse, MaybeSource<List<Coin>>>) this::getItemsRx);
        return Maybe.fromCallable(() -> getItems(source, symbols, currency));
    }

    @Override
    public List<Coin> getItems(CoinSource source, List<Long> coinIds, long lastUpdated, Currency currency) {
        return null;
    }

    @Override
    public Maybe<List<Coin>> getItemsRx(CoinSource source, List<Long> coinIds, long lastUpdated, Currency currency) {
        return null;
    }

    *
     * private api



    private Coin getItem(CmcCoinResponse response) {
        Coin result = null;
        if (!response.hasError()) {
            result = mapper.toItem(response.getCoins(), true);
        }
        return result;
    }

    private List<Coin> getCoins(CmcCoinListingResponse response) {
        if (response != null && !response.hasError()) {
            Collection<CmcCoin> items = response.getItemRx();
            if (!DataUtil.isEmpty(items)) {
                List<Coin> result = new ArrayList<>(items.size());
                Stream.of(items).forEach(item -> result.add(mapper.toItem(item, false)));
                return result;
            }
        }
        return null;
    }

    private List<Coin> getItems(CmcCoinsResponse response) {
        if (!response.hasError()) {
            List<CmcCoin> items = response.getItemRx();
            if (!DataUtil.isEmpty(items)) {
                List<Coin> result = new ArrayList<>(items.size());
                Stream.of(items).forEach(item -> result.add(mapper.toItem(item, true)));
                return result;
            }
        }
        return null;
    }

    private Maybe<Coin> getItemRx(CmcCoinResponse response) {
        if (!response.hasError()) {
            CmcCoin item = response.getCoins();
            return Maybe.just(item).map(in -> mapper.toItem(in, true));
        }
        return null;
    }

    private Maybe<Coin> getItemRx(CmcQuotesResponse response) {
        if (response.hasError() || !response.hasData()) {
            return null;
        }
        CmcCoin item = response.getFirst();
        return Maybe.just(item).map(in -> mapper.toItem(in, true));
    }



    private Maybe<List<Coin>> getItemsRx(CoinSource source, CmcQuotesResponse response) {
        if (response == null || response.hasError() || !response.hasData()) {
            return null;
        }
        Collection<CmcCoin> items = response.getData().values();
        return Flowable.fromIterable(items)
                .map(in -> mapper.toItem(in, true))
                .toSortedList((left, right) -> left.getRank() - right.getRank())
                .toMaybe();
    }

    private Maybe<List<Coin>> getItemsRx(CmcCoinsResponse response) {
        if (!response.hasError()) {
            Collection<CmcCoin> items = response.getItemRx();
            return Flowable.fromIterable(items)
                    .map(in -> mapper.toItem(in, true))
                    .toList()
                    .toMaybe();
        }
        return null;
    }*/
}
