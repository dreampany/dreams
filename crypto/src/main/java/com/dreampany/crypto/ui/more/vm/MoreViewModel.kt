package com.dreampany.crypto.ui.more.vm

import android.app.Application
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.ui.vm.BaseViewModel
import com.dreampany.theme.Colors
import com.dreampany.crypto.R
import com.dreampany.crypto.data.enums.Action
import com.dreampany.crypto.data.enums.State
import com.dreampany.crypto.data.enums.Subtype
import com.dreampany.crypto.data.enums.Type
import com.dreampany.crypto.data.model.more.More
import com.dreampany.crypto.ui.more.model.MoreItem
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
class MoreViewModel
@Inject constructor(
    application: Application,
    rm: ResponseMapper,
    private val colors: Colors
) : BaseViewModel<Type, Subtype, State, Action, More, MoreItem, UiTask<Type, Subtype, State, Action, More>>(
    application,
    rm
) {

    fun loadMores() {
        uiScope.launch {
            //postProgress(true)
            val result = getMores()
            postResult(result.toItems())
        }
    }

    private suspend fun getMores() = withContext(Dispatchers.IO) {
        val result = arrayListOf<More>()
        /*result.add(
            More(
                Type.MORE, Subtype.SETTINGS, R.drawable.ic_crypto, R.string.title_feature_crypto,
                colors.nextColor(Type.MORE.name)
            )
        )*/
        result.add(
            More(
                Type.MORE, Subtype.APPS, R.drawable.ic_apps_black_24dp, R.string.more_apps,
                colors.nextColor(Type.MORE.name)
            )
        )
        result.add(
            More(
                Type.MORE, Subtype.RATE_US, R.drawable.ic_rate_review_black_24dp, R.string.rate_us,
                colors.nextColor(Type.MORE.name)
            )
        )
        result.add(
            More(
                Type.MORE, Subtype.FEEDBACK, R.drawable.ic_feedback_black_24dp, R.string.title_feedback,
                colors.nextColor(Type.MORE.name)
            )
        )
        /*result.add(
            More(
                Type.MORE, Subtype.LICENSE, R.drawable.ic_security_black_24dp, R.string.license,
                colors.nextColor(Type.MORE.name)
            )
        )*/
        /*result.add(
            More(
                Type.MORE, Subtype.ABOUT, R.drawable.ic_info_black_24dp, R.string.about,
                colors.nextColor(Type.MORE.name)
            )
        )*/
        result
    }

    private suspend fun List<More>.toItems(): List<MoreItem> {
        val list = this
        return withContext(Dispatchers.IO) {
            list.map { MoreItem(it) }
        }
    }

    private fun postProgress(progress: Boolean) {
        postProgressMultiple(
            Type.MORE,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            progress = progress
        )
    }


    private fun postError(error: SmartError) {
        postMultiple(
            Type.MORE,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            error = error,
            showProgress = false
        )
    }

    private fun postResult(result: List<MoreItem>) {
        postMultiple(
            Type.MORE,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            result = result,
            showProgress = false
        )
    }

}