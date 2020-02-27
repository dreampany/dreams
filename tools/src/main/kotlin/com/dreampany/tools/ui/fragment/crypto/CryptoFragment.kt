package com.dreampany.tools.ui.fragment.crypto

import android.os.Bundle
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.injector.annote.ActivityScope
import com.dreampany.framework.ui.fragment.BaseFragment
import com.dreampany.framework.ui.fragment.BaseStateFragment
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.util.TextUtil
import com.dreampany.tools.R
import com.dreampany.tools.data.model.Coin
import com.dreampany.tools.data.model.Station
import java.lang.IllegalStateException
import javax.inject.Inject

/**
 * Created by roman on 2/25/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class CryptoFragment
@Inject constructor() : BaseStateFragment<BaseFragment>() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_tabpager_fixed
    }

    override fun onStartUi(state: Bundle?) {

    }

    override fun onStopUi() {

    }

    override fun pageTitles(): Array<String> {
        return TextUtil.getStrings(context, R.string.coin_info)
    }

    override fun pageClasses(): Array<Class<BaseFragment>> {
        val info: Class<BaseFragment> = CryptoInfoFragment::class.java as Class<BaseFragment>
        return arrayOf<Class<BaseFragment>>(info)
    }

    override fun pageTasks(): Array<UiTask<*>> {
        val task = getCurrentTask<UiTask<Coin>>() ?: throw IllegalStateException()
        val info = UiTask<Coin>(
            type = Type.CRYPTO,
            action = Action.OPEN,
            state = State.INFO,
            input = task.input
        )
        return arrayOf<UiTask<*>>(info)
    }
}