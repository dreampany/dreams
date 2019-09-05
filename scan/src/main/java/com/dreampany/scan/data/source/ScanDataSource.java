package com.dreampany.scan.data.source;

import android.graphics.Bitmap;

import com.dreampany.framework.data.model.Flag;
import com.dreampany.scan.data.enums.ScanType;
import com.dreampany.scan.data.model.Scan;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * Created by Hawladar Roman on 6/29/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public interface ScanDataSource {
    Completable putScan(Scan scan);

    Completable putScans(List<Scan> scans);

    Completable toggle(Scan scan);

    Completable toggle(Flag flag);

    Single<Boolean> isFlagged(Scan scan);

    Single<Boolean> isFlagged(Flag flag);

    Flowable<List<Scan>> getScans();

    Flowable<Scan> getScan(ScanType type, Bitmap bitmap);

    Flowable<List<Flag>> getFlags(String type, String subtype);

    Flowable<List<Scan>> getFlagScans();
}
