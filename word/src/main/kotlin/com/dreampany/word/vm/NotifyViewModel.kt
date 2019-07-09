package com.dreampany.word.vm

import android.app.Application
import com.dreampany.frame.data.model.State
import com.dreampany.frame.data.source.repository.StateRepository
import com.dreampany.frame.misc.AppExecutors
import com.dreampany.frame.misc.ResponseMapper
import com.dreampany.frame.misc.RxMapper
import com.dreampany.frame.util.TimeUtil
import com.dreampany.network.manager.NetworkManager
import com.dreampany.translation.data.source.repository.TranslationRepository
import com.dreampany.word.data.enums.ItemState
import com.dreampany.word.data.enums.ItemSubtype
import com.dreampany.word.data.enums.ItemType
import com.dreampany.word.data.misc.StateMapper
import com.dreampany.word.data.source.pref.Pref
import com.dreampany.word.data.source.repository.WordRepository
import com.dreampany.word.misc.Constants
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
    val stateMapper: StateMapper,
    val stateRepo: StateRepository,
    val worRepo: WordRepository,
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
            /*if (DataUtil.isEmpty(result)) {
                emitter.onError(EmptyException())
            } else {
                emitter.onSuccess(result)
            }*/
        }
    }

    private fun postResult(item: WordItem) {

    }

    private fun postFailed(error: Throwable) {

    }

    fun getState(type: ItemType, subtype: ItemSubtype, state: ItemState): State? {
        val state = stateRepo.getItem(type.name, subtype.name, state.name)
        return state
    }
}