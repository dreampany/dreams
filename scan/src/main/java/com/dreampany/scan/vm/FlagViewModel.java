package com.dreampany.scan.vm;

import android.app.Application;

import com.dreampany.frame.misc.AppExecutors;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.misc.exception.ExtraException;
import com.dreampany.frame.misc.exception.MultiException;
import com.dreampany.frame.vm.BaseViewModel;
import com.dreampany.network.NetworkManager;
import com.dreampany.scan.data.model.Scan;
import com.dreampany.scan.data.source.repository.ScanRepository;
import com.dreampany.scan.ui.model.ScanItem;
import com.dreampany.scan.ui.model.UiTask;

import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Publisher;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;


import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * Created by Hawladar Roman on 6/29/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class FlagViewModel extends BaseViewModel<Scan, ScanItem, UiTask<Scan>> {

    private final ScanRepository repository;
    private final Set<Scan> uiFlags;

    @Inject
    FlagViewModel(Application application,
                  RxMapper rx,
                  AppExecutors ex,
                  ResponseMapper rm,
                  NetworkManager network,
                  ScanRepository repository) {
        super(application, rx, ex, rm);
        this.repository = repository;
        uiFlags = Collections.synchronizedSet(new HashSet<>());
        loads(false); // first time load
    }

    @NotNull
    @Override
    protected Flowable<String> getTitle() {
        return Flowable.empty();
    }

    @NotNull
    @Override
    protected Flowable<String> getSubtitle() {
        return Flowable.empty();
    }



    public void loads(boolean fresh) {
        if (!preLoads(fresh)) {
            return;
        }
        Disposable disposable = getRx()
                .backToMain(getItems())
                .doOnSubscribe(subscription -> postProgressMultiple(true))
                .subscribe(items -> {
                    postResult(items);
                }, error -> {
                    postFailureMultiple(new MultiException(error, new ExtraException()));
                });
        addMultipleSubscription(disposable);
    }


    public Disposable toggle(Scan scan) {
        if (hasSingleDisposable()) {
            return getSingleDisposable();
        }
        Disposable disposable = getRx()
                .backToMain(toggleImpl(scan))
                .subscribe(items -> {
                    postResult(items);
                }, error -> {
                    postFailureMultiple(new MultiException(error, new ExtraException()));
                });
        addSingleSubscription(disposable);
        return disposable;
    }

    private Flowable<List<ScanItem>> getItems() {
        return repository
                .getFlagScans()
                .flatMap((Function<List<Scan>, Publisher<List<ScanItem>>>) this::getItems);
    }


    private Flowable<List<ScanItem>> getItems(List<Scan> scans) {
        return Flowable.fromCallable(() -> {

            return null;
/*            List<Scan> newScans = new ArrayList<>(scans);
            List<Scan> oldScans = getMap().getValues();
            List<Scan> added = ListUtils.subtract(newScans, oldScans);
            List<Scan> removed = ListUtils.subtract(oldScans, newScans);
            List<Scan> oldUpdated = ListUtils.subtract(oldScans, removed);
            List<Scan> newUpdated = ListUtils.subtract(newScans, added);

            List<Scan> updated = new ArrayList<>();
            for (Scan newScan : newUpdated) {
                if (oldUpdated.contains(newScan)) {
                    Scan oldScan = oldUpdated.get(oldUpdated.indexOf(newScan));
                    //if last updated time miss match || uiFlags containing miss match
                    if (newScan.getTime() > oldScan.getTime() || uiFlags.contains(newScan) != repository.hasFlagged(newScan)) {
                        updated.add(newScan);
                    }
                }
            }

            add(added);
            remove(removed);
            update(updated);

            List<ScanItem> items = new ArrayList<>(added.size() + removed.size() + updated.size());

            for (Scan scan : added) {
                ScanItem item = getAddedItem(scan);
                items.add(item);
                support(item);
            }

            for (Scan scan : removed) {
                ScanItem item = getRemovedItem(scan);
                items.add(item);
                support(item);
            }

            for (Scan scan : updated) {
                ScanItem item = getUpdatedItem(scan);
                items.add(item);
                support(item);
            }

            return items;*/
        });
    }

    //todo need to improve for flowable and completable working
    private Flowable<ScanItem> toggleImpl(Scan scan) {
        return Flowable.fromCallable(() -> {
/*            repository.toggle(scan).blockingAwait();
            ScanItem item = getUpdatedItem(scan);
            support(item);
            return item;*/
            return null;
        });
    }

}
