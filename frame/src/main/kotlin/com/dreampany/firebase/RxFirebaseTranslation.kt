package com.dreampany.firebase

import com.dreampany.language.Language
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.FirebaseApp
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateModelManager
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateRemoteModel
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions
import io.reactivex.Maybe
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
class RxFirebaseTranslation @Inject constructor() {

    private val firebase: FirebaseApp
    private val manager: FirebaseTranslateModelManager
    private val natural: FirebaseNaturalLanguage

    private val bucket: MutableMap<String, Boolean>
    private var inited: Boolean = false

    init {
        firebase = FirebaseApp.getInstance()
        manager = FirebaseTranslateModelManager.getInstance()
        natural = FirebaseNaturalLanguage.getInstance()
        bucket = mutableMapOf()
        init()
    }

    fun isReady(language: String): Boolean {
        val ready = bucket.get(language) ?: false
        Timber.v("Ready Check %s %s", language, ready)
        return ready
    }

    fun ready(language: String) {
        Timber.v("Ready Fired for %s", language)
        val languageCode = convertToFirebaseLanguage(language)
        val conditions = FirebaseModelDownloadConditions.Builder()
            .build()
        val model = FirebaseTranslateRemoteModel.Builder(languageCode)
            .setDownloadConditions(conditions)
            .build()
        manager.downloadRemoteModelIfNeeded(model)
            .addOnSuccessListener {
                bucket.put(language, true)
                Timber.v("Firebase Translation Model Downloaded %s %s", language, true)
            }.addOnFailureListener { error ->
                Timber.e(error)
            }
    }

    fun translateRx(source: String, target: String, input: String): Maybe<String> {
        Timber.v("Firebase Translation Fired")
        return Maybe.create { emitter ->
            val task = toTask(source, target, input)
            task.addOnSuccessListener { output ->
                if (!emitter.isDisposed) {
                    if (output != null) {
                        Timber.v("Firebase Translation Result %s %s", input, output)
                        emitter.onSuccess(output)
                    } else {
                        emitter.onComplete()
                    }
                }
            }.addOnFailureListener { error ->
                if (!emitter.isDisposed) {
                    emitter.onError(error)
                }
            }
        }
    }


    fun init() {
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

    private fun toTask(source: String, target: String, input: String): Task<String> {
        val sourceCode = convertToFirebaseLanguage(source)
        val targetCode = convertToFirebaseLanguage(target)
        val options = FirebaseTranslatorOptions.Builder()
            .setSourceLanguage(sourceCode)
            .setTargetLanguage(targetCode)
            .build()

        val translator = natural.getTranslator(options)
        val task = translator.downloadModelIfNeeded().continueWithTask { task ->
            if (task.isSuccessful) {
                translator.translate(input)
            } else{
                Tasks.forException<String>(task.exception ?: Exception(""))
            }
        }
        return task
    }

    private fun convertToFirebaseLanguage(language: String): Int {
        when (language) {
            Language.AFRIKAANS.code -> {
                return FirebaseTranslateLanguage.AF
            }
            Language.ARABIC.code -> {
                return FirebaseTranslateLanguage.AR
            }
            Language.BELARUSIAN.code -> {
                return FirebaseTranslateLanguage.BE
            }
            Language.BULGARIAN.code -> {
                return FirebaseTranslateLanguage.BG
            }
            Language.BENGALI.code -> {
                return FirebaseTranslateLanguage.BN
            }
            Language.CATALAN.code -> {
                return FirebaseTranslateLanguage.CA
            }
            Language.CZECH.code -> {
                return FirebaseTranslateLanguage.CS
            }
            Language.ENGLISH.code -> {
                return FirebaseTranslateLanguage.EN
            }
            Language.SPANISH.code -> {
                return FirebaseTranslateLanguage.ES
            }
            Language.FRENCH.code -> {
                return FirebaseTranslateLanguage.FR
            }
            Language.HINDI.code -> {
                return FirebaseTranslateLanguage.HI
            }
            Language.RUSSIAN.code -> {
                return FirebaseTranslateLanguage.RU
            }
            Language.CHINESE.code -> {
                return FirebaseTranslateLanguage.ZH
            }
        }
        return FirebaseTranslateLanguage.ZH
    }
}