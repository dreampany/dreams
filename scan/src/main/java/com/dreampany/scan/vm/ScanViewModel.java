package com.dreampany.scan.vm;

import android.app.Application;

import com.dreampany.framework.misc.AppExecutors;
import com.dreampany.framework.misc.ResponseMapper;
import com.dreampany.framework.misc.RxMapper;
import com.dreampany.framework.misc.SmartMap;
import com.dreampany.framework.misc.exceptions.ExtraException;
import com.dreampany.framework.misc.exceptions.MultiException;
import com.dreampany.framework.ui.vm.BaseViewModel;
import com.dreampany.network.NetworkManager;
import com.dreampany.scan.data.enums.ScanType;
import com.dreampany.scan.data.model.Scan;
import com.dreampany.scan.data.source.repository.ScanRepository;
import com.dreampany.scan.ui.model.ScanItem;
import com.dreampany.scan.ui.model.UiTask;
import com.github.oliveiradev.lib.Rx2Photo;
import com.github.oliveiradev.lib.shared.TypeRequest;

import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Publisher;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;


import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * Created by Hawladar Roman on 6/29/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class ScanViewModel extends BaseViewModel<Scan, ScanItem, UiTask<Scan>> {

    private final ScanRepository repository;
    private final Set<Scan> uiFlags;

    @Inject
    ScanViewModel(Application application,
                  RxMapper rx,
                  AppExecutors ex,
                  ResponseMapper rm,
                  NetworkManager network,
                  ScanRepository repository) {
        super(application, rx, ex, rm);
        this.repository = repository;
        uiFlags = Collections.synchronizedSet(new HashSet<>());
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


    public void load(boolean fresh) {
        if (hasSingleDisposable()) {
            return;
        }
        Scan scan = getTask().getInput();
        Disposable disposable = getRx()
                .backToMain(getItem(scan))
                .doOnSubscribe(subscription -> postProgressMultiple(true))
                .subscribe(this::postResult, error -> {
                    postFailureMultiple(new MultiException(error, new ExtraException()));
                });
        addSingleSubscription(disposable);
    }


    public void loadScan(ScanType type, boolean fresh) {
        if (hasSingleDisposable()) {
            return;
        }
        Disposable disposable = getRx()
                .backToMain(getScanItem(type))
                .doOnSubscribe(subscription -> postProgressMultiple(true))
                .subscribe(this::postResult, error -> {
                    postFailureMultiple(new MultiException(error, new ExtraException()));
                });
        addSingleSubscription(disposable);
    }

    private Flowable<ScanItem> getScanItem(ScanType type) {
        switch (type) {
            case TEXT:
                return getScanItemFromCamera();
            case BARCODE:
            default:
                return getScanItemFromCamera();
        }
    }

    private Flowable<ScanItem> getScanItemFromCamera() {
        return Rx2Photo.with(getApplication())
                .requestBitmap(TypeRequest.CAMERA)
                .toFlowable(BackpressureStrategy.LATEST)
                .map(bitmap -> repository.getScan(ScanType.TEXT, bitmap).blockingSingle())
                .map(ScanItem::getItem);
    }

    private Flowable<List<ScanItem>> getItems() {
        return repository
                .getScans()
                .flatMap((Function<List<Scan>, Publisher<List<ScanItem>>>) this::getItems);
    }

    private Flowable<ScanItem> getItem(Scan scan) {
        return Flowable.fromCallable(() -> {
            ScanItem item = ScanItem.getItem(scan);
            boolean flagged = repository.isFlagged(scan).blockingGet();
            item.setFlagged(flagged);
            return item;
        });
    }


    private Flowable<List<ScanItem>> getItems(List<Scan> scans) {
        return Flowable.fromCallable(() -> {

            return null;
        });
    }

    //todo need to improve for flowable and completable working
    private Flowable<ScanItem> toggleImpl(Scan scan) {
        return Flowable.fromCallable(() -> {
            repository.toggle(scan).blockingAwait();
            ScanItem item = null;
            return item;
        });
    }


}
