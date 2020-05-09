package com.dreampany.tools.api.history

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by roman on 9/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface MuffinService {
    @GET(Constants.Api.HISTORY_MUFFIN_LABS_DAY_MONTH)
    fun getWikiHistory(
        @Path(Constants.Keys.MONTH) month: Int,
        @Path(Constants.Keys.DAY) day: Int
    ): Call<WikiHistoryResponse>
}