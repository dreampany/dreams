package com.dreampany.history.data.misc

import com.dreampany.frame.data.model.ImageLink
import com.dreampany.frame.misc.SmartCache
import com.dreampany.frame.misc.SmartMap
import com.dreampany.frame.util.DataUtilKt
import com.dreampany.history.data.model.History
import com.dreampany.history.misc.Constants
import com.dreampany.history.misc.ImageLinkAnnote
import com.dreampany.history.misc.ImageLinkItemAnnote
import com.dreampany.history.ui.model.HistoryItem
import com.dreampany.history.ui.model.ImageLinkItem
import org.jsoup.nodes.Element
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
    @ImageLinkAnnote val cache: SmartCache<String, ImageLink>,
    @ImageLinkItemAnnote val uiMap: SmartMap<String, ImageLinkItem>,
    @ImageLinkItemAnnote val uiCache: SmartCache<String, ImageLinkItem>
) {

    fun getUiItem(id: String): ImageLinkItem? {
        return uiMap.get(id)
    }

    fun putUiItem(id: String, uiItem: ImageLinkItem) {
        uiMap.put(id, uiItem)
    }

    fun toItem(ref: String, input: Element): ImageLink? {
        val baseUrl = input.baseUri()
        val relUrl = input.attr(Constants.ImageParser.SOURCE)
        val title = input.attr(Constants.ImageParser.ALTERNATE)

        val url = baseUrl.plus(relUrl)

        val id = DataUtilKt.join(ref, url)

        var output: ImageLink? = map.get(id)
        if (output == null) {
            output = ImageLink(id)
            map.put(id, output)
        }
        output.ref = ref
        output.url = url
        output.title = title
        return output
    }
}