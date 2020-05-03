package com.dreampany.framework.data.source.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url


/**
 * Created by roman on 2019-10-07
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface RemoteService {
    @GET
    fun get(@Url url: String): Call<ResponseBody>
}