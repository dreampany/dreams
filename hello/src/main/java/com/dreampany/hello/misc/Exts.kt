package com.dreampany.hello.misc

import com.dreampany.hello.data.model.User
import com.google.firebase.auth.FirebaseUser

/**
 * Created by roman on 26/9/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
val FirebaseUser.user : User
    get() {
        val user = User(uid)
        user.name = displayName
        user.photo = photoUrl.toString()
        user.email = email
        user.phone = phoneNumber
        return user
    }