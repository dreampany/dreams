package com.dreampany.firebase

import com.dreampany.language.Language
import com.google.firebase.FirebaseApp
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateModelManager
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateRemoteModel
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Roman-372 on 7/12/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

@Singleton
class RxFirebaseTranslation {

    private val firebase: FirebaseApp
    private val manager: FirebaseTranslateModelManager
    private val translator: FirebaseNaturalLanguage

    private val bucket: MutableMap<String, Boolean>
    private var inited: Boolean = false

    @Inject
    constructor() {
        firebase = FirebaseApp.getInstance()
        manager = FirebaseTranslateModelManager.getInstance()
        translator = FirebaseNaturalLanguage.getInstance()
        bucket = mutableMapOf()

        init()
    }

    fun isReady(language: Language): Boolean {
        return bucket.get(language.code) ?: false
    }

    fun ready(language: Language) {
        val firebaseLanguageCode = convertToFirebaseLanguage(language)
        val conditions = FirebaseModelDownloadConditions.Builder()
            .build()
        val model = FirebaseTranslateRemoteModel.Builder(firebaseLanguageCode)
            .setDownloadConditions(conditions)
            .build()
        manager.downloadRemoteModelIfNeeded(model)
            .addOnSuccessListener {
                bucket.put(language.code, true)
                Timber.v("Firebase Translation Model Downloaded %s %s", language.code, true)
            }.addOnFailureListener {error ->
                Timber.e(error)
            }
    }

    private fun init() {
        manager.getAvailableModels(firebase)
            .addOnSuccessListener { models ->
                models.forEach { model ->
                    bucket.put(model.languageCode, true)
                    Timber.v("Firebase Translation Model %s %s", model.languageCode, true)
                }
                inited = true
            }.addOnFailureListener { error ->
                inited = true
                Timber.e(error)
            }
    }

    private fun convertToFirebaseLanguage(language: Language): Int {
        when(language) {
            Language.ARABIC->{
                return FirebaseTranslateLanguage.AR
            }
        }
        return 0
    }
}