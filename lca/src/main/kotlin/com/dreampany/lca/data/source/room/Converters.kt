package com.dreampany.lca.data.source.room

import androidx.room.TypeConverter
import com.dreampany.frame.util.DataUtil
import com.dreampany.lca.data.enums.CoinSource
import com.dreampany.lca.data.enums.Currency
import com.dreampany.lca.data.enums.IcoStatus
import com.dreampany.lca.data.model.Quote
import com.dreampany.lca.data.model.SourceInfo
import com.google.common.base.Strings
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by Roman-372 on 8/2/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Converters {

    private val gson = Gson()
    private val typeOfListString = object : TypeToken<List<String>>() {}.type
    private val typeOfMap = object : TypeToken<Map<Currency, Quote>>() {}.type
    private val typeOfListPrices = object : TypeToken<List<List<Float>>>() {}.type

    @TypeConverter
    @Synchronized
    fun toString(values: List<String>): String? {
        return if (DataUtil.isEmpty(values)) {
            null
        } else gson.toJson(values, typeOfListString)
    }

    @TypeConverter
    @Synchronized
    fun toList(json: String): List<String>? {
        return if (DataUtil.isEmpty(json)) {
            null
        } else gson.fromJson<List<String>>(json, typeOfListString)
    }

    @TypeConverter
    @Synchronized
    fun fromPriceQuotes(quotes: Map<Currency, Quote>?): String? {
        return if (quotes == null || quotes.isEmpty()) {
            null
        } else gson.toJson(quotes, typeOfMap)
    }

    @TypeConverter
    @Synchronized
    fun fromMapString(json: String): Map<Currency, Quote>? {
        return if (Strings.isNullOrEmpty(json)) {
            null
        } else gson.fromJson<Map<Currency, Quote>>(json, typeOfMap)
    }

    @TypeConverter
    @Synchronized
    fun fromListOfListPrice(prices: List<List<Float>>?): String? {
        return if (prices == null || prices.isEmpty()) {
            null
        } else gson.toJson(prices, typeOfListPrices)

    }

    @TypeConverter
    @Synchronized
    fun fromListOfListPricesString(json: String): List<List<Float>>? {
        return if (Strings.isNullOrEmpty(json)) {
            null
        } else gson.fromJson<List<List<Float>>>(json, typeOfListPrices)
    }

    @TypeConverter
    @Synchronized
    fun toString(currency: Currency?): String? {
        return currency?.name
    }

    @TypeConverter
    @Synchronized
    fun toCurrency(json: String?): Currency? {
        return if (json.isNullOrEmpty()) null else Currency.valueOf(json)
    }

    @TypeConverter
    @Synchronized
    fun toString(source: CoinSource?): String? {
        return source?.name
    }

    @TypeConverter
    @Synchronized
    fun toCoinSource(json: String?): CoinSource? {
        return if (json.isNullOrEmpty()) null else CoinSource.valueOf(json)
    }

    @TypeConverter
    @Synchronized
    fun toIcoStatusString(status: IcoStatus?): String? {
        return status?.name
    }

    @TypeConverter
    @Synchronized
    fun toIcoStatus(json: String?): IcoStatus? {
        return if (json.isNullOrEmpty()) null else IcoStatus.valueOf(json)
    }
}