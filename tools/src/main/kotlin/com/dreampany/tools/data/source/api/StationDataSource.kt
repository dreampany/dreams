package com.dreampany.tools.data.source.api

import com.dreampany.framework.data.source.api.DataSource
import com.dreampany.framework.data.source.api.DataSourceRx
import com.dreampany.tools.data.model.Station
import io.reactivex.Maybe

/**
 * Created by roman on 2019-10-11
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface StationDataSource : DataSource<Station>, DataSourceRx<Station> {

    fun getItemsOfCountry(countryCode: String, limit: Long): List<Station>?

    fun getItemsOfCountryRx(countryCode: String, limit: Long): Maybe<List<Station>>

    fun getItemsOfTrends(limit: Long): List<Station>?

    fun getItemsOfTrendsRx(limit: Long): Maybe<List<Station>>

    fun getItemsOfPopular(limit: Long): List<Station>?

    fun getItemsOfPopularRx(limit: Long): Maybe<List<Station>>
}