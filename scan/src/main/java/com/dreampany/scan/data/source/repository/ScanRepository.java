package com.dreampany.scan.data.source.repository;

import android.graphics.Bitmap;

import com.dreampany.framework.data.model.Flag;
import com.dreampany.framework.data.source.repository.Repository;
import com.dreampany.framework.misc.Local;
import com.dreampany.framework.misc.ResponseMapper;
import com.dreampany.framework.misc.RxMapper;
import com.dreampany.framework.misc.SmartCache;
import com.dreampany.framework.misc.SmartMap;
import com.dreampany.scan.data.enums.ScanType;
import com.dreampany.scan.data.model.Scan;
import com.dreampany.scan.data.source.ScanDataSource;
import com.dreampany.scan.misc.ScanAnnote;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;


import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import timber.log.Timber;

/**
 * Created by Hawladar Roman on 6/29/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Singleton
public class ScanRepository extends Repository<String, Scan> implements ScanDataSource {

    private final ScanDataSource localSource;
    private final Map<Scan, Boolean> flags;


    @Inject
    ScanRepository(
            @ScanAnnote SmartCache<String, Scan> cache,
            @ScanAnnote SmartMap<String, Scan> map,
            @Local ScanDataSource localSource,
            ResponseMapper responseMapper,
            RxMapper rxMapper) {
        super(cache, map, responseMapper, rxMapper);
        this.localSource = localSource;
        flags = Maps.newConcurrentMap();
    }

    @Override
    public Completable putScan(Scan scan) {
        return null;
    }

    @Override
    public Completable putScans(List<Scan> scans) {
        return null;
    }

    @Override
    public Completable toggle(Scan scan) {
        return null;
    }

    @Override
    public Completable toggle(Flag flag) {
        return null;
    }

    @Override
    public Single<Boolean> isFlagged(Scan scan) {
        return null;
    }

    @Override
    public Single<Boolean> isFlagged(Flag flag) {
        return null;
    }

    @Override
    public Flowable<List<Scan>> getScans() {
        return null;
    }

    @Override
    public Flowable<Scan> getScan(ScanType type, Bitmap bitmap) {
        return getScanWithSave(type, bitmap);
    }

    @Override
    public Flowable<List<Flag>> getFlags(String type, String subtype) {
        return null;
    }

    @Override
    public Flowable<List<Scan>> getFlagScans() {
        return null;
    }

    public boolean hasFlagged(Scan scan) {
        return flags.containsKey(scan) && flags.get(scan);
    }

    private Flowable<Scan> getScanWithSave(ScanType type, Bitmap bitmap) {
        return localSource.getScan(type, bitmap)
                .onErrorReturnItem(new Scan())
                .filter(item -> !(item == null))
                .doOnNext(item -> rxMapper.compute(putScan(item)).subscribe());
    }
}
