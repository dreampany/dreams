package com.dreampany.framework.data.source.repository

import com.dreampany.framework.data.enums.Level
import com.dreampany.framework.data.enums.Subtype
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.data.model.Point
import com.dreampany.framework.data.source.api.PointDataSource
import com.dreampany.framework.misc.ResponseMapper
import com.dreampany.framework.injector.annote.Room
import com.dreampany.framework.misc.RxMapper
import io.reactivex.Maybe
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2019-09-15
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class PointRepository
@Inject constructor(
    rx: RxMapper,
    rm: ResponseMapper,
    @Room private val room: PointDataSource
) : Repository<String, Point>(rx, rm), PointDataSource {
    override fun getAllPoints(): Long {
        return room.getAllPoints()
    }

    override fun getAllPoints(type: Type): Long {
        return room.getAllPoints(type)
    }

    override fun getAllPoints(type: Type, subtype: Subtype): Long {
        return room.getAllPoints(type, subtype)
    }

    override fun getItem(id: String, type: Type, subtype: Subtype, level: Level): Point? {
        return room.getItem(id, type, subtype, level)
    }

    override fun getItemRx(id: String, type: Type, subtype: Subtype, level: Level): Maybe<Point> {
        return room.getItemRx(id, type, subtype, level)
    }

    override fun isEmpty(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isEmptyRx(): Maybe<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCountRx(): Maybe<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isExists(t: Point): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isExistsRx(t: Point): Maybe<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItem(t: Point): Long {
        return room.putItem(t)
    }

    override fun putItemRx(t: Point): Maybe<Long> {
        return room.putItemRx(t)
    }

    override fun putItems(ts: List<Point>): List<Long>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItemsRx(ts: List<Point>): Maybe<List<Long>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(t: Point): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteRx(t: Point): Maybe<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(ts: List<Point>): List<Long>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteRx(ts: List<Point>): Maybe<List<Long>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItem(id: String): Point? {
        return room.getItem(id)
    }

    override fun getItemRx(id: String): Maybe<Point> {
        return room.getItemRx(id)
    }

    override fun getItems(): List<Point>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(): Maybe<List<Point>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItems(limit: Long): List<Point>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(limit: Long): Maybe<List<Point>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}