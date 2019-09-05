package com.dreampany.media.data.source.repository;

import com.dreampany.framework.misc.Memory;
import com.dreampany.framework.misc.ResponseMapper;
import com.dreampany.framework.misc.Room;
import com.dreampany.framework.misc.RxMapper;
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
                  @Memory MediaDataSource<Apk> memory,
                  @Room MediaDataSource<Apk> room) {
        super(rx, rm, memory, room);
    }
}

