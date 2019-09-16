package com.dreampany.framework.data.misc

import com.dreampany.framework.data.enums.Level
import com.dreampany.framework.data.enums.Subtype
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.data.model.Point
import com.dreampany.framework.data.source.api.PointDataSource
import com.dreampany.framework.misc.PointAnnote
import com.dreampany.framework.misc.SmartCache
import com.dreampany.framework.misc.SmartMap
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2019-09-15
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class PointMapper
@Inject constructor(
    @PointAnnote private val map: SmartMap<String, Point>,
    @PointAnnote private val cache: SmartCache<String, Point>
) : Mapper() {

    fun isExists(id: String): Boolean {
        return map.contains(id)
    }

    fun isExists(id: String, type: Type, subtype: Subtype, level: Level): Boolean {
        if (isExists(id)) {
            val item = getItem(id)
            return item.hasProperty(type, subtype, level)
        }
        return false
    }

    fun putItem(item: Point) {
        map.put(item.id, item)
    }

    private fun getItem(id: String): Point {
        return map.get(id)
    }

    fun getItem(
        id: String, type: Type, subtype: Subtype, level: Level, credit: Int
        ,source: PointDataSource
    ): Point? {
        var item: Point? = null
        if (isExists(id, type, subtype, level)) {
            item = map.get(id)
        }
        if (item == null) {
            item = source.getItem(id, type, subtype, level)
            if (item != null) {
                map.put(id, item)
            }
        }
        if (item == null) {
            item = Point(id = id, type = type, subtype = subtype, level = level)
            map.put(id, item)
        }
        item.credit = credit
        return item
    }

}