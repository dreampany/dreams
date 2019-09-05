package com.dreampany.share.vm;

import android.app.Application;

import com.dreampany.framework.misc.AppExecutors;
import com.dreampany.framework.misc.ResponseMapper;
import com.dreampany.framework.misc.RxMapper;
import com.dreampany.framework.misc.SmartMap;
import com.dreampany.framework.misc.exception.ExtraException;
import com.dreampany.framework.misc.exception.MultiException;
import com.dreampany.framework.ui.vm.BaseViewModel;
import com.dreampany.media.data.enums.MediaType;
import com.dreampany.media.data.model.Media;
import com.dreampany.media.data.source.repository.ApkRepository;
import com.dreampany.media.data.source.repository.ImageRepository;
import com.dreampany.media.data.source.repository.MediaRepository;
import com.dreampany.network.NetworkManager;
import com.dreampany.share.ui.model.MediaItem;
import com.dreampany.share.ui.model.UiTask;
import com.google.common.collect.Maps;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;


import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * Created by Hawladar Roman on 7/17/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class ShareViewModel extends BaseViewModel<Media, MediaItem, UiTask<Media>> {

    private final NetworkManager network;
    private final Map<MediaType, MediaRepository> repos;

    @Inject
    ShareViewModel(Application application,
                   RxMapper rx,
                   AppExecutors ex,
                   ResponseMapper rm,
                   NetworkManager network,
                   ApkRepository apkRepo,
                   ImageRepository imageRepo) {
        super(application, rx, ex, rm);
        this.network = network;
        repos = Maps.newConcurrentMap();
        repos.put(MediaType.APK, apkRepo);
        repos.put(MediaType.IMAGE, imageRepo);
    }


    public void loads(boolean fresh) {
        if (fresh ) {
            removeMultipleSubscription();
        }
        if (hasMultipleDisposable()|| true) {
            notifyUiState();
            return;
        }
        Disposable disposable = getRx()
                .backToMain(getItemsRx())
                .doOnSubscribe(subscription -> postProgressMultiple(true))
                .subscribe(this::postResult, error -> {
                    postFailureMultiple(new MultiException(error, new ExtraException()));
                });
        addMultipleSubscription(disposable);
    }

    private Flowable<List<MediaItem>> getItemsRx() {
        return getItemsRx(MediaType.APK);
    }

    private Flowable<List<MediaItem>> getItemsRx(MediaType... types) {
        List<Flowable<List<Media>>> sources = new ArrayList<>();
        for (MediaType type : types) {
            sources.add(repos.get(type).getItemsRx().toFlowable());
        }
        return Flowable.concat(sources)
                .flatMap((Function<List<Media>, Publisher<List<MediaItem>>>) this::getItemsRx);
    }

    private Flowable<List<MediaItem>> getItemsRx(List<Media> result) {
        return Flowable.fromCallable(() -> {
            List<MediaItem> items = new ArrayList<>(result.size());

            for (Media media : result) {
                MediaItem item = getItem(media);
                items.add(item);
            }
            return items;
        });
    }

    private MediaItem getItem(Media media) {
        SmartMap<Long, MediaItem> map = getUiMap();
        MediaItem item = map.get(media.getId());
        if (item == null) {
            item = MediaItem.getItem(media);
            map.put(media.getId(), item);
        }
        return item;
    }
}
