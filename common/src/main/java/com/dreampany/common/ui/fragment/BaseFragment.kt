package com.dreampany.common.ui.fragment

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import androidx.annotation.StringRes
import androidx.annotation.XmlRes
import androidx.preference.PreferenceFragmentCompat
import com.dreampany.common.misc.func.Executors
import javax.inject.Inject

/**
 * Created by roman on 7/11/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
abstract class BaseFragment : PreferenceFragmentCompat() {

    @Inject
    protected lateinit var ex: Executors

    @get:LayoutRes
    open val layoutRes: Int = 0

    @get:XmlRes
    open val prefLayoutRes: Int = 0

    @get:MenuRes
    open val menuRes: Int = 0

    @get:StringRes
    open val titleRes: Int = 0

    @get:StringRes
    open val subtitleRes: Int = 0

    protected abstract fun onStartUi(state: Bundle?)

    protected abstract fun onStopUi()
}