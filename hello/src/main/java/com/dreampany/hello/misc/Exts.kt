package com.dreampany.hello.misc

import com.dreampany.framework.misc.exts.age
import com.dreampany.framework.misc.exts.color
import com.dreampany.framework.misc.exts.countryCodeToFlag
import com.dreampany.framework.misc.exts.value
import com.dreampany.hello.R
import com.dreampany.hello.data.enums.Gender
import com.dreampany.hello.data.model.Auth
import com.dreampany.hello.data.model.Country
import com.dreampany.hello.data.model.User
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseUser
import com.hbb20.CCPCountry
import com.hbb20.CountryCodePicker
import java.util.*

/**
 * Created by roman on 26/9/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
fun MaterialButton.active() {
    this.setBackgroundColor(context.color(R.color.textColorPrimary))
    this.isEnabled = true
}

fun MaterialButton.inactive() {
    this.setBackgroundColor(context.color(R.color.textColorSecondary))
    this.isEnabled = false
}

fun MaterialButton.active(active: Boolean) {
    if (active) active()
    else inactive()
}

val Calendar?.isValidAge: Boolean
    get() = this?.age.value >= Constants.Date.MIN_AGE

/*val FirebaseUser.user: User
    get() {
        val user = User(uid)
        user.name = displayName
        user.photo = photoUrl.toString()
        user.email = email
        user.phone = phoneNumber
        return user
    }*/

fun FirebaseUser.auth(ref: String): Auth {
    val output = Auth(uid)
    output.ref = ref
    output.email = email
    return output
}

fun Auth.map(deviceId: String): Map<String, Any> {
    val output = HashMap<String, Any>()
    output.put(Constants.Keys.Firestore.TIME, time)
    output.put(Constants.Keys.Firestore.ID, id)
    output.put(Constants.Keys.Firestore.REF, ref)
    username?.let { output.put(Constants.Keys.Firestore.USERNAME, it) }
    email?.let { output.put(Constants.Keys.Firestore.EMAIL, it) }
    password?.let { output.put(Constants.Keys.Firestore.PASSWORD, it) }
    type?.let { output.put(Constants.Keys.Firestore.TYPE, mapOf(deviceId to it.name)) }
    output.put(Constants.Keys.Firestore.REGISTERED, mapOf(deviceId to registered))
    output.put(Constants.Keys.Firestore.VERIFIED, mapOf(deviceId to verified))
    output.put(Constants.Keys.Firestore.LOGGED, mapOf(deviceId to logged))
    return output
}

val User.map: Map<String, Any>
    get() {
        val output = HashMap<String, Any>()
        output.put(Constants.Keys.Firestore.TIME, time)
        output.put(Constants.Keys.Firestore.ID, id)
        output.put(Constants.Keys.Firestore.REF, ref)
        name?.let { output.put(Constants.Keys.Firestore.NAME, it) }
        photo?.let { output.put(Constants.Keys.Firestore.PHOTO, it) }
        output.put(Constants.Keys.Firestore.BIRTHDAY, birthday)
        country?.let { output.put(Constants.Keys.Firestore.COUNTRY, it.map) }
        gender?.let { output.put(Constants.Keys.Firestore.GENDER, it.name) }
        phone?.let { output.put(Constants.Keys.Firestore.PHONE, it) }
        return output
    }

val CCPCountry.country: Country
    get() {
        val output = Country(nameCode)
        output.name = name
        output.flag = nameCode.countryCodeToFlag
        return output
    }

val Country.map: Map<String, Any>
    get() {
        val output = HashMap<String, Any>()
        output.put(Constants.Keys.Firestore.TIME, time)
        output.put(Constants.Keys.Firestore.ID, id)
        output.put(Constants.Keys.Firestore.NAME, name)
        output.put(Constants.Keys.Firestore.FLAG, flag)
        return output
    }

val Map<String, Any>.country: Country
    get() {
        val time = get(Constants.Keys.Firestore.TIME) as Long
        val id = get(Constants.Keys.Firestore.ID) as String
        val name = get(Constants.Keys.Firestore.NAME) as String
        val flag = get(Constants.Keys.Firestore.FLAG) as String

        val output = Country(id)
        output.time = time
        output.name = name
        output.flag = flag
        return output
    }

fun Map<String, Any>.auth(deviceId: String): Auth {
    val time = get(Constants.Keys.Firestore.TIME) as Long
    val id = get(Constants.Keys.Firestore.ID) as String
    val ref = get(Constants.Keys.Firestore.REF) as String
    val username = get(Constants.Keys.Firestore.USERNAME) as String?
    val email = get(Constants.Keys.Firestore.EMAIL) as String?
    val password = get(Constants.Keys.Firestore.PASSWORD) as String?
    val type =
        (get(Constants.Keys.Firestore.TYPE) as? Map<String, String>)?.get(deviceId)
    val registered =
        (get(Constants.Keys.Firestore.REGISTERED) as? Map<String, Boolean>)?.get(deviceId) ?: false
    val verified =
        (get(Constants.Keys.Firestore.VERIFIED) as? Map<String, Boolean>)?.get(deviceId) ?: false
    val logged =
        (get(Constants.Keys.Firestore.LOGGED) as? Map<String, Boolean>)?.get(deviceId) ?: false

    val output = Auth(id)
    output.time = time
    output.ref = ref
    output.username = username
    output.email = email
    output.password = password
    type?.let { output.type = Auth.Type.valueOf(it) }
    output.registered = registered
    output.verified = verified
    output.verified = verified
    output.logged = logged
    return output
}

val Map<String, Any>.user: User
    get() {
        val time = get(Constants.Keys.Firestore.TIME) as Long
        val id = get(Constants.Keys.Firestore.ID) as String
        val ref = get(Constants.Keys.Firestore.REF) as String
        val name = get(Constants.Keys.Firestore.NAME) as String?
        val photo = get(Constants.Keys.Firestore.PHOTO) as String?
        val phone = get(Constants.Keys.Firestore.PHONE) as String?
        val birthday = get(Constants.Keys.Firestore.BIRTHDAY) as Long
        val gender = get(Constants.Keys.Firestore.GENDER) as String?
        val country = (get(Constants.Keys.Firestore.COUNTRY) as? Map<String, Any>)

        val output = User(id)
        output.time = time
        output.ref = ref
        output.name = name
        output.photo = photo
        output.phone = phone
        output.birthday = birthday
        gender?.let { output.gender = Gender.valueOf(it) }
        country?.let { output.country = it.country }
        return output
    }


val CountryCodePicker.selectedCountry: CCPCountry?
    get() = CCPCountry.getLibraryMasterCountriesEnglish()
        .find { this.selectedCountryNameCode == it.nameCode }