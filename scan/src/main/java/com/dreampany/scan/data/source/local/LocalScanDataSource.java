package com.dreampany.scan.data.source.local;

import android.graphics.Bitmap;

import com.dreampany.frame.data.model.Flag;
import com.dreampany.frame.data.source.local.FlagDao;
import com.dreampany.scan.data.enums.ScanType;
import com.dreampany.scan.data.model.Scan;
import com.dreampany.scan.data.source.ScanDataSource;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;

import java.util.List;

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
public class LocalScanDataSource implements ScanDataSource {

    private final FlagDao flagDao;
    private final ScanDao scanDao;

    public LocalScanDataSource(FlagDao flagDao, ScanDao scanDao) {
        this.flagDao = flagDao;
        this.scanDao = scanDao;
    }

    @Override
    public Completable putScan(Scan scan) {
        return Completable.fromAction(() -> scanDao.insert(scan));
    }

    @Override
    public Completable putScans(List<Scan> scans) {
        return Completable.fromAction(() -> scanDao.insert(scans));
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
        return scanDao.getScans();
    }

    @Override
    public Flowable<Scan> getScan(ScanType type, Bitmap bitmap) {
        return Flowable.fromCallable(() -> getScanImp(type, bitmap));
    }

    @Override
    public Flowable<List<Flag>> getFlags(String type, String subtype) {
        return null;
    }

    @Override
    public Flowable<List<Scan>> getFlagScans() {
        return null;
    }

    private Scan getScanImp(ScanType type, Bitmap bitmap) throws Exception {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionTextDetector detector = FirebaseVision.getInstance().getVisionTextDetector();
        Task<FirebaseVisionText> task = detector.detectInImage(image);
        if (task.isSuccessful()) {
            FirebaseVisionText text = task.getResult();
            StringBuilder result = new StringBuilder();
            for (FirebaseVisionText.Block block : text.getBlocks()) {
                result.append(block.getText());
            }
            return new Scan(result.toString(), type);
        } else {
            Exception exception = task.getException();
            Timber.e(exception);
            throw exception;
        }
    }
}
