package com.dreampany.tools.misc

import android.content.Context
import android.content.Intent
import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.misc.Constants
import com.dreampany.framework.util.TextUtil
import com.dreampany.tools.R
import com.dreampany.tools.data.model.Feature
import java.util.concurrent.TimeUnit


/**
 * Created by Hawladar Roman on 29/5/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
class Constants {

    companion object Screen {
        fun database(name: String): String = Constants.database(name)
        fun database(name: String, type: String): String = Constants.database(name, type)

        fun lastAppId(context: Context): String = Constants.lastAppId(context)
        fun more(context: Context): String = Constants.more(context)
        fun about(context: Context): String = Constants.about(context)
        fun settings(context: Context): String = Constants.settings(context)
        fun license(context: Context): String = Constants.license(context)

        fun app(context: Context): String =
            lastAppId(context) + Sep.HYPHEN + TextUtil.getString(context, R.string.app_name)

        fun launch(context: Context): String = Constants.launch(context)
        fun navigation(context: Context): String = Constants.navigation(context)
        fun tools(context: Context): String = Constants.tools(context)

        fun home(context: Context): String = lastAppId(context) + Sep.HYPHEN + "home"
        fun appHome(context: Context): String = lastAppId(context) + Sep.HYPHEN + "app-home"
        fun noteHome(context: Context): String = lastAppId(context) + Sep.HYPHEN + "note-home"
        fun wordHome(context: Context): String = lastAppId(context) + Sep.HYPHEN + "word-home"
        fun vpnHome(context: Context): String = lastAppId(context) + Sep.HYPHEN + "vpn-home"
        fun radioHome(context: Context): String = lastAppId(context) + Sep.HYPHEN + "radio-home"

        fun favoriteNotes(context: Context): String =
            lastAppId(context) + Sep.HYPHEN + "favorite-notes"

        fun editNote(context: Context): String = lastAppId(context) + Sep.HYPHEN + "edit-note"

        fun word(context: Context): String = lastAppId(context) + Sep.HYPHEN + "word"
        fun favoriteWords(context: Context): String =
            lastAppId(context) + Sep.HYPHEN + "favorite-words"

        fun wordVision(context: Context): String = lastAppId(context) + Sep.HYPHEN + "word-vision"
        fun wordQuiz(context: Context): String = lastAppId(context) + Sep.HYPHEN + "word-quiz"
        fun relatedQuiz(context: Context): String = lastAppId(context) + Sep.HYPHEN + "related-quiz"

        fun radioStations(context: Context, state: State): String =
            lastAppId(context) + Sep.HYPHEN + "${state.name.toLowerCase()}-related-quiz"

        fun vpnServers(context: Context): String =
            lastAppId(context) + Sep.HYPHEN + "vpn-servers"

        fun isOn(context: Context, screen: String, type: Type): Boolean {
            when (type) {
                Type.WORD -> {
                    when (screen) {
                        wordHome(context), word(context), favoriteWords(context), wordVision(context),
                        wordQuiz(context),
                        relatedQuiz(context) -> {
                            return true
                        }

                    }
                }
            }
            return false
        }

        fun hasThreshold(context: Context, screen: String, type: Type): Boolean {
            when (type) {
                Type.WORD -> {
                    if (isOn(context, screen, type)) return true
                }
            }
            return false
        }

        fun getThreshold(context: Context, screen: String, type: Type): Int {
            when (type) {
                Type.WORD -> {
                    if (isOn(context, screen, type)) return Threshold.WORD
                }
            }
            return Threshold.DEFAULT
        }
    }

    object Event {
        const val ERROR = Constants.Event.ERROR
        const val APPLICATION = Constants.Event.APPLICATION
        const val ACTIVITY = Constants.Event.ACTIVITY
        const val FRAGMENT = Constants.Event.FRAGMENT
        const val NOTIFICATION = Constants.Event.NOTIFICATION
    }

    object Notify {
        const val FOREGROUND_ID = Constants.Notify.FOREGROUND_ID
        const val PLAYER_FOREGROUND_ID = 104
        const val FOREGROUND_CHANNEL_ID = Constants.Notify.FOREGROUND_CHANNEL_ID
        const val PLAYER_FOREGROUND_CHANNEL_ID = "player_" + Constants.Notify.FOREGROUND_CHANNEL_ID
    }

    object Sep {
        const val DOT = Constants.Sep.DOT
        const val COMMA = Constants.Sep.COMMA
        const val COMMA_SPACE = Constants.Sep.COMMA_SPACE
        const val SPACE = Constants.Sep.SPACE
        const val HYPHEN = Constants.Sep.HYPHEN
        const val SEMI_COLON = Constants.Sep.SEMI_COLON
        const val EQUAL = Constants.Sep.EQUAL
        const val SPACE_HYPHEN_SPACE = Constants.Sep.SPACE_HYPHEN_SPACE
        const val LEAF_SEPARATOR = Constants.Sep.LEAF_SEPARATOR
    }

    object Default {
        val NULL = Constants.Default.NULL
        const val BOOLEAN = Constants.Default.BOOLEAN
        const val CHARACTER = Constants.Default.CHARACTER
        const val INT = Constants.Default.INT
        const val LONG = Constants.Default.LONG
        const val FLOAT = Constants.Default.FLOAT
        const val DOUBLE = Constants.Default.DOUBLE
        const val STRING = Constants.Default.STRING
    }

    object Tag {
        const val NOTIFY_SERVICE = Constants.Tag.NOTIFY_SERVICE
        const val LANGUAGE_PICKER = "language-picker"
    }

    object Action {
        const val START_SERVICE = Constants.Action.START_SERVICE
        const val STOP_SERVICE = Constants.Action.STOP_SERVICE

        object Vpn {
            const val VPN_STATUS = "de.blinkt.openvpn.VPN_STATUS"
        }
    }

    object Time {
        val NOTIFY = TimeUnit.MINUTES.toSeconds(1)
        val SERVER = TimeUnit.DAYS.toMillis(1)
        val STATION = TimeUnit.DAYS.toMillis(10)
        val FIREBASE = TimeUnit.HOURS.toMillis(1)


        object Word {
            val FREQUENT = TimeUnit.MINUTES.toMillis(1)
            val NORMAL = TimeUnit.MINUTES.toMillis(3)
            val LAZY = TimeUnit.MINUTES.toMillis(5)
            val DEAD = TimeUnit.HOURS.toMillis(1)
        }

        fun minuteToMillis(minutes: Long): Long {
            return TimeUnit.MINUTES.toMillis(minutes)
        }
    }

    object Period {
        val NOTIFY = TimeUnit.MINUTES.toMillis(1)
        val LOAD = TimeUnit.MINUTES.toMillis(3)
    }

    object Date {
        const val FORMAT_MONTH_DAY = "dd MMMM"
    }

    object Database {
        const val POINT = "point"
        const val NOTE = "note"
        const val WORD = "word"
        const val VPN = "vpn"
        const val RADIO = "radio"
    }

    object Pref {
        const val LEVEL = "level"
        const val LANGUAGE = "language"
        const val DEFAULT_POINT = "default_point"
        const val LOAD = "load"
        const val WORD = "word"
        const val VPN = "vpn"

        const val PLAYER_RETRY_TIMEOUT = "player_retry_timeout"

        const val WORD_TRACK_LOADED = "word_track_loaded"
        const val WORD_COMMON_LOADED = "word_common_loaded"
        const val WORD_ALPHA_LOADED = "word_alpha_loaded"
        const val WORD_LAST = "word_last"
        const val WORD_TRACK_START_AT = "word_track_start_at"
        //const val WORD_TRACK_TIME = "word_track_time"
        const val WORD_TRACK_COUNT = "word_track_count"
        const val SERVER_TIME = "server_time"

        const val DOWNLOAD = "download"
        const val UPLOAD = "upload"

        object Word {
            const val COUNT_SYNCED = "count_synced"
            const val TIME_LOAD = "time_load"
            const val TIME_SYNC = "time_sync"
            const val TIME_TRACK = "time_track"
        }

        object Radio {
            const val RADIO = "radio"
            const val STATION_STATE = "station_state"
            const val STATION_TIME = "station_time"
        }

        object Vpn {
            const val SERVER = "server"
        }
    }

    object Count {
        const val DEFAULT_POINT = 999

        object Word {
            const val COMMON = 1000
            const val ALPHA = 370099
            const val PAGE = 1000
            const val DEFAULT_PER_TRACK = 1
            const val PER_TRACK = 10
            const val TRACK = 100

            const val FREQUENT = 100
            const val NORMAL = 1000
            const val LAZY = 10000
        }
    }

    object Limit {
        const val WORD_RESOLVE = 10
        const val WORD_RECENT = 100
        const val WORD_SEARCH = 1000
        const val WORD_SUGGESTION = 10
        const val WORD_OCR = 1000
        const val WORD_TRACK = 1000L
        const val QUIZ_OPTIONS = 4

        object Radio {
            const val STATIONS = 100L
        }

        object Vpn {
            const val SERVERS = 100L
        }
    }

    object Threshold {
        const val DEFAULT = 1
        const val WORD = 3
    }

    object Assets {
        const val WORDS_COMMON = "common.txt"
        const val WORDS_ALPHA = "alpha.txt"
    }

    object Firebase {
        const val WEIGHT = "weight"
        const val SOURCE = "source"
        const val EXTRA = "extra"
        const val WORDS = "words"
        const val TRACK = "track"
        const val TRACK_WORDS = "track-words"
    }

    object Api {

        object Radio {
            const val RADIO_BROWSER = "http://www.radio-browser.info/webservice/"
            const val RADIO_BROWSER_STATIONS_OF_COUNTRY =
                "json/stations/bycountrycodeexact/{${Station.COUNTRY_CODE}}"
            const val RADIO_BROWSER_STATIONS_OF_TRENDS =
                "json/stations/topclick/{${Station.LIMIT}}"
            const val RADIO_BROWSER_STATIONS_OF_POPULAR =
                "json/stations/topvote/{${Station.LIMIT}}"
            const val RADIO_BROWSER_STATIONS_SEARCH = "json/stations/search"
        }
    }

    object Demo {
        const val ID = Constants.Key.ID
    }

    object Quiz {
        const val ID = Constants.Key.ID
        const val TYPE = Constants.Key.TYPE
        const val SUBTYPE = Constants.Key.SUBTYPE
        const val LEVEL = Constants.Key.LEVEL
        const val POINT_ID = "point_id"
        val OptionCharArray: CharArray = CharArray(4).apply {
            set(0, 'A')
            set(1, 'B')
            set(2, 'C')
            set(3, 'D')
        }
    }

    object Feature {
        const val ID = Constants.Key.ID
    }

    object App {
        const val ID = Constants.Key.ID
    }

    object Barcode {
        const val ID = Constants.Key.ID
    }

    object Note {
        const val ID = Constants.Key.ID
    }

    object Word {
        const val ID = Constants.Key.ID
        const val PART_OF_SPEECH = "part_of_speech"
        const val LEFTER = "lefter"
        const val RIGHTER = "righter"
        const val RECENT_WORD = "recent_word"
    }

    object Example {
        const val DOCUMENT_ID = "document_id"
        const val EXAMPLE_ID = "example_id"
    }

    object Contact {
        const val ID = Constants.Key.ID
    }

    object Coin {
        const val ID = Constants.Key.ID
        const val MARKET_PAIRS = "market_pairs"
        const val CIRCULATING_SUPPLY = "circulating_supply"
        const val TOTAL_SUPPLY = "total_supply"
        const val MAX_SUPPLY = "max_supply"
        const val LAST_UPDATED = "last_updated"
        const val DATE_ADDED = "date_added"
    }

    object Quote {
        const val ID = Constants.Key.ID
        const val CURRENCY = "currency"
        const val DAY_VOLUME = "day_volume"
        const val MARKET_CAP = "market_cap"
        const val HOUR_CHANGE = "hour_change"
        const val DAY_CHANGE = "day_change"
        const val WEEK_CHANGE = "week_change"
        const val LAST_UPDATED = "last_updated"
    }

    object Server {
        const val ID = Constants.Key.ID
        const val COUNTRY_NAME = "country_name"
        const val COUNTRY_CODE = "country_code"
        const val LOG_TYPE = "log_type"
    }

    object Station {
        const val ID = Constants.Key.ID
        const val CHANGE_UUID = "change_uuid"
        const val STATION_UUID = "station_uuid"
        const val COUNTRY_CODE = "country_code"
        const val NEGATIVE_VOTES = "negative_votes"
        const val LAST_CHANGE_TIME = "last_change_time"
        const val LAST_CHECK_OK = "last_check_ok"
        const val LAST_CHECK_TIME = "last_check_time"
        const val LAST_CHECK_OK_TIME = "last_check_ok_time"
        const val CLICK_TIMESTAMP = "click_timestamp"
        const val CLICK_COUNT = "click_count"
        const val CLICK_TREND = "click_trend"
        const val LIMIT = "limit"

        object Remote {
            const val CHANGE_UUID = "changeuuid"
            const val STATION_UUID = "stationuuid"
            const val COUNTRY_CODE = "countrycode"
            const val NEGATIVE_VOTES = "negativevotes"
            const val LAST_CHANGE_TIME = "lastchangetime"
            const val LAST_CHECK_OK = "lastcheckok"
            const val LAST_CHECK_TIME = "lastchecktime"
            const val LAST_CHECK_OK_TIME = "lastcheckoktime"
            const val CLICK_TIMESTAMP = "clicktimestamp"
            const val CLICK_COUNT = "clickcount"
            const val CLICK_TREND = "clicktrend"
        }
    }

    object RequestCode {
        object Vpn {
            const val START_VPN_PROFILE = 1
            const val OPEN_SERVER = 2
        }

        object Note {
            const val ADD = 1
            const val EDIT = 2
            const val VIEW = 3
        }

        const val SETTINGS = 5
        const val FAVORITE = 6
        const val QUIZ = 5
    }

    object Service {
        const val VPN_ADDRESS = "10.0.0.2"
        const val VPN_ROUTE = "0.0.0.0"

        const val PLAYER_SERVICE_STATE_CHANGE = "radio_player_state_change"
        const val PLAYER_SERVICE_STATE = "radio_player_state"
        const val PLAYER_SERVICE_UPDATE = "radio_player_state"

        object Command {
            const val RESUME = "resume"
            const val PAUSE = "pause"
            const val STOP = "stop"
            const val NEXT = "next"
            const val PREVIOUS = "previous"
            const val MEDIA_BUTTON = Intent.ACTION_MEDIA_BUTTON
        }
    }

    object Translation {
        const val YANDEX_URL = com.dreampany.translation.misc.Constants.Yandex.URL
    }

    object Delimiter {
        const val COMMA = Constants.Delimiter.COMMA
    }

    object VpnGate {
        const val FILE_NAME = "vpngate.csv"
        const val URL = "http://www.vpngate.net/api/iphone"
        const val SERVER_PARTS = 15
        const val INDEX_HOST = 0
        const val INDEX_IP = 1
        const val INDEX_SCORE = 2
        const val INDEX_PING = 3
        const val INDEX_SPEED = 4
        const val INDEX_COUNTRY_NAME = 5
        const val INDEX_COUNTRY_CODE = 6
        const val INDEX_NUM_VPN_SESSIONS = 7
        const val INDEX_UPTIME = 8
        const val INDEX_TOTAL_USERS = 9
        const val INDEX_TOTAL_TRAFFIC = 10
        const val INDEX_LOG_TYPE = 11
        const val INDEX_OPERATOR = 12
        const val INDEX_MESSAGE = 13
        const val INDEX_CONFIG_DATA = 14
    }

    object File {
        const val BYTE_ARRAY_SIZE = 4096
    }

    object Order {
        fun getOrder(type: Type): Int {
            when (type) {
                Type.VPN -> return 1
                Type.RADIO -> return 2
                Type.APP -> return 3
                Type.WORD -> return 4
                Type.NOTE -> return 5
                else -> return Int.MAX_VALUE
            }
        }
    }

    object Extension {
        const val M3U8 = ".m3u8"
    }

    object Header {
        const val ICY_METADATA = "Icy-MetaData"
        const val ICY_METADATA_OK = "1"
        const val ACCEPT_ENCODING = "Accept-Encoding"
        const val ACCEPT_ENCODING_IDENTITY = "identity"
    }

    object MimeType {
        const val AUDIO_MPEG = "audio/mpeg"
    }

    object ContentType {
        const val APPLE_MPEGURL = "application/vnd.apple.mpegurl"
        const val X_MPEGURL = "application/x-mpegurl"
    }

    object ShoutCast {
        const val ICY_META_INT = "icy-metaint"
        const val ICY_BR = "icy-br"
        const val ICY_AUDIO_INFO = "ice-audio-info"
        const val ICY_DESCRIPTION = "icy-description"
        const val ICY_GENRE = "icy-genre"
        const val ICY_NAME = "icy-name"
        const val ICY_URL = "icy-url"
        const val SERVER = "Server"
        const val PUBLIC = "icy-pub"
        const val ICY_CHANNELS = "ice-channels"
        const val CHANNELS = "channels"
        const val ICY_SAMPLE_RATE = "ice-samplerate"
        const val SAMPLE_RATE = "samplerate"
        const val ICY_BIT_RATE = "ice-bitrate"
        const val BIT_RATE = "bitrate"
    }

    object Stream {
        const val TITLE = "StreamTitle"
    }

    object Radio {
        const val STATION_ID = "station_id"
        const val PLAY_BY_STATION_ID = "play_by_station_id"
        const val PLAY_BY_STATION_UUID = "play_by_station_uuid"

        const val FULL_VOLUME = 100f
        const val DUCK_VOLUME = 40f
    }

    object Vpn {
        const val STATUS = "status"
        const val DETAIL_STATUS = "detailstatus"
        const val NOPROCESS = "NOPROCESS"
    }
}