package com.dreampany.share.data.source.repository.share;

import com.dreampany.framework.misc.ResponseMapper;
import com.dreampany.framework.misc.Room;
import com.dreampany.framework.misc.RxMapper;
import com.dreampany.media.data.model.Image;
import com.dreampany.share.data.source.api.ShareDataSource;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Hawladar Roman on 8/14/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Singleton
public class ImageShareRepository extends ShareRepository<Image> {

    @Inject
    ImageShareRepository(RxMapper rx,
                       ResponseMapper rm,
                       @Room ShareDataSource<Image> local) {
        super(rx, rm, local);
    }
}
