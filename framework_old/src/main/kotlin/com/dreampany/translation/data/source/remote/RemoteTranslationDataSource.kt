package com.dreampany.translation.data.source.remote

import com.dreampany.framework.api.key.KeyManager
import com.dreampany.framework.misc.exceptions.EmptyException
import com.dreampany.network.manager.NetworkManager
import com.dreampany.translation.data.misc.TextTranslationMapper
import com.dreampany.translation.data.model.TextTranslation
import com.dreampany.translation.data.source.api.TranslationDataSource
import com.dreampany.translation.data.source.api.YandexTranslationService
import com.dreampany.translation.misc.Constants
import io.reactivex.Maybe
import timber.log.Timber

/**
 * Created by Roman-372 on 7/4/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class RemoteTranslationDataSource
constructor(
    private val network: NetworkManager,
    private val keyM: KeyManager,
    private val mapper: TextTranslationMapper,
    private val service: YandexTranslationService
) : TranslationDataSource {
    init {
        keyM.setKeys(
            Constants.Yandex.TRANSLATE_API_KEY_ROMAN_BJIT_QURAN,
            Constants.Yandex.TRANSLATE_API_KEY_ROMAN_BJIT_WORD,
            Constants.Yandex.TRANSLATE_API_KEY_DREAMPANY
        )
    }

    override fun delete(ts: List<TextTranslation>): List<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteRx(ts: List<TextTranslation>): Maybe<List<Long>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isReady(target: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun ready(target: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isExistsRx(source: String, target: String, input: String): Maybe<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isExists(source: String, target: String, input: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItem(source: String, target: String, input: String): TextTranslation? {
        if (network.isObserving() && !network.hasInternet()) {
            return null
        }
        for (index in 0..keyM.length - 1) {
            try {
                val key = keyM.getKey()
                val lang = mapper.toLanguagePair(source, target)
                val response = service.getTranslation(key, input, lang).execute()
                if (response.isSuccessful) {
                    val textResponse = response.body()
                    textResponse?.let {
                        val result = mapper.toItem(source, target, input, it)
                        return result
                    }
                }
            } catch (error: Throwable) {
                Timber.e(error)
                keyM.randomForwardKey()
            }
        }
        return null
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

    override fun getItemRx(
        source: String,
        target: String,
        input: String
    ): Maybe<TextTranslation> {
        return Maybe.create { emitter ->
            val result = getItem(source, target, input)
            if (!emitter.isDisposed) {
                if (result != null) {
                    emitter.onSuccess(result)
                } else {
                    emitter.onError(EmptyException())
                }
            }
        }
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

}