package com.dreampany.music.data.source.local;

import com.dreampany.framework.data.source.local.FlagDao;
import com.dreampany.framework.misc.SmartMap;
import com.dreampany.music.data.model.Song;
import com.dreampany.music.data.source.MusicDataSource;
import com.dreampany.music.misc.SortOrder;

import java.util.List;

import javax.inject.Singleton;

import io.reactivex.Flowable;

/**
 * Created by Hawladar Roman on 6/25/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Singleton
public class LocalMusicDataSource implements MusicDataSource {

    private final FlagDao flagDao;
    private final SmartMap<String, Song> songs;

    public LocalMusicDataSource(FlagDao flagDao) {
        this.flagDao = flagDao;
        songs = new SmartMap<>();
    }

    @Override
    public Flowable<List<Song>> getSongs() {
        return null;
    }
}
