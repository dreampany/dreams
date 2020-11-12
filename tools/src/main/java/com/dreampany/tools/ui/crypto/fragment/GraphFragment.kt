package com.dreampany.tools.ui.crypto.fragment

import android.os.Bundle
import com.dreampany.framework.inject.annote.ActivityScope
import com.dreampany.framework.misc.exts.task
import com.dreampany.framework.ui.fragment.InjectFragment
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.Action
import com.dreampany.tools.data.enums.State
import com.dreampany.tools.data.enums.Subtype
import com.dreampany.tools.data.enums.Type
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.data.source.crypto.pref.Prefs
import com.dreampany.tools.databinding.CryptoGraphFragmentBinding
import com.dreampany.tools.ui.crypto.vm.CoinViewModel
import javax.inject.Inject

/**
 * Created by roman on 27/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class GraphFragment
@Inject constructor() : InjectFragment() {

    @Inject
    internal lateinit var pref: Prefs

    private lateinit var bind: CryptoGraphFragmentBinding
    private lateinit var vm: CoinViewModel
    private lateinit var input: Coin

    override val layoutRes: Int = R.layout.crypto_graph_fragment

    override fun onStartUi(state: Bundle?) {
        val task = (task ?: return) as UiTask<Type, Subtype, State, Action, Coin>
        input = task.input ?: return

    }

    override fun onStopUi() {
     }

}