package com.dreampany.share.data.misc;

import android.net.Uri;

import com.dreampany.framework.data.model.State;
import com.dreampany.framework.misc.SmartCache;
import com.dreampany.framework.misc.SmartMap;
import com.dreampany.media.data.misc.MediaMapper;
import com.dreampany.media.data.model.Image;
import com.dreampany.media.data.source.api.MediaDataSource;
import com.dreampany.media.misc.ImageAnnote;

import javax.inject.Inject;

/**
 * Created by Hawladar Roman on 8/14/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class ImageStateMapper extends MediaMapper {

    private final SmartMap<Long, Image> map;
    private final SmartCache<Long, Image> cache;

    @Inject
    ImageStateMapper(@ImageAnnote SmartMap<Long, Image> map,
                     @ImageAnnote SmartCache<Long, Image> cache) {
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

    public Image toItem(State in, MediaDataSource<Image> source) {
        if (in == null) {
            return null;
        }
        Image out = map.get(in.getId());
        if (out == null) {
            out = source.getItem(in.getId());
            map.put(in.getId(), out);
        }
        return out;
    }
}
