package com.dreampany.tools.vm

import android.app.Application
import com.dreampany.frame.data.enums.State
import com.dreampany.frame.data.enums.Subtype
import com.dreampany.frame.data.enums.Type
import com.dreampany.frame.data.misc.StoreMapper
import com.dreampany.frame.data.model.Response
import com.dreampany.frame.data.model.Store
import com.dreampany.frame.data.source.repository.StoreRepository
import com.dreampany.frame.misc.*
import com.dreampany.frame.ui.model.UiTask
import com.dreampany.frame.util.AndroidUtil
import com.dreampany.frame.util.DataUtil
import com.dreampany.frame.util.DataUtilKt
import com.dreampany.frame.util.TimeUtil
import com.dreampany.frame.vm.BaseViewModel
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.data.misc.LoadRequest
import com.dreampany.tools.data.misc.WordMapper
import com.dreampany.tools.data.model.Load
import com.dreampany.tools.data.model.Word
import com.dreampany.tools.data.source.pref.Pref
import com.dreampany.tools.data.source.pref.WordPref
import com.dreampany.tools.data.source.repository.WordRepository
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.model.LoadItem
import kotlinx.coroutines.Runnable
import timber.log.Timber
import java.util.ArrayList
import javax.inject.Inject

/**
 * Created by Roman-372 on 8/19/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class LoaderViewModel
@Inject constructor(
    application: Application,
    rx: RxMapper,
    ex: AppExecutors,
    rm: ResponseMapper,
    private val network: NetworkManager,
    private val pref: Pref,
    private val wordPref: WordPref,
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepository,
    private val mapper: WordMapper,
    private val repo: WordRepository,
    @Favorite private val favorites: SmartMap<String, Boolean>
) : BaseViewModel<Load, LoadItem, UiTask<Load>>(application, rx, ex, rm) {

    private val commonWords = mutableListOf<Word>()
    private val alphaWords = mutableListOf<Word>()
    private var commonLoading = false
    private var alphaLoading = false

    fun request(request: LoadRequest) {
        if (!wordPref.isCommonLoaded() && !commonLoading) {
            ex.postToIO(Runnable {
                commonLoading = true
                loadCommons(request)
                commonLoading = false
                request(request)
            })
            return
        }
        if (!wordPref.isAlphaLoaded() && !alphaLoading) {
            ex.postToIO(Runnable {
                alphaLoading = true
                loadAlphas(request)
                alphaLoading = false
            })
        }
    }


    fun loadCommons(request: LoadRequest) {
        buildCommonWords()

        var current = storeRepo.getCountByType(Type.WORD, Subtype.DEFAULT, State.RAW)
        val load = Load(current = current, total = current)
        val item = LoadItem.getItem(load)
        ex.postToUi(Runnable { postResult(request.action, item) })

        val last = wordPref.getLastWord()
        val lastIndex = if (last != null) commonWords.indexOf(last) else -1

        if (lastIndex > 0) {
            DataUtil.removeFirst(commonWords, lastIndex + 1)
        }

        while (!commonWords.isEmpty()) {
            val words = DataUtilKt.takeFirst(commonWords, Constants.Count.WORD_PAGE)
            if (words.isNullOrEmpty()) {
                continue
            }
            var resultOf = repo.putItems(words)
            if (DataUtil.isEqual(words, resultOf)) {
                val states = ArrayList<Store>()
                words.forEach { word ->
                    val s = Store(word.id, Type.WORD, Subtype.DEFAULT, State.RAW)
                    states.add(s)
                }
                resultOf = storeRepo.putItems(states)
            }

            if (DataUtil.isEqual(words, resultOf)) {
                val lastWord = DataUtil.pullLast(words)
                wordPref.setLastWord(lastWord)
                current = storeRepo.getCountByType(Type.WORD, Subtype.DEFAULT, State.RAW)
                load.current = current
                load.total = current

                Timber.v("%d Last Common Word = %s", current, lastWord!!.toString())
                ex.postToUi(Runnable { postResult(request.action, item) })
                AndroidUtil.sleep(100)
            }
        }
        if (commonWords.isEmpty()) {
            wordPref.commitCommonLoaded()
            wordPref.clearLastWord()
        }
    }

    fun loadAlphas(request: LoadRequest) {
        buildAlphaWords()
        var current = storeRepo.getCountByType(Type.WORD, Subtype.DEFAULT, State.RAW)
        val load = Load(current = current, total = current)
        val item = LoadItem.getItem(load)
        ex.postToUi(Runnable { postResult(request.action, item) })

        val last = wordPref.getLastWord()
        val lastIndex = if (last != null) alphaWords.indexOf(last) else -1

        if (lastIndex > 0) {
            DataUtil.removeFirst(alphaWords, lastIndex + 1)
        }

        while (!alphaWords.isEmpty()) {
            val words = DataUtilKt.takeFirst(alphaWords, Constants.Count.WORD_PAGE)
            if (words.isNullOrEmpty()) {
                continue
            }
            var resultOf = repo.putItems(words)
            if (DataUtil.isEqual(words, resultOf)) {
                val states = ArrayList<Store>()
                words.forEach { word ->
                    val s = Store(word.id, Type.WORD, Subtype.DEFAULT, State.RAW)
                    states.add(s)
                }
                resultOf = storeRepo.putItems(states)
            }

            if (DataUtil.isEqual(words, resultOf)) {
                val lastWord = DataUtil.pullLast(words)
                wordPref.setLastWord(lastWord)
                current = storeRepo.getCountByType(Type.WORD, Subtype.DEFAULT, State.RAW)
                load.current = current
                load.total = current

                Timber.v("%d Last Alpha Word = %s", current, lastWord!!.toString())
                ex.postToUi(Runnable { postResult(request.action, item) })
                AndroidUtil.sleep(100)
            }
        }
        if (alphaWords.isEmpty()) {
            wordPref.commitAlphaLoaded()
            wordPref.clearLastWord()
        }
    }

    private fun buildCommonWords() {
        if (commonWords.size != Constants.Count.WORD_COMMON) {
            val words = repo.getCommonItems()
            commonWords.clear()
            commonWords.addAll(words!!)
        }
    }

    private fun buildAlphaWords() {
        if (alphaWords.size != Constants.Count.WORD_ALPHA) {
            val words = repo.getAlphaItems()
            alphaWords.clear()
            alphaWords.addAll(words!!)
        }
    }

}
