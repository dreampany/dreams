package com.dreampany.tools.data.mapper

import com.dreampany.framework.data.enums.Quality
import com.dreampany.framework.data.misc.Mapper
import com.dreampany.framework.data.model.Country
import com.dreampany.framework.misc.SmartCache
import com.dreampany.framework.misc.SmartMap
import com.dreampany.framework.util.NetworkUtil
import com.dreampany.framework.util.TimeUtil
import com.dreampany.tools.data.model.Server
import com.dreampany.tools.data.source.pref.VpnPref
import com.dreampany.tools.injector.annote.CountryAnnote
import com.dreampany.tools.injector.annote.CountryItemAnnote
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.injector.annote.ServerAnnote
import com.dreampany.tools.injector.annote.ServerItemAnnote
import com.dreampany.tools.ui.model.CountryItem
import com.dreampany.tools.ui.model.ServerItem
import okhttp3.ResponseBody
import timber.log.Timber
import java.io.*
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by roman on 2019-10-06
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class CountryMapper
@Inject constructor(
    @CountryAnnote private val map: SmartMap<String, Country>,
    @CountryAnnote private val cache: SmartCache<String, Country>,
    @CountryItemAnnote private val uiMap: SmartMap<String, CountryItem>,
    @CountryItemAnnote private val uiCache: SmartCache<String, CountryItem>
) : Mapper() {

    fun getUiItem(id: String): CountryItem? {
        return uiMap.get(id)
    }

    fun putUiItem(id: String, uiItem: CountryItem) {
        uiMap.put(id, uiItem)
    }

    fun getItem(code: String?, name: String?): Country? {
        if (code.isNullOrEmpty() || name.isNullOrEmpty()) {
            return null
        }

        var out: Country? = map.get(code)
        if (out == null) {
            out = Country(code)
            map.put(code, out)
        }
        out.run {
            this.name = name
        }
        return out
    }


}