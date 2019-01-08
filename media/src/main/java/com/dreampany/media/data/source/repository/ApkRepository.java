package com.dreampany.media.data.source.repository;

import com.dreampany.frame.misc.Memory;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.Room;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.media.data.model.Apk;
import com.dreampany.media.data.source.api.MediaDataSource;

import javax.inject.Inject;
import javax.inject.Singleton;


/**
 * Created by Hawladar Roman on 7/16/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */

@Singleton
public class ApkRepository extends MediaRepository<Apk> {

    @Inject
    ApkRepository(RxMapper rx,
                  ResponseMapper rm,
                  @Room MediaDataSource<Apk> local,
                  @Memory MediaDataSource<Apk> memory) {
        super(rx, rm, local, memory);
    }
}

