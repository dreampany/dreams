package com.dreampany.framework.misc.func

import com.google.gson.Gson
import retrofit2.Response
import javax.inject.Inject
import kotlin.reflect.KClass

/**
 * Created by roman on 17/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Parser
@Inject constructor(
    private val gson: Gson
) {

    fun <T : Any> parseError(resp: Response<T>, clazz: KClass<T>): T? {
        try {
            val error = resp.errorBody()?.byteStream() ?: return null
            return gson.fromJson(error.reader(), clazz.java)
        } catch (error: Throwable) {
            //Timber.e(error)
            return null
        }
    }
}