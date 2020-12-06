package com.dreampany.hello.misc

import com.dreampany.framework.misc.exts.age
import com.dreampany.framework.misc.exts.color
import com.dreampany.hello.R
import com.dreampany.hello.data.model.Auth
import com.dreampany.hello.data.model.User
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseUser
import java.util.*

/**
 * Created by roman on 26/9/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
fun MaterialButton.active() {
    this.setBackgroundColor(context.color(R.color.textColorPrimary))
    this.isEnabled = true
}

fun MaterialButton.inactive() {
    this.setBackgroundColor(context.color(R.color.textColorSecondary))
    this.isEnabled = false
}

fun MaterialButton.active(active: Boolean) {
    if (active) active()
    else inactive()
}

val Calendar.isValidAge: Boolean
    get() = this.age >= Constants.Date.MIN_AGE

val FirebaseUser.user: User
    get() {
        val user = User(uid)
        user.name = displayName
        user.photo = photoUrl.toString()
        user.email = email
        user.phone = phoneNumber
        return user
    }

val FirebaseUser.auth: Auth
    get() {
        val output = Auth(uid)
        output.email = email
        return output
    }

fun Auth.map(deviceId: String): Map<String, Any> {
    val output = HashMap<String, Any>()
    output.put(Constants.Keys.Firestore.TIME, time)
    output.put(Constants.Keys.Firestore.ID, id)
    output.put(Constants.Keys.Firestore.REF, ref)
    username?.let { output.put(Constants.Keys.Firestore.USERNAME, it) }
    email?.let { output.put(Constants.Keys.Firestore.EMAIL, it) }
    password?.let { output.put(Constants.Keys.Firestore.PASSWORD, it) }
    output.put(Constants.Keys.Firestore.REGISTERED, mapOf(deviceId to registered))
    output.put(Constants.Keys.Firestore.VERIFIED, mapOf(deviceId to verified))
    output.put(Constants.Keys.Firestore.LOGGED, mapOf(deviceId to logged))
    return output
}
