package com.dreampany.tools.data.source.wifi.room.converters

import androidx.room.TypeConverter
import com.dreampany.framework.data.source.room.converter.Converter
import com.dreampany.tools.data.enums.wifi.Width

/**
 * Created by roman on 3/19/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Converters : Converter() {

    @Synchronized
    @TypeConverter
    fun toString(input: Width?): String? {
        return if (input == null) null else input.name
    }

    @Synchronized
    @TypeConverter
    fun toWidth(input: String?): Width? {
        return if (input.isNullOrEmpty()) null else Width.valueOf(input)
    }
}