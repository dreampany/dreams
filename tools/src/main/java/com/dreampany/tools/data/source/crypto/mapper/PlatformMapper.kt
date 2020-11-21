package com.dreampany.tools.data.source.crypto.mapper

import com.dreampany.tools.api.crypto.model.cmc.CryptoPlatform
import com.dreampany.tools.data.model.crypto.Platform
import com.google.common.collect.Maps
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 11/18/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class PlatformMapper
@Inject constructor() {

    private val platforms: MutableMap<String, Platform>

    init {
        platforms = Maps.newConcurrentMap()
    }

    @Throws
    @Synchronized
    suspend fun write(input: Platform) = platforms.put(input.id, input)

    @Throws
    @Synchronized
    suspend fun read(input: CryptoPlatform?): Platform? {
        if (input == null) return null
        var output: Platform? = platforms.get(input.id)
        if (output == null) {
            output = Platform(input.id)
            platforms.put(input.id, output)
        }

        output.name = input.name
        output.symbol = input.symbol
        output.slug = input.slug
        output.tokenAddress = input.tokenAddress

        return output
    }
}