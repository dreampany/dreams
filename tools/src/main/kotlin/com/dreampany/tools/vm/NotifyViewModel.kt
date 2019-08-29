package com.dreampany.tools.vm

import android.app.Application
import com.dreampany.frame.api.notify.NotifyManager
import com.dreampany.frame.data.enums.Action
import com.dreampany.frame.data.enums.State
import com.dreampany.frame.data.enums.Subtype
import com.dreampany.frame.data.enums.Type
import com.dreampany.frame.data.misc.StoreMapper
import com.dreampany.frame.data.source.repository.StoreRepository
import com.dreampany.frame.misc.AppExecutors
import com.dreampany.frame.misc.ResponseMapper
import com.dreampany.frame.misc.RxMapper
import com.dreampany.frame.ui.model.UiTask
import com.dreampany.frame.util.DataUtil
import com.dreampany.frame.util.TextUtil
import com.dreampany.frame.util.TimeUtil
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.R
import com.dreampany.tools.app.App
import com.dreampany.tools.data.misc.WordMapper
import com.dreampany.tools.data.misc.WordRequest
import com.dreampany.tools.data.model.Word
import com.dreampany.tools.data.source.pref.Pref
import com.dreampany.tools.data.source.pref.WordPref
import com.dreampany.tools.data.source.repository.WordRepository
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.activity.NavigationActivity
import com.dreampany.tools.ui.model.WordItem
import com.dreampany.translation.data.source.repository.TranslationRepository
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Runnable
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2019-08-03
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class NotifyViewModel
@Inject constructor(
    private val application: Application,
    private val rx: RxMapper,
    private val ex: AppExecutors,
    private val rm: ResponseMapper,
    private val network: NetworkManager,
    private val pref: Pref,
    private val wordPref: WordPref,
    private val notify: NotifyManager,
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepository,
    private val mapper: WordMapper,
    private val repo: WordRepository,
    private val translationRepo: TranslationRepository
) {

    private val disposables: CompositeDisposable

    init {
        disposables = CompositeDisposable()
    }

    fun request(request: WordRequest) {
        when (request.type) {
            Type.WORD -> {
                requestWord(request)
            }
        }
    }

    private fun requestWord(request: WordRequest) {
        when (request.action) {
            Action.SYNC -> {
                ex.postToNetwork(Runnable {
                    syncWord(request)
                })
            }
        }
    }

    private fun syncWord(request: WordRequest) {
        if (!TimeUtil.isExpired(wordPref.getLastWordSyncTime(), Constants.Delay.WordSyncTimeMS)) {
            return
        }
        val rawStore = storeRepo.getItem(Type.WORD, Subtype.DEFAULT, State.RAW)
        rawStore?.run {
            Timber.v("Sync Word/.. %s", this.toString())
            try {
                var item = mapper.getItem(this, repo)
                item?.run {
                    if (request.history) {
                        wordPref.setRecentWord(item)
                        putStore(item.id, Type.WORD, Subtype.DEFAULT, State.HISTORY)
                    }
                    val uiItem = getUiItem(request, this)
                    wordPref.commitLastWordSyncTime()
                }
            } catch (error : Throwable) {

            }
        }
    }

    private fun getUiItem(request: WordRequest, item: Word): WordItem {
        var uiItem: WordItem? = mapper.getUiItem(item.id)
        if (uiItem == null) {
            uiItem = WordItem.getItem(item)
        }
        uiItem.item = item
        adjustTranslate(request, uiItem)
        return uiItem
    }

    private fun adjustTranslate(request: WordRequest, item: WordItem) {
        var translation: String? = null
        if (request.translate) {
            if (item.hasTranslation(request.target)) {
                translation = item.getTranslationBy(request.target)
            } else {
                val textTranslation =
                    translationRepo.getItem(request.source!!, request.target!!, item.item.id)
                textTranslation?.let {
                    Timber.v("Translation %s - %s", request.id, it.output)
                    item.addTranslation(request.target!!, it.output)
                    translation = it.output
                }
            }
        }
        item.translation = translation
    }

    private fun putStore(id: String, type: Type, subtype: Subtype, state: State): Long {
        val store = storeMapper.getItem(id, type, subtype, state)
        return storeRepo.putItem(store)
    }

    private fun notify(item: WordItem) {
        val app = application as App
        if (app.isVisible()) {
            //return;
        }

/*        val title = TextUtil.getString(app, R.string.notify_title_word_sync)
        var message: String? = if (!DataUtil.isEmpty(item.translation))
            app.getString(R.string.notify_word_translation_format, item.item.id, item.translation)
        else app.getString(R.string.notify_word_format, item.item.id, item.item.getPartOfSpeech())
        var targetClass: Class<*> = NavigationActivity::class.java

        val task = UiTask<Word>(
            type = Type.WORD, UiSubtype.VIEW, item.item, null)

        notify.showNotification(title!!, message!!, R.drawable.ic_notification, targetClass, task)
        app.throwAnalytics(
            Constants.Event.NOTIFICATION,
            Constants.notifyWordSync(application)
        )*/
    }


/*    fun clearIf() {
       // disposables.clear()
    }

    private fun postResult(result: List<DemoItem>) {
        val app = application as App
        if (app.isVisible()) {
            //return;
        }
    }

    private fun postFailed(error: Throwable) {
        val app = application as App
        Timber.v(error)
        //app.throwAnalytics(Constants.Event.ERROR, Constants.notifyHistory(application), error)
    }*/
}