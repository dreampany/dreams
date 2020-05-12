package com.dreampany.tools.data.source.crypto.room.converters

import androidx.room.TypeConverter
import com.dreampany.framework.data.source.room.converter.Converter
import com.dreampany.tools.data.enums.crypto.Currency

/**
 * Created by roman on 3/19/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Converters : Converter() {

    @Synchronized
    @TypeConverter
    fun toString(input: Currency?): String? {
        return if (input == null) null else input.name
    }

    @Synchronized
    @TypeConverter
    fun toCurrency(input: String?): Currency? {
        return if (input.isNullOrEmpty()) null else Currency.valueOf(input)
    }
}