/*
package com.dreampany.network.api;

import android.content.Context;

import com.dreampany.frame.misc.AppExecutors;
import com.dreampany.network.data.model.Network;
import com.dreampany.network.misc.RxMapper;
import com.github.pwittchen.reactivenetwork.library.rx2.Connectivity;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.common.collect.Sets;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import timber.log.Timber;

import javax.inject.Inject;
import java.util.Set;

*/
/**
 * Created by Hawladar Roman on 8/18/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 *//*

public class InternetApi {

    public interface Callback {
        void onResult(Network network);
    }

    private final Context context;
    private final RxMapper rx;
    private final AppExecutors ex;
    private Disposable disposable;
    private final Set<Callback> callbacks;
    private volatile boolean connected;
    private volatile Single<Boolean> lastState;

    @Inject
    InternetApi(Context context,
                RxMapper rx,
                AppExecutors ex) {
        this.context = context;
        this.rx = rx;
        this.ex = ex;
        callbacks = Sets.newConcurrentHashSet();
    }

    public void start(Callback callback) {
        callbacks.add(callback);
        if (isStarted()) {
            //ex.postToNetwork(() -> postResult(connected));
            return;
        }
        Timber.v("Network Observer created for new");
        disposable = ReactiveNetwork.observeNetworkConnectivity(context)
                .flatMapSingle((Function<Connectivity, SingleSource<Boolean>>) connectivity -> {
                    Timber.v("Connectivity %s %s", connectivity.available(), connectivity.toString());
                    if (connectivity.available()) {
                        Single<Boolean> single = ReactiveNetwork.checkInternetConnectivity();
                        boolean result = single.blockingGet();
                        Timber.v("Connectivity Result %s", result);
                        return Single.just(result);
                    }

                    return Single.just(false);
                })
                .subscribeOn(rx.io())
                .observeOn(rx.io())
                .subscribe(this::postResult, this::postError);
    }

    public void stop(Callback callback) {
        callbacks.remove(callback);
        if (!isStarted()) {
            return;
        }
        if (!callbacks.isEmpty()) {
            return;
        }
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    private void postResult(boolean connected) {
        this.connected = connected;
        for (Callback callback : callbacks) {
            Network network = new Network(Network.Type.)
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

    private boolean isStarted() {
        return !(disposable == null || disposable.isDisposed());
    }
}
*/
