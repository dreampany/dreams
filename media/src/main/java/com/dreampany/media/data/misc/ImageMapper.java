package com.dreampany.media.data.misc;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.dreampany.frame.misc.SmartCache;
import com.dreampany.frame.misc.SmartMap;
import com.dreampany.frame.util.CursorUtil;
import com.dreampany.frame.util.DataUtil;
import com.dreampany.frame.util.FileUtil;
import com.dreampany.frame.util.TimeUtil;
import com.dreampany.media.data.model.Image;
import com.dreampany.media.misc.ImageAnnote;

import javax.inject.Inject;

/**
 * Created by Hawladar Roman on 7/17/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class ImageMapper extends MediaMapper {

    private final SmartMap<Long, Image> map;
    private final SmartCache<Long, Image> cache;

    @Inject
    ImageMapper(@ImageAnnote SmartMap<Long, Image> map,
                @ImageAnnote SmartCache<Long, Image> cache) {
        this.map = map;
        this.cache = cache;
    }

    public Image toImage(Context context, Cursor cursor) {
        if (context == null || cursor == null) {
            return null;
        }

        int idIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
        int dataIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
        int sizeIndex = cursor.getColumnIndex(MediaStore.Images.Media.SIZE);
        int displayNameIndex = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
        int titleIndex = cursor.getColumnIndex(MediaStore.Images.Media.TITLE);
        int dateAddedIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED);
        int dateModifiedIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED);
        int mimeTypeIndex = cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE);
        int dateTakenIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);

        long mediaId = cursor.getLong(idIndex);
        String uri = cursor.getString(dataIndex);
        long size = cursor.getLong(sizeIndex);
        String displayName = cursor.getString(displayNameIndex);
        String title = cursor.getString(titleIndex);
        long dateAdded = cursor.getLong(dateAddedIndex);
        long dateModified = cursor.getLong(dateModifiedIndex);
        String mimeType = cursor.getString(mimeTypeIndex);
        long dateTaken = cursor.getLong(dateTakenIndex);
        if (!FileUtil.isFileExists(uri)) {
            return null;
        }

        String thumbUri = getImageThumbUri(context, mediaId);
        long id = DataUtil.getSha512(uri);
        Image out = map.get(id);
        if (out == null) {
            out = new Image();
            map.put(id, out);
        }
        out.setId(id);
        out.setTime(TimeUtil.currentTime());
        out.setDateTaken(dateTaken);
        out.setId(FileUtil.getFileHash(uri));
        out.setUri(uri);
        out.setThumbUri(thumbUri);
        out.setDisplayName(displayName);
        out.setTitle(title);
        out.setSize(size);
        out.setDateAdded(dateAdded);
        out.setDateModified(dateModified);
        out.setMimeType(mimeType);
        return out;
    }

    @Override
    public Uri getUri() {
        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }

    @Override
    public String[] getProjection() {
        return projection;
    }

    @Override
    public String getSelection() {
        return null;
    }

    @Override
    public String[] getSelectionArgs() {
        return null;
    }

    @Override
    public String getSortOrder() {
        return MediaStore.Images.Media.DATE_TAKEN + " desc";
    }

    private String getImageThumbUri(Context context, long id) {
        Cursor cursor = MediaStore.Images.Thumbnails.queryMiniThumbnail(
                context.getContentResolver(),
                id,
                MediaStore.Images.Thumbnails.MINI_KIND,
                new String[]{MediaStore.Images.Thumbnails.DATA}
        );

        if (!CursorUtil.hasCursor(cursor)) return null;

        String uri = null;
        if (cursor.moveToFirst()) {
            uri = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA));
        }
        CursorUtil.closeCursor(cursor);
        return uri;
    }

    private final String[] projection = {

            //base
            MediaStore.Images.Media._ID,

            //media
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.TITLE,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.DATE_MODIFIED,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.WIDTH,
            MediaStore.Images.Media.HEIGHT,

            //image
            MediaStore.Images.Media.DATE_TAKEN,
    };
}
