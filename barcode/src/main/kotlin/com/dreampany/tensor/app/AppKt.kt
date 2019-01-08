package com.dreampany.tensor.app

import com.dreampany.frame.app.BaseAppKt
import com.dreampany.tensor.BuildConfig
import com.dreampany.tensor.injector.DaggerAppComponentKt
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication


/**
 * Created by Hawladar Roman on 5/22/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
class AppKt : BaseAppKt() {

    override fun isDebug(): Boolean {
        return BuildConfig.DEBUG;
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponentKt.builder().application(this).build()
    }
}