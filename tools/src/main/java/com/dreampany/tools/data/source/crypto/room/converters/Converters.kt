package com.dreampany.tools.data.source.crypto.room.converters

import androidx.room.TypeConverter
import com.dreampany.common.data.source.room.converter.Converter
import com.google.gson.reflect.TypeToken

/**
 * Created by roman on 3/19/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Converters : Converter() {
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