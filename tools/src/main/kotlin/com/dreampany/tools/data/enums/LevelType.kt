package com.dreampany.tools.data.enums

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-08-05
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
enum class LevelType : Parcelable {
    DEFAULT,
    PRIVATE,
    PRIVATE_FIRST_CLASS,
    SPECIALIST,
    CORPORAL,
    SERGEANT,
    STAFF_SERGEANT,
    SERGEANT_FIRST_CLASS,
    MASTER_SERGEANT,
    FIRST_SERGEANT,
    SERGEANT_MAJOR,
    COMMAND_SERGEANT_MAJOR,
    SERGEANT_MAJOR_OF_THE_ARMY,
    SECOND_LIEUTENANT,
    FIRST_LIEUTENANT,
    CAPTAIN,
    MAJOR,
    LIEUTENANT_COLONEL,
    COLONEL,
    BRIGADIER_GENERAL,
    MAJOR_GENERAL,
    LIEUTENANT_GENERAL,
    GENERAL,
    GENERAL_OF_THE_ARMY
}