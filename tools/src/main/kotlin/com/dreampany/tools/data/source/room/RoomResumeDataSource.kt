package com.dreampany.tools.data.source.room

import com.dreampany.tools.data.mapper.ResumeMapper
import com.dreampany.tools.data.mapper.ServerMapper
import com.dreampany.tools.data.model.Resume
import com.dreampany.tools.data.source.api.ResumeDataSource
import com.dreampany.tools.data.source.api.ServerDataSource
import com.dreampany.tools.data.source.room.dao.ResumeDao
import com.dreampany.tools.data.source.room.dao.ServerDao
import io.reactivex.Maybe

/**
 * Created by roman on 2020-01-12
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class RoomResumeDataSource
constructor(
    private val mapper: ResumeMapper,
    private val dao: ResumeDao
) : ResumeDataSource {
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

    override fun isExists(t: Resume): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isExistsRx(t: Resume): Maybe<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItem(t: Resume): Long {
        return dao.insertOrReplace(t)
    }

    override fun putItemRx(t: Resume): Maybe<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItems(ts: List<Resume>): List<Long>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItemsRx(ts: List<Resume>): Maybe<List<Long>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(t: Resume): Int {
        return dao.delete(t)
    }

    override fun delete(ts: List<Resume>): List<Long>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteRx(t: Resume): Maybe<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteRx(ts: List<Resume>): Maybe<List<Long>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItem(id: String): Resume? {
        return dao.getItem(id)
    }

    override fun getItemRx(id: String): Maybe<Resume> {
        return dao.getItemRx(id)
    }

    override fun getItems(): List<Resume>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItems(limit: Long): List<Resume>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(): Maybe<List<Resume>> {
        return dao.getItemsRx()
    }

    override fun getItemsRx(limit: Long): Maybe<List<Resume>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}