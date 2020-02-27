package com.dreampany.tools.ui.fragment.crypto

import android.os.Bundle
import com.dreampany.framework.injector.annote.ActivityScope
import com.dreampany.framework.ui.fragment.BaseFragment
import com.dreampany.framework.ui.fragment.BaseStateFragment
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.tools.R
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun pageClasses(): Array<Class<BaseFragment>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun pageTasks(): Array<UiTask<*>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}