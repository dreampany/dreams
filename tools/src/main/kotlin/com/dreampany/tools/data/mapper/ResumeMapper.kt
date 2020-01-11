package com.dreampany.tools.data.mapper

import com.dreampany.framework.data.misc.Mapper
import com.dreampany.framework.misc.SmartCache
import com.dreampany.framework.misc.SmartMap
import com.dreampany.tools.data.model.Resume
import com.dreampany.tools.data.model.Server
import com.dreampany.tools.data.source.pref.VpnPref
import com.dreampany.tools.injector.annote.ResumeAnnote
import com.dreampany.tools.injector.annote.ResumeItemAnnote
import com.dreampany.tools.injector.annote.ServerAnnote
import com.dreampany.tools.injector.annote.ServerItemAnnote
import com.dreampany.tools.ui.model.ResumeItem
import com.dreampany.tools.ui.model.ServerItem
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2020-01-12
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class ResumeMapper
@Inject constructor(
    @ResumeAnnote private val map: SmartMap<String, Resume>,
    @ResumeAnnote private val cache: SmartCache<String, Resume>,
    @ResumeItemAnnote private val uiMap: SmartMap<String, ResumeItem>,
    @ResumeItemAnnote private val uiCache: SmartCache<String, ResumeItem>
) : Mapper() {

    fun putUiItem(id: String, uiItem: ResumeItem) {
        uiMap.put(id, uiItem)
    }

    fun getUiItem(id: String): ResumeItem? {
        return uiMap.get(id)
    }

}