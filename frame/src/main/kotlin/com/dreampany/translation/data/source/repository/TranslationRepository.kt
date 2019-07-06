package com.dreampany.translation.data.source.repository

import com.dreampany.frame.data.source.repository.RepositoryKt
import com.dreampany.frame.misc.*
import com.dreampany.translation.data.model.TextTranslation
import com.dreampany.translation.data.source.api.TextTranslationDataSource
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
    @Room val room: TextTranslationDataSource,
    @Firestore val firestore: TextTranslationDataSource,
    @Remote val remote: TextTranslationDataSource
) : RepositoryKt<String, TextTranslation>(rx, rm), TextTranslationDataSource {
    override fun isExistsRx(input: String, source: String, target: String): Maybe<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun isExists(input: String, source: String, target: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItem(input: String, source: String, target: String): TextTranslation? {
        return getItemRx(input, source, target).blockingGet()
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

    override fun isExists(t: TextTranslation?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItem(id: String?): TextTranslation {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemRx(input: String, source: String, target: String): Maybe<TextTranslation> {
        val roomIf = getRoomItemIfRx(input, source, target)
        val firestoreIf = getFirestoreItemIfRx(input, source, target)
        val remoteIf = getRemoteItemIfRx(input, source, target)
        return concatSingleFirstRx(roomIf, firestoreIf, remoteIf)
    }

    override fun isExistsRx(t: TextTranslation?): Maybe<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItem(t: TextTranslation?): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItemRx(t: TextTranslation?): Maybe<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItems(ts: MutableList<TextTranslation>?): MutableList<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItemsRx(ts: MutableList<TextTranslation>?): Maybe<MutableList<Long>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(t: TextTranslation?): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteRx(t: TextTranslation?): Maybe<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(ts: MutableList<TextTranslation>?): MutableList<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteRx(ts: MutableList<TextTranslation>?): Maybe<MutableList<Long>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemRx(id: String?): Maybe<TextTranslation> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItems(): MutableList<TextTranslation> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(): Maybe<MutableList<TextTranslation>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItems(limit: Int): MutableList<TextTranslation> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(limit: Int): Maybe<MutableList<TextTranslation>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun getRoomItemIfRx(
        text: String,
        source: String,
        target: String
    ): Maybe<TextTranslation> {
        return room.isExistsRx(text, source, target).map {
            if (it) {
                room.getItem(text, source, target)
            } else {
                Maybe.empty<TextTranslation>().blockingGet()
            }
        }
    }

    private fun getFirestoreItemIfRx(
        text: String,
        source: String,
        target: String
    ): Maybe<TextTranslation> {
        val maybe = firestore.getItemRx(text, source, target)
        return contactSingleSuccess(maybe, Consumer {
            rx.compute(room.putItemRx(it)).subscribe(
                Functions.emptyConsumer<Any>(),
                Functions.emptyConsumer<Any>()
            )
        })
    }

    private fun getRemoteItemIfRx(text: String, source: String, target: String): Maybe<TextTranslation> {
        val maybe = firestore.getItemRx(text, source, target)
        return contactSingleSuccess(maybe, Consumer {
            rx.compute(room.putItemRx(it)).subscribe(Functions.emptyConsumer<Any>(), Functions.emptyConsumer<Any>())
            rx.compute(firestore.putItemRx(it)).subscribe(Functions.emptyConsumer<Any>(), Functions.emptyConsumer<Any>())
        })
    }

}