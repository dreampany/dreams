package com.dreampany.tools.data.source.crypto.pref

import android.content.Context
import com.dreampany.common.data.source.pref.BasePref
import com.dreampany.tools.misc.constant.AppConstants
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class CryptoPref
@Inject constructor(
    context: Context
) : BasePref(context) {

    override fun getPrivateName(context: Context): String {
        return AppConstants.Keys.PrefKeys.CRYPTO
    }
}