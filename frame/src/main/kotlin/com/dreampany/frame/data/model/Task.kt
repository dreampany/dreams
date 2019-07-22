package com.dreampany.frame.data.model

import android.os.Parcelable

/**
 * Created by roman on 2019-07-13
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class Task<T : Parcelable>(var input: T?, var comment: String?) :
    BaseParcelKt() {

/*    var input: T? = null
    var comment: String? = null*/

/*     constructor(`in`: Parcel) {
        comment = `in`.readString()
        if (`in`.readByte().toInt() == 0) {
            input = null
        } else {
            val itemClazz = `in`.readSerializable() as Class<*>
            input = `in`.readParcelable(itemClazz.classLoader)
        }
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(comment)
        if (input == null) {
            dest.writeByte(0.toByte())
        } else {
            dest.writeByte(1.toByte())
            val itemClazz = input!!.javaClass
            dest.writeSerializable(itemClazz)
            dest.writeParcelable(input, flags)
        }
    }

    override fun describeContents(): Int {
        return 0
    }*/
}