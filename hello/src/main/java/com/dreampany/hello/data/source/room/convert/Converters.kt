package com.dreampany.hello.data.source.room.convert

import androidx.room.TypeConverter
import com.dreampany.framework.data.source.room.converter.Converter
import com.dreampany.hello.data.enums.Gender

/**
 * Created by roman on 26/9/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Converters : Converter() {

    @Synchronized
    @TypeConverter
    fun toString(input: Gender?): String? {
        return if (input == null) null else input.name
    }

    @Synchronized
    @TypeConverter
    fun toGender(input: String?): Gender? {
        return if (input.isNullOrEmpty()) null else Gender.valueOf(input)
    }
}