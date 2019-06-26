package com.dreampany.share.vm;

import android.app.Application;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.dreampany.frame.misc.AppExecutors;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.misc.SmartMap;
import com.dreampany.frame.misc.exception.EmptyException;
import com.dreampany.frame.vm.BaseViewModel;
import com.dreampany.media.data.enums.MediaType;
import com.dreampany.media.data.model.Apk;
import com.dreampany.media.data.model.Image;
import com.dreampany.media.data.source.repository.ImageRepository;
import com.dreampany.share.data.model.SelectEvent;
import com.dreampany.share.data.source.repository.share.ImageShareRepository;
import com.dreampany.share.misc.Comparators;
import com.dreampany.share.misc.exception.SelectException;
import com.dreampany.share.ui.model.MediaItem;
import com.dreampany.share.ui.model.UiTask;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;


import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * Created by Hawladar Roman on 7/18/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */

public class ImageViewModel extends BaseViewModel<Image, MediaItem, UiTask<Image>> {

    private final ImageRepository repo;
    private final ImageShareRepository shareRepo;
    private final Comparators comparators;
    private final MutableLiveData<SelectEvent> select;
    private LifecycleOwner selectOwner;
    private Disposable selectDisposable;

    @Inject
    ImageViewModel(Application application,
                   RxMapper rx,
                   AppExecutors ex,
                   ResponseMapper rm,
                   ImageRepository repo,
                   ImageShareRepository shareRepo,
                   Comparators comparators) {
        super(application, rx, ex, rm);
        this.repo = repo;
        this.shareRepo = shareRepo;
        this.comparators = comparators;
        select = new MutableLiveData<>();
    }

    @Override
    public void clear() {
        if (selectOwner != null) {
            select.removeObservers(selectOwner);
        }
        removeSubscription(selectDisposable);
        super.clear();
    }

    public void observeSelect(LifecycleOwner owner, Observer<SelectEvent> observer) {
        selectOwner = owner;
        observe(selectOwner, observer, select);
    }


    public void loads(boolean fresh) {
        if (!preLoads(fresh)) {
            return;
        }
        Disposable disposable = getRx()
                .backToMain(getItemsRx())
                .doOnSubscribe(subscription -> postProgressMultiple(true))
                .subscribe(items -> {
                    postResult(items);
                }, this::postFailureMultiple);
        addMultipleSubscription(disposable);
    }


    public void loadsWithShare(boolean fresh) {
        getEx().postToUi(() -> {
            if (!preLoads(fresh)) {
                return;
            }
            Disposable disposable = getRx()
                    .backToMain(getItemsWithShareRx())
                    .doOnSubscribe(subscription -> postProgressMultiple(true))
                    .subscribe(items -> {
                        postResult(items);
                        notifySelect();
                    }, this::postFailureMultiple);
            addMultipleSubscription(disposable);
        }, 500L);
    }


    public void loadsShared(boolean fresh) {
        if (!preLoads(fresh)) {
            return;
        }
        Disposable disposable = getRx()
                .backToMain(getSharedItemsRx())
                .doOnSubscribe(subscription -> postProgressMultiple(true))
                .subscribe(items -> {
                    postResult(items);
                }, this::postFailureMultiple);
        addMultipleSubscription(disposable);
    }

    public void toggleSelect(Image image) {
        if (hasSingleDisposable()) {
            return;
        }
        Disposable disposable = getRx()
                .backToMain(toggleSelectImpl(image))
                .subscribe(item -> {
                    postResult(item);
                    notifySelect();
                }, this::postFailure);
        addSingleSubscription(disposable);
    }

    public void toggleShare(Apk item) {

    }

    public void toggleShare(List<Apk> items) {

    }

    public void selectToShare() {
        if (!preLoads(false)) {
            return;
        }
        Disposable disposable = getRx()
                .backToMain(putSharedItemsRx())
                .doOnSubscribe(subscription -> postProgressMultiple(true))
                .subscribe(items -> {
                    //postResult(items);
                    getUiSelects().clear();
                    notifySelect();
                }, this::postFailureMultiple);
        addMultipleSubscription(disposable);
    }


