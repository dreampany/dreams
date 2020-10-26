package com.dreampany.tools.data.source.misc.pref

import android.content.Context
import com.dreampany.framework.data.source.pref.Pref
import com.dreampany.tools.misc.constants.Constants
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 26/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class Prefs
@Inject constructor(
    context: Context,
    private val gson: Gson
) : Pref(context) {

    override fun getPrivateName(context: Context): String = Constants.Keys.Pref.MISC

}