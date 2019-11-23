package com.dreampany.tools.ui.vm

import android.Manifest
import android.annotation.TargetApi
import android.app.Application
import android.content.Context
import android.os.Build
import android.telecom.TelecomManager
import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.enums.Subtype
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.data.source.repository.StoreRepository
import com.dreampany.framework.misc.AppExecutor
import com.dreampany.framework.util.AndroidUtil
import com.dreampany.framework.util.PermissionUtil
import kotlinx.coroutines.Runnable
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2019-11-23
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class BlockViewModel
@Inject constructor(
    private val application: Application,
    private val ex: AppExecutor,
    private val storeRepo: StoreRepository
)
{
    @TargetApi(Build.VERSION_CODES.P)
    fun blockContactIf(contactId: String) {
        ex.postToDisk(Runnable {
            if (storeRepo.isExists(contactId, Type.CONTACT, Subtype.DEFAULT, State.BLOCKED)) {
                if (AndroidUtil.hasPie() && PermissionUtil.hasPermission(application, Manifest.permission.ANSWER_PHONE_CALLS)) {
                    val telecomManager =
                        application.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
                    telecomManager.endCall()
                }
            }
        })
    }
}