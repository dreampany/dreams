package com.dreampany.pair.data.mapper

import com.dreampany.common.misc.func.Mapper
import com.dreampany.pair.data.model.User
import com.google.common.collect.Maps
import com.google.firebase.auth.FirebaseUser
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class Mappers
@Inject constructor(
    gson: Gson
) : Mapper(gson) {

    private val users: MutableMap<String, User>

    init {
        users = Maps.newConcurrentMap()
    }

    fun getUser(input: FirebaseUser, name: String): User {
        var out: User? = users.get(input.uid)
        if (out == null) {
            out = User(id = input.uid)
            users.put(input.uid, out)
        }
        out.apply {
            this.email = input.email
            this.name = name
        }
        return out
    }
}