package com.dreampany.share.vm;

import android.app.Application;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.dreampany.frame.misc.AppExecutors;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.frame.ui.vm.BaseViewModel;
import com.dreampany.media.data.enums.MediaType;
import com.dreampany.media.data.model.Media;
import com.dreampany.share.data.model.SelectEvent;
import com.dreampany.share.ui.model.MediaItem;
import com.dreampany.share.ui.model.UiTask;
import com.google.common.collect.Maps;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;



/**
 * Created by Hawladar Roman on 7/18/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class MediaViewModel extends BaseViewModel<Media, MediaItem, UiTask<Media>> {

    private final Map<MediaType, SelectEvent> events;
    private final MutableLiveData<Set<SelectEvent>> select;
    private LifecycleOwner selectOwner;

    @Inject
    MediaViewModel(Application application,
                   RxMapper rx,
                   AppExecutors ex,
                   ResponseMapper rm) {
        super(application, rx, ex, rm);
        events = Maps.newConcurrentMap();
        select = new MutableLiveData<>();
    }

    @Override
    protected void onCleared() {
        if (selectOwner != null) {
            select.removeObservers(selectOwner);
        }
        super.onCleared();
    }


    public void onSelect(SelectEvent event) {
        events.put(event.getType(), event);
        select.setValue(new HashSet<>(events.values()));
    }

    public void observeSelect(LifecycleOwner owner, Observer<Set<SelectEvent>> observer) {
        selectOwner = owner;
        observe(selectOwner, observer, select);
    }
}
