package com.dreampany.hello.misc

import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.age
import com.dreampany.framework.misc.exts.color
import com.dreampany.framework.misc.exts.countryCodeToFlag
import com.dreampany.framework.misc.exts.value
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.hello.R
import com.dreampany.hello.data.enums.Gender
import com.dreampany.hello.data.model.Auth
import com.dreampany.hello.data.model.Country
import com.dreampany.hello.data.model.User
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.android.gms.tasks.Tasks
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.*
import com.google.firebase.database.*
import com.google.firebase.firestore.FieldValue
import com.hbb20.CCPCountry
import com.hbb20.CountryCodePicker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

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

fun FirebaseUser.auth(ref: String): Auth {
    val output = Auth(uid)
    output.ref = ref
    output.email = email
    return output
}

fun FirebaseUser.user(ref: String): User {
    val output = User(uid)
    output.ref = ref
    output.name = displayName
    output.photo = photoUrl.toString()
    output.phone = phoneNumber
    return output
}

fun Auth.map(deviceId: String): Map<String, Any> {
    val output = HashMap<String, Any>()
    output.put(Constants.Keys.Firebase.TIME, FieldValue.serverTimestamp())
    output.put(Constants.Keys.Firebase.ID, id)
    output.put(Constants.Keys.Firebase.REF, ref)
    username?.let { output.put(Constants.Keys.Firebase.USERNAME, it) }
    email?.let { output.put(Constants.Keys.Firebase.EMAIL, it) }
    password?.let { output.put(Constants.Keys.Firebase.PASSWORD, it) }
    type?.let { output.put(Constants.Keys.Firebase.TYPE, mapOf(deviceId to it.name)) }
    output.put(Constants.Keys.Firebase.REGISTERED, mapOf(deviceId to registered))
    output.put(Constants.Keys.Firebase.VERIFIED, mapOf(deviceId to verified))
    output.put(Constants.Keys.Firebase.LOGGED, mapOf(deviceId to logged))
    return output
}

val User.map: Map<String, Any>
    get() {
        val output = HashMap<String, Any>()
        output.put(Constants.Keys.Firebase.TIME, FieldValue.serverTimestamp())
        output.put(Constants.Keys.Firebase.ID, id)
        output.put(Constants.Keys.Firebase.REF, ref)
        name?.let { output.put(Constants.Keys.Firebase.NAME, it) }
        photo?.let { output.put(Constants.Keys.Firebase.PHOTO, it) }
        output.put(Constants.Keys.Firebase.BIRTHDAY, birthday)
        country?.let { output.put(Constants.Keys.Firebase.COUNTRY, it.map) }
        gender?.let { output.put(Constants.Keys.Firebase.GENDER, it.name) }
        phone?.let { output.put(Constants.Keys.Firebase.PHONE, it) }
        return output
    }

fun String.writeMap(): Map<String, Any> {
    val output = HashMap<String, Any>()
    output.put(Constants.Keys.Firebase.ID, this)
    output.put(Constants.Keys.Firebase.ONLINE, true)
    output.put(Constants.Keys.Firebase.CREATED_AT, ServerValue.TIMESTAMP)
    output.put(Constants.Keys.Firebase.TIME, ServerValue.TIMESTAMP)
    return output
}

fun String.trackMap(index: Long): Map<String, Any> {
    val output = HashMap<String, Any>()
    output.put(Constants.Keys.Firebase.ID, this)
    output.put(Constants.Keys.Firebase.INDEX, index)
    output.put(Constants.Keys.Firebase.ONLINE, true)
    output.put(Constants.Keys.Firebase.CREATED_AT, ServerValue.TIMESTAMP)
    output.put(Constants.Keys.Firebase.TIME, ServerValue.TIMESTAMP)
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
        output.put(Constants.Keys.Firebase.TIME, time)
        output.put(Constants.Keys.Firebase.ID, id)
        output.put(Constants.Keys.Firebase.NAME, name)
        output.put(Constants.Keys.Firebase.FLAG, flag)
        return output
    }

val Map<String, Any>.country: Country
    get() {
        val time = get(Constants.Keys.Firebase.TIME) as Long
        val id = get(Constants.Keys.Firebase.ID) as String
        val name = get(Constants.Keys.Firebase.NAME) as String
        val flag = get(Constants.Keys.Firebase.FLAG) as String

        val output = Country(id)
        output.time = time
        output.name = name
        output.flag = flag
        return output
    }

