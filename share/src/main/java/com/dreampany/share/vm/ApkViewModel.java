package com.dreampany.share.vm;

import android.app.Application;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.dreampany.frame.data.model.Response;
import com.dreampany.frame.misc.AppExecutors;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.misc.SmartMap;
import com.dreampany.frame.misc.exception.EmptyException;
import com.dreampany.frame.misc.exception.ExtraException;
import com.dreampany.frame.misc.exception.MultiException;
import com.dreampany.frame.vm.BaseViewModel;
import com.dreampany.media.data.enums.MediaType;
import com.dreampany.media.data.model.Apk;
import com.dreampany.media.data.source.repository.ApkRepository;
import com.dreampany.share.data.model.SelectEvent;
import com.dreampany.share.data.source.repository.share.ApkShareRepository;
import com.dreampany.share.misc.Comparators;
import com.dreampany.share.misc.exception.SelectException;
import com.dreampany.share.ui.model.MediaItem;
import com.dreampany.share.ui.model.UiTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;


import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import timber.log.Timber;

/**
 * Created by Hawladar Roman on 7/18/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */

public class ApkViewModel extends BaseViewModel<Apk, MediaItem, UiTask<Apk>> {

    private final ApkRepository repo;
    private final ApkShareRepository shareRepo;
    private final Comparators comparators;

    @Inject
    ApkViewModel(Application application,
                 RxMapper rx,
                 AppExecutors ex,
                 ResponseMapper rm,
                 ApkRepository repo,
                 ApkShareRepository shareRepo,
                 Comparators comparators) {
        super(application, rx, ex, rm);
        this.repo = repo;
        this.shareRepo = shareRepo;
        this.comparators = comparators;
    }

    @Override
    public void clear() {
        super.clear();
    }


    public void loads(boolean important, boolean progress) {
        if (!takeAction(important, getMultipleDisposable())) {
            return;
        }
        Disposable disposable = getRx()
                .backToMain(getItemsRx())
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


    public void loadsWithShare(boolean important, boolean progress) {
        getEx().postToUi(() -> {
            if (!takeAction(important, getMultipleDisposable())) {
                return;
            }
            Disposable disposable = getRx()
                    .backToMain(getItemsWithShareRx())
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
                        notifySelect();
                    }, error -> {
                        if (progress) {
                            postProgress(false);
                        }
                        postFailures(new MultiException(error, new ExtraException()));
                    });
            addMultipleSubscription(disposable);
        }, 500L);
    }


    public void loadsShared(boolean important, boolean progress) {
        if (!takeAction(important, getMultipleDisposable())) {
            return;
        }
        Disposable disposable = getRx()
                .backToMain(getSharedItemsRx())
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

    public void toggleSelect(Apk apk) {
        if (hasSingleDisposable()) {
            return;
        }
        Disposable disposable = getRx()
                .backToMain(toggleSelectImpl(apk))
                .subscribe(item -> {
                    postResult(Response.Type.UPDATE, item);
                    notifySelect();
                }, this::postFailure);
        addSingleSubscription(disposable);
    }

    public void toggleShare(Apk item) {

    }

    public void toggleShare(List<Apk> items) {

    }

    public void selectToShare() {
        if (!takeAction(false, getMultipleDisposable())) {
            return;
        }
        Disposable disposable = getRx()
                .backToMain(putSharedItemsRx())
                .doOnSubscribe(subscription -> {
                    postProgress(true);
                }).subscribe(items -> {
                    getUiSelects().clear();
                    notifySelect();
                }, this::postFailure);
        addMultipleSubscription(disposable);
    }


    public void notifySelect() {
        if (!takeAction(false, getSingleDisposable())) {
            return;
        }
        Disposable disposable = getRx()
                .backToMain(getSelectEventRx())
                .subscribe(new Consumer<SelectEvent>() {
                    @Override
                    public void accept(SelectEvent selectEvent) throws Exception {

                    }
                }, this::postFailure);
        addSingleSubscription(disposable);
    }

    private Maybe<List<MediaItem>> getItemsRx() {
        return repo.getItemsRx()
                .flatMap((Function<List<Apk>, MaybeSource<List<MediaItem>>>) this::getItemsRx);
    }

    private Maybe<List<MediaItem>> getItemsWithShareRx() {
        return repo.getItemsRx()
                .flatMap((Function<List<Apk>, MaybeSource<List<MediaItem>>>) this::getItemsWithShareRx);
    }

    private Maybe<List<MediaItem>> getSharedItemsRx() {
        return shareRepo.getSharedItemsRx(repo)
                .flatMap((Function<List<Apk>, MaybeSource<List<MediaItem>>>) this::getSharedItemsRx);
    }

    private Maybe<List<Long>> putSharedItemsRx() {
        if (!hasSelection()) {
            return Maybe.error(new EmptyException());
        }
        List<Apk> items = new ArrayList<>(getUiSelects());
        return shareRepo.putItemsRx(items);
    }


    private Maybe<List<MediaItem>> getItemsRx(List<Apk> result) {
        return Maybe.fromCallable(() -> {
            List<MediaItem> items = new ArrayList<>(result.size());

            for (Apk apk : result) {
                MediaItem item = getItem(apk);
                items.add(item);
            }
            return items;
        });
    }


    private Maybe<List<MediaItem>> getItemsWithShareRx(List<Apk> result) {
        return Maybe.fromCallable(() -> {
            Collections.sort(result, comparators.getDisplayNameComparator());
            List<MediaItem> items = new ArrayList<>(result.size());
            for (Apk apk : result) {
                MediaItem item = getItemWithShare(apk);
                items.add(item);
            }
            return items;
        });
    }


    private Maybe<List<MediaItem>> getSharedItemsRx(List<Apk> result) {
        return Maybe.fromCallable(() -> {
            List<MediaItem> items = new ArrayList<>(result.size());

            for (Apk apk : result) {
                MediaItem item = getItem(apk);
                items.add(item);
            }
            return items;
        });
    }

    private Single<SelectEvent> getSelectEventRx() {
        return Single.fromCallable(() -> {
            int selected = getUiSelects().size();
            int total = repo.getCount();
            SelectEvent event = new SelectEvent(MediaType.APK, selected, total);
            return event;
        });
    }

    private MediaItem getItem(Apk apk) {
        SmartMap<Long, MediaItem> map = getUiMap();
        MediaItem item = map.get(apk.getId());
        if (item == null) {
            item = MediaItem.getItem(apk);
            map.put(apk.getId(), item);
        }
        return item;
    }

    private MediaItem getItemWithShare(Apk apk) {
        SmartMap<Long, MediaItem> map = getUiMap();
        MediaItem item = map.get(apk.getId());
        if (item == null) {
            item = MediaItem.getItem(apk);
            map.put(apk.getId(), item);
        }
        adjustShare(item);
        adjustSelect(item);
        return item;
    }

    private void adjustShare(MediaItem item) {
        boolean shared = shareRepo.isShared((Apk) item.getItem());
        item.setShared(shared);
    }

    private void adjustSelect(MediaItem item) {
        boolean selected = getUiSelects().contains((Apk) item.getItem());
        item.setSelected(selected);
    }

    private Flowable<MediaItem> toggleSelectImpl(Apk apk) {
        return Flowable.fromCallable(() -> {
            boolean shared = shareRepo.isShared(apk);
            if (shared) {
                throw new SelectException("This item is already shared!");
            }
            if (getUiSelects().contains(apk)) {
                getUiSelects().remove(apk);
            } else {
                getUiSelects().add(apk);
            }
            MediaItem item = getItemWithShare(apk);
            return item;
        });
    }
}
