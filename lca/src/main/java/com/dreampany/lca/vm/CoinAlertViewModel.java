package com.dreampany.lca.vm;

import android.app.Application;
import com.dreampany.frame.misc.AppExecutors;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.misc.SmartMap;
import com.dreampany.frame.misc.exception.EmptyException;
import com.dreampany.frame.misc.exception.ExtraException;
import com.dreampany.frame.misc.exception.MultiException;
import com.dreampany.frame.vm.BaseViewModel;
import com.dreampany.lca.data.model.Coin;
import com.dreampany.lca.data.model.CoinAlert;
import com.dreampany.lca.data.source.repository.CoinAlertRepository;
import com.dreampany.lca.ui.model.CoinAlertItem;
import com.dreampany.lca.ui.model.UiTask;
import io.reactivex.Maybe;
import io.reactivex.disposables.Disposable;

import javax.inject.Inject;

/**
 * Created by Roman-372 on 3/6/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
public class CoinAlertViewModel
        extends BaseViewModel<CoinAlert, CoinAlertItem, UiTask<CoinAlert>> {

    CoinAlertRepository repo;

    @Inject
    CoinAlertViewModel(Application application,
                       RxMapper rx,
                       AppExecutors ex,
                       ResponseMapper rm,
                       CoinAlertRepository repo) {
        super(application, rx, ex, rm);
        this.repo = repo;
    }

    @Override
    public void clear() {
        super.clear();
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
                    postResult(result);
                }, error -> {
                    postFailureMultiple(new MultiException(error, new ExtraException()));
                });
        addSingleSubscription(disposable);
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
                    postResult(result);
                }, error -> {
                    postFailureMultiple(new MultiException(error, new ExtraException()));
                });
        addSubscription(disposable);
    }

    /* internal api */
    Maybe<CoinAlertItem> getItemRx(Coin coin) {
        return Maybe.create(emitter -> {
            CoinAlert alert = repo.getItem(coin.getSymbol());
            CoinAlertItem item = null;
            if (alert != null) {
                item = getItem(coin, alert);
            }
            if (emitter.isDisposed()) {
                return;
            }
            if (item == null) {
                emitter.onError(new EmptyException());
            } else {
                emitter.onSuccess(item);
            }
        });
    }

    Maybe<CoinAlertItem> saveRx(Coin coin, CoinAlert alert) {
        return Maybe.create(emitter -> {
            long result = repo.putItem(alert);
            CoinAlertItem item = result == -1 ? null : getItem(coin, alert);
            if (item == null) {
                emitter.onError(new NullPointerException());
            } else {
                emitter.onSuccess(item);
            }
        });
    }

    CoinAlertItem getItem(Coin coin, CoinAlert alert) {
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
