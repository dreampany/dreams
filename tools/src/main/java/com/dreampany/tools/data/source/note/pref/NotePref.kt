package com.dreampany.tools.data.source.note.pref

import android.content.Context
import com.dreampany.framework.data.source.pref.BasePref
import com.dreampany.tools.misc.constant.NoteConstants
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class NotePref
@Inject constructor(
    context: Context
) : BasePref(context) {

    override fun getPrivateName(context: Context): String {
        return NoteConstants.Keys.PrefKeys.NOTE
    }


}