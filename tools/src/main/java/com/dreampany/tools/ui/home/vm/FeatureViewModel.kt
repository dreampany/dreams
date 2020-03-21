package com.dreampany.tools.ui.home.vm

import android.app.Application
import com.dreampany.common.theme.Colors
import com.dreampany.common.misc.func.ResponseMapper
import com.dreampany.common.ui.model.UiTask
import com.dreampany.common.ui.vm.BaseViewModel
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.Subtype
import com.dreampany.tools.data.enums.Type
import com.dreampany.tools.ui.model.FeatureItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
) : BaseViewModel<FeatureItem, FeatureItem, UiTask<FeatureItem, Type, Subtype>, Type, Subtype>(
    application,
    rm
) {


    fun loadFeatures() {
        uiScope.launch {
            postProgressMultiple(Type.FEATURE, Subtype.DEFAULT, progress = true)
            val result = getFeatures()
            postMultiple(Type.FEATURE, Subtype.DEFAULT, result = result, showProgress = true)
        }

    }

   private suspend fun getFeatures() = withContext(Dispatchers.IO) {
        val features = arrayListOf<FeatureItem>()
        features.add(FeatureItem(Type.FEATURE, Subtype.CRYPTO, R.string.title_feature_crypto, colors.nextColor(Type.FEATURE.name)))
        features.add(FeatureItem(Type.FEATURE, Subtype.QUESTION, R.string.title_feature_question, colors.nextColor(Type.FEATURE.name)))
        features.add(FeatureItem(Type.FEATURE, Subtype.RADIO, R.string.title_feature_radio, colors.nextColor(Type.FEATURE.name)))
        features
    }

}