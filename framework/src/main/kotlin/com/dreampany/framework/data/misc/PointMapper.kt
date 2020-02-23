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
        id: String,
        type: Type,
        subtype: Subtype,
        level: Level,
        points: Long,
        extra: String?,
        source: PointDataSource
    ): Point? {
        var point: Point? = null
        if (isExists(id, type, subtype, level)) {
            point = map.get(id)
        }
        if (point == null) {
            point = source.getItem(id, type, subtype, level)
        }
        if (point == null) {
            point = Point(id = id, type = type, subtype = subtype, level = level)
        }
        point.points = points
        point.extra = extra
        map.put(id, point)
        return point
    }

    fun getItem(
        type: Type,
        subtype: Subtype,
        source: PointDataSource
    ): Point? {
        val credit = source.getAllPoints(type, subtype)
        var point = Point(type = type, subtype = subtype, points = credit)
        return point
    }

    fun getItem(
        source: PointDataSource
    ): Point? {
        val credit = source.getAllPoints()
        var point = Point(points = credit)
        return point
    }

}