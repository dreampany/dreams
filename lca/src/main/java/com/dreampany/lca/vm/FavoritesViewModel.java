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
import com.dreampany.lca.data.model.Currency;
import com.dreampany.lca.data.source.pref.Pref;
import com.dreampany.lca.data.source.repository.ApiRepository;
import com.dreampany.lca.ui.model.CoinItem;
import com.dreampany.lca.ui.model.UiTask;
import com.dreampany.network.manager.NetworkManager;
import com.dreampany.network.data.model.Network;
import hugo.weaving.DebugLog;
import io.reactivex.Maybe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

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

    private static final boolean OPEN = true;

    private final NetworkManager network;
    private final Pref pref;
    private final ApiRepository repo;
    private SmartAdapter.Callback<CoinItem> uiCallback;
    private Disposable updateDisposable;

    @Inject
    FavoritesViewModel(Application application,
                       RxMapper rx,
                       AppExecutors ex,
                       ResponseMapper rm,
                       NetworkManager network,
                       Pref pref,
                       ApiRepository repo) {
        super(application, rx, ex, rm);
        this.network = network;
        this.pref = pref;
        this.repo = repo;
    }

    @Override
    public void clear() {
        network.deObserve(this, true);
        this.uiCallback = null;
        removeUpdateDisposable();
        super.clear();
    }

    @Override
    public void onResult(Network... networks) {
        UiState state = UiState.OFFLINE;
        for (Network network : networks) {
            if (network.isConnected()) {
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

    public void removeUpdateDisposable() {
        removeSubscription(updateDisposable);
    }

    public void refresh(boolean onlyUpdate, boolean withProgress) {
        if (onlyUpdate) {
            update(withProgress);
            return;
        }
        loads(true, withProgress);
    }

    @DebugLog
    public void loads(boolean fresh, boolean withProgress) {
        if (!OPEN) {
            return;
        }
        if (!preLoads(fresh)) {
            return;
        }
        CoinSource source = CoinSource.CMC;
        Currency currency = Currency.USD;
        Disposable disposable = getRx()
                .backToMain(getFavoriteItemsRx(source, currency))
                .doOnSubscribe(subscription -> {
                    if (withProgress) {
                        postProgress(true);
                    }
                })
                .subscribe(
                        result -> {
                            Timber.v("Posting Result");
                            if (withProgress) {
                                postProgress(false);
                            }
                            postResult(result);
                            /*if (!DataUtil.isEmpty(result)) {
                                postResult(result);
                            } else {
                                postFailure(new EmptyException());
                            }*/
                        },
                        error -> postFailureMultiple(new MultiException(error, new ExtraException()))
                );
        addMultipleSubscription(disposable);
    }

    public void update(boolean withProgress) {
        if (!OPEN) {
            return;
        }
        if (hasDisposable(updateDisposable)) {
            return;
        }
        Currency currency = Currency.USD;
        updateDisposable = getRx()
                .backToMain(getVisibleItemsIfRx(currency))
                .doOnSubscribe(subscription -> {
                    if (withProgress) {
                        postProgress(true);
                    }
                })
                .subscribe(
                        result -> {
                            Timber.v("Posting Result");
                            if (withProgress) {
                                postProgress(false);
                            }
                            postResult(result);
/*                            if (!DataUtil.isEmpty(result)) {
                                postResult(result);
                            } else {
                                postFailure(new EmptyException());
                            }*/
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Timber.v("Favorite Error %s", throwable.toString());
                                postFailure(throwable);
                            }
                        });
        addSubscription(updateDisposable);
    }

    /**
     * private api
     */
    private Maybe<List<CoinItem>> getFavoriteItemsRx(CoinSource source, Currency currency) {
        return Maybe.fromCallable(() -> {
            List<CoinItem> result = new ArrayList<>();
            List<Coin> real = repo.getFavorites(source, currency);
            if (real == null) {
                real = new ArrayList<>();
            }
            List<CoinItem> ui = uiCallback.getItems();
            for (Coin coin : real) {
                CoinItem item = getItem(coin);
                item.setFlagged(true);
                result.add(item);
            }

            if (!DataUtil.isEmpty(ui)) {
                for (CoinItem item : ui) {
                    if (!real.contains(item.getItem())) {
                        item.setFlagged(false);
                        result.add(item);
                    }
                }
            }

            Timber.v("Favorite Result in VM %d", result.size());
            return result;
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

    private List<CoinItem> getVisibleItemsIf(Currency currency) {
        if (uiCallback == null) {
            return null;
        }
        List<CoinItem> items = uiCallback.getVisibleItems();
        if (!DataUtil.isEmpty(items)) {
            List<String> symbols = new ArrayList<>();
            for (CoinItem item : items) {
                symbols.add(item.getItem().getSymbol());
            }
            items = null;
            if (!DataUtil.isEmpty(symbols)) {
                String[] result = DataUtil.toStringArray(symbols);
                List<Coin> coins = repo.getItemsIf(CoinSource.CMC, result, currency);
                if (!DataUtil.isEmpty(coins)) {
                    items = getItems(coins);
                }
            }
        }
        return items;
    }

    private CoinItem getItem(Coin coin) {
        SmartMap<Long, CoinItem> map = getUiMap();
        CoinItem item = map.get(coin.getId());
        if (item == null) {
            item = CoinItem.getSimpleItem(coin);
            map.put(coin.getId(), item);
        }
        item.setItem(coin);
        adjustFlag(coin, item);
        return item;
    }

    private List<CoinItem> getItems(List<Coin> result) {
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
            CoinItem item = getItem(coin);
            items.add(item);
        }
        return items;
    }

    private void adjustFlag(Coin coin, CoinItem item) {
        boolean flagged = repo.isFavorite(coin);
        item.setFlagged(flagged);
    }
}
