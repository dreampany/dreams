package com.dreampany.lca.data.source.remote;

import com.dreampany.frame.misc.exception.EmptyException;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.frame.util.NumberUtil;
import com.dreampany.frame.util.TimeUtil;
import com.dreampany.lca.api.cmc.model.CmcCoin;
import com.dreampany.lca.api.cmc.model.CmcListingResponse;
import com.dreampany.lca.api.cmc.model.CmcQuotesResponse;
import com.dreampany.lca.data.enums.CoinSource;
import com.dreampany.lca.data.misc.CoinMapper;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.enums.Currency;
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
        keys.add(Constants.Key.CMC_PRO_DREAM_DEBUG_1);

        indexQueue = new CircularFifoQueue<>(keys.size());
        indexStatus = Maps.newConcurrentMap();
        for (int index = 0; index < keys.size(); index++) {
            indexQueue.add(index);
            indexStatus.put(index, MutablePair.of(TimeUtil.currentTime(), 0));
        }
        int randIndex = NumberUtil.nextRand(keys.size());
        while (randIndex > 0) {
            iterateQueue();
            randIndex--;
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
                return;
            }
            if (DataUtil.isEmpty(result)) {
                emitter.onError(new EmptyException());
            } else {
                emitter.onSuccess(result);
            }
        });
    }

    @Override
    public Coin getItem(CoinSource source, Currency currency, long id) {
        if (network.isObserving() && !network.hasInternet()) {
            return null;
        }
        String ids = String.valueOf(id);
        Timber.v("Ids %s", ids);
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
    public Maybe<Coin> getItemRx(CoinSource source, Currency currency, long id) {
        return Maybe.create(emitter -> {
            Coin result = getItem(source, currency, id);
            if (emitter.isDisposed()) {
                return;
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
        Timber.v("Ids %s", ids);
        for (int loop = 0; loop < keys.size(); loop++) {
            String apiKey = getApiKey();
            try {
                Response<CmcQuotesResponse> response = service.getQuotesByIds(apiKey, currency.name(), ids).execute();
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
    public Maybe<List<Coin>> getItemsRx(CoinSource source, Currency currency, List<Long> ids) {
        return Maybe.create(emitter -> {
            List<Coin> result = getItems(source, currency, ids);
            if (emitter.isDisposed()) {
                return;
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
        indexStatus.get(index).right++;
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
}
