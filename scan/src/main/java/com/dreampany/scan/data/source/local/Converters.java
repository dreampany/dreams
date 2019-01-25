package com.dreampany.scan.data.source.local;

import androidx.room.TypeConverter;

import com.dreampany.scan.data.enums.ScanType;

/**
 * Created by Hawladar Roman on 15/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public class Converters {
    @TypeConverter
    synchronized public static int fromScanType(ScanType type) {
        return type.ordinal();
    }

    @TypeConverter
    synchronized public static ScanType toScanType(int code) {
        return ScanType.valueOf(code);
    }
}
