package com.dreampany.history.data.misc

import com.dreampany.frame.misc.SmartCache
import com.dreampany.frame.misc.SmartMap
import com.dreampany.frame.util.DataUtilKt
import com.dreampany.history.data.enums.HistorySource
import com.dreampany.history.data.enums.LinkSource
import com.dreampany.history.data.model.ImageLink
import com.dreampany.history.misc.Constants
import com.dreampany.history.misc.ImageLinkAnnote
import com.dreampany.history.misc.ImageLinkItemAnnote
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

    fun convertSource(input: HistorySource): LinkSource {
        when (input) {
            HistorySource.WIKIPEDIA -> {
                return LinkSource.WIKIPEDIA
            }
        }
    }

    fun toItem(source: LinkSource, ref: String, input: Element): ImageLink? {

        var id = input.attr(Constants.ImageParser.SOURCE)

        if (!DataUtilKt.isValidUrl(id)) {
            id = getValidUrl(source, input)
        }

        if (!DataUtilKt.isValidUrl(id)) {
            return null
        }

        val title = input.attr(Constants.ImageParser.ALTERNATE)
        var output: ImageLink? = map.get(id)
        if (output == null) {
            output = ImageLink(id)
            map.put(id, output)
        }
        output.source = source
        output.ref = ref
        output.title = title
        return output
    }

    private fun getValidUrl(source: LinkSource, input: Element): String? {
        when (source) {
            LinkSource.WIKIPEDIA -> {
                if (input.hasParent()) {
                    val parent = input.parent()
                    val href = parent.attr(Constants.ImageParser.HREF)
                    return Constants.toUrl(source, href)
                }
            }
        }
        return null
    }
}