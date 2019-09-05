package com.dreampany.framework.data.source.pref

import android.content.Context
import com.dreampany.framework.data.enums.Rank
import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.enums.Subtype
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.misc.Constants

/**
 * Created by roman on 2019-07-19
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class FramePref(context: Context) : BasePref(context) {

    fun has(type: Type, subtype: Subtype, state: State): Boolean {
        return getPrivately(type.name + subtype.name + state.name, Constants.Default.BOOLEAN)
    }

    fun set(type: Type, subtype: Subtype, state: State, value: Boolean) {
        setPrivately(type.name + subtype.name + state.name, value)
    }

    fun setVersionCode(versionCode: Int) {
        setPrivately(Constants.Pref.VERSION_CODE, versionCode)
    }

    fun getVersionCode(): Int {
        return getPrivately(Constants.Pref.VERSION_CODE, Constants.Default.INT)
    }

    fun setRank(rank: Rank) {
        setPrivately(Constants.Pref.RANK, rank)
    }

    fun getRank(rank: Rank): Rank {
        return getPrivately(Constants.Pref.RANK, Rank::class.java, rank)
    }
}