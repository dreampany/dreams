package com.dreampany.media.data.source.memory;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;

import com.dreampany.frame.util.CursorUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Maybe;
import timber.log.Timber;

/**
 * Created by Hawladar Roman on 3/8/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public abstract class MediaProvider<T> {

    Cursor getCursor() {
        return null;
    }

     T getItem(Cursor cursor) {
        return null;
     }

    List<T> getItems() {
        return getItems(Integer.MAX_VALUE);
    }

    Maybe<List<T>> getItemsRx() {
        return getItemsRx(Integer.MAX_VALUE);
    }

    List<T> getItems(int limit) {
        Cursor cursor = getCursor();
        if (!CursorUtil.hasCursor(cursor)) {
            return null;
        }

        List<T> items = null;
        if (cursor.moveToFirst()) {
            items = new ArrayList<>(cursor.getCount());
            do {
                T item = getItem(cursor);
                if (item != null) {
                    items.add(item);
                }
            } while (items.size() < limit && cursor.moveToNext());
        }
        CursorUtil.closeCursor(cursor);
        return items;
    }

    Maybe<List<T>> getItemsRx(int limit) {
        return Maybe.fromCallable(() -> getItems(limit));
    }

    Cursor resolveQuery(
            Context context,
            Uri uri,
            String[] projection,
            String selection,
            String[] selectionArgs,
            String sortOrder) {

        try {
            return context.getApplicationContext().getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
        } catch (SQLiteException invalidQuery) {
            Timber.e("ContentProvider makes exception: %s", invalidQuery.toString());
            return null;
        }
    }
}
