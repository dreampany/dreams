package com.dreampany.media.data.source.room;

import androidx.room.TypeConverter;

import com.dreampany.media.data.enums.MediaType;
import com.google.common.base.Strings;

/**
 * Created by Hawladar Roman on 15/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public final class Converters {

    @TypeConverter
    synchronized public static String toString(MediaType type) {
        if (type == null) {
            return null;
        }
        return type.name();
    }

    @TypeConverter
    synchronized public static MediaType toMediaType(String value) {
        if (Strings.isNullOrEmpty(value)) {
            return MediaType.NONE;
        }
        return MediaType.valueOf(value);
    }
}
