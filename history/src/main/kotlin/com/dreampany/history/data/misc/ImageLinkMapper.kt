package com.dreampany.history.data.misc

import com.dreampany.frame.data.model.ImageLink
import com.dreampany.frame.misc.SmartCache
import com.dreampany.frame.misc.SmartMap
import com.dreampany.history.data.enums.HistoryType
import com.dreampany.history.data.model.History
import com.dreampany.history.data.source.remote.WikiHistory
import com.dreampany.history.misc.ImageLinkAnnote
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Roman-372 on 7/30/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class ImageLinkMapper
@Inject constructor(
    @ImageLinkAnnote val map: SmartMap<String, ImageLink>,
    @ImageLinkAnnote val cache: SmartCache<String, ImageLink>
) {

/*    fun toItem(ref: String, url: String, title:String): ImageLink? {

    }*/
}