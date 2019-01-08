package com.dreampany.share.data.misc;

import android.net.Uri;

import com.dreampany.frame.data.model.State;
import com.dreampany.frame.misc.SmartCache;
import com.dreampany.frame.misc.SmartMap;
import com.dreampany.media.data.misc.MediaMapper;
import com.dreampany.media.data.model.Apk;
import com.dreampany.media.data.source.api.MediaDataSource;
import com.dreampany.media.misc.ApkAnnote;

import javax.inject.Inject;

/**
 * Created by Hawladar Roman on 8/14/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class ApkStateMapper extends MediaMapper {

    private final SmartMap<Long, Apk> map;
    private final SmartCache<Long, Apk> cache;

    @Inject
    ApkStateMapper(@ApkAnnote SmartMap<Long, Apk> map,
                   @ApkAnnote SmartCache<Long, Apk> cache) {
        this.map = map;
        this.cache = cache;
    }

    @Override
    public Uri getUri() {
        return null;
    }

    @Override
    public String[] getProjection() {
        return new String[0];
    }

    @Override
    public String getSelection() {
        return null;
    }

    @Override
    public String[] getSelectionArgs() {
        return new String[0];
    }

    @Override
    public String getSortOrder() {
        return null;
    }

    public Apk toItem(State in, MediaDataSource<Apk> source) {
        if (in == null) {
            return null;
        }
        Apk out = map.get(in.getId());
        if (out == null) {
            out = source.getItem(in.getId());
            map.put(in.getId(), out);
        }
        return out;
    }
}
