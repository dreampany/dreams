package com.dreampany.language

import android.os.Parcelable
import com.dreampany.language.misc.Constants
import kotlinx.android.parcel.Parcelize
import kotlin.collections.ArrayList

/**
 * Created by Roman-372 on 7/12/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
enum class Language(val code: String, val country: String) : Parcelable {

    AFRIKAANS(Constants.LanguageCode.AFRIKAANS, Constants.LanguageCountry.AFRIKAANS),
    ARABIC(Constants.LanguageCode.ARABIC, Constants.LanguageCountry.ARABIC),
    BELARUSIAN(Constants.LanguageCode.BELARUSIAN, Constants.LanguageCountry.BELARUSIAN),
    BULGARIAN(Constants.LanguageCode.BULGARIAN, Constants.LanguageCountry.BULGARIAN),
    BENGALI(Constants.LanguageCode.BENGALI, Constants.LanguageCountry.BENGALI),
    CATALAN(Constants.LanguageCode.CATALAN, Constants.LanguageCountry.CATALAN),
    CZECH(Constants.LanguageCode.CZECH, Constants.LanguageCountry.CZECH),
    ENGLISH(Constants.LanguageCode.ENGLISH, Constants.LanguageCountry.ENGLISH),
    SPANISH(Constants.LanguageCode.SPANISH, Constants.LanguageCountry.SPANISH),
    FRENCH(Constants.LanguageCode.FRENCH, Constants.LanguageCountry.FRENCH),
    HINDI(Constants.LanguageCode.HINDI, Constants.LanguageCountry.HINDI),
    RUSSIAN(Constants.LanguageCode.RUSSIAN, Constants.LanguageCountry.RUSSIAN),
    CHINESE(Constants.LanguageCode.CHINESE, Constants.LanguageCountry.CHINESE);

    override fun toString(): String {
        return country
    }

    companion object {
        fun getAll(): ArrayList<Language> {
            //return values().toList()
            val result = ArrayList<Language>()
            result.add(Language.AFRIKAANS)
            result.add(Language.ARABIC)
            result.add(Language.BELARUSIAN)
            result.add(Language.BULGARIAN)
            result.add(Language.BENGALI)
            result.add(Language.CATALAN)
            result.add(Language.CZECH)
            result.add(Language.ENGLISH)
            result.add(Language.SPANISH)
            result.add(Language.FRENCH)
            result.add(Language.HINDI)
            result.add(Language.RUSSIAN)
            result.add(Language.CHINESE)
            return result
        }
    }
}