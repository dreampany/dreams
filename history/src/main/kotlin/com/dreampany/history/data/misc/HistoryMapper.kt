package com.dreampany.history.data.misc

import com.dreampany.frame.misc.SmartCache
import com.dreampany.frame.misc.SmartMap
import com.dreampany.frame.util.DataUtilKt
import com.dreampany.frame.util.TimeUtil
import com.dreampany.frame.util.TimeUtilKt
import com.dreampany.history.data.enums.HistoryType
import com.dreampany.history.data.model.History
import com.dreampany.history.data.model.Link
import com.dreampany.history.data.source.remote.WikiHistory
import com.dreampany.history.data.source.remote.WikiLink
import com.dreampany.history.misc.Constants
import com.dreampany.history.misc.HistoryAnnote
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Roman-372 on 7/25/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class HistoryMapper
@Inject constructor(
    @HistoryAnnote val map: SmartMap<String, History>,
    @HistoryAnnote val cache: SmartCache<String, History>
) {

    fun toItem(date: String, url: String, input: WikiHistory, inputType: HistoryType): History {
        val day = TimeUtilKt.getDay(date, Constants.Date.MONTH_DAY)
        val month = TimeUtilKt.getMonth(date, Constants.Date.MONTH_DAY)
        val id = DataUtilKt.join(day, month, input.year)
        var output: History? = map.get(id)
        if (output == null) {
            output = History(id)
            map.put(id, output)
        }
        output.time = TimeUtil.currentTime()
        output.id = id
        output.type = inputType
        output.day = day
        output.month = month
        output.year = input.year
        output.text = input.text
        output.html = input.html
        output.url = url
        output.links = toLinks(input.links)
        return output
    }

    fun toLinks(inputLinks: MutableList<WikiLink>): MutableList<Link> {
        val links = mutableListOf<Link>()
        inputLinks.forEach {
            links.add(toLink(it))
        }
        return links
    }

    fun toLink(inputLink: WikiLink): Link {
        return Link(inputLink.title, inputLink.link)
    }
}