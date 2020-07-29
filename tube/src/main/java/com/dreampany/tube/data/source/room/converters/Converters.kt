package com.dreampany.tube.data.source.room.converters

import androidx.room.TypeConverter
import com.dreampany.framework.data.source.room.converter.Converter
import com.dreampany.tube.data.enums.CategoryType

/**
 * Created by roman on 3/19/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Converters : Converter() {

    @Synchronized
    @TypeConverter
    fun toString(input: CategoryType?): String? {
        return if (input == null) null else input.name
    }

    @Synchronized
    @TypeConverter
    fun toCategoryType(input: String?): CategoryType? {
        return if (input.isNullOrEmpty()) null else CategoryType.valueOf(input)
    }
}