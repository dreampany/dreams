package com.dreampany.tools.data.source.room.converters

import androidx.room.TypeConverter
import com.dreampany.framework.data.source.room.converters.Converters
import com.dreampany.tools.data.enums.Currency

/**
 * Created by roman on 2019-11-15
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class CryptoConverters : Converters() {

    @Synchronized
    @TypeConverter
    fun toCurrencyValue(currency: Currency?): String? {
        return if (currency == null) null else currency.name
    }

    @Synchronized
    @TypeConverter
    fun toCurrency(value: String?): Currency? {
        return if (value.isNullOrEmpty()) null else Currency.valueOf(value)
    }
}