package com.dreampany.tools.ui.vm

import android.app.Application
import com.dreampany.framework.api.notify.NotifyManager
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.enums.Subtype
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.data.misc.StoreMapper
import com.dreampany.framework.data.model.Store
import com.dreampany.framework.data.source.pref.ConfigPref
import com.dreampany.framework.data.source.repository.StoreRepository
import com.dreampany.framework.misc.AppExecutor
import com.dreampany.framework.misc.ResponseMapper
import com.dreampany.framework.misc.RxMapper
import com.dreampany.framework.ui.enums.UiType
import com.dreampany.framework.util.NumberUtil
import com.dreampany.network.data.model.Network
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.app.App
import com.dreampany.tools.data.mapper.WordMapper
import com.dreampany.tools.ui.request.WordRequest
import com.dreampany.tools.data.model.word.Word
import com.dreampany.tools.data.source.pref.Pref
import com.dreampany.tools.data.source.pref.WordPref
import com.dreampany.tools.data.source.repository.WordRepository
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.model.word.WordItem
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
    private val ex: AppExecutor,
    private val rm: ResponseMapper,
    private val network: NetworkManager,
    private val configPref: ConfigPref,
    private val pref: Pref,
    private val wordPref: WordPref,
    private val notify: NotifyManager,
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepository,
    private val mapper: WordMapper,
    private val repo: WordRepository,
    private val translationRepo: TranslationRepository
) : NetworkManager.Callback {

    private val disposables: CompositeDisposable

    init {
        disposables = CompositeDisposable()
        network.observe(this, checkInternet = true)
    }

    override fun onNetworks(networks: List<Network>) {
        Timber.v(networks.toString())
    }

    fun clear() {
        //network.deObserve(this)
    }

    fun request(request: WordRequest) {
        if (application is App) {
            if (!application.isVisible()) {
                Timber.v("NotifyViewModel Periodic Request denied [App is Hidden]")
                return
            }
        }
        Timber.v("NotifyViewModel Periodic Request accepted [App is visible]")
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
                    if (NumberUtil.randomBool()) {
                        syncOfTrack(request)
                    } else {
                        syncWord(request)
                    }
                })
            }
        }
    }

    private fun syncWord(request: WordRequest) {
        Timber.v("SyncWord fired")
        val screen = configPref.getScreen(UiType.FRAGMENT)
        val has = Constants.hasThreshold(application, screen, request.type)
        if (!mapper.isSyncExpired(has)) {
            return
        }
        val threshold = Constants.getThreshold(application, screen, request.type)
        for (index in 1..threshold) {
            Timber.v("Getting... FAW Store")
            val store = nextStore(request.type, request.subtype, State.RAW, exclude = State.TRACK)
            store?.run {
                Timber.v("RAW Next sync word %s", id)
                if (hasStore(this.id, this.type, this.subtype, State.ERROR, State.FULL)) {
                    removeStore(this.id, this.type, this.subtype, State.RAW)
                    removeStore(this.id, this.type, this.subtype, State.TRACK)
                    return@run
                }
                Timber.v("RAW Next sync word %s", id)
                syncStore(request, this)
            }
        }
        val fullState = storeRepo.getCountByType(request.type, request.subtype, State.FULL)
        wordPref.setSyncedCount(fullState.toLong())
        if (network.hasInternet()) {
            mapper.commitSyncExpiredTime()
        }
    }

    private fun syncOfTrack(request: WordRequest) {
        val screen = configPref.getScreen(UiType.FRAGMENT)
        val has = Constants.hasThreshold(application, screen, request.type)
        if (!mapper.isTrackExpired(has)) {
            return
        }
        val threshold = Constants.getThreshold(application, screen, request.type)
        Timber.v("Getting... Store")
        for (index in 1..threshold) {
            Timber.v("Getting... TRACK Store")
            var store = nextStore(request.type, request.subtype, State.TRACK)
            store?.run {
                Timber.v("TRACK Next sync word %s", id)
                if (hasStore(this.id, this.type, this.subtype, State.ERROR, State.FULL)) {
                    removeStore(this.id, this.type, this.subtype, State.RAW)
                    removeStore(this.id, this.type, this.subtype, State.TRACK)
                    return@run
                }

                // TODO required batch read
                val result = syncStore(request, this)
            }
        }

        val fullState = storeRepo.getCountByType(request.type, request.subtype, State.FULL)
        wordPref.setSyncedCount(fullState.toLong())
        if (network.hasInternet()) {
            mapper.commitTrackExpiredTime()
        }

        /*        var trackCount = 0
        var trackPer = if (fullState < Constants.Count.Word.TRACK)
            Constants.Count.Word.PER_TRACK else Constants.Count.Word.DEFAULT_PER_TRACK
        do {
            Timber.v("Getting... TRACK Store")
            var store = nextStore(request.type, request.subtype, State.TRACK)
            store?.run {
                Timber.v("TRACK Next sync word %s", id)
                if (hasStore(this.id, this.type, this.subtype, State.ERROR, State.FULL)) {
                    removeStore(this.id, this.type, this.subtype, State.RAW)
                    removeStore(this.id, this.type, this.subtype, State.TRACK)
                    return@run
                }

                // TODO required batch read
                val result = syncStore(request, this)
                if (result) {
                    trackCount++
                }
            }
            if ((store == null && network.hasInternet()) || trackCount == trackPer) {
                mapper.commitTrackExpiredTime()
            }
            AndroidUtil.sleep(500L)
        } while (store != null && trackCount < trackPer)*/
    }

    private fun nextStore(
        type: Type,
        subtype: Subtype,
        target: State,
        exclude: State? = null
    ): Store? {
        var store: Store? = null
        do {
            store = if (exclude == null) storeRepo.getRandomItem(type, subtype, target)
            else storeRepo.getRandomItem(type, subtype, target, exclude)

            if (store == null) break
            /*if (storeRepo.isExists(store.id, Type.WORD, Subtype.DEFAULT, fallback)) {
                storeRepo.delete(store)
                store = null
            }*/
        } while (store == null)
        return store
    }

    private fun syncStore(request: WordRequest, store: Store): Boolean {
        Timber.v("Sync Word/.. %s", this.toString())
        try {
            var item = mapper.getItem(store, repo)
            item?.run {
                if (request.history) {
                    if (!isEmpty()) {
                        //wordPref.setRecentWord(this)
                        //putStore(id, request.type, request.subtype, State.HISTORY)
                    }
                }
                val uiItem = getUiItem(request, this)
                return true
            }
        } catch (error: Throwable) {
            Timber.e(error)
        }
        return false
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
            if (item.hasTranslation(request.targetLang)) {
                translation = item.getTranslationBy(request.targetLang)
            } else {
                val textTranslation =
                    translationRepo.getItem(
                        request.sourceLang!!,
                        request.targetLang!!,
                        item.item.id
                    )
                textTranslation?.let {
                    Timber.v("Translation %s - %s", item.item.id, it.output)
                    item.addTranslation(request.targetLang!!, it.output)
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

    private fun hasStore(id: String, type: Type, subtype: Subtype, vararg states: State): Boolean {
        val result = arrayOf<State>()
        states?.forEach {
            result.plusElement(it)
        }
        return storeRepo.isExists(id, type, subtype, result)
    }

    private fun removeStore(id: String, type: Type, subtype: Subtype, state: State): Int {
        val store = storeMapper.getItem(id, type, subtype, state)
        val result = storeRepo.delete(store)
        return result
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