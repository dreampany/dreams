package com.dreampany.translate.vm

import android.app.Application
import com.dreampany.frame.data.misc.StateMapper
import com.dreampany.frame.data.model.Response
import com.dreampany.frame.data.source.repository.StateRepository
import com.dreampany.frame.misc.*
import com.dreampany.frame.misc.exception.EmptyException
import com.dreampany.frame.misc.exception.ExtraException
import com.dreampany.frame.misc.exception.MultiException
import com.dreampany.frame.util.DataUtil
import com.dreampany.frame.vm.BaseViewModel
import com.dreampany.language.Language
import com.dreampany.network.manager.NetworkManager
import com.dreampany.translate.data.model.TextTranslationRequest
import com.dreampany.translate.data.source.pref.Pref
import com.dreampany.translate.misc.Constants
import com.dreampany.translate.ui.model.TextTranslationItem
import com.dreampany.translate.ui.model.TranslationItem
import com.dreampany.translate.ui.model.UiTaskKt
import com.dreampany.translation.data.model.TextTranslation
import com.dreampany.translation.data.model.Translation
import com.dreampany.translation.data.source.repository.TranslationRepository
import io.reactivex.Maybe
import javax.inject.Inject

/**
 * Created by Roman-372 on 7/11/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class TranslationViewModel @Inject constructor(
    application: Application,
    rx: RxMapper,
    ex: AppExecutors,
    rm: ResponseMapper,
    val network: NetworkManager,
    val pref: Pref,
    val stateMapper: StateMapper,
    val stateRepo: StateRepository,
    val translationRepo: TranslationRepository,
    @Favorite val favorites: SmartMap<String, Boolean>
) : BaseViewModel<Translation, TranslationItem<*, *, *>, UiTaskKt<Translation>>(
    application, rx, ex, rm
) {

    init {
        val language = pref.getTargetLanguage(Language.ENGLISH)
        if (!language.equals(Language.ENGLISH)) {
            //translationRepo.ready(language.code)
        }
    }

    fun setSourceLanguage(language: Language) {
        pref.setSourceLanguage(language)
    }

    fun setTargetLanguage(language: Language) {
        pref.setTargetLanguage(language)
    }

    fun getSourceLanguage(): Language {
        return pref.getSourceLanguage(Language.ENGLISH)
    }

    fun getTargetLanguage(): Language {
        return pref.getTargetLanguage(Language.ENGLISH)
    }

    fun getLanguages(): List<Language> {
        val result = ArrayList<Language>()
        result.add(Language.AFRIKAANS)
        result.add(Language.ARABIC)
        result.add(Language.BELARUSIAN)
        result.add(Language.BULGARIAN)
        result.add(Language.BENGALI)
        result.add(Language.CATALAN)
        result.add(Language.CZECH)
        result.add(Language.ENGLISH)
        result.add(Language.SPANISH)
        result.add(Language.FRENCH)
        result.add(Language.HINDI)
        result.add(Language.RUSSIAN)
        result.add(Language.CHINESE)
        return result
    }

    fun load(request: TextTranslationRequest) {
        if (!takeAction(request.important, singleDisposable)) {
            return
        }
        val disposable = rx
            .backToMain(translateItemRx(request))
            .doOnSubscribe { subscription ->
                if (request.progress) {
                    postProgress(true)
                }
            }
            .subscribe({ result ->
                if (request.progress) {
                    postProgress(false)
                }
                postResult(Response.Type.GET, result)
            }, { error ->
                if (request.progress) {
                    postProgress(false)
                }
                postFailures(MultiException(error, ExtraException()))
            })
        addSingleSubscription(disposable)
    }

    private fun translateItemRx(request: TextTranslationRequest): Maybe<TranslationItem<*, *, *>> {
        return Maybe.create { emitter ->

            var result: TranslationItem<*, *, *>? = null
            val textTranslation = translationRepo.getItem(request.source, request.target, request.input)
            if (textTranslation != null) {
                result = getItem(textTranslation)
            }

            if (!emitter.isDisposed) {
                if (result != null) {
                    emitter.onSuccess(result)
                } else {
                    emitter.onComplete()
                }
            }
        }
    }

    private fun getItem(item: TextTranslation): TranslationItem<*, *, *> {
        val map = uiMap
        var result: TranslationItem<*, *, *>? = map.get(item.id)
        if (result == null) {
            result = TextTranslationItem.getItem(item)
            map.put(item.id, result)
        }
        return result
    }
}