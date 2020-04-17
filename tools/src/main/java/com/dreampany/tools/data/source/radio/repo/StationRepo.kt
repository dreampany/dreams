package com.dreampany.tools.data.source.radio.repo

import com.dreampany.common.inject.annote.Remote
import com.dreampany.common.misc.func.ResponseMapper
import com.dreampany.common.misc.func.RxMapper
import com.dreampany.tools.data.enums.radio.StationOrder
import com.dreampany.tools.data.source.radio.mapper.StationMapper
import com.dreampany.tools.data.source.radio.api.StationDataSource
import com.dreampany.tools.data.source.radio.pref.RadioPref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 18/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class StationRepo
@Inject constructor(
    rx: RxMapper,
    rm: ResponseMapper,
    private val pref: RadioPref,
    private val mapper: StationMapper,
    @Remote private val remote: StationDataSource
) : StationDataSource {

    @Throws
    override suspend fun getItemsOfCountry(
        countryCode: String,
        hideBroken: Boolean,
        order: StationOrder,
        reverse: Boolean,
        offset: Long,
        limit: Long
    ) = withContext(Dispatchers.IO) {
        remote.getItemsOfCountry(countryCode,hideBroken, order, reverse, offset, limit)
    }

    @Throws
    override suspend fun getItemsOfTrends(limit: Long) = withContext(Dispatchers.IO) {
        remote.getItemsOfTrends(limit)
    }

    @Throws
    override suspend fun getItemsOfPopular(limit: Long) = withContext(Dispatchers.IO) {
        remote.getItemsOfPopular(limit)
    }
}