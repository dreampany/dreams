package com.dreampany.media.data.source.repository;

import com.dreampany.frame.misc.Memory;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.Room;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.media.data.model.Image;
import com.dreampany.media.data.source.api.MediaDataSource;

import javax.inject.Inject;

import io.reactivex.Maybe;

/**
 * Created by Hawladar Roman on 8/2/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public class ImageRepository extends MediaRepository<Image> {

    @Inject
    ImageRepository(RxMapper rx,
                  ResponseMapper rm,
                  @Room MediaDataSource<Image> local,
                    @Memory MediaDataSource<Image> memory){
        super(rx, rm, local, memory);
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