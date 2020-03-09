package com.dreampany.tools.data.mapper

import android.content.Context
import com.dreampany.framework.data.misc.Mapper
import com.dreampany.tools.data.source.pref.LockPref
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

/**
 * Created by roman on 3/9/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class LockMapper
@Inject constructor(
    private val context: Context,
    private val pref: LockPref
) : Mapper() {

    private val locks: MutableList<String>
    private val unlocked: MutableList<String>

    init {
        locks = Collections.synchronizedList(ArrayList<String>())
        unlocked = Collections.synchronizedList(ArrayList<String>())
        locks.addAll(pref.getLockedPackages())
    }

    fun getAllLocks(): ArrayList<String> {
        return ArrayList(locks)
    }

    fun addLock(pack: String) {
        locks.add(pack)
        pref.setLocks(locks)
    }

    fun deleteLock(pack: String) {
        locks.remove(pack)
        pref.setLocks(locks)
    }

    fun isLocked(pack: String): Boolean {
        return locks.contains(pack)
    }

    fun isUnlocked(pack: String) : Boolean {
        Timber.v("AppService %s", unlocked.toString())
        return unlocked.contains(pack)
    }

    fun addInUnlock(pack: String) {
        unlocked.add(pack)
    }
}
