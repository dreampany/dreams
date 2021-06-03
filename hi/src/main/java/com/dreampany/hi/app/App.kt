package com.dreampany.hi.app

import com.dreampany.common.app.BaseApp
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : BaseApp() {

    override fun onOpen() {
        FirebaseApp.initializeApp(this)
        Fresco.initialize(this)
    }

    override fun onClose() {

    }
}