package com.dreampany.history.data.source.remote

import com.dreampany.history.misc.Constants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

/**
 * Created by roman on 2019-07-24
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface WikiHistoryService {
    //@Headers(Constants.Retrofit.CONNECTION_CLOSE)
    @GET(Constants.Api.HISTORY_MUFFIN_LABS_DAY_MONTH)
    fun getWikiHistory(@Path(Constants.History.DAY) day: Int, @Path(Constants.History.MONTH) month: Int): Call<WikiHistoryResponse>
}