package com.dreampany.lca.data.source.room;

import android.arch.persistence.room.TypeConverter;

import com.dreampany.lca.data.enums.IcoStatus;

/**
 * Created by Hawladar Roman on 15/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public class IcoStatusConverter {
    @TypeConverter
    public static IcoStatus toStatus(String name) {
        return IcoStatus.valueOf(name);
    }

    @TypeConverter
    public static String toName(IcoStatus status) {
        return status.name();
    }
}
