package com.dreampany.tools.api.radio

import com.dreampany.framework.misc.constant.Constants
import com.dreampany.framework.misc.exts.parseInt
import com.dreampany.tools.misc.constants.RadioConstants
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
                response.header(RadioConstants.Keys.ShoutCast.ICY_META_INT)?.toInt() ?: return null

            val cast = ShoutCast()
            cast.metadataOffset = metadataOffset
            cast.bitrate = response.header(RadioConstants.Keys.ShoutCast.ICY_BR).parseInt()
            cast.audioInfo = response.header(RadioConstants.Keys.ShoutCast.ICY_AUDIO_INFO)
            cast.desc = response.header(RadioConstants.Keys.ShoutCast.ICY_DESCRIPTION)
            cast.genre = response.header(RadioConstants.Keys.ShoutCast.ICY_GENRE)
            cast.name = response.header(RadioConstants.Keys.ShoutCast.ICY_NAME)
            cast.url = response.header(RadioConstants.Keys.ShoutCast.ICY_URL)
            cast.server = response.header(RadioConstants.Keys.ShoutCast.SERVER)
            cast.public = response.header(RadioConstants.Keys.ShoutCast.PUBLIC).parseInt() > 0

            cast.audioInfo?.run {

                val params = getAudioParams(this)

                cast.channels = params.get(RadioConstants.Keys.ShoutCast.ICY_CHANNELS).parseInt()
                if (cast.channels == 0) {
                    cast.channels = params.get(RadioConstants.Keys.ShoutCast.CHANNELS).parseInt()
                }

                cast.sampleRate = params.get(RadioConstants.Keys.ShoutCast.ICY_SAMPLE_RATE).parseInt()
                if (cast.sampleRate == 0) {
                    cast.sampleRate = params.get(RadioConstants.Keys.ShoutCast.SAMPLE_RATE).parseInt()
                }

                if (cast.bitrate == 0) {
                    cast.bitrate = params.get(RadioConstants.Keys.ShoutCast.ICY_BIT_RATE).parseInt()
                    if (cast.bitrate == 0) {
                        cast.bitrate = params.get(RadioConstants.Keys.ShoutCast.BIT_RATE).parseInt()
                    }
                }
            }
            return cast
        }

        fun decodeStream(meta: Map<String, String>?): Stream? {
            val stream = Stream()
            stream.meta = meta
            if (meta != null && meta.containsKey(RadioConstants.Keys.Stream.TITLE)) {
                stream.title = meta.get(RadioConstants.Keys.Stream.TITLE)
                stream.title?.let {
                    val parts = it.split(Constants.Sep.SPACE_HYPHEN_SPACE.toRegex(), 2)
                    stream.artist = parts.first()
                    stream.track = parts.last()
                }
            }

            return stream
        }

        fun decodeShoutCastMetadata(meta: String): Map<String, String> {
            val data = hashMapOf<String, String>()
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

        private fun getAudioParams(info: String): Map<String, String> {
            val params = linkedMapOf<String, String>()
            val pairs = info.split(Constants.Sep.SEMI_COLON)
            pairs.forEach {
                val idx = it.indexOf(Constants.Sep.EQUAL)
                params[it.substring(0, idx)] = it.substring(idx + 1)
            }
            return params
        }
    }
}