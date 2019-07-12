package com.dreampany.translation.data.source.remote

import com.dreampany.frame.api.key.KeyManager
import com.dreampany.frame.misc.exception.EmptyException
import com.dreampany.network.manager.NetworkManager
import com.dreampany.translation.data.misc.TextTranslationMapper
import com.dreampany.translation.data.model.TextTranslation
import com.dreampany.translation.data.source.api.TranslationDataSource
import com.dreampany.translation.misc.Constants
import io.reactivex.Maybe
import timber.log.Timber
import javax.inject.Singleton

/**
 * Created by Roman-372 on 7/4/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

@Singleton
class RemoteTranslationDataSource
constructor(
    val network: NetworkManager,
    val mapper: TextTranslationMapper,
    val keyM: KeyManager,
    val service: YandexTranslationService
) : TranslationDataSource {
    override fun isReady(target: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun ready(target: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    init {
        keyM.setKeys(
            Constants.Yandex.TRANSLATE_API_KEY_ROMAN_BJIT_QURAN,
            Constants.Yandex.TRANSLATE_API_KEY_ROMAN_BJIT_WORD
        )
    }

    override fun isExistsRx(input: String, source: String, target: String): Maybe<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isExists(input: String, source: String, target: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItem(input: String, source: String, target: String): TextTranslation? {
        for (index in 0..keyM.length - 1) {
            try {
                val key = keyM.getKey()
                val lang = mapper.toLanguagePair(source, target)
                val response = service.getTranslation(key, input, lang).execute()
                if (response.isSuccessful) {
                    val textResponse = response.body()
                    textResponse?.let {
                        val result = mapper.toItem(input, source, target, it)
                        return result
                    }
                }
            } catch (error: Throwable) {
                Timber.e(error)
                keyM.forwardKey()
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

    override fun isExists(t: TextTranslation?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItem(id: String?): TextTranslation {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemRx(input: String, source: String, target: String): Maybe<TextTranslation> {
        return Maybe.create { emitter ->
            val result = getItem(input, source, target)
            if (!emitter.isDisposed) {
                if (result != null) {
                    emitter.onSuccess(result)
                } else {
                    emitter.onError(EmptyException())
                }
            }
        }
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

}