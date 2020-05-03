package com.dreampany.framework.util;

import android.database.Cursor;

/**
 * Created by Hawladar Roman on 8/3/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public final class CursorUtil {
    private CursorUtil() {}

    public static boolean hasCursor(Cursor cursor) {
        if (cursor == null || cursor.isClosed()) return false;
        if (cursor.getCount() <= 0) {
            closeCursor(cursor);
            return false;
        }
        return true;
    }

    public static void closeCursor(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    public static String getString(Cursor cursor, String key) {
        return cursor.getString(cursor.getColumnIndex(key));
    }
}
