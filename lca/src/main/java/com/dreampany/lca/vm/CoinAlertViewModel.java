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
import com.dreampany.frame.vm.BaseViewModel;
import com.dreampany.lca.data.enums.CoinSource;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.model.CoinAlert;
import com.dreampany.lca.data.model.Currency;
import com.dreampany.lca.data.source.repository.CoinAlertRepository;
import com.dreampany.lca.data.source.repository.CoinRepository;
import com.dreampany.lca.ui.model.CoinAlertItem;
import com.dreampany.lca.ui.model.UiTask;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Roman-372 on 3/6/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
public class CoinAlertViewModel
        extends BaseViewModel<CoinAlert, CoinAlertItem, UiTask<CoinAlert>> {

    private CoinRepository coinRepo;
    private CoinAlertRepository alertRepo;
    private SmartAdapter.Callback<CoinAlertItem> uiCallback;

    @Inject
    CoinAlertViewModel(Application application,
                       RxMapper rx,
                       AppExecutors ex,
                       ResponseMapper rm,
                       CoinRepository coinRepo,
                       CoinAlertRepository alertRepo) {
        super(application, rx, ex, rm);
        this.coinRepo = coinRepo;
        this.alertRepo = alertRepo;
    }

    @Override
    public void clear() {
        this.uiCallback = null;
        super.clear();
    }

    public void setUiCallback(SmartAdapter.Callback<CoinAlertItem> callback) {
        this.uiCallback = callback;
    }

    public void load(Coin coin, boolean withProgress) {
        if (!preLoad(true)) {
            return;
        }
        Disposable disposable = getRx()
                .backToMain(getItemRx(coin))
                .doOnSubscribe(subscription -> {
                    if (withProgress) {
                        postProgress(true);
                    }
                })
                .subscribe(result -> {
                    if (withProgress) {
                        postProgress(false);
                    }
                    postResult(Response.Type.GET, result);
                }, error -> {
                    if (withProgress) {
                        postProgress(false);
                    }
                    postFailure(error);
                });
        //addSingleSubscription(disposable);
    }

    public void loads(boolean fresh, boolean withProgress) {
        if (!preLoads(fresh)) {
            return;
        }
        Disposable disposable = getRx()
                .backToMain(getAlertsRx())
                .doOnSubscribe(subscription -> {
                    if (withProgress) {
                        postProgress(true);
                    }
                })
                .subscribe(result -> {
                    if (withProgress) {
                        postProgress(false);
                    }
                    postResult(Response.Type.GET, result);
                }, error -> {
                    if (withProgress) {
                        postProgress(false);
                    }
                    postFailures(new MultiException(error, new ExtraException()));
                });
//        addMultipleSubscription(disposable);
    }

    public void save(Coin coin, CoinAlert alert, boolean withProgress) {
        if (!preLoad(true)) {
            return;
        }
        Disposable disposable = getRx()
                .backToMain(saveRx(coin, alert))
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
        addSubscription(disposable);
    }

    public void delete(CoinAlertItem item, boolean withProgress) {
        if (!preLoad(true)) {
            return;
        }
        Disposable disposable = getRx()
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
                    getEx().postToUi(() -> loads(true, false), 2000L);
                }, error -> {
                    if (withProgress) {
                        postProgress(false);
                    }
                    postFailure(error);
                });
        addSubscription(disposable);
    }

    /* private api */
    private Maybe<CoinAlertItem> getItemRx(Coin coin) {
        return Maybe.create(emitter -> {
            CoinAlert alert = alertRepo.getItem(coin.getSymbol());
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

    private Maybe<List<CoinAlertItem>> getAlertsRx() {
        return alertRepo.getItemsRx()
                .flatMap((Function<List<CoinAlert>, MaybeSource<List<CoinAlertItem>>>) this::getItemsRx);
    }

    private Maybe<List<CoinAlertItem>> getItemsRx(List<CoinAlert> result) {
        return Flowable.fromIterable(result)
                .map(alert -> {
                    Coin coin = coinRepo.getItem(CoinSource.CMC, alert.getSymbol(), Currency.USD);
                    return getItem(coin, alert);
                })
                .toList()
                .toMaybe();
    }

    private Maybe<CoinAlertItem> saveRx(Coin coin, CoinAlert alert) {
        return Maybe.create(emitter -> {
            long result = alertRepo.putItem(alert);
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
            int result = alertRepo.delete(item.getItem());
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
