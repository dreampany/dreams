package com.dreampany.tools.api.crypto.remote.service

import com.dreampany.tools.api.crypto.remote.response.gecko.GeckoTickersResponse
import com.dreampany.tools.misc.constants.CryptoConstants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by roman on 11/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface GeckoService {
    @GET(CryptoConstants.Gecko.TICKERS)
    fun tickers(
        @HeaderMap headers: Map<String, String>,
        @Path(CryptoConstants.Gecko.ID) id: String,
        @Query(CryptoConstants.Gecko.INCLUDE_IMAGE) includeImage: Boolean
    ): Call<GeckoTickersResponse>
}