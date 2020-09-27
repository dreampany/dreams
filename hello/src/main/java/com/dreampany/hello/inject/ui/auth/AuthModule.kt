package com.dreampany.hello.inject.ui.auth

import com.dreampany.hello.ui.auth.activity.AuthActivity
import com.dreampany.hello.ui.auth.activity.AuthInfoActivity
import com.dreampany.hello.ui.auth.activity.LoginActivity
import com.dreampany.hello.ui.auth.activity.SignupActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by roman on 27/9/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class AuthModule {

    @ContributesAndroidInjector
    abstract fun auth(): AuthActivity

    @ContributesAndroidInjector
    abstract fun signup(): SignupActivity

    @ContributesAndroidInjector
    abstract fun login(): LoginActivity

    @ContributesAndroidInjector
    abstract fun info(): AuthInfoActivity
}