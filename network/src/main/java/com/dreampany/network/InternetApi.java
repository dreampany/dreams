package com.dreampany.network;

import android.content.Context;

import com.dreampany.network.misc.RxMapper;
import com.github.pwittchen.reactivenetwork.library.rx2.Connectivity;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.common.collect.Sets;

import java.util.Set;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import hugo.weaving.DebugLog;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import timber.log.Timber;

/**
 * Created by Hawladar Roman on 8/18/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public class InternetApi {

    public interface Callback {
        void onResult(boolean internet);
    }

    private final Context context;
    private final RxMapper rx;
    private volatile boolean started;
    private Disposable disposable;
    private final Set<Callback> callbacks;
    private volatile boolean connected;
    private volatile Single<Boolean> lastState;

    @Inject
    InternetApi(Context context,
                RxMapper rx) {
        this.context = context.getApplicationContext();
        this.rx = rx;
        callbacks = Sets.newConcurrentHashSet();
    }

    public void start(Callback callback) {
        callbacks.add(callback);
        if (started) {
            postResult(connected);
            return;
        }
        disposable = ReactiveNetwork.observeNetworkConnectivity(context)
                .flatMapSingle((Function<Connectivity, SingleSource<Boolean>>) connectivity -> {
                    if (connectivity.available()) {
                        return ReactiveNetwork.checkInternetConnectivity();
                    }
                    return Single.just(false);
                })
                .subscribeOn(rx.io())
                .observeOn(rx.io())
                .subscribe(this::postResult, this::postError);
    }

    public void stop(Callback callback) {
        callbacks.remove(callback);
        if (!started) {
            return;
        }
        if (!callbacks.isEmpty()) {
            return;
        }
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @DebugLog
    private void postResult(boolean connected) {
        this.connected = connected;
        for (Callback callback : callbacks) {
            callback.onResult(connected);
        }
    }

    private void postError(Throwable error) {
        Timber.e(error);
    }

    private Single<Boolean> checkInternet() {
        if (lastState == null) {
            lastState = ReactiveNetwork.checkInternetConnectivity();
        }
        return lastState;
    }
}
