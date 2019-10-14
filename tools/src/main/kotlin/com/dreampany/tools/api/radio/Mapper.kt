package com.dreampany.tools.api.radio

import com.dreampany.framework.util.NumberUtil
import com.dreampany.tools.misc.Constants
import okhttp3.Response

/**
 * Created by roman on 2019-10-14
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Mapper {
    companion object {

        fun decodeShoutCast(response: Response): ShoutCast? {
            val metadataOffset =
                NumberUtil.parseInt(response.header(Constants.ShoutCast.ICY_META_INT), 0)
            if (metadataOffset == 0) return null

            val cast = ShoutCast()
            cast.metadataOffset = metadataOffset
            cast.bitrate = NumberUtil.parseInt(response.header(Constants.ShoutCast.ICY_BR), 0)
            cast.audioInfo = response.header(Constants.ShoutCast.ICY_AUDIO_INFO)
            cast.desc = response.header(Constants.ShoutCast.ICY_DESCRIPTION)
            cast.genre = response.header(Constants.ShoutCast.ICY_GENRE)
            cast.name = response.header(Constants.ShoutCast.ICY_NAME)
            cast.url = response.header(Constants.ShoutCast.ICY_URL)
            cast.server = response.header(Constants.ShoutCast.SERVER)
            cast.public = NumberUtil.parseInt(response.header(Constants.ShoutCast.PUBLIC), 0) > 0

            cast.audioInfo?.run {

                val params = getAudioParams(this)

                cast.channels = NumberUtil.parseInt(params.get(Constants.ShoutCast.ICY_CHANNELS), 0)
                if (cast.channels == 0) {
                    cast.channels = NumberUtil.parseInt(params.get(Constants.ShoutCast.CHANNELS), 0)
                }

                cast.sampleRate =
                    NumberUtil.parseInt(params.get(Constants.ShoutCast.ICY_SAMPLE_RATE), 0)
                if (cast.sampleRate == 0) {
                    cast.sampleRate =
                        NumberUtil.parseInt(params.get(Constants.ShoutCast.SAMPLE_RATE), 0)
                }

                if (cast.bitrate == 0) {
                    cast.bitrate =
                        NumberUtil.parseInt(params.get(Constants.ShoutCast.ICY_BIT_RATE), 0)
                    if (cast.bitrate == 0) {
                        cast.bitrate =
                            NumberUtil.parseInt(params.get(Constants.ShoutCast.BIT_RATE), 0)
                    }
                }
            }
            return cast
        }

        fun decodeShoutCastMetadata(meta: String): Map<String, Any> {
            val data = hashMapOf<String, Any>()
            val parts = meta.split(Constants.Sep.SEMI_COLON)
            for (part in parts) {
                val dx = part.indexOf(Constants.Sep.EQUAL)
                if (dx < 1) continue

                val isString = (dx + 1 < part.length
                        && part.get(part.length - 1) == '\''
                        && part.get(dx + 1) == '\'')

                val key = part.substring(0, dx)
                val value = if (isString)
                    part.substring(dx + 2, part.length - 1)
                else if (dx + 1 < part.length)
                    part.substring(dx + 1)
                else
                    Constants.Default.STRING

                data.put(key, value)
            }
            return data
        }

        private fun getAudioParams(info: String): Map<String, Any> {
            val params = linkedMapOf<String, Any>()
            val pairs = info.split(Constants.Sep.SEMI_COLON)
            pairs.forEach {
                val idx = it.indexOf(Constants.Sep.EQUAL)
                params[it.substring(0, idx)] = it.substring(idx + 1)
            }
            return params
        }
    }
}