fun Map<String, Any>.auth(deviceId: String): Auth {
    val time = get(Constants.Keys.Firebase.TIME) as Long
    val id = get(Constants.Keys.Firebase.ID) as String
    val ref = get(Constants.Keys.Firebase.REF) as String
    val username = get(Constants.Keys.Firebase.USERNAME) as String?
    val email = get(Constants.Keys.Firebase.EMAIL) as String?
    val password = get(Constants.Keys.Firebase.PASSWORD) as String?
    val type =
        (get(Constants.Keys.Firebase.TYPE) as? Map<String, String>)?.get(deviceId)
    val registered =
        (get(Constants.Keys.Firebase.REGISTERED) as? Map<String, Boolean>)?.get(deviceId) ?: false
    val verified =
        (get(Constants.Keys.Firebase.VERIFIED) as? Map<String, Boolean>)?.get(deviceId) ?: false
    val logged =
        (get(Constants.Keys.Firebase.LOGGED) as? Map<String, Boolean>)?.get(deviceId) ?: false

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
        val time = get(Constants.Keys.Firebase.TIME) as Long
        val id = get(Constants.Keys.Firebase.ID) as String
        val ref = get(Constants.Keys.Firebase.REF) as String
        val name = get(Constants.Keys.Firebase.NAME) as String?
        val photo = get(Constants.Keys.Firebase.PHOTO) as String?
        val phone = get(Constants.Keys.Firebase.PHONE) as String?
        val birthday = get(Constants.Keys.Firebase.BIRTHDAY) as Long
        val gender = get(Constants.Keys.Firebase.GENDER) as String?
        val country = (get(Constants.Keys.Firebase.COUNTRY) as? Map<String, Any>)

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

val Map<String, Any>.id: String
    get() = (get(Constants.Keys.Firebase.ID) as String?) ?: Constant.Default.STRING

val Map<String, Any>.index: Long
    get() = (get(Constants.Keys.Firebase.INDEX) as Long?) ?: Constant.Default.LONG

val Map<String, Any>.online: Boolean
    get() = (get(Constants.Keys.Firebase.ONLINE) as Boolean?) ?: Constant.Default.BOOLEAN

val Map<String, Any>.createdAt: Long
    get() = (get(Constants.Keys.Firebase.CREATED_AT) as Long?) ?: Constant.Default.LONG

val Map<String, Any>.time: Long
    get() = (get(Constants.Keys.Firebase.TIME) as Long?) ?: Constant.Default.LONG

val CountryCodePicker.selectedCountry: CCPCountry?
    get() = CCPCountry.getLibraryMasterCountriesEnglish()
        .find { this.selectedCountryNameCode == it.nameCode }

val SmartError?.isAuthException: Boolean get() = this?.error is FirebaseAuthException
val SmartError?.isUserCollision: Boolean get() = this?.error is FirebaseAuthUserCollisionException
val SmartError?.isInvalidCredentials: Boolean get() = this?.error is FirebaseAuthInvalidCredentialsException
val SmartError?.isWeakPassword: Boolean get() = this?.error is FirebaseAuthWeakPasswordException
val SmartError?.isInvalidUser: Boolean get() = this?.error is FirebaseAuthInvalidUserException
val SmartError?.isFirebaseError: Boolean get() = isAuthException or isUserCollision or isInvalidCredentials or isWeakPassword or isInvalidUser

@Throws
fun Query.snapshot(): DataSnapshot {
    val source = TaskCompletionSource<DataSnapshot>()
    val task = source.task
    ref.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            source.setResult(snapshot)
        }

        override fun onCancelled(error: DatabaseError) {
            source.setException(error.toException())
        }
    })
    return Tasks.await(task)
}

/*suspend fun DatabaseReference.value(): DataSnapshot {
    return withContext(Dispatchers.Unconfined) {
        suspendCoroutine<DataSnapshot> { continuation ->
            addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    continuation.resume(snapshot)
                }

                override fun onCancelled(error: DatabaseError) {
                    continuation.resumeWithException(error.toException())
                }
            })
        }
    }
}

suspend fun Query.value(): DataSnapshot {
    return suspendCoroutine<DataSnapshot> { continuation ->
        addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                continuation.resume(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                continuation.resumeWithException(error.toException())
            }
        })
    }
}*/

