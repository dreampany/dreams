package com.dreampany.word.vm

import android.app.Application
import com.dreampany.frame.api.notify.NotifyManager
import com.dreampany.frame.data.enums.Language
import com.dreampany.frame.data.model.State
import com.dreampany.frame.data.source.repository.StateRepository
import com.dreampany.frame.misc.AppExecutors
import com.dreampany.frame.misc.ResponseMapper
import com.dreampany.frame.misc.RxMapper
import com.dreampany.frame.misc.exception.EmptyException
import com.dreampany.frame.util.DataUtil
import com.dreampany.frame.util.TextUtil
import com.dreampany.frame.util.TimeUtil
import com.dreampany.network.manager.NetworkManager
import com.dreampany.translation.data.source.repository.TranslationRepository
import com.dreampany.word.R
import com.dreampany.word.app.App
import com.dreampany.word.data.enums.ItemState
import com.dreampany.word.data.enums.ItemSubtype
import com.dreampany.word.data.enums.ItemType
import com.dreampany.word.data.misc.StateMapper
import com.dreampany.word.data.misc.WordMapper
import com.dreampany.word.data.model.Word
import com.dreampany.word.data.source.pref.Pref
import com.dreampany.word.data.source.repository.WordRepository
import com.dreampany.word.misc.Constants
import com.dreampany.word.ui.activity.NavigationActivity
import com.dreampany.word.ui.enums.UiSubtype
import com.dreampany.word.ui.enums.UiType
import com.dreampany.word.ui.model.UiTask
import com.dreampany.word.ui.model.WordItem
import io.reactivex.Maybe
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2019-07-09
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

@Singleton
class NotifyViewModel @Inject constructor(
    val application: Application,
    val rx: RxMapper,
    val ex: AppExecutors,
    val rm: ResponseMapper,
    val network: NetworkManager,
    val pref: Pref,
    val notify: NotifyManager,
    val stateMapper: StateMapper,
    val wordMapper: WordMapper,
    val stateRepo: StateRepository,
    val wordRepo: WordRepository,
    val translationRepo: TranslationRepository
) {

    fun notifyIf() {
        notifySync()
    }

    fun clearIf() {

    }

    private fun notifySync() {
        if (!TimeUtil.isExpired(pref.getLastWordSyncTime(), Constants.Delay.WordSyncTimeMS)) {
            return
        }
        pref.commitLastWordSyncTime()
        Timber.e("Fire Alert Notification")
        val disposable = rx
            .backToMain(getSyncWordItemRx())
            .subscribe({ this.postResult(it) }, { this.postFailed(it) })
    }

    private fun getSyncWordItemRx(): Maybe<WordItem> {
        return Maybe.create<WordItem> { emitter ->
            //find raw word item
            val state = getState(ItemType.WORD, ItemSubtype.DEFAULT, ItemState.RAW)
            if (state == null) {
                return@create
            }
            if (emitter.isDisposed) {
                return@create
            }
            val item = wordMapper.toItem(state, wordRepo)
            val source = Language.ENGLISH.code
            val target = pref.getLanguage(Language.ENGLISH).code
            val result = getItem(item, source, target, true);
            if (DataUtil.isEmpty(result)) {
                emitter.onError(EmptyException())
            } else {
                emitter.onSuccess(result)
            }
        }
    }

    private fun postResult(item: WordItem) {
        val app = application as App
        if (app.isVisible()) {
            //return;
        }

        val title = TextUtil.getString(app, R.string.notify_title_word_sync)
        var message: String? = if (!DataUtil.isEmpty(item.translation))
            app.getString(R.string.notify_word_translation_format, item.item.id, item.translation)
        else app.getString(R.string.notify_word_format, item.item.id, item.item.partOfSpeech)
        var targetClass: Class<*> = NavigationActivity::class.java

        val task = UiTask<Word>(false)
        task.setInput(item.item)
        task.setUiType(UiType.WORD)
        task.setSubtype(UiSubtype.VIEW)

        notify.showNotification(title, message!!, R.drawable.ic_notification, targetClass, task)
        app.throwAnalytics(
            Constants.Event.NOTIFICATION,
            Constants.notifyWordSync(application)
        )
    }

    private fun postFailed(error: Throwable) {
        Timber.v(error)
    }

    fun getState(type: ItemType, subtype: ItemSubtype, state: ItemState): State? {
        val state = stateRepo.getItem(type.name, subtype.name, state.name)
        return state
    }

    private fun getItem(word: Word, source: String, target: String, fully: Boolean): WordItem {
        val item = WordItem.getSimpleItem(word)
        item!!.setItem(word)
        adjustTranslate(item, source, target)
        return item
    }

    private fun adjustTranslate(item: WordItem, source: String, target: String) {
        if (source.equals(target)) {
            return
        }
        var translation: String? = null
        if (item.hasTranslation(target)) {
            translation = item.getTranslationBy(target)
        } else {
            val textTranslation =
                translationRepo.getItem(item.item.id, source, target)
            Timber.v("Translation %s - %s", item.item.id, translation)
            textTranslation?.let {
                item.addTranslation(target, it.output)
                translation = it.output
            }
        }
        item.translation = translation
    }
}