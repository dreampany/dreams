package com.dreampany.translation.data.source.machine

import com.dreampany.firebase.RxFirebaseTranslation
import com.dreampany.network.manager.NetworkManager
import com.dreampany.translation.data.misc.TextTranslationMapper
import com.dreampany.translation.data.model.TextTranslation
import com.dreampany.translation.data.source.api.TranslationDataSource
import io.reactivex.Maybe
import javax.inject.Singleton

/**
 * Created by Roman-372 on 7/11/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class MachineTranslationDataSource constructor(
    val network: NetworkManager,
    val mapper: TextTranslationMapper,
    val tranlator: RxFirebaseTranslation
) : TranslationDataSource {

    override fun isReady(target: String): Boolean {
        return tranlator.isReady(target)
    }

    override fun ready(target: String) {
        tranlator.ready(target)
    }

    override fun isExists(source: String, target: String, input: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isExists(t: TextTranslation): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isExistsRx(source: String, target: String, input: String): Maybe<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isEmpty(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isExistsRx(t: TextTranslation): Maybe<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItem(source: String, target: String, input: String): TextTranslation? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isEmptyRx(): Maybe<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCountRx(): Maybe<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItem(t: TextTranslation): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItems(ts: List<TextTranslation>): List<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putItemsRx(ts: List<TextTranslation>): Maybe<List<Long>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(t: TextTranslation): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteRx(t: TextTranslation): Maybe<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteRx(ts: List<TextTranslation>): Maybe<List<Long>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItem(id: String): TextTranslation? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemRx(source: String, target: String, input: String): Maybe<TextTranslation> {
        return tranlator.translateRx(source, target, input)
            .map {output-> mapper.toItem(source, target, input, output)}
    }

    override fun putItemRx(t: TextTranslation): Maybe<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(ts: List<TextTranslation>): List<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemRx(id: String): Maybe<TextTranslation> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItems(): ArrayList<TextTranslation> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(): Maybe<List<TextTranslation>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItems(limit: Long): ArrayList<TextTranslation> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsRx(limit: Long): Maybe<List<TextTranslation>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}