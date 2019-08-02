package com.dreampany.lca.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.frame.data.model.BaseParcel
import com.dreampany.lca.misc.Constants
import com.google.common.base.Objects
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-07-24
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
@Entity(
    indices = [Index(value = [Constants.SourceInfo.ID], unique = true)],
    primaryKeys = [Constants.SourceInfo.ID]
)
data class SourceInfo(

    @ColumnInfo(name = Constants.SourceInfo.ID)
    private var id: String,
    @ColumnInfo(name = Constants.SourceInfo.NAME)
    private var name: String,
    @ColumnInfo(name = Constants.SourceInfo.LANGUAGE)
    private var language: String,
    @ColumnInfo(name = Constants.SourceInfo.IMAGE_URL)
    private var imageUrl: String

) : BaseParcel() {

    @Ignore
    constructor() : this ("") {

    }

    constructor(id: String): this(id, "", "", "") {

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as SourceInfo
        return Objects.equal(item.id, id)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

    fun setId(id : String) {
        this.id = id
    }

    fun getId() : String {
        return id
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getName() : String {
        return name
    }

    fun setLanguage(language: String) {
        this.language = language
    }

    fun getLanguage() : String {
        return language
    }

    fun setImageUrl(imageUrl: String) {
        this.imageUrl = imageUrl
    }

    fun getImageUrl() : String {
        return imageUrl
    }
}