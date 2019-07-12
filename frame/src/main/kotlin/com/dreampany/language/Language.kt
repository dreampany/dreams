package com.dreampany.language

import android.os.Parcelable
import com.dreampany.language.misc.Constants
import kotlinx.android.parcel.Parcelize

/**
 * Created by Roman-372 on 7/12/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
enum class Language(val code: String, val country: String) : Parcelable {
    ARABIC(Constants.LanguageCode.ARABIC, Constants.LanguageCountry.ARABIC),
    BENGALI(Constants.LanguageCode.BENGALI, Constants.LanguageCountry.BENGALI),
    CHINESE(Constants.LanguageCode.CHINESE, Constants.LanguageCountry.CHINESE),
    HINDI(Constants.LanguageCode.HINDI, Constants.LanguageCountry.HINDI),
    FRENCH(Constants.LanguageCode.FRENCH, Constants.LanguageCountry.FRENCH),
    RUSSIA(Constants.LanguageCode.RUSSIA, Constants.LanguageCountry.RUSSIA),
    SPANISH(Constants.LanguageCode.SPANISH, Constants.LanguageCountry.SPANISH),
    ENGLISH(Constants.LanguageCode.ENGLISH, Constants.LanguageCountry.ENGLISH)
}