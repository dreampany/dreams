package com.dreampany.word.vm

import android.app.Application
import com.dreampany.frame.api.notify.NotifyManager
import com.dreampany.language.Language
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
        Timber.v("Fire Notification")
        val disposable = rx
            .backToMain(getSyncWordItemRx())
            .subscribe({ this.postResult(it) }, { this.postFailed(it) })
    }

    private fun getSyncWordItemRx(): Maybe<WordItem> {
        return Maybe.create { emitter ->
            //find raw word item
            //val stateCount = stateRepo.getCount(ItemType.WORD.name, ItemSubtype.DEFAULT.name, ItemState.RAW.name)
            val state = getState(ItemType.WORD, ItemSubtype.DEFAULT, ItemState.RAW)
            if (state == null) {
                return@create
            }
            if (emitter.isDisposed) {
                return@create
            }
            Timber.v("Statue %s", state.toString())
            var item = wordMapper.toItem(state, wordRepo)
            item = getItemIf(item)
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

        val task = UiTask<Word>(false, UiType.WORD, UiSubtype.VIEW)
        task.input = item.item

        notify.showNotification(title!!, message!!, R.drawable.ic_notification, targetClass, task)
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

    fun hasState(word: Word, subtype: ItemSubtype, state: ItemState): Boolean {
        return stateRepo.getCountById(word.id, ItemType.WORD.name, subtype.name, state.name) > 0
    }

    private fun getItem(word: Word, source: String, target: String, fully: Boolean): WordItem {
        val item = WordItem.getItem(word)
        item.item = word
        adjustTranslate(item, source, target)
        return item
    }

    fun putState(word: Word, subtype: ItemSubtype, state: ItemState): Long {
        val s = State(word.id, ItemType.WORD.name, subtype.name, state.name)
        s.time = TimeUtil.currentTime()
        return stateRepo.putItem(s)
    }

    fun putItem(word: Word, subtype: ItemSubtype, state: ItemState): Long {
        var result = wordRepo.putItem(word)
        if (result != -1L) {
            result = putState(word, subtype, state)
        }
        return result
    }

    fun getItemIf(word: Word): Word? {
        var result = getRoomItemIf(word)
        if (result == null) {
            result = getFirestoreItemIf(word)
        }
        if (result == null) {
            result = getRemoteItemIf(word)
        }
        return result
    }

    private fun getRoomItemIf(word: Word): Word? {
        return if (!hasState(word, ItemSubtype.DEFAULT, ItemState.FULL)) {
            null
        } else wordRepo.getRoomItem(word.id, true)
    }

    private fun getFirestoreItemIf(word: Word): Word? {
        val result = wordRepo.getFirestoreItem(word.id, true)
        if (result != null) {
            Timber.v("Firestore result success")
            this.putItem(result, ItemSubtype.DEFAULT, ItemState.FULL)
        }
        return result
    }

    private fun getRemoteItemIf(word: Word): Word? {
        val result = wordRepo.getRemoteItem(word.id, true)
        if (result != null) {
            this.putItem(result, ItemSubtype.DEFAULT, ItemState.FULL)
            wordRepo.putFirestoreItem(result)
        }
        return result
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