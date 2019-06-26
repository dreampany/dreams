package com.dreampany.match.data.source.pref

import android.content.Context

import com.dreampany.frame.data.source.pref.FramePref
import com.dreampany.match.data.model.User
import com.dreampany.match.misc.Constants

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Hawladar Roman on 3/7/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Singleton
class Pref @Inject
internal constructor(context: Context) : FramePref(context) {

    fun setUser(user: User) {
        setPrivately(Constants.UserConst.USER, user)
    }

    fun getUser(): User {
        return getPrivately(
            Constants.UserConst.USER,
            User::class.java,
            Constants.getAnonymousUser()
        )
    }
}