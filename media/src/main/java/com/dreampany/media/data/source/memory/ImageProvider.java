package com.dreampany.media.data.source.memory;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.dreampany.media.data.misc.ImageMapper;
import com.dreampany.media.data.model.Image;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Hawladar Roman on 3/8/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Singleton
public class ImageProvider extends MediaProvider<Image> {

    private final Context context;
    private final ImageMapper mapper;

    @Inject
    ImageProvider(Context context, ImageMapper mapper) {
        this.context = context;
        this.mapper = mapper;
    }

    @Override
    Cursor getCursor() {
        Uri uri = mapper.getUri();
        String[] projection = mapper.getProjection();
        String selection = mapper.getSelection();
        String[] selectionArgs = mapper.getSelectionArgs();
        String sortOrder = mapper.getSortOrder();
        return resolveQuery(context, uri, projection, selection, selectionArgs, sortOrder);
    }

    @Override
    Image getItem(Cursor cursor) {
        return mapper.toImage(context, cursor);
    }
}
