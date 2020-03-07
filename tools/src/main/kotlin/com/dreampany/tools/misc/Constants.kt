package com.dreampany.tools.misc

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import com.dreampany.framework.data.enums.Difficult
import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.enums.Subtype
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.misc.Constants
import com.dreampany.framework.misc.extensions.string
import com.dreampany.framework.util.TextUtil
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.CoinSort
import com.dreampany.tools.data.enums.Currency
import com.dreampany.tools.data.enums.Order
import com.dreampany.tools.data.model.Feature
import com.dreampany.tools.data.model.crypto.Coin
import com.dreampany.tools.data.model.resume.Resume
import com.dreampany.tools.data.model.Server
import com.dreampany.tools.data.model.question.Question
import com.dreampany.tools.ui.model.crypto.CoinItem
import com.dreampany.tools.ui.model.ContactItem
import com.google.common.collect.Maps
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.Comparator
import kotlin.collections.HashMap


/**
 * Created by roman on 2/26/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Constants {

    companion object {
        fun database(name: String): String = Constants.database(name)
        fun database(name: String, type: String): String = Constants.database(name, type)

        fun lastAppId(context: Context?): String = Constants.lastAppId(context)
        fun more(context: Context?): String = Constants.more(context)
        fun about(context: Context?): String = Constants.about(context)
        fun settings(context: Context?): String = Constants.settings(context)
        fun license(context: Context?): String = Constants.license(context)

        fun app(context: Context?): String =
            lastAppId(context) + Sep.HYPHEN + TextUtil.getString(context, R.string.app_name)

        fun launch(context: Context?): String = Constants.launch(context)
        fun navigation(context: Context?): String = Constants.navigation(context)
        fun tools(context: Context?): String = Constants.tools(context)

        /* feature */
        fun home(context: Context?): String = lastAppId(context) + Sep.HYPHEN + "home"

        /* app */
        fun appHome(context: Context?): String = lastAppId(context) + Sep.HYPHEN + "app-home"

        /* crypto */
        fun cryptoHome(context: Context?): String = lastAppId(context) + Sep.HYPHEN + "crypto-home"
        fun cryptoInfo(context: Context?): String = lastAppId(context) + Sep.HYPHEN + "crypto-info"
        fun cryptoMarket(context: Context?): String =
            lastAppId(context) + Sep.HYPHEN + "crypto-market"

        fun cryptoGraph(context: Context?): String =
            lastAppId(context) + Sep.HYPHEN + "crypto-graph"

        /* note */
        fun noteHome(context: Context?): String = lastAppId(context) + Sep.HYPHEN + "note-home"

        fun editNote(context: Context?): String = lastAppId(context) + Sep.HYPHEN + "edit-note"
        fun favoriteNotes(context: Context?): String =
            lastAppId(context) + Sep.HYPHEN + "favorite-notes"

        /* resume */
        fun resumeHome(context: Context?): String = lastAppId(context) + Sep.HYPHEN + "resume-home"

        fun resume(context: Context?): String = lastAppId(context) + Sep.HYPHEN + "resume"

        /* word */
        fun wordHome(context: Context?): String = lastAppId(context) + Sep.HYPHEN + "word-home"

        fun word(context: Context?): String = lastAppId(context) + Sep.HYPHEN + "word"
        fun favoriteWords(context: Context?): String =
            lastAppId(context) + Sep.HYPHEN + "favorite-words"

        fun wordVision(context: Context?): String = lastAppId(context) + Sep.HYPHEN + "word-vision"
        fun wordQuiz(context: Context?): String = lastAppId(context) + Sep.HYPHEN + "word-quiz"
        fun relatedQuiz(context: Context?): String =
            lastAppId(context) + Sep.HYPHEN + "related-quiz"

        /* radio */
        fun radioHome(context: Context?): String = lastAppId(context) + Sep.HYPHEN + "radio-home"

        fun radioStations(context: Context?, state: State): String =
            lastAppId(context) + Sep.HYPHEN + "${state.name.toLowerCase(Locale.getDefault())}-related-quiz"

        fun favoriteStations(context: Context?): String =
            lastAppId(context) + Sep.HYPHEN + "favorite-stations"

        /* vpn */
        fun vpnHome(context: Context?): String = lastAppId(context) + Sep.HYPHEN + "vpn-home"

        fun favoriteServers(context: Context?): String =
            lastAppId(context) + Sep.HYPHEN + "favorite-servers"

        fun vpnServers(context: Context?): String =
            lastAppId(context) + Sep.HYPHEN + "vpn-servers"

        fun vpnCountries(context: Context?): String =
            lastAppId(context) + Sep.HYPHEN + "vpn-countries"

        /* block */
        fun blockHome(context: Context?): String = lastAppId(context) + Sep.HYPHEN + "block-home"

        fun isOn(context: Context, screen: String, type: Type): Boolean {
            when (type) {
                Type.WORD -> {
                    when (screen) {
                        wordHome(context),
                        word(context),
                        favoriteWords(context),
                        wordVision(context),
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

        fun getFeatures(): List<Triple<Type, Subtype, Int>> {
            val pairs = arrayListOf<Triple<Type, Subtype, Int>>()
            pairs.add(Triple(Type.APP, Subtype.DEFAULT, R.string.title_feature_app))
            pairs.add(Triple(Type.NOTE, Subtype.DEFAULT, R.string.title_feature_note))
            pairs.add(Triple(Type.WORD, Subtype.DEFAULT, R.string.title_feature_word))
            pairs.add(Triple(Type.RADIO, Subtype.DEFAULT, R.string.title_feature_radio))
            pairs.add(Triple(Type.VPN, Subtype.DEFAULT, R.string.title_feature_vpn))
            pairs.add(Triple(Type.CRYPTO, Subtype.DEFAULT, R.string.title_feature_crypto))
            pairs.add(Triple(Type.RESUME, Subtype.DEFAULT, R.string.title_feature_resume))
            pairs.add(Triple(Type.QUESTION, Subtype.DEFAULT, R.string.title_feature_question))
            pairs.add(Triple(Type.LOCK, Subtype.DEFAULT, R.string.title_feature_lock))
            //pairs.add(Triple(Type.BLOCK, Subtype.DEFAULT, R.string.title_feature_block))
            //pairs.add(Triple(Type.TODO, Subtype.DEFAULT, R.string.title_feature_todo))
            //pairs.add(Triple(Type.ENGLISH, Subtype.DEFAULT, R.string.title_feature_english))
            return pairs
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
        const val PLUS = Constants.Sep.PLUS
        const val DOT = Constants.Sep.DOT
        const val COMMA = Constants.Sep.COMMA
        const val COMMA_SPACE = Constants.Sep.COMMA_SPACE
        const val SPACE = Constants.Sep.SPACE
        const val HYPHEN = Constants.Sep.HYPHEN
        const val SEMI_COLON = Constants.Sep.SEMI_COLON
        const val EQUAL = Constants.Sep.EQUAL
        const val SPACE_HYPHEN_SPACE = Constants.Sep.SPACE_HYPHEN_SPACE
        const val LEAF_SEPARATOR = Constants.Sep.LEAF_SEPARATOR
        const val SLASH = Constants.Sep.SLASH
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
        val LIST = Constants.Default.LIST
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

    object Orders {
        fun getOrder(type: Type): Int {
            when (type) {
                Type.VPN -> return 1
                Type.CRYPTO -> return 2
                Type.RESUME -> return 3
                Type.RADIO -> return 4
                Type.APP -> return 5
                Type.NOTE -> return 6
                Type.WORD -> return 7
                Type.ENGLISH -> return 8
                Type.QUESTION -> return 9
                Type.LOCK -> return 10
                Type.BLOCK -> return 11
                else -> return Int.MAX_VALUE
            }
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

        object Crypto {
            val LISTING = TimeUnit.HOURS.toMillis(1)
            val COIN = TimeUnit.MINUTES.toMillis(10)
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
        const val CRYPTO = "crypto"
        const val CONTACT = "contact"
        const val RESUME = "resume"
        const val QUESTION = "question"
    }

    object Pref {

        object NAME {
            const val CRYPTO = "crypto"
            const val LOAD = "load"
            const val RADIO = "radio"
            const val VPN = "vpn"
            const val WORD = "word"
            const val LOCK = "lock"
            const val BLOCK = "contact"
        }

        object Service {
            const val APP_SERVICE = "app_service"
        }

        const val LEVEL = "level"
        const val LANGUAGE = "language"
        const val DEFAULT_POINT = "default_point"
        const val QUESTION = "question"

        const val PLAYER_RETRY_TIMEOUT = "player_retry_timeout"

        const val WORD_LAST = "word_last"
        const val WORD_TRACK_START_AT = "word_track_start_at"

        //const val WORD_TRACK_TIME = "word_track_time"
        const val WORD_TRACK_COUNT = "word_track_count"
        const val SERVER_TIME = "server_time"

        const val DOWNLOAD = "download"
        const val UPLOAD = "upload"

        object Word {
            const val WORD_TRACK_LOADED = "word_track_loaded"
            const val WORD_COMMON_LOADED = "word_common_loaded"
            const val WORD_ALPHA_LOADED = "word_alpha_loaded"
            const val COUNT_SYNCED = "count_synced"
            const val TIME_LOAD = "time_load"
            const val TIME_SYNC = "time_sync"
            const val TIME_TRACK = "time_track"
        }

        object Radio {
            const val STATION_STATE = "station_state"
            const val STATION_TIME = "station_time"
        }

        object Vpn {
            const val SERVER = "server"
        }

        object Crypto {
            const val EXPIRE = "expire"
            const val CURRENCY = "currency"
            const val SORT = "sort"
            const val ORDER = "order"
        }

        object Block {
        }

        object Question {
            const val CATEGORY = "category"
            const val TYPE = "type"
            const val DIFFICULT = "difficult"
        }

        object Lock {
            const val PASSCODE = "passcode"
            const val SERVICE = "service"
            const val LOCKED_PACKAGES = "locked_packages"
        }
    }

    object Count {
        const val DEFAULT_POINT = 999L

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
        const val WORD_RECENT = 100
        const val WORD_SEARCH = 1000
        const val WORD_SUGGESTION = 10
        const val WORD_OCR = 1000
        const val WORD_TRACK = 1000L
        const val QUIZ_OPTIONS = 4

        object Word {
            const val WORD_RESOLVE = 10
            const val HISTORY = 1000L
        }

        object Radio {
            const val STATIONS = 100L
        }

        object Vpn {
            const val SERVERS = 100L
            const val COUNTRIES = 50L
        }

        object Crypto {
            const val LIST = 100L
            const val TRADES = 10L
            const val EXCHANGES = 10L
        }

        object Question {
            const val MIN_LIMIT = 10L
        }
    }

    object Cache {
        object Word {
            var HISTORY = false
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
                "json/stations/bycountrycodeexact/{${Keys.Station.COUNTRY_CODE}}"
            const val RADIO_BROWSER_STATIONS_OF_TRENDS =
                "json/stations/topclick/{${Keys.Station.LIMIT}}"
            const val RADIO_BROWSER_STATIONS_OF_POPULAR =
                "json/stations/topvote/{${Keys.Station.LIMIT}}"
            const val RADIO_BROWSER_STATIONS_SEARCH = "json/stations/search"
        }

        object Crypto {
            const val CoinMarketCapImageUrl =
                "https://s2.coinmarketcap.com/static/img/coins/64x64/%s.png" //id reference
        }
    }

    object Points {
        const val QUESTION = 10L
    }

    object Keys {
        object Demo {
            const val ID = Constants.Key.ID
        }

        object Common {
            const val POINT_ID = "point_id"
        }

        object Resume {
            const val ID = Constants.Key.ID
        }

        object Profile {
            const val TIME = "profile_time"
            const val ID = "profile_id"
            const val NAME = "profile_name"
            const val DESIGNATION = "profile_designation"
            const val PHONE = "profile_phone"
            const val EMAIL = "profile_email"
            const val CURRENT_ADDRESS = "profile_current_address"
            const val PERMANENT_ADDRESS = "profile_permanent_address"
        }

        object Skill {
            const val TIME = "skill_time"
            const val ID = "skill_id"
            const val TITLE = "skill_title"
        }

        object Experience {
            const val TIME = "experience_time"
            const val ID = "experience_id"
            const val COMPANY = "experience_company"
            const val LOCATION = "experience_location"
            const val DESIGNATION = "experience_designation"
            const val DESCRIPTION = "experience_description"
            const val FROM = "experience_from"
            const val TO = "experience_to"
        }

        object Project {
            const val TIME = "project_time"
            const val ID = "project_id"
            const val NAME = "project_name"
            const val DESCRIPTION = "project_description"
            const val FROM = "project_from"
            const val TO = "project_to"
        }

        object School {
            const val TIME = "school_time"
            const val ID = "school_id"
            const val NAME = "school_name"
            const val LOCATION = "school_location"
            const val DEGREE = "school_degree"
            const val DESCRIPTION = "school_description"
        }

        object Server {
            const val ID = Constants.Key.ID
            const val COUNTRY_NAME = "country_name"
            const val COUNTRY_CODE = "country_code"
            const val LOG_TYPE = "log_type"
        }

        object Coin {
            const val CRYPTO = "crypto"
            const val COINS = "coins"
            const val QUOTES = "quotes"

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
            const val VOLUME_24H = "volume_24h"
            const val MARKET_CAP = "market_cap"
            const val CHANGE_1H = "percent_change_1h"
            const val CHANGE_24H = "percent_change_24h"
            const val CHANGE_7D = "percent_change_7d"
            const val LAST_UPDATED = "last_updated"
        }

        object Trade {
            const val ID = Constants.Key.ID
            const val FROM_SYMBOL = "from_symbol"
            const val TO_SYMBOL = "to_symbol"
            const val VOLUME_24H = "volume_24h"
            const val VOLUME_24H_TO = "volume_24h_to"
        }

        object Exchange {
            const val ID = Constants.Key.ID
            const val FROM_SYMBOL = "from_symbol"
            const val TO_SYMBOL = "to_symbol"
            const val VOLUME_24H = "volume_24h"
            const val CHANGE_24H = "change_24h"
            const val CHANGE_PCT_24H = "change_pct_24h"
        }

        object Question {
            const val ID = Constants.Key.ID
        }

        object Order {
            const val ASCENDING = "asc"
            const val DESCENDING = "desc"
        }

        object Contact {
            const val ID = Constants.Key.ID
            const val NICK_NAME = "nick_name"
            const val AVATAR_URL = "avatar_url"
            const val PHONE_NUMBER = "phone_number"
            const val COUNTRY_CODE = "country_code"
        }

        object Quiz {
            const val ID = Constants.Key.ID
            const val TYPE = Constants.Key.TYPE
            const val SUBTYPE = Constants.Key.SUBTYPE
            const val LEVEL = Constants.Key.LEVEL
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

        object Message {
            const val ID = Constants.Key.ID
        }

        object Example {
            const val DOCUMENT_ID = "document_id"
            const val EXAMPLE_ID = "example_id"
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
    }

    object Values {

        object QuestionValues {

            object Misc {
                const val ALL = "All"
            }

            object CategoryValues {
                const val GENERAL_KNOWLEDGE = "General Knowledge"
                const val BOOK = "Entertainment: Books"
                const val FILM = "Entertainment: Film"
                const val MUSIC = "Entertainment: Music"
                const val MUSICAL_THEATRE = "Entertainment: Musicals & Theatres"
                const val TELEVISION = "Entertainment: Television"
                const val VIDEO_GAME = "Entertainment: Video Games"
                const val BOARD_GAME = "Entertainment: Board Games"
                const val SCIENCE_NATURE = "Science & Nature"
                const val COMPUTER = "Science: Computers"
                const val MATHEMATICS = "Science: Mathematics"
                const val MYTHOLOGY = "Mythology"
                const val SPORTS = "Sports"
                const val GEOGRAPHY = "Geography"
                const val HISTORY = "History"
                const val POLITICS = "Politics"
                const val ART = "Art"
                const val CELEBRITIES = "Celebrities"
                const val ANIMALS = "Animals"
                const val VEHICLES = "Vehicles"
                const val COMICS = "Entertainment: Comics"
                const val GADGETS = "Science: Gadgets"
                const val ANIME_MANGA = "Entertainment: Japanese Anime & Manga"
                const val CARTOON_ANIMATION = "Entertainment: Cartoon & Animations"
            }

            object TypeValues {
                const val TRUE_FALSE = "boolean"
                const val MULTIPLE = "multiple"
            }

            object TypeUiValues {
                const val TRUE_FALSE = "True / False"
                const val MULTIPLE = "Multiple Choice"
            }

            object DifficultValues {
                const val EASY = "easy"
                const val MEDIUM = "medium"
                const val HARD = "hard"
            }

            fun getCategory(value: String?): Question.Category? {
                if (value.isNullOrEmpty()) return null
                when (value) {
                    CategoryValues.GENERAL_KNOWLEDGE -> return Question.Category.GENERAL_KNOWLEDGE
                    CategoryValues.BOOK -> return Question.Category.BOOK
                    CategoryValues.FILM -> return Question.Category.FILM
                    CategoryValues.MUSIC -> return Question.Category.MUSIC
                    CategoryValues.MUSICAL_THEATRE -> return Question.Category.MUSICAL_THEATRE
                    CategoryValues.TELEVISION -> return Question.Category.TELEVISION
                    CategoryValues.VIDEO_GAME -> return Question.Category.VIDEO_GAME
                    CategoryValues.BOARD_GAME -> return Question.Category.BOARD_GAME
                    CategoryValues.SCIENCE_NATURE -> return Question.Category.SCIENCE_NATURE
                    CategoryValues.COMPUTER -> return Question.Category.COMPUTER
                    CategoryValues.MATHEMATICS -> return Question.Category.MATHEMATICS
                    CategoryValues.MYTHOLOGY -> return Question.Category.MYTHOLOGY
                    CategoryValues.SPORTS -> return Question.Category.SPORTS
                    CategoryValues.GEOGRAPHY -> return Question.Category.GEOGRAPHY
                    CategoryValues.HISTORY -> return Question.Category.HISTORY
                    CategoryValues.POLITICS -> return Question.Category.POLITICS
                    CategoryValues.ART -> return Question.Category.ART
                    CategoryValues.CELEBRITIES -> return Question.Category.CELEBRITIES
                    CategoryValues.ANIMALS -> return Question.Category.ANIMALS
                    CategoryValues.VEHICLES -> return Question.Category.VEHICLES
                    CategoryValues.COMICS -> return Question.Category.COMICS
                    CategoryValues.GADGETS -> return Question.Category.GADGETS
                    CategoryValues.ANIME_MANGA -> return Question.Category.ANIME_MANGA
                    CategoryValues.CARTOON_ANIMATION -> return Question.Category.CARTOON_ANIMATION
                }
                return null
            }

            fun getCategoryOfUi(category: Question.Category?): String? {
                if (category == null) return Misc.ALL
                when (category) {
                    Question.Category.GENERAL_KNOWLEDGE -> return CategoryValues.GENERAL_KNOWLEDGE
                    Question.Category.BOOK -> return CategoryValues.BOOK
                    Question.Category.FILM -> return CategoryValues.FILM
                    Question.Category.MUSIC -> return CategoryValues.MUSIC
                    Question.Category.MUSICAL_THEATRE -> return CategoryValues.MUSICAL_THEATRE
                    Question.Category.TELEVISION -> return CategoryValues.TELEVISION
                    Question.Category.VIDEO_GAME -> return CategoryValues.VIDEO_GAME
                    Question.Category.BOARD_GAME -> return CategoryValues.BOARD_GAME
                    Question.Category.SCIENCE_NATURE -> return CategoryValues.SCIENCE_NATURE
                    Question.Category.COMPUTER -> return CategoryValues.COMPUTER
                    Question.Category.MATHEMATICS -> return CategoryValues.MATHEMATICS
                    Question.Category.MYTHOLOGY -> return CategoryValues.MYTHOLOGY
                    Question.Category.SPORTS -> return CategoryValues.SPORTS
                    Question.Category.GEOGRAPHY -> return CategoryValues.GEOGRAPHY
                    Question.Category.HISTORY -> return CategoryValues.HISTORY
                    Question.Category.POLITICS -> return CategoryValues.POLITICS
                    Question.Category.ART -> return CategoryValues.ART
                    Question.Category.CELEBRITIES -> return CategoryValues.CELEBRITIES
                    Question.Category.ANIMALS -> return CategoryValues.ANIMALS
                    Question.Category.VEHICLES -> return CategoryValues.VEHICLES
                    Question.Category.COMICS -> return CategoryValues.COMICS
                    Question.Category.GADGETS -> return CategoryValues.GADGETS
                    Question.Category.ANIME_MANGA -> return CategoryValues.ANIME_MANGA
                    Question.Category.CARTOON_ANIMATION -> return CategoryValues.CARTOON_ANIMATION
                }
            }

            fun getTypeOfUi(value: String?): Question.Type? {
                if (value.isNullOrEmpty()) return null
                when (value) {
                    TypeUiValues.TRUE_FALSE -> return Question.Type.TRUE_FALSE
                    TypeUiValues.MULTIPLE -> return Question.Type.MULTIPLE
                }
                return null
            }

            fun getType(value: String?): Question.Type? {
                if (value.isNullOrEmpty()) return null
                when (value) {
                    TypeValues.TRUE_FALSE -> return Question.Type.TRUE_FALSE
                    TypeValues.MULTIPLE -> return Question.Type.MULTIPLE
                }
                return null
            }

            fun getDifficult(value: String?): Difficult? {
                if (value.isNullOrEmpty()) return null
                when (value) {
                    DifficultValues.EASY -> return Difficult.EASY
                    DifficultValues.MEDIUM -> return Difficult.MEDIUM
                    DifficultValues.HARD -> return Difficult.HARD
                }
                return null
            }

            fun getDifficultOfUi(difficult: Difficult?): String? {
                if (difficult == null) return Misc.ALL
                when (difficult) {
                    Difficult.EASY -> return DifficultValues.EASY
                    Difficult.MEDIUM -> return DifficultValues.MEDIUM
                    Difficult.HARD -> return DifficultValues.HARD
                }
            }
        }
    }

    object RequestCode {
        object Vpn {
            const val START_VPN_PROFILE = 1
            const val OPEN_SERVER = 2
            const val OPEN_COUNTRY = 3
        }

        const val ADD = 1
        const val EDIT = 2
        const val VIEW = 3
        const val OPEN = 4
        const val PREVIEW = 5
        const val SETTINGS = 6
        const val FAVORITE = 7
        const val QUIZ = 8
        const val OCR = 9
    }

    object Service {
        const val VPN_ADDRESS = "10.0.0.2"
        const val VPN_ROUTE = "0.0.0.0"

        const val PLAYER_SERVICE_STATE_CHANGE = "radio_player_state_change"
        const val PLAYER_SERVICE_STATE = "radio_player_state"
        const val PLAYER_SERVICE_UPDATE = "radio_player_state"

        object Command {
            const val START = "start"
            const val RESUME = "resume"
            const val PAUSE = "pause"
            const val STOP = "stop"
            const val NEXT = "next"
            const val PREVIOUS = "previous"
            const val MEDIA_BUTTON = Intent.ACTION_MEDIA_BUTTON
            const val START_LOCK = "start_lock"
            const val STOP_LOCK = "stop_lock"
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

    object Comparators {

        object Vpn {
            fun getServerComparator(): Comparator<Server> {
                return object : Comparator<Server> {
                    override fun compare(
                        left: Server, right: Server
                    ): Int {
                        if (left.quality != null && right.quality != null) {
                            if (left.quality != right.quality) {
                                return right.quality!!.code.compareTo(left.quality!!.code)
                            }
                        }
                        if (left.speed != right.speed) {
                            return right.speed.compareTo(left.speed)
                        }
                        return return right.sessions.compareTo(left.sessions)
                    }

                }
            }
        }

        object Block {
            fun getUiComparator(): Comparator<ContactItem> {
                return object : Comparator<ContactItem> {
                    override fun compare(
                        left: ContactItem, right: ContactItem
                    ): Int {
                        val leftContact = left.item
                        val rightContact = right.item
                        return rightContact.time.compareTo(leftContact.time)
                    }

                }
            }
        }

        object Crypto {
            private val comparators: HashMap<Pair<CoinSort, Order>, Comparator<Coin>> =
                Maps.newHashMap()
            private val UI_COMPARATORS: HashMap<Pair<CoinSort, Order>, Comparator<CoinItem>> =
                Maps.newHashMap()

            fun getComparator(
                currency: Currency,
                sort: CoinSort,
                order: Order
            ): Comparator<Coin> {
                val pair = Pair(sort, order)
                if (!comparators.containsKey(pair)) {
                    comparators.put(pair, createComparator(currency, sort, order))
                }
                return comparators.get(pair)!!
            }

            fun getUiComparator(
                currency: Currency,
                sort: CoinSort,
                order: Order
            ): Comparator<CoinItem> {
                val pair = Pair(sort, order)
                if (!UI_COMPARATORS.containsKey(pair)) {
                    UI_COMPARATORS.put(pair, createUiComparator(currency, sort, order))
                }
                return UI_COMPARATORS.get(pair)!!
            }

            private fun createComparator(
                currency: Currency,
                sort: CoinSort,
                order: Order
            ): Comparator<Coin> {
                when (sort) {
                    CoinSort.MARKET_CAP -> {
                        return object : Comparator<Coin> {
                            override fun compare(
                                left: Coin, right: Coin
                            ): Int {
                                val leftQuote = left.getQuote(currency)
                                val rightQuote = right.getQuote(currency)

                                val leftCap =
                                    if (leftQuote != null) leftQuote.getMarketCap() else Default.DOUBLE
                                val rightCap =
                                    if (rightQuote != null) rightQuote.getMarketCap() else Default.DOUBLE

                                when (order) {
                                    Order.ASCENDING -> {
                                        return leftCap.compareTo(rightCap)
                                    }
                                    Order.DESCENDING -> {
                                        return rightCap.compareTo(leftCap)
                                    }
                                }
                            }

                        }
                    }
                }
            }

            private fun createUiComparator(
                currency: Currency,
                sort: CoinSort,
                sortDirection: com.dreampany.tools.data.enums.Order
            ): java.util.Comparator<CoinItem> {
                when (sort) {
                    CoinSort.MARKET_CAP -> {
                        return object : java.util.Comparator<CoinItem> {
                            override fun compare(
                                left: CoinItem, right: CoinItem
                            ): Int {
                                val leftQuote = left.item.getQuote(currency)
                                val rightQuote = right.item.getQuote(currency)

                                val leftCap =
                                    if (leftQuote != null) leftQuote.getMarketCap() else Default.DOUBLE
                                val rightCap =
                                    if (rightQuote != null) rightQuote.getMarketCap() else Default.DOUBLE

                                when (sortDirection) {
                                    Order.ASCENDING -> {
                                        return leftCap.compareTo(rightCap)
                                    }
                                    Order.DESCENDING -> {
                                        return rightCap.compareTo(leftCap)
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
    }

    object Extra {
        fun getDrawableWithCountryCode(context: Context, countryCode: String): Int {
            val resources: Resources = context.getResources()
            val resName = "flag_" + countryCode
            val resourceId = resources.getIdentifier(
                resName,
                "drawable",
                context.getPackageName()
            )
            return resourceId
        }

        fun toPrintableContent(context: Context, resume: Resume): String {
            val html = StringBuilder()
            html.append(
                String.format(
                    "<!DOCTYPE html>\n" +
                            "<html>\n" +
                            "<head>\n" +
                            "<title>Resume</title>\n" +
                            "<meta charset=UTF-8>\n" +
                            "<link rel=\"shortcut icon\" href=https://ssl.gstatic.com/docs/documents/images/kix-favicon6.ico>\n" +
                            "<style type=text/css>body{font-family:arial,sans,sans-serif;margin:0}iframe{border:0;frameborder:0;height:100%%;width:100%%}#header,#footer{background:#f0f0f0;padding:10px 10px}#header{border-bottom:1px #ccc solid}#footer{border-top:1px #ccc solid;border-bottom:1px #ccc solid;font-size:13}#contents{margin:6px}.dash{padding:0 6px}</style>\n" +
                            "</head>\n" +
                            "<body>\n" +
                            "<div id=contents>\n" +
                            "<style type=text/css>@import url('https://themes.googleusercontent.com/fonts/css?kit=xTOoZr6X-i3kNg7pYrzMsnEzyYBuwf3lO_Sc3Mw9RUVbV0WvE1cEyAoIq5yYZlSc');ol{margin:0;padding:0}table td,table th{padding:0}.c26{border-right-style:solid;padding:3.6pt 3.6pt 3.6pt 3.6pt;border-bottom-color:#fff;border-top-width:0;border-right-width:0;border-left-color:#fff;vertical-align:top;border-right-color:#fff;border-left-width:0;border-top-style:solid;border-left-style:solid;border-bottom-width:0;width:176.3pt;border-top-color:#fff;border-bottom-style:solid}.c4{border-right-style:solid;padding:5pt 5pt 5pt 5pt;border-bottom-color:#fff;border-top-width:0;border-right-width:0;border-left-color:#fff;vertical-align:top;border-right-color:#fff;border-left-width:0;border-top-style:solid;border-left-style:solid;border-bottom-width:0;width:327.7pt;border-top-color:#fff;border-bottom-style:solid}.c16{color:#000;font-weight:700;text-decoration:none;vertical-align:baseline;font-size:12pt;font-family:\"Raleway\";font-style:normal}.c7{color:#000;font-weight:400;text-decoration:none;vertical-align:baseline;font-size:10pt;font-family:\"Lato\";font-style:normal}.c13{color:#000;font-weight:700;text-decoration:none;vertical-align:baseline;font-size:10pt;font-family:\"Lato\";font-style:normal}.c1{color:#666;font-weight:400;text-decoration:none;vertical-align:baseline;font-size:9pt;font-family:\"Lato\";font-style:normal}.c19{color:#000;font-weight:400;text-decoration:none;vertical-align:baseline;font-size:6pt;font-family:\"Lato\";font-style:normal}.c20{color:#f2511b;font-weight:700;text-decoration:none;vertical-align:baseline;font-size:16pt;font-family:\"Raleway\";font-style:normal}.c6{padding-top:0;padding-bottom:0;line-height:1.0;text-align:left}.c32{padding-top:5pt;padding-bottom:0;line-height:1.15;text-align:left}.c0{padding-top:10pt;padding-bottom:0;line-height:1.0;text-align:left}.c22{padding-top:5pt;padding-bottom:0;line-height:1.0;text-align:left}.c10{color:#d44500;text-decoration:none;vertical-align:baseline;font-style:normal}.c2{padding-top:0;padding-bottom:0;line-height:1.15;text-align:left}.c33{padding-top:3pt;padding-bottom:0;line-height:1.0;text-align:left}.c9{padding-top:4pt;padding-bottom:0;line-height:1.15;text-align:left}.c23{border-spacing:0;border-collapse:collapse;margin:0 auto}.c30{color:#000;text-decoration:none;vertical-align:baseline;font-style:normal}.c3{padding-top:6pt;padding-bottom:0;line-height:1.15;text-align:left}.c14{padding-top:16pt;padding-bottom:0;line-height:1.15;text-align:left}.c28{padding-top:6pt;padding-bottom:0;line-height:1.0;text-align:left}.c18{font-size:9pt;font-family:\"Lato\";font-weight:400}.c24{font-size:14pt;font-family:\"Lato\";font-weight:700}.c8{font-size:10pt;font-family:\"Lato\";font-weight:400}.c5{font-size:11pt;font-family:\"Lato\";font-weight:400}.c31{background-color:#fff;max-width:504pt;padding:36pt 54pt 36pt 54pt}.c35{font-weight:700;font-size:24pt;font-family:\"Raleway\"}.c11{orphans:2;widows:2;height:11pt}.c21{height:auto}.c15{height:auto}.c27{height:auto}.c34{height:auto}.c29{height:auto}.c25{font-size:10pt}.c12{page-break-after:avoid}.c17{height:265pt}.title{padding-top:6pt;color:#000;font-weight:700;font-size:24pt;padding-bottom:0;font-family:\"Raleway\";line-height:1.0;page-break-after:avoid;orphans:2;widows:2;text-align:left}.subtitle{padding-top:3pt;color:#f2511b;font-weight:700;font-size:16pt;padding-bottom:0;font-family:\"Raleway\";line-height:1.0;page-break-after:avoid;orphans:2;widows:2;text-align:left}li{color:#000;font-size:11pt;font-family:\"Lato\"}p{margin:0;color:#000;font-size:11pt;font-family:\"Lato\"}h1{padding-top:4pt;color:#000;font-weight:700;font-size:12pt;padding-bottom:0;font-family:\"Raleway\";line-height:1.15;page-break-after:avoid;orphans:2;widows:2;text-align:left}h2{padding-top:6pt;color:#000;font-weight:700;font-size:11pt;padding-bottom:0;font-family:\"Lato\";line-height:1.15;page-break-after:avoid;orphans:2;widows:2;text-align:left}h3{padding-top:6pt;color:#666;font-size:9pt;padding-bottom:0;font-family:\"Lato\";line-height:1.15;page-break-after:avoid;orphans:2;widows:2;text-align:left}h4{padding-top:8pt;-webkit-text-decoration-skip:none;color:#666;text-decoration:underline;font-size:11pt;padding-bottom:0;line-height:1.15;page-break-after:avoid;text-decoration-skip-ink:none;font-family:\"Trebuchet MS\";orphans:2;widows:2;text-align:left}h5{padding-top:8pt;color:#666;font-size:11pt;padding-bottom:0;font-family:\"Trebuchet MS\";line-height:1.15;page-break-after:avoid;orphans:2;widows:2;text-align:left}h6{padding-top:8pt;color:#666;font-size:11pt;padding-bottom:0;font-family:\"Trebuchet MS\";line-height:1.15;page-break-after:avoid;font-style:italic;orphans:2;widows:2;text-align:left}</style>\n" +
                            "<p class=\"c2 c29\"><span class=c19></span></p>\n" +
                            "<a id=t.b7144d62fc47a2bfcf177a3c3dd72df0e868051e></a>\n" +
                            "<a id=t.0></a>\n" +
                            "<table class=c23>\n" +
                            "            <tbody>\n" +
                            "                <tr class=\"c21\">\n" +
                            "                    <td class=\"c26\" colspan=\"1\" rowspan=\"1\">\n" +
                            "                        <p class=\"c6 c12 title\" id=\"h.4prkjmzco10w\"><span>%s</span></p>\n" +
                            "                        <p class=\"c33 subtitle\" id=\"h.o2iwx3vdck7p\"><span class=\"c20\">%s</span></p>\n" +
                            "                    </td>\n" +
                            "                    <td class=\"c4\" colspan=\"1\" rowspan=\"1\">\n" +
                            "                        <p class=\"c6\"><span style=\"overflow: hidden; display: inline-block; margin: 0.00px 0.00px; border: 0.00px solid #000000; transform: rotate(0.00rad) translateZ(0px); -webkit-transform: rotate(0.00rad) translateZ(0px); width: 418.00px; height: 2.67px;\"><img alt=\"\" src=\"https://lh4.googleusercontent.com/j7t3_XjsJ1PHIrgcWuJOWmQ2fFs9q-TT_LNTDfAXGnVu49aapNgutWcfK1k7pPzGtsu9lOvPynvLW07b_KwpVV0ituspJAXOQ_IBZyN087cqGsXawUahO2qDRCQZ8-qq4esAcP7M\" style=\"width: 418.00px; height: 2.67px; margin-left: 0.00px; margin-top: 0.00px; transform: rotate(0.00rad) translateZ(0px); -webkit-transform: rotate(0.00rad) translateZ(0px);\" title=\"horizontal line\"></span></p>\n" +
                            "                        <h1 class=\"c3\" id=\"h.lf5wiiqsu4ub\"><span>%s</span></h1>\n" +
                            "                        <p class=\"c6\"><span class=\"c7\">%s</span></p>\n" +
                            "                        <p class=\"c6\"><span class=\"c25\">%s</span></p>\n" +
                            "                        <p class=\"c0\"><span class=\"c10 c8\">%s</span></p>\n" +
                            "                        <p class=\"c6\"><span class=\"c8 c10\">%s</span></p>\n" +
                            "                    </td>\n" +
                            "                </tr>",
                    resume.profile?.name.string(),
                    resume.profile?.designation.string(),
                    resume.profile?.name.string(),
                    resume.profile?.currentAddress.string(),
                    resume.profile?.permanentAddress.string(),
                    resume.profile?.phone.string(),
                    resume.profile?.email.string()
                )
            )
            if (!resume.skills.isNullOrEmpty()) {
                val skill = StringBuilder()
                resume.skills?.forEach {
                    if (skill.isNotEmpty()) {
                        skill.append(Sep.COMMA_SPACE)
                    }
                    skill.append(it.title)
                }
                html.append(
                    String.format(
                        "\n" +
                                "                <tr class=\"c27\">\n" +
                                "                    <td class=\"c26\" colspan=\"1\" rowspan=\"1\">\n" +
                                "                        <p class=\"c6\"><span class=\"c24\">ㅡ</span></p>\n" +
                                "                        <h1 class=\"c9\" id=\"h.61e3cm1p1fln\"><span class=\"c16\">" + context.getString(
                            R.string.title_skills
                        ) + "</span></h1></td>\n" +
                                "                    <td class=\"c4\" colspan=\"1\" rowspan=\"1\">\n" +
                                "                        <p class=\"c2\"><span style=\"overflow: hidden; display: inline-block; margin: 0.00px 0.00px; border: 0.00px solid #000000; transform: rotate(0.00rad) translateZ(0px); -webkit-transform: rotate(0.00rad) translateZ(0px); width: 418.00px; height: 2.67px;\"><img alt=\"\" src=\"https://lh3.googleusercontent.com/n8bZfGajkthDbPpbjeiRJ4w7rNUmj1iFxdZKCHUOVnfH9FgHVt5EBo3vOYIIoE3augYQ_DCZJUzdlStyJ5RaldVrSG36sTE0CjIot2qaiJ3YRyr2i87bt9Y9d0ngdseS9PpG0HzM\" style=\"width: 418.00px; height: 2.67px; margin-left: 0.00px; margin-top: 0.00px; transform: rotate(0.00rad) translateZ(0px); -webkit-transform: rotate(0.00rad) translateZ(0px);\" title=\"horizontal line\"></span></p>\n" +
                                "                        <p class=\"c3\"><span class=\"c7\">%s</span></p>\n" +
                                "                    </td>\n" +
                                "                </tr>", skill.toString()
                    )
                )
            }
            if (!resume.experiences.isNullOrEmpty()) {
                html.append(
                    "\n" +
                            "                <tr class=\"c15\">\n" +
                            "                    <td class=\"c26\" colspan=\"1\" rowspan=\"1\">\n" +
                            "                        <p class=\"c6\"><span class=\"c24\">ㅡ</span></p>\n" +
                            "                        <h1 class=\"c9\" id=\"h.tk538brb1kdf\"><span class=\"c16\">" + context.getString(
                        R.string.title_experience
                    ) + "</span></h1></td>\n" +
                            "                    <td class=\"c4\" colspan=\"1\" rowspan=\"1\">\n"
                )
                var first = true
                for (experience in resume.experiences!!) {
                    html.append(
                        String.format(
                            "<h2 class=\"%s\" id=\"h.u3uy0857ab2n\"><span class=\"c5\">%s </span><span class=\"c30 c5\">/ %s</span></h2>\n" +
                                    "                        <h3 class=\"c2\" id=\"h.re1qtuma0rpm\"><span class=\"c1\">%s</span></h3>\n" +
                                    "                        <p class=\"c32\"><span class=\"c7\">%s</span></p>\n",
                            if (first) "c3" else "c14",
                            experience.company.string(),
                            experience.location.string(),
                            experience.designation.string(),
                            experience.description.string()
                        )
                    )
                    first = false
                }
                html.append(
                    "</td>\n" +
                            "                </tr>"
                )
            }

            if (!resume.projects.isNullOrEmpty()) {
                html.append(
                    "\n" +
                            "                <tr class=\"c15\">\n" +
                            "                    <td class=\"c26\" colspan=\"1\" rowspan=\"1\">\n" +
                            "                        <p class=\"c6\"><span class=\"c24\">ㅡ</span></p>\n" +
                            "                        <h1 class=\"c9\" id=\"h.tk538brb1kdf\"><span class=\"c16\">" + context.getString(
                        R.string.title_project
                    ) + "</span></h1></td>\n" +
                            "                    <td class=\"c4\" colspan=\"1\" rowspan=\"1\">\n"
                )
                var first = true
                for (project in resume.projects!!) {
                    html.append(
                        String.format(
                            "<h2 class=\"%s\" id=\"h.u3uy0857ab2n\"><span class=\"c5\">%s </span><span class=\"c30 c5\">/ %s</span></h2>\n" +
                                    "                        <p class=\"c32\"><span class=\"c7\">%s</span></p>\n",
                            if (first) "c3" else "c14",
                            project.name.string(),
                            project.description.string(),
                            project.description.string()
                        )
                    )
                    first = false
                }
                html.append(
                    "</td>\n" +
                            "                </tr>"
                )
            }

            if (!resume.schools.isNullOrEmpty()) {
                html.append(
                    "\n" +
                            "                <tr class=\"c15\">\n" +
                            "                    <td class=\"c26\" colspan=\"1\" rowspan=\"1\">\n" +
                            "                        <p class=\"c6\"><span class=\"c24\">ㅡ</span></p>\n" +
                            "                        <h1 class=\"c9\" id=\"h.tk538brb1kdf\"><span class=\"c16\">" + context.getString(
                        R.string.title_school
                    ) + "</span></h1></td>\n" +
                            "                    <td class=\"c4\" colspan=\"1\" rowspan=\"1\">\n"
                )
                var first = true
                for (school in resume.schools!!) {
                    html.append(
                        java.lang.String.format(
                            "<h2 class=\"%s\" id=\"h.u3uy0857ab2n\"><span class=\"c5\">%s </span><span class=\"c30 c5\">/ %s</span></h2>\n" +
                                    "                        <h3 class=\"c2\" id=\"h.re1qtuma0rpm\"><span class=\"c1\">%s</span></h3>\n" +
                                    "                        <p class=\"c32\"><span class=\"c7\">%s</span></p>\n",
                            if (first) "c3" else "c14",
                            school.name.string(),
                            school.degree,
                            school.location.string(),
                            school.description.string()
                        )
                    )
                    first = false
                }
                html.append(
                    "</td>\n" + "</tr>"
                )
            }

            html.append(
                "</tbody>\n" +
                        "</table>\n" +
                        "<p class=\"c2 c11\"><span class=\"c30 c5\"></span></p>\n" +
                        "</div>\n" +
                        "</body>\n" +
                        "</html>"
            )
            return html.toString()
        }
    }

}