package com.dreampany.tools.data.source.repository

import com.dreampany.framework.data.misc.StoreMapper
import com.dreampany.framework.data.source.repository.Repository
import com.dreampany.framework.data.source.repository.StoreRepository
import com.dreampany.framework.misc.ResponseMapper
import com.dreampany.framework.injector.annote.Room
import com.dreampany.framework.misc.RxMapper
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.data.mapper.ContactMapper
import com.dreampany.tools.data.model.Contact
import com.dreampany.tools.data.source.api.ContactDataSource
import io.reactivex.Maybe
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2019-11-16
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class ContactRepository
@Inject constructor(
    rx: RxMapper,
    rm: ResponseMapper,
    private val network: NetworkManager,
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepository,
    private val mapper: ContactMapper,
    @Room private val room: ContactDataSource
) : Repository<String, Contact>(rx, rm), ContactDataSource {

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

    override fun isExists(t: Contact): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isExistsRx(t: Contact): Maybe<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItem(t: Contact): Long {
        return room.putItem(t)
    }

    override fun putItemRx(t: Contact): Maybe<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItems(ts: List<Contact>): List<Long>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItemsRx(ts: List<Contact>): Maybe<List<Long>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(t: Contact): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteRx(t: Contact): Maybe<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(ts: List<Contact>): List<Long>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteRx(ts: List<Contact>): Maybe<List<Long>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItem(id: String): Contact? {
        return room.getItem(id)
    }

    override fun getItemRx(id: String): Maybe<Contact> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItems(): List<Contact>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(): Maybe<List<Contact>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItems(limit: Long): List<Contact>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(limit: Long): Maybe<List<Contact>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}