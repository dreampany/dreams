package com.dreampany.tools.data.source.pref

import android.content.Context
import com.dreampany.framework.data.source.pref.FramePref
import com.dreampany.tools.misc.Constants
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 1/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class LockPref
@Inject constructor(
    context: Context,
    val gson: Gson
) : FramePref(context) {

    private val type = object : TypeToken<ArrayList<String>>() {}.type

    override fun getPrivateName(context: Context): String {
        return Constants.Pref.NAME.LOCK
    }

    fun setPin(pin: String) {
        setPrivately(Constants.Pref.Lock.PIN, pin)
    }

    fun getPin(): String {
        return getPrivately(Constants.Pref.Lock.PIN, Constants.Default.STRING)
    }

    fun hasPin() : Boolean {
        return getPin().isEmpty().not()
    }

    fun commitServicePermitted() {
        setPrivately(Constants.Pref.Lock.SERVICE, true)
    }

    fun isServicePermitted(): Boolean {
        return getPrivately(Constants.Pref.Lock.SERVICE, false)
    }

    fun addLockedPackage(pkg: String) {
        val packages = getLockedPackages()
        packages.add(pkg)
        setLocks(packages)
    }

    fun removeLockedPackage(pkg: String) {
        val packages = getLockedPackages()
        packages.remove(pkg)
        setLocks(packages)
    }

    fun setLocks(locks: MutableList<String>) {
        val json = gson.toJson(locks, type)
        setPrivately(Constants.Pref.Lock.LOCKED_PACKAGES, json)
    }

    fun getLockedPackages(): ArrayList<String> {
        val json = getPrivately(Constants.Pref.Lock.LOCKED_PACKAGES, Constants.Default.STRING)
        return if (json.isEmpty()) arrayListOf() else gson.fromJson(json, type)
    }
}