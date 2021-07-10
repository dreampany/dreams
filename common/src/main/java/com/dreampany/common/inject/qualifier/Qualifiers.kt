package com.dreampany.common.inject.qualifier

import javax.inject.Qualifier

/**
 * Created by roman on 7/10/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Pref

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Memory

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Room

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Remote

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Nearby