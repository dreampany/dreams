package com.dreampany.tools.ui.crypto.activity

import android.os.Bundle
import com.dreampany.common.ui.activity.InjectActivity
import com.dreampany.tools.R

/**
 * Created by roman on 26/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class CoinActivity : InjectActivity() {


    override fun hasBinding(): Boolean = true

    override fun homeUp(): Boolean = true

    override fun layoutRes(): Int = R.layout.coin_activity

    override fun toolbarId(): Int = R.id.toolbar
    
    override fun onStartUi(state: Bundle?) {

    }

    override fun onStopUi() {
     }
}