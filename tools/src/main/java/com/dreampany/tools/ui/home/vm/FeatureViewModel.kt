package com.dreampany.tools.ui.home.vm

import android.app.Application
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.ui.vm.BaseViewModel
import com.dreampany.theme.Colors
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.home.Action
import com.dreampany.tools.data.enums.home.State
import com.dreampany.tools.data.enums.home.Subtype
import com.dreampany.tools.data.enums.home.Type
import com.dreampany.tools.data.model.home.Feature
import com.dreampany.tools.ui.home.model.FeatureItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class FeatureViewModel
@Inject constructor(
    application: Application,
    rm: ResponseMapper,
    private val colors: Colors
) : BaseViewModel<Type, Subtype, State, Action, Feature, FeatureItem, UiTask<Type, Subtype, State, Action, Feature>>(
    application,
    rm
) {

    fun loadFeatures() {
        Timber.v("loadFeatures called")
        uiScope.launch {
            //postProgress(true)
            val result = getFeatures()
            postResult(result.toItems())
        }
    }

    private suspend fun getFeatures() = withContext(Dispatchers.IO) {
        val features = arrayListOf<Feature>()
        features.add(
            Feature(
                Type.FEATURE,
                Subtype.WIFI,
                R.drawable.ic_signal_wifi_4_bar,
                R.string.title_feature_wifi,
                colors.nextColor(Type.FEATURE.name)
            )
        )
        features.add(
            Feature(
                Type.FEATURE,
                Subtype.CRYPTO,
                R.drawable.ic_crypto,
                R.string.title_feature_crypto,
                colors.nextColor(Type.FEATURE.name)
            )
        )
        /*features.add(
            Feature(
                Type.FEATURE,
                Subtype.QUESTION,
                R.drawable.ic_crypto,
                R.string.title_feature_question,
                colors.nextColor(Type.FEATURE.name)
            )
        )*/
        features.add(
            Feature(
                Type.FEATURE,
                Subtype.RADIO,
                R.drawable.ic_radio_black_24dp,
                R.string.title_feature_radio,
                colors.nextColor(Type.FEATURE.name)
            )
        )
        features.add(
            Feature(
                Type.FEATURE,
                Subtype.NOTE,
                R.drawable.ic_event_note_black_24dp,
                R.string.title_feature_note,
                colors.nextColor(Type.FEATURE.name)
            )
        )
        features.add(
            Feature(
                Type.FEATURE,
                Subtype.HISTORY,
                R.drawable.ic_history_black_24dp,
                R.string.title_feature_history,
                colors.nextColor(Type.FEATURE.name)
            )
        )
        features
    }

    private suspend fun List<Feature>.toItems(): List<FeatureItem> {
        val list = this
        return withContext(Dispatchers.IO) {
            list.map { FeatureItem(it) }
        }
    }

    private fun postProgress(progress: Boolean) {
        postProgressMultiple(
            Type.FEATURE,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            progress = progress
        )
    }


    private fun postError(error: SmartError) {
        postMultiple(
            Type.FEATURE,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            error = error,
            showProgress = false
        )
    }

    private fun postResult(result: List<FeatureItem>) {
        postMultiple(
            Type.FEATURE,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            result = result,
            showProgress = false
        )
    }

}