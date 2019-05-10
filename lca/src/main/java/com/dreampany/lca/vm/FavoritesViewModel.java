package com.dreampany.lca.vm;


import android.app.Application;

import com.dreampany.frame.data.enums.UiState;
import com.dreampany.frame.data.model.Response;
import com.dreampany.frame.misc.AppExecutors;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.misc.SmartMap;
import com.dreampany.frame.misc.exception.EmptyException;
import com.dreampany.frame.misc.exception.ExtraException;
import com.dreampany.frame.misc.exception.MultiException;
import com.dreampany.frame.ui.adapter.SmartAdapter;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.frame.vm.BaseViewModel;
import com.dreampany.lca.data.enums.CoinSource;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.enums.Currency;
import com.dreampany.lca.data.source.pref.Pref;
import com.dreampany.lca.data.source.repository.ApiRepository;
import com.dreampany.lca.misc.CurrencyFormatter;
import com.dreampany.lca.ui.model.CoinItem;
import com.dreampany.lca.ui.model.UiTask;
import com.dreampany.network.manager.NetworkManager;
import com.dreampany.network.data.model.Network;

import io.reactivex.Maybe;
import io.reactivex.disposables.Disposable;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Hawladar Roman on 5/31/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class FavoritesViewModel
        extends BaseViewModel<Coin, CoinItem, UiTask<Coin>>
        implements NetworkManager.Callback {

    private final NetworkManager network;
    private final Pref pref;
    private final ApiRepository repo;
    private final CurrencyFormatter formatter;
    private SmartAdapter.Callback<CoinItem> uiCallback;

    @Inject
    FavoritesViewModel(Application application,
                       RxMapper rx,
                       AppExecutors ex,
                       ResponseMapper rm,
                       NetworkManager network,
                       Pref pref,
                       ApiRepository repo,
                       CurrencyFormatter formatter) {
        super(application, rx, ex, rm);
        this.network = network;
        this.pref = pref;
        this.repo = repo;
        this.formatter = formatter;
    }

    @Override
    public void clear() {
        network.deObserve(this, true);
        this.uiCallback = null;
        super.clear();
    }

    @Override
    public void onResult(Network... networks) {
        UiState state = UiState.OFFLINE;
        for (Network network : networks) {
            if (network.hasInternet()) {
                state = UiState.ONLINE;
                Response<List<CoinItem>> result = getOutputs().getValue();
                if (result instanceof Response.Failure) {
                    getEx().postToUi(() -> loads(false, false), 250L);
                }
            }
        }
        UiState finalState = state;
        getEx().postToUiSmartly(() -> updateUiState(finalState));
    }

    public void setUiCallback(SmartAdapter.Callback<CoinItem> callback) {
        this.uiCallback = callback;
    }

    public void start() {
        network.observe(this, true);
    }


    public void refresh(boolean update, boolean important, boolean progress) {
        if (update) {
            update(important, progress);
            return;
        }
        loads(important, progress);
    }

    public void loads(boolean important, boolean progress) {
        if (!takeAction(important, getMultipleDisposable())) {
            return;
        }
        CoinSource source = CoinSource.CMC;
        Currency currency = pref.getCurrency(Currency.USD);
        Disposable disposable = getRx()
                .backToMain(getFavoriteItemsRx(source, currency))
                .doOnSubscribe(subscription -> {
                    if (progress) {
                        postProgress(true);
                    }
                })
                .subscribe(result -> {
                    if (progress) {
                        postProgress(false);
                    }
                    postResult(Response.Type.ADD, result);
                }, error -> {
                    if (progress) {
                        postProgress(false);
                    }
                    postFailures(new MultiException(error, new ExtraException()));
                });
        addMultipleSubscription(disposable);
    }

    public void update(boolean important, boolean progress) {
        if (!takeAction(important, getSingleDisposable())) {
            return;
        }
        Currency currency = pref.getCurrency(Currency.USD);
        Disposable disposable = getRx()
                .backToMain(getVisibleItemsIfRx(currency))
                .doOnSubscribe(subscription -> {
                    if (progress) {
                        postProgress(true);
                    }
                })
                .subscribe(
                        result -> {
                            if (progress) {
                                postProgress(false);
                            }
                            postResult(Response.Type.UPDATE, result);
                        }, error -> {
                            if (progress) {
                                postProgress(false);
                            }
                            postFailures(new MultiException(error, new ExtraException()));
                        });
        addMultipleSubscription(disposable);
    }

    public void toggleFavorite(Coin coin) {
        Currency currency = pref.getCurrency(Currency.USD);
        Disposable disposable = getRx()
                .backToMain(toggleImpl(currency, coin))
                .subscribe(result -> postResult(Response.Type.UPDATE, result, false), this::postFailure);
    }

    /* private api */
    private List<CoinItem> getFavoriteItems(CoinSource source, Currency currency) {
        List<CoinItem> result = new ArrayList<>();
        List<Coin> real = repo.getFavorites(source, currency);
        if (real == null) {
            real = new ArrayList<>();
        }
        List<CoinItem> ui = uiCallback.getItems();
        for (Coin coin : real) {
            CoinItem item = getItem(currency, coin);
            item.setFavorite(true);
            result.add(item);
        }

        if (!DataUtil.isEmpty(ui)) {
            for (CoinItem item : ui) {
                if (!real.contains(item.getItem())) {
                    item.setFavorite(false);
                    result.add(item);
                }
            }
        }
        return result;
    }

    private Maybe<List<CoinItem>> getFavoriteItemsRx(CoinSource source, Currency currency) {
        return Maybe.create(emitter -> {
            List<CoinItem> result = getFavoriteItems(source, currency);
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

    private Maybe<List<CoinItem>> getVisibleItemsIfRx(Currency currency) {
        return Maybe.fromCallable(() -> {
            List<CoinItem> result = getVisibleItemsIf(currency);
            if (DataUtil.isEmpty(result)) {
                throw new EmptyException();
            }
            return result;
        }).onErrorReturn(throwable -> new ArrayList<>());
    }

    private Maybe<CoinItem> toggleImpl(Currency currency, Coin coin) {
        return Maybe.fromCallable(() -> {
            repo.toggleFavorite(coin);
            return getItem(currency, coin);
        });
    }

    private List<CoinItem> getVisibleItemsIf(Currency currency) {
        if (uiCallback == null) {
            return null;
        }
        List<CoinItem> items = uiCallback.getVisibleItems();
        if (!DataUtil.isEmpty(items)) {
            List<Long> coinIds = new ArrayList<>();
            for (CoinItem item : items) {
                coinIds.add(item.getItem().getCoinId());
            }
            items = null;
            if (!DataUtil.isEmpty(coinIds)) {
                List<Coin> coins = repo.getItemsIf(CoinSource.CMC, currency, coinIds);
                if (!DataUtil.isEmpty(coins)) {
                    items = getItems(coins, currency);
                }
            }
        }
        return items;
    }

    private CoinItem getItem(Currency currency, Coin coin) {
        SmartMap<Long, CoinItem> map = getUiMap();
        CoinItem item = map.get(coin.getId());
        if (item == null) {
            item = CoinItem.getSimpleItem(coin, currency);
            item.setFormatter(formatter);
            map.put(coin.getId(), item);
        }
        item.setItem(coin);
        adjustFavorite(coin, item);
        adjustAlert(coin, item);
        return item;
    }

    private List<CoinItem> getItems(List<Coin> result, Currency currency) {
        List<Coin> coins = new ArrayList<>(result);
        List<Coin> ranked = new ArrayList<>();
        for (Coin coin : coins) {
            if (coin.getRank() > 0) {
                ranked.add(coin);
            }
        }
        Collections.sort(ranked, (left, right) -> left.getRank() - right.getRank());
        coins.removeAll(ranked);
        coins.addAll(0, ranked);

        List<CoinItem> items = new ArrayList<>(coins.size());
        for (Coin coin : coins) {
            CoinItem item = getItem(currency, coin);
            items.add(item);
        }
        return items;
    }

    private void adjustFavorite(Coin coin, CoinItem item) {
        item.setFavorite(repo.isFavorite(coin));
    }

    private void adjustAlert(Coin coin, CoinItem item) {
        item.setAlert(repo.hasAlert(coin));
    }
}
