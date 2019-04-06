package com.dreampany.lca.vm;

import android.app.Application;

import com.dreampany.frame.data.model.Response;
import com.dreampany.frame.misc.AppExecutors;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.misc.SmartMap;
import com.dreampany.frame.misc.exception.ExtraException;
import com.dreampany.frame.misc.exception.MultiException;
import com.dreampany.frame.ui.adapter.SmartAdapter;
import com.dreampany.frame.util.TimeUtil;
import com.dreampany.frame.vm.BaseViewModel;
import com.dreampany.lca.data.enums.CoinSource;
import com.dreampany.lca.data.misc.CoinAlertMapper;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.model.CoinAlert;
import com.dreampany.lca.data.model.Currency;
import com.dreampany.lca.data.source.pref.Pref;
import com.dreampany.lca.data.source.repository.ApiRepository;
import com.dreampany.lca.misc.Constants;
import com.dreampany.lca.ui.model.CoinAlertItem;
import com.dreampany.lca.ui.model.UiTask;
import com.dreampany.network.manager.NetworkManager;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * Created by Roman-372 on 3/6/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
public class CoinAlertViewModel
        extends BaseViewModel<CoinAlert, CoinAlertItem, UiTask<CoinAlert>> {

    private final NetworkManager network;
    private final Pref pref;
    private final CoinAlertMapper mapper;
    private final ApiRepository repo;
    private SmartAdapter.Callback<CoinAlertItem> uiCallback;

    @Inject
    CoinAlertViewModel(Application application,
                       RxMapper rx,
                       AppExecutors ex,
                       ResponseMapper rm,
                       NetworkManager network,
                       Pref pref,
                       CoinAlertMapper mapper,
                       ApiRepository repo) {
        super(application, rx, ex, rm);
        this.network = network;
        this.pref = pref;
        this.mapper = mapper;
        this.repo = repo;
    }

    @Override
    public void clear() {
        this.uiCallback = null;
        super.clear();
    }

    public void setUiCallback(SmartAdapter.Callback<CoinAlertItem> callback) {
        this.uiCallback = callback;
    }

    public void load(Coin coin, boolean progress) {
        if (!takeAction(true, getSingleDisposable())) {
            return;
        }
        Disposable disposable = getRx()
                .backToMain(getItemRx(coin))
                .doOnSubscribe(subscription -> {
                    if (progress) {
                        postProgress(true);
                    }
                })
                .subscribe(result -> {
                    if (progress) {
                        postProgress(false);
                    }
                    postResult(Response.Type.GET, result);
                }, error -> {
                    if (progress) {
                        postProgress(false);
                    }
                    postFailure(error);
                });
        addSingleSubscription(disposable);
    }

    public void loads(boolean important, boolean progress) {
        if (!takeAction(important, getMultipleDisposable())) {
            return;
        }
        Currency currency = pref.getCurrency(Currency.USD);
        Disposable disposable = getRx()
                .backToMain(getAlertsRx(currency))
                .doOnSubscribe(subscription -> {
                    if (progress) {
                        postProgress(true);
                    }
                })
                .subscribe(result -> {
                    if (progress) {
                        postProgress(false);
                    }
                    postResult(Response.Type.GET, result);
                }, error -> {
                    if (progress) {
                        postProgress(false);
                    }
                    postFailures(new MultiException(error, new ExtraException()));
                });
        addMultipleSubscription(disposable);
    }

    public void save(Coin coin, double priceUp, double priceDown, boolean withProgress) {
        Disposable disposable =   getRx()
                .backToMain(saveRx(coin, priceUp, priceDown))
                .doOnSubscribe(subscription -> {
                    if (withProgress) {
                        postProgress(true);
                    }
                })
                .subscribe(result -> {
                    if (withProgress) {
                        postProgress(false);
                    }
                    postResult(Response.Type.ADD, result);
                }, error -> {
                    if (withProgress) {
                        postProgress(false);
                    }
                    postFailure(error);
                });
    }

    public void delete(CoinAlertItem item, boolean withProgress) {
        Disposable disposable =  getRx()
                .backToMain(deleteRx(item))
                .doOnSubscribe(subscription -> {
                    if (withProgress) {
                        postProgress(true);
                    }
                })
                .subscribe(result -> {
                    if (withProgress) {
                        postProgress(false);
                    }
                    postResult(Response.Type.DELETE, result);
                    getEx().postToUi(loadRunner, 2000L);
                }, error -> {
                    if (withProgress) {
                        postProgress(false);
                    }
                    postFailure(error);
                });
    }

    /* private api */
    private final Runnable loadRunner = () -> loads(true, false);

    private Maybe<CoinAlertItem> getItemRx(Coin coin) {
        return Maybe.create(emitter -> {
            CoinAlert alert = repo.getCoinAlert(coin.getCoinId());
            CoinAlertItem item;
            if (alert != null) {
                item = getItem(coin, alert);
            } else {
                item = CoinAlertItem.getItem(coin, alert);
                item.setEmpty(true);
            }
            if (emitter.isDisposed()) {
                return;
            }
            emitter.onSuccess(item);
        });
    }

    private Maybe<List<CoinAlertItem>> getAlertsRx(Currency currency) {
        return repo.getCoinAlertsRx()
                .flatMap((Function<List<CoinAlert>, MaybeSource<List<CoinAlertItem>>>) alerts -> getItemsRx(currency, alerts));
    }

    private Maybe<List<CoinAlertItem>> getItemsRx(Currency currency, List<CoinAlert> result) {
        return Flowable.fromIterable(result)
                .map(alert -> {
                    Coin coin = repo.getItemIf(CoinSource.CMC, currency, alert.getId());
                    return getItem(coin, alert);
                })
                .toList()
                .toMaybe();
    }

    private Maybe<CoinAlertItem> saveRx(Coin coin, double priceUp, double priceDown) {
        return Maybe.create(emitter -> {
            CoinAlert alert = mapper.toItem(coin.getSymbol(), priceUp, priceDown, true);
            long result = repo.putItem(coin, alert);
            CoinAlertItem item = result == -1 ? null : getItem(coin, alert);
            if (emitter.isDisposed()) {
                return;
            }
            if (item == null) {
                emitter.onError(new NullPointerException());
            } else {
                emitter.onSuccess(item);
            }
        });
    }

    private Maybe<CoinAlertItem> deleteRx(CoinAlertItem item) {
        return Maybe.create(emitter -> {
            int result = repo.delete(item.getCoin(), item.getItem());
            if (emitter.isDisposed()) {
                return;
            }
            if (result == -1) {
                emitter.onError(new IllegalStateException());
            } else {
                emitter.onSuccess(item);
            }
        });
    }

    private CoinAlertItem getItem(Coin coin, CoinAlert alert) {
        SmartMap<Long, CoinAlertItem> map = getUiMap();
        CoinAlertItem item = map.get(alert.getId());
        if (item == null) {
            item = CoinAlertItem.getItem(coin, alert);
            map.put(alert.getId(), item);
        }
        item.setItem(alert);
        return item;
    }


}
