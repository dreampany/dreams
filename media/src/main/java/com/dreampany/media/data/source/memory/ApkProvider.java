package com.dreampany.media.data.source.memory;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.annimon.stream.Stream;
import com.dreampany.frame.util.AndroidUtil;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.media.data.misc.ApkMapper;
import com.dreampany.media.data.model.Apk;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

/**
 * Created by Hawladar Roman on 8/6/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Singleton
public class ApkProvider extends MediaProvider<Apk> {

    private final Context context;
    private final ApkMapper mapper;

    @Inject
    ApkProvider(Context context, ApkMapper mapper) {
        this.context = context;
        this.mapper = mapper;
    }

    @Override
    List<Apk> getItems() {
        return getItems(Integer.MAX_VALUE);
    }

    @Override
    Maybe<List<Apk>> getItemsRx() {
        return getItemsRx(Integer.MAX_VALUE);
    }

    @Override
    List<Apk> getItems(int limit) {
        PackageManager pm = context.getApplicationContext().getPackageManager();
        List<ApplicationInfo> infos = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        if (DataUtil.isEmpty(infos)) {
            return null;
        }
        List<Apk> items = new ArrayList<>(infos.size());
        Stream.of(infos).forEach(info -> {
            if (items.size() >= limit) {
                return;
            }
            if (AndroidUtil.isValid(pm, info) && !AndroidUtil.isSystemApp(info)) {
                items.add(mapper.toItem(info, pm));
            }
        });
        return items;
    }

    @Override
    Maybe<List<Apk>> getItemsRx(int limit) {
        return Maybe.fromCallable(() -> getItems(limit));
    }
}
