package com.dreampany.tools.data.source.pref

import android.content.Context
import com.dreampany.framework.data.source.pref.BasePref
import com.dreampany.tools.misc.constants.Constants
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 16/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class Pref
@Inject constructor(
    context: Context
) : BasePref(context) {

    override fun getPrivateName(context: Context): String = Constants.Keys.Pref.PREF


}