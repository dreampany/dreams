package com.dreampany.music.data.source.repository;

import com.dreampany.frame.data.source.repository.Repository;
import com.dreampany.frame.misc.ResponseMapper;
import com.dreampany.frame.misc.RxMapper;
import com.dreampany.music.data.model.Song;
import com.dreampany.music.data.source.MusicDataSource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;


import io.reactivex.Flowable;

/**
 * Created by Hawladar Roman on 6/25/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Singleton
public class SongRepository extends Repository<String, Song> implements MusicDataSource {


    @Inject
    SongRepository(ResponseMapper responseMapper,
                   RxMapper rxMapper) {
        super(responseMapper, rxMapper);
    }

    @Override
    public Flowable<List<Song>> getSongs() {
        return null;
    }
}