    public void notifySelect() {
        if (hasDisposable(selectDisposable)) {
            return;
        }
        selectDisposable = getRx()
                .backToMain(getSelectEventRx())
                .subscribe(select::setValue, this::postFailure);
        addSubscription(selectDisposable);
    }

    private Maybe<List<MediaItem>> getItemsRx() {
        return repo.getItemsRx()
                .flatMap((Function<List<Image>, MaybeSource<List<MediaItem>>>) this::getItemsRx);
    }

    private Maybe<List<MediaItem>> getItemsWithShareRx() {
        return repo.getItemsRx(false)
                .flatMap((Function<List<Image>, MaybeSource<List<MediaItem>>>) this::getItemsWithShareRx);
    }

    private Maybe<List<MediaItem>> getSharedItemsRx() {
        return shareRepo.getSharedItemsRx(repo)
                .flatMap((Function<List<Image>, MaybeSource<List<MediaItem>>>) this::getSharedItemsRx);
    }

    private Maybe<List<Long>> putSharedItemsRx() {
        if (!hasSelection()) {
            return Maybe.error(new EmptyException());
        }
        List<Image> items = new ArrayList<>(getUiSelects());
        return shareRepo.putItemsRx(items);
    }


    private Maybe<List<MediaItem>> getItemsRx(List<Image> result) {
        return Maybe.fromCallable(() -> {
            List<MediaItem> items = new ArrayList<>(result.size());

            for (Image image : result) {
                MediaItem item = getItem(image);
                items.add(item);
            }
            return items;
        });
    }


    private Maybe<List<MediaItem>> getItemsWithShareRx(List<Image> result) {
        return Maybe.fromCallable(() -> {
            Collections.sort(result, comparators.getDateModifiedComparator());
            List<MediaItem> items = new ArrayList<>(result.size());
            for (Image image : result) {
                MediaItem item = getItemWithShare(image);
                items.add(item);
            }
            return items;
        });
    }


    private MaybeSource<List<MediaItem>> getSharedItemsRx(List<Image> result) {
        return Maybe.fromCallable(() -> {
            List<MediaItem> items = new ArrayList<>(result.size());

            for (Image image : result) {
                MediaItem item = getItem(image);
                items.add(item);
            }
            return items;
        });
    }

    private Single<SelectEvent> getSelectEventRx() {
        return Single.fromCallable(() -> {
            int selected = getUiSelects().size();
            int total = repo.getCount();
            SelectEvent event = new SelectEvent(MediaType.IMAGE, selected, total);
            return event;
        });
    }

    private MediaItem getItem(Image image) {
        SmartMap<Long, MediaItem> map = getUiMap();
        MediaItem item = map.get(image.getId());
        if (item == null) {
            item = MediaItem.getItem(image);
            map.put(image.getId(), item);
        }
        return item;
    }

    private MediaItem getItemWithShare(Image image) {
        SmartMap<Long, MediaItem> map = getUiMap();
        MediaItem item = map.get(image.getId());
        if (item == null) {
            item = MediaItem.getItem(image);
            map.put(image.getId(), item);
        }
        adjustShare(item);
        adjustSelect(item);
        return item;
    }

    private void adjustShare(MediaItem item) {
        boolean shared =  shareRepo.isShared((Image) item.getItem());
        item.setShared(shared);
    }

    private void adjustSelect(MediaItem item) {
        boolean selected = getUiSelects().contains((Image) item.getItem());
        item.setSelected(selected);
    }

    private Flowable<MediaItem> toggleSelectImpl(Image image) {
        return Flowable.fromCallable(() -> {
            boolean shared = shareRepo.isShared(image);
            if (shared) {
                throw new SelectException("This item is already shared!");
            }
            if (getUiSelects().contains(image)) {
                getUiSelects().remove(image);
            } else {
                getUiSelects().add(image);
            }
            MediaItem item = getItemWithShare(image);
            return item;
        });
    }
}
