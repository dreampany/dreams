package com.dreampany.tools.data.source.crypto.room.converters

import androidx.room.TypeConverter
import com.dreampany.common.data.source.room.converter.Converter
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
    fun toValue(currency: Currency?): String? {
        return if (currency == null) null else currency.name
    }

    @Synchronized
    @TypeConverter
    fun toCurrency(value: String?): Currency? {
        return if (value.isNullOrEmpty()) null else Currency.valueOf(value)
    }

   /* private val photoType = object : TypeToken<List<Photo>>() {}.type

    @TypeConverter
    @Synchronized
    fun toPhotosString(photos: List<Photo>?): String? {
        return if (photos.isNullOrEmpty()) null
        else gson.toJson(photos, photoType)
    }

    @TypeConverter
    @Synchronized
    fun toDefList(json: String?): List<Photo>? {
        return if (json.isNullOrEmpty()) null
        else gson.fromJson(json, photoType)
    }*/
}