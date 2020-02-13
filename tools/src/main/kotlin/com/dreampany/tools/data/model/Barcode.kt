package com.dreampany.tools.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.data.model.BaseParcel
import com.dreampany.framework.util.TimeUtilKt
import com.dreampany.tools.data.enums.*
import com.dreampany.tools.misc.Constants
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-08-04
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
@Entity(
    indices = [Index(
        value = [Constants.Keys.Barcode.ID],
        unique = true
    )],
    primaryKeys = [Constants.Keys.Barcode.ID]
)
data class Barcode(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING,
    var format: BarcodeFormat = BarcodeFormat.DEFAULT,
    var type: BarcodeType = BarcodeType.DEFAULT,
    var email: Email? = Constants.Default.NULL,
    var phone: Phone? = Constants.Default.NULL,
    var sms: Sms? = Constants.Default.NULL,
    var wifi: Wifi? = Constants.Default.NULL,
    var url: UrlBookmark? = Constants.Default.NULL,
    var geoPoint: GeoPoint? = Constants.Default.NULL,
    var calendarEvent: CalendarEvent? = Constants.Default.NULL,
    var contactInfo: ContactInfo? = Constants.Default.NULL,
    var driverLicense: DriverLicense? = Constants.Default.NULL
) : Base() {

    @Ignore
    constructor() : this(time = TimeUtilKt.currentMillis()) {

    }

    constructor(id: String) : this(time = TimeUtilKt.currentMillis(), id = id) {

    }

    @Parcelize
    data class Wifi(
        var time: Long = Constants.Default.LONG,
        var encryptionType: EncryptionType = EncryptionType.DEFAULT,
        var ssid: String? = Constants.Default.NULL,
        var password: String? = Constants.Default.NULL
    ) : BaseParcel() {

    }

    @Parcelize
    data class UrlBookmark(
        var time: Long = Constants.Default.LONG,
        var title: String? = Constants.Default.NULL,
        var url: String? = Constants.Default.NULL
    ) : BaseParcel() {

    }

    @Parcelize
    data class Sms(
        var time: Long = Constants.Default.LONG,
        var message: String? = Constants.Default.NULL,
        var phoneNumber: String? = Constants.Default.NULL
    ) : BaseParcel() {

    }

    @Parcelize
    data class GeoPoint(
        var time: Long = Constants.Default.LONG,
        var lat: Double = Constants.Default.DOUBLE,
        var lng: Double = Constants.Default.DOUBLE
    ) : BaseParcel() {

    }

    @Parcelize
    data class ContactInfo(
        var time: Long = Constants.Default.LONG,
        var name: PersonName? = Constants.Default.NULL,
        var organization: String? = Constants.Default.NULL,
        var title: String? = Constants.Default.NULL,
        var phones: List<Phone>? = Constants.Default.NULL,
        var emails: List<Email>? = Constants.Default.NULL,
        var urls: Array<String>? = Constants.Default.NULL,
        var addresses: List<Address>? = Constants.Default.NULL
    ) : BaseParcel() {

    }


    @Parcelize
    data class Email(
        var time: Long = Constants.Default.LONG,
        var type: EmailType = EmailType.DEFAULT,
        var address: String? = Constants.Default.NULL,
        var subject: String? = Constants.Default.NULL,
        var body: String? = Constants.Default.NULL
    ) : BaseParcel() {

    }

    @Parcelize
    data class Phone(
        var time: Long = Constants.Default.LONG,
        var type: PhoneType = PhoneType.DEFAULT,
        var number: String? = Constants.Default.NULL
    ) : BaseParcel() {

    }

    @Parcelize
    data class PersonName(
        var time: Long = Constants.Default.LONG,
        var formattedName: String? = Constants.Default.NULL,
        var pronunciation: String? = Constants.Default.NULL,
        var prefix: String? = Constants.Default.NULL,
        var first: String? = Constants.Default.NULL,
        var middle: String? = Constants.Default.NULL,
        var last: String? = Constants.Default.NULL,
        var suffix: String? = Constants.Default.NULL
    ) : BaseParcel() {

    }

    @Parcelize
    data class DriverLicense(
        var time: Long = Constants.Default.LONG,
        var documentType: String? = Constants.Default.NULL,
        var firstName: String? = Constants.Default.NULL,
        var middleName: String? = Constants.Default.NULL,
        var lastName: String? = Constants.Default.NULL,
        var gender: String? = Constants.Default.NULL,
        var addressStreet: String? = Constants.Default.NULL,
        var addressCity: String? = Constants.Default.NULL,
        var addressState: String? = Constants.Default.NULL,
        var addressZip: String? = Constants.Default.NULL,
        var licenseNumber: String? = Constants.Default.NULL,
        var issueDate: String? = Constants.Default.NULL,
        var expiryDate: String? = Constants.Default.NULL,
        var birthDate: String? = Constants.Default.NULL,
        var issuingCountry: String? = Constants.Default.NULL
    ) : BaseParcel() {

    }

    @Parcelize
    data class CalendarEvent(
        var time: Long = Constants.Default.LONG,
        var summary: String? = Constants.Default.NULL,
        var description: String? = Constants.Default.NULL,
        var location: String? = Constants.Default.NULL,
        var organizer: String? = Constants.Default.NULL,
        var status: String? = Constants.Default.NULL,
        var start: CalendarDateTime? = Constants.Default.NULL,
        var end: CalendarDateTime? = Constants.Default.NULL
    ) : BaseParcel() {

    }

    @Parcelize
    data class Address(
        var time: Long = Constants.Default.LONG,
        var addressType : AddressType = AddressType.DEFAULT,
        var addressLines: Array<String>? = Constants.Default.NULL
    ) : BaseParcel() {

    }

    @Parcelize
    data class CalendarDateTime(
        var time: Long = Constants.Default.LONG,
        var year : Int = Constants.Default.INT,
        var month : Int = Constants.Default.INT,
        var day : Int = Constants.Default.INT,
        var hours : Int = Constants.Default.INT,
        var minutes : Int = Constants.Default.INT,
        var seconds : Int = Constants.Default.INT,
        var utc : Boolean = Constants.Default.BOOLEAN,
        var rawValue : String? = Constants.Default.NULL
    ) : BaseParcel() {

    }
}