package com.dreampany.music.data.source;

import com.dreampany.music.data.model.Song;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Hawladar Roman on 6/25/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public interface MusicDataSource {
    Flowable<List<Song>> getSongs();
}
