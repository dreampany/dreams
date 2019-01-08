package com.dreampany.share.data.source.repository.share;

import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.Room;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.media.data.model.Apk;
import com.dreampany.share.data.source.ShareDataSource;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Maybe;

/**
 * Created by Hawladar Roman on 8/14/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Singleton
public class ApkShareRepository extends ShareRepository<Apk> {

    @Inject
    ApkShareRepository(RxMapper rx,
                       ResponseMapper rm,
                       @Room ShareDataSource<Apk> local) {
        super(rx, rm, local);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Maybe<Boolean> isEmptyRx() {
        return null;
    }
}
