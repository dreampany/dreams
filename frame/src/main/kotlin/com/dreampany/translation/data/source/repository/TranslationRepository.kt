package com.dreampany.translation.data.source.repository

import com.dreampany.framework.data.source.repository.Repository
import com.dreampany.framework.misc.*
import com.dreampany.translation.data.model.TextTranslation
import com.dreampany.translation.data.source.api.TranslationDataSource
import io.reactivex.Maybe
import io.reactivex.functions.Consumer
import io.reactivex.internal.functions.Functions
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Roman-372 on 7/4/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

@Singleton
class TranslationRepository
@Inject constructor(
    rx: RxMapper,
    rm: ResponseMapper,
    @Room private val room: TranslationDataSource,
    @Machine private val machine: TranslationDataSource,
    @Firestore private val firestore: TranslationDataSource,
    @Remote private val remote: TranslationDataSource
) : Repository<String, TextTranslation>(rx, rm), TranslationDataSource {

    override fun isReady(target: String): Boolean {
        return machine.isReady(target)
    }

    override fun ready(target: String) {
        if (!isReady(target)) {
            machine.ready(target)
        }
    }

    override fun putItems(ts: List<TextTranslation>): List<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItemsRx(ts: List<TextTranslation>): Maybe<List<Long>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(ts: List<TextTranslation>): List<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteRx(ts: List<TextTranslation>): Maybe<List<Long>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isExistsRx(source: String, target: String, input: String): Maybe<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun isExists(source: String, target: String, input: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItem(source: String, target: String, input: String): TextTranslation? {
        return getItemRx(source, target, input).blockingGet()
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

    override fun isExists(t: TextTranslation): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItem(id: String): TextTranslation {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemRx(source: String, target: String, input: String): Maybe<TextTranslation> {
        val roomIf = getRoomItemIfRx(source, target, input)
        val machineIf = getMachineItemIfRx(source, target, input)
        val firestoreIf = getFirestoreItemIfRx(source, target, input)
        val remoteIf = getRemoteItemIfRx(source, target, input)
        return concatSingleFirstRx(roomIf, firestoreIf, remoteIf)
    }

    override fun isExistsRx(t: TextTranslation): Maybe<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItem(t: TextTranslation): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItemRx(t: TextTranslation): Maybe<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(t: TextTranslation): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteRx(t: TextTranslation): Maybe<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemRx(id: String): Maybe<TextTranslation> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItems(): List<TextTranslation> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(): Maybe<List<TextTranslation>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItems(limit: Long): List<TextTranslation> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(limit: Long): Maybe<List<TextTranslation>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun getRoomItemIfRx(
        source: String, target: String, input: String
    ): Maybe<TextTranslation> {
        return room.isExistsRx(source, target, input).map {
            if (it) {
                room.getItem(source, target, input)
            } else {
                Maybe.empty<TextTranslation>().blockingGet()
            }
        }
    }

    private fun getMachineItemIfRx(
        source: String, target: String, input: String
    ): Maybe<TextTranslation> {
        val maybe = machine.getItemRx(source, target, input)
            //if (machine.isReady(target)) machine.getItemRx(source, target, input) else Maybe.empty()
        return concatSingleSuccess(maybe, Consumer {
            rx.compute(room.putItemRx(it))
                .subscribe(Functions.emptyConsumer<Any>(), Functions.emptyConsumer<Any>())
        })
    }

    private fun getFirestoreItemIfRx(
        source: String, target: String, input: String
    ): Maybe<TextTranslation> {
        val maybe = firestore.getItemRx(source, target, input)
        return concatSingleSuccess(maybe, Consumer {
            rx.compute(room.putItemRx(it))
                .subscribe(Functions.emptyConsumer<Any>(), Functions.emptyConsumer<Any>())
        })
    }

    private fun getRemoteItemIfRx(
        source: String, target: String, input: String
    ): Maybe<TextTranslation> {
        val maybe = remote.getItemRx(source, target, input)
        return concatSingleSuccess(maybe, Consumer {
            rx.compute(room.putItemRx(it))
                .subscribe(Functions.emptyConsumer<Any>(), Functions.emptyConsumer<Any>())
            rx.compute(firestore.putItemRx(it))
                .subscribe(Functions.emptyConsumer<Any>(), Functions.emptyConsumer<Any>())
        })
    }

}