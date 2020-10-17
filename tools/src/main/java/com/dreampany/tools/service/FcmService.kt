package com.dreampany.tools.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Created by roman on 16/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class FcmService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {

    }
}