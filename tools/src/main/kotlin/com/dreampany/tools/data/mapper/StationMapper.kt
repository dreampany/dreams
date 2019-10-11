package com.dreampany.tools.data.mapper

import com.dreampany.framework.data.misc.Mapper
import com.dreampany.framework.misc.SmartCache
import com.dreampany.framework.misc.SmartMap
import com.dreampany.tools.data.model.Station
import com.dreampany.tools.data.source.pref.StationPref
import com.dreampany.tools.injector.annotation.StationAnnote
import com.dreampany.tools.injector.annotation.StationItemAnnote
import com.dreampany.tools.ui.model.StationItem
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2019-10-11
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class StationMapper
@Inject constructor(
    @StationAnnote private val map: SmartMap<String, Station>,
    @StationAnnote private val cache: SmartCache<String, Station>,
    @StationItemAnnote private val uiMap: SmartMap<String, StationItem>,
    @StationItemAnnote private val uiCache: SmartCache<String, StationItem>,
    private val pref: StationPref
) : Mapper() {
